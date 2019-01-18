package de.stadionVerbundSchuetz.service;

import de.stadionVerbundSchuetz.entity.Adresse;
import de.stadionVerbundSchuetz.entity.Buchung;
import de.stadionVerbundSchuetz.entity.Kategorie;
import de.stadionVerbundSchuetz.entity.Stadion;
import de.stadionVerbundSchuetz.ui.model.BenutzerModel;
import de.stadionVerbundSchuetz.ui.model.BuchungModel;
import de.stadionVerbundSchuetz.ui.model.SitzplatzModel;
import de.stadionVerbundSchuetz.ui.model.StadionModel;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.persistence.RollbackException;
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

  @Resource
  UserTransaction utx;

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

  public void erzeugeStadienDaten() {
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
    List<Kategorie> kategorien = new ArrayList<>();
    // public Kategorie(int kategorienr, Boolean stehplatz, String name, double preis, Stadion stadion)
    TypedQuery<Stadion> query = em.createQuery("SELECT s FROM Stadion AS s", Stadion.class);
    List<Stadion> resultStadien = query.getResultList();
    if (query.getResultList().size() > 0) {
      for (Stadion item : resultStadien) {
        double preisStehplatz = 10 + (int) (Math.random()*25);
        double preisSitzplatz = preisStehplatz + (int) (Math.random()*20);
        double preisVip = preisSitzplatz + (int) (Math.random()*35);
        Kategorie k1 = new Kategorie(0, true, "Stehplatz", preisStehplatz, item);
        sitzplatzService.kategorieAnlegen(k1, item);
        Kategorie k2 = new Kategorie(1, false, "Sitzplatz", preisSitzplatz, item);
        sitzplatzService.kategorieAnlegen(k2, item);
        Kategorie k3 = new Kategorie(2, false, "VIP Louge", preisVip, item);
        sitzplatzService.kategorieAnlegen(k3, item);
      }
    }
  }

  public List select(String s){
    try {
      Query q1 = em.createQuery(s);
      List results = q1.getResultList();
      return results;
    } catch (Exception e){
      logger.log(Level.INFO, "Exception: " + e.toString());
      return null;
    }
  }


  }
