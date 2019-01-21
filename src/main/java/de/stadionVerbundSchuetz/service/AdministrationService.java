package de.stadionVerbundSchuetz.service;

import de.stadionVerbundSchuetz.entity.*;
import de.stadionVerbundSchuetz.ui.model.BenutzerModel;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class AdministrationService {

    @Inject
    private Logger logger;

    @PersistenceContext(unitName = "stadionVerbundPU")
    private EntityManager em;

    @Inject
    BenutzerModel benutzerModel;


    @Inject
    SitzplatzService sitzplatzService;

    @Inject
    StadionService stadionService;


    @Transactional
    public void loescheAlles() {
        try {

            Query q1 = em.createQuery("DELETE FROM Block");
            Query q2 = em.createQuery("DELETE FROM Platz");
            Query q3 = em.createQuery("DELETE FROM Kategorie ");
            Query q4 = em.createQuery("DELETE FROM Buchung");
            Query q5 = em.createQuery("DELETE FROM Stadion ");
            Query q6 = em.createQuery("DELETE FROM Benutzer ");

            q1.executeUpdate();
            q2.executeUpdate();
            q3.executeUpdate();
            q4.executeUpdate();
            q5.executeUpdate();
            q6.executeUpdate();

            benutzerModel.abmelden();

        } catch (Exception e) {
            logger.log(Level.INFO, "Exception: " + e.toString());
        }
    }

    @Transactional
    public void alleBuchungenLöschen() {
        try {
            Query q1 = em.createQuery("DELETE FROM Buchung");
            q1.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.INFO, "Exception: " + e.toString());
        }
    }

    public void erzeugeStadienDaten() {
        try {
            List<Stadion> stadien = new ArrayList<>();
            Adresse a1 = new Adresse("Rohrdamm", "45", "31737", "Rinteln");
            Stadion s1 = new Stadion("Waldstadion", a1);
            Adresse a2 = new Adresse("Fasanenstrasse", "7", "20359", "Hamburg");
            Stadion s2 = new Stadion("Millerntor-Stadion", a2);
            Adresse a3 = new Adresse("Straße der Pariser Kommune", "14", "48155", "Münster");
            Stadion s3 = new Stadion("Hänsch-Arena", a3);
            Adresse a4 = new Adresse("Unter den Linden", "31", "19006", "Schwerin");
            Stadion s4 = new Stadion("Vogtlandstadion", a4);
            Adresse a5 = new Adresse("Meinekestraße", "74", "37434", "Krebeck");
            Stadion s5 = new Stadion("Stadion Im Haag", a5);
            stadien.add(s1);
            stadien.add(s2);
            stadien.add(s3);
            stadien.add(s4);
            stadien.add(s5);
            for (Stadion item : stadien) {
                stadionService.anlegen(item);
            }
            TypedQuery<Stadion> query = em.createQuery("SELECT s FROM Stadion AS s", Stadion.class);
            List<Stadion> resultStadien = query.getResultList();
            if (query.getResultList().size() > 0) {
                for (Stadion itemStadion : resultStadien) {
                    double preisStehplatz = 10 + (int) (Math.random() * 25);
                    double preisSitzplatz = preisStehplatz + (int) (Math.random() * 20);
                    double preisVip = preisSitzplatz + (int) (Math.random() * 35);

                    String abkuerzung = itemStadion.getName().substring(0,3);

                    Kategorie k1 = new Kategorie(0, true, abkuerzung+"-Stehplatz", preisStehplatz, itemStadion);
                    sitzplatzService.kategorieAnlegen(k1, itemStadion);
                    Kategorie k2 = new Kategorie(1, false, abkuerzung+"-Sitzplatz", preisSitzplatz, itemStadion);
                    sitzplatzService.kategorieAnlegen(k2, itemStadion);
                    Kategorie k3 = new Kategorie(2, false, abkuerzung+"-VIP Louge", preisVip, itemStadion);
                    sitzplatzService.kategorieAnlegen(k3, itemStadion);
                    TypedQuery<Kategorie> kategorieQuery = em.createQuery("SELECT k FROM Kategorie AS k where stadionKategorie = :stadionKategorie", Kategorie.class);
                    kategorieQuery.setParameter("stadionKategorie", itemStadion);
                    List<Kategorie> queryResultKategorie = kategorieQuery.getResultList();
                    List<Block> blockListe = new ArrayList<>();
                    int countPlaetzeStadion = 0;
                    String blockname[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
                    String blockNummer[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
                    String ausrichtung[] = {"Nord", "Nord-Ost", "Ost", "Süd-Ost", "Süd", "Süd-West", "West", "Nord-West"};
                    do {

                        int anzahlSitzeJeReihe = 5 + (int) (Math.random() * 20);
                        int anzahlReihe = 1 + (int) (Math.random() * 5);

                        countPlaetzeStadion += anzahlSitzeJeReihe * anzahlReihe;

                        int randomIndexBlockName = new Random().nextInt(blockname.length);
                        int randomIndexBlockNummer = new Random().nextInt(blockNummer.length);
                        int randomIndexAusrichtung = new Random().nextInt(ausrichtung.length);
                        int randomIndexKategorie = new Random().nextInt(queryResultKategorie.size());
                        String BlockName = blockname[randomIndexBlockName] + blockNummer[randomIndexBlockNummer];
                        String ausrichtungStr = ausrichtung[randomIndexAusrichtung];
                        Kategorie kat = queryResultKategorie.get(randomIndexKategorie);

                        //String name, String ausrichtung, Platz plaetze, Kategorie kategorie, Stadion stadionBlock
                        Platz p1 = new Platz(anzahlSitzeJeReihe, anzahlReihe, itemStadion);
                        Block b1 = new Block(BlockName, ausrichtungStr, p1, kat, itemStadion);
                        blockListe.add(b1);
                    } while (countPlaetzeStadion < 250);
                    sitzplatzService.bloeckeHinzufuegen(itemStadion, blockListe);
                }
            }
        } catch (Exception e) {
            logger.log(Level.INFO, "Exception: " + e.toString());
        }
    }

    public List select(String s) {
        try {
            Query q1 = em.createQuery(s);
            List results = q1.getResultList();
            return results;
        } catch (Exception e) {
            logger.log(Level.INFO, "Exception: " + e.toString());
            return null;
        }
    }


}
