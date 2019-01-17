package de.stadionVerbundSchuetz.service;

import de.stadionVerbundSchuetz.entity.Adresse;
import de.stadionVerbundSchuetz.entity.Benutzer;
import de.stadionVerbundSchuetz.utils.BCrypt;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jws.WebService;
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
@WebService
public class BenutzerService {

    @Inject
    private Logger logger;

    private static final int workload = 12;

    @PersistenceContext(unitName = "stadionVerbundPU")
    private EntityManager entityManager;

    @Transactional
    public Benutzer anlegen(String name, Adresse adresse, String passwort, String email) {
        if (this.getBenutzerBeiEmail(email) != null) {
            return null;
        } else {
            String hashedPasswort = this.hashPasswort(passwort);
            Benutzer b = new Benutzer(name, adresse, hashedPasswort, email);
            entityManager.persist(b);
            logger.log(Level.INFO, "Benutzer "+ b.getEmail() +"angelegt");
            return b;
        }
    }

    public Benutzer findeBeiEmail(String email) {
        Query q = entityManager.createQuery("Select b FROM Benutzer as b WHERE b.email= :email");
        q.setParameter("email", email);
        List<Benutzer> benutzer = q.getResultList();
        if (benutzer.isEmpty()) {
            return null;
        } else {
            return benutzer.get(0);
        }
    }

    @Transactional
    public Benutzer anmelden(String email, String passwort) {
        Benutzer b = findeBeiEmail(email);
        if (b == null) {
            return null;
        } else {
            if (BCrypt.checkpw(passwort, b.getPasswort())) {
                return b;
            } else{
                return null;
            }
        }

    }

    @Transactional
    public Benutzer loeschen(Benutzer zuLoeschen) {
        Benutzer temp = entityManager.find(Benutzer.class, zuLoeschen.getBenutzer_id());
        entityManager.remove(temp);
        System.out.println("Benutzer wurde erfolgreich gelöscht");
        return temp;
    }

    public List<Benutzer> findeAlleBenutzer() {
        TypedQuery<Benutzer> query = entityManager.createQuery("SELECT b FROM Benutzer AS b", Benutzer.class);
        List<Benutzer> queryErgebnis = query.getResultList();
        return queryErgebnis;
        // return null;
    }

    @Transactional
    public Benutzer getBenutzerBeiEmail(String email) {
        return findeBeiEmail(email);
    }

    /*
        passwortgenerator
        https://gist.github.com/craSH/5217757
    */

    @Transactional
    public String hashPasswort(String passwort) {
        String salt = BCrypt.gensalt(workload);
        String hashedPasswort = BCrypt.hashpw(passwort, salt);
        return hashedPasswort;
    }
}
