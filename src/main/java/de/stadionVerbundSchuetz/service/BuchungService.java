package de.stadionVerbundSchuetz.service;

import de.stadionVerbundSchuetz.entity.Benutzer;
import de.stadionVerbundSchuetz.entity.Buchung;
import de.stadionVerbundSchuetz.entity.Platz;
import de.stadionVerbundSchuetz.entity.Stadion;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;

@RequestScoped
@WebService
public class BuchungService {

    @Inject
    private Logger logger;

    @Inject
    private TicketServiceIF ticketServiceIF;

    @Resource
    private UserTransaction userTransaction;

    @PersistenceContext(unitName = "stadionVerbundPU")
    private EntityManager entityManager;

    @Transactional
    @WebMethod(exclude = true)
    public Buchung anlegen(Buchung buchung) {
        try {
            entityManager.persist(buchung);
            ticketServiceIF.erstelleTicket(buchung);
            return buchung;
        } catch (Exception e) {
            logger.log(Level.INFO, "Exception: " + e.toString());
            return null;
        }
    }

    @Transactional
    @WebMethod(exclude = true)
    public Buchung anlegenBuchungWebService(Buchung buchung) {
        try {
            userTransaction.begin();
            entityManager.persist(buchung);
            userTransaction.commit();
            return buchung;
        } catch (Exception e) {
            logger.log(Level.INFO, "Exception: " + e.toString());
            return null;
        }
    }

    @WebMethod(exclude = true)
    public Buchung findeBuchung(long buchungNr) {
        Buchung gefunden = entityManager.find(Buchung.class, buchungNr);
        return gefunden;
    }

    @WebMethod(exclude = true)
    public List<Buchung> findeAlleBuchungen() {
        TypedQuery<Buchung> query = entityManager.createQuery("SELECT b FROM Buchung AS b", Buchung.class);
        List<Buchung> queryErgebnis = query.getResultList();
        return queryErgebnis;
        // return null;
    }

    public Stadion bucheStadionAutomatisch(@WebParam(name = "spielId") long spielId, @WebParam(name = "anzahlZuschauer") int anzahlZuschauer, @WebParam(name = "spielDatum") Date spielDatum) {
        try {
            //Prüfen auf Datum in der Vergangenheit zurzeit nicht eingebaut, da sonst Effekt beim Abschluss einer Wette nicht möglich (hinzufügen von spielDatum.compareTo(new Date()) >= 0)
            if (anzahlZuschauer > 0 && spielDatum != null && spielId >= 0) {
                if (pruefeSpielIdVorhanden(spielId).size() == 0) {
                    // if (benutzerService.anmelden(email, passwort) != null) {
                    TypedQuery<Stadion> query = entityManager.createQuery("SELECT s FROM Stadion AS s", Stadion.class);
                    List<Stadion> queryStadion = query.getResultList();
                    if (queryStadion.size() > 0) {
                        Map<Integer, Stadion> plaetzeMap = new HashMap<>();
                        for (Stadion itemStadion : queryStadion) {
                            int platzCount = 0;
                            List<Platz> itemStadionPlaetze = itemStadion.getPlaetze();
                            for (Platz itemPlaetze : itemStadionPlaetze) {
                                platzCount += (itemPlaetze.getAnzahlReihe() * itemPlaetze.getAnzahlSitzeReihe());
                            }
                            if (platzCount > 0 && platzCount >= anzahlZuschauer) {
                                plaetzeMap.put(platzCount, itemStadion);
                            }
                        }
                        if (plaetzeMap.size() > 0) {
                            Stadion bestesStadion = null;
                            List<Map.Entry<Integer, Stadion>> sortiertePlaetzeMap =
                                    plaetzeMap.entrySet()
                                            .stream()
                                            .sorted(reverseOrder(Map.Entry.comparingByKey()))
                                            .collect(Collectors.toList());
                            int mapIdx = 0;
                            do {
                                bestesStadion = sortiertePlaetzeMap.get(mapIdx).getValue();
                                mapIdx++;
                            } while (pruefeSpielDatumVerfuegbar(spielDatum, bestesStadion).size() > 0 || mapIdx > sortiertePlaetzeMap.size());
                            Buchung buchung = new Buchung(spielId, spielDatum, bestesStadion);
                            Buchung tempBuchung = this.anlegenBuchungWebService(buchung);
                            logger.log(Level.INFO, "Buchung " + tempBuchung + " über Webservice angelegt");
                            if (bestesStadion != null & tempBuchung != null) {
                                //Ist ein Thread da es ansonsten blockierend wirkt,
                                //und für den Buchungsreturn nicht wichtig ist, da es
                                //einfach im Hintergrund ablaufen soll.
                                ticketServiceIF.erstelleTicket(tempBuchung);
                                return bestesStadion;
                            } else {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            logger.log(Level.INFO, "Exception: " + e.toString());
            return null;
        }
        return null;
    }

    @WebMethod(exclude = true)
    public List<Buchung> findeBuchungFuerBenutzer(Benutzer benutzer) {
        TypedQuery<Buchung> query;
        if (benutzer.getEmail().equals("admin@StadionverbundSchuetz.de")) {
            query = entityManager.createQuery("SELECT b FROM Buchung AS b", Buchung.class);
        } else {
            query = entityManager.createQuery("SELECT b FROM Buchung AS b WHERE b.benutzerId = :benutzer ", Buchung.class);
            query.setParameter("benutzer", benutzer);
        }
        List<Buchung> queryErgebnis = query.getResultList();
        return queryErgebnis;
    }

    @WebMethod(exclude = true)
    public List<Buchung> pruefeSpielDatumVerfuegbar(Date spieldatum, Stadion stadion) {
        TypedQuery<Buchung> query = entityManager.createQuery("SELECT b FROM Buchung AS b where b.spieldatum = :spieldatum and b.stadion = :stadion ", Buchung.class);
        query.setParameter("spieldatum", spieldatum);
        query.setParameter("stadion", stadion);
        List<Buchung> queryErgebnis = query.getResultList();
        return queryErgebnis;
    }

    @WebMethod(exclude = true)
    public List<Buchung> pruefeSpielIdVorhanden(long spielId) {
        TypedQuery<Buchung> query = entityManager.createQuery("SELECT b FROM Buchung AS b where b.spielid = :spielId", Buchung.class);
        query.setParameter("spielId", spielId);
        List<Buchung> queryErgebnis = query.getResultList();
        return queryErgebnis;
    }

    @Transactional
    @WebMethod(exclude = true)
    public Buchung loeschen(Buchung zuLoeschen) {
        Buchung temp = entityManager.find(Buchung.class, zuLoeschen.getBuchung_id());
        entityManager.remove(temp);
        return temp;
    }
}
