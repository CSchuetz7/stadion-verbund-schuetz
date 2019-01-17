package de.StadionverbandSchuetz.service;

import de.StadionverbandSchuetz.entity.Buchung;
import de.StadionverbandSchuetz.ui.model.BenutzerModel;
import de.StadionverbandSchuetz.ui.model.BuchungModel;
import de.StadionverbandSchuetz.ui.model.SitzplatzModel;
import de.StadionverbandSchuetz.ui.model.StadionModel;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.transaction.*;
import java.util.List;
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
      Query q7 = em.createNativeQuery("DROP TABLE Stadion");

      q1.executeUpdate();
      q2.executeUpdate();
      q3.executeUpdate();
      q4.executeUpdate();
      q5.executeUpdate();
      q6.executeUpdate();
      q7.executeUpdate();

      benutzerModel.abmelden();

    } catch (Exception e) {
      logger.log(Level.INFO, "Exception: " + e.toString());
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
