package de.StadionverbandSchuetz.service;

import de.StadionverbandSchuetz.entity.Buchung;
import de.StadionverbandSchuetz.entity.Platz;
import de.StadionverbandSchuetz.entity.Stadion;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class StadionService implements StadionServiceIF {

    @Inject
    private Logger logger;

    @PersistenceContext(unitName = "stadionVerbundPU")
    private EntityManager entityManager;

    @Transactional
    public Stadion anlegen(Stadion stadion) {
        try {
            TypedQuery<Stadion> query = entityManager.createQuery("SELECT s FROM Stadion AS s where s.name = :name", Stadion.class);
            query.setParameter("name", stadion.getName());
            if (query.getResultList().size() > 0) {
                return null;
            }
            if (!stadion.getName().equals("") &&
                    !stadion.getAdresse().getStrassenname().equals("") &&
                    !stadion.getAdresse().getHausnummer().equals("") &&
                    !stadion.getAdresse().getOrt().equals("") &&
                    !stadion.getAdresse().getPlz().equals("")) {
                entityManager.persist(stadion);
                logger.log(Level.INFO, "Stadion " + stadion.toString() + " angelegt");
                return stadion;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean pruefeObStadionDatenSchonVorhanden(Stadion stadion) {
        TypedQuery<Stadion> query = entityManager.createQuery("SELECT s FROM Stadion AS s where stadion_id != :stadion", Stadion.class);
        //Abfrage mit Stadion_id um gleiches zu bearbeitendes Stadion zu ignorieren, da ja Daten gleich sind
        query.setParameter("stadion", stadion.getStadion_id());
        List<Stadion> queryResult = query.getResultList();
        Boolean vorhandenFlag = false;
        if (queryResult.size() > 0) {
            for (Stadion itemBlock : queryResult) {
                //Prüfen ob Daten schon vorhanden (Name des Stadions oder selbe Adresse)
                if (stadion.getName().equals(itemBlock.getName()) || (stadion.getAdresse().getPlz().equals(itemBlock.getAdresse().getPlz()) &&
                        stadion.getAdresse().getOrt().equals(itemBlock.getAdresse().getOrt()) && stadion.getAdresse().getHausnummer().equals(itemBlock.getAdresse().getHausnummer()) &&
                        stadion.getAdresse().getStrassenname().equals(itemBlock.getAdresse().getStrassenname()))) {
                    vorhandenFlag = true;
                    break;
                }
            }
            return vorhandenFlag;
        } else {
            return false;
        }
    }

        public Stadion findeStadion ( long stadionNr){
            Stadion gefunden = entityManager.find(Stadion.class, stadionNr);
            return gefunden;
        }

        public int findePlatzAnzahl (Stadion stadion){
            TypedQuery<Stadion> query = entityManager.createQuery("SELECT s FROM Stadion AS s where stadion_id = :stadion_id", Stadion.class);
            query.setParameter("stadion_id", stadion.getStadion_id());
            List<Stadion> queryStadion = query.getResultList();
            if (queryStadion.size() > 0) {
                int platzCount = 0;
                for (Stadion itemStadion : queryStadion) {
                    List<Platz> itemStadionPlaetze = itemStadion.getPlaetze();
                    for (Platz itemPlaetze : itemStadionPlaetze) {
                        platzCount += (itemPlaetze.getAnzahlReihe() * itemPlaetze.getAnzahlSitzeReihe());
                    }
                }
                return platzCount;
            } else {
                return 0;
            }
        }

        public List<Stadion> findeAlleStadien () {
            // Benutzung von dieses Query Abfrage, da ab einer Anzahl von größeren Platz Collection, die Abfrage extrem lansamg wird da er durch alle referenzen durchiteriert (Plaetze->Block->Kategorie);
            // String query = "SELECT NEW Stadion(s.stadion_id, s.name, s.adresse, s.bloecke.size) FROM Stadion AS s";
            String query = "SELECT s FROM Stadion AS s";
            TypedQuery<Stadion> typedQuery = entityManager.createQuery(query, Stadion.class);
            List<Stadion> queryErgebnis = typedQuery.getResultList();
            return queryErgebnis;
            // return null;
        }
/*
  Standard Abfrage, bei erhöhter Collection Zahl der Plaetze extrem langsam
  public List<Stadion> findeAlleStadien() {
    Query query = entityManager.createQuery("SELECT s FROM Stadion AS s", Stadion.class);
    List<Stadion> queryErgebnis = query.getResultList();
    System.out.println("ALLE: " + Arrays.toString(queryErgebnis.toArray()));
    return queryErgebnis;
    // return null;
  } */

        @Transactional
        public Stadion aendern (Stadion zuBearbeiten){
            try {
                Stadion temp = entityManager.find(Stadion.class, zuBearbeiten.getStadion_id());
                temp.setName(zuBearbeiten.getName());
                temp.setAdresse(zuBearbeiten.getAdresse());
                logger.log(Level.INFO, "Stadion " + temp.toString() + " geändert");
                return entityManager.merge(temp);
            } catch (Exception e) {
                return null;
            }
        }

        public Boolean pruefeObBuchungfuerStadionVorhanden (Stadion stadion){
            TypedQuery<Buchung> query = entityManager.createQuery("SELECT b FROM Buchung AS b where stadion = :stadion", Buchung.class);
            query.setParameter("stadion", stadion);
            List<Buchung> queryStadion = query.getResultList();
            if (queryStadion.size() > 0) {
                return true;
            } else {
                return false;
            }
        }

        @Transactional
        public Stadion loeschen (Stadion zuLoeschen){
            try {
                Stadion temp = entityManager.find(Stadion.class, zuLoeschen.getStadion_id());
                entityManager.remove(temp);
                logger.log(Level.INFO, "Stadion " + temp.toString() + " gelöscht");
                return temp;
            } catch (Exception e) {
                return null;
            }
        }
    }
