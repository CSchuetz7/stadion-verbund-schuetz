package de.stadionVerbundSchuetz.service;

import de.stadionVerbundSchuetz.entity.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class SitzplatzService implements SitzplatzServiceIF {

    @Inject
    private Logger logger;

    @PersistenceContext(unitName = "stadionVerbundPU")
    private EntityManager entityManager;

    public List<Platz> findeAllePlaetze() {
        TypedQuery<Platz> query = entityManager.createQuery("SELECT p FROM Platz AS p", Platz.class);
        return query.getResultList();
    }

    public List<Block> findeAlleBloecke() {
        TypedQuery<Block> query = entityManager.createQuery("SELECT b FROM Block AS b", Block.class);
        return query.getResultList();
    }

    public Kategorie findeKategorie(long kategorieNr) {
        return entityManager.find(Kategorie.class, kategorieNr);
    }

    public List<Kategorie> findeAlleKategorien() {
        String query = "SELECT k FROM Kategorie AS k";
        TypedQuery<Kategorie> typedQuery = entityManager.createQuery(query, Kategorie.class);
        List<Kategorie> queryErgebnis = typedQuery.getResultList();
        return queryErgebnis;
    }

    public List<Platz> findePlaetze() {
        TypedQuery<Platz> query = entityManager.createQuery("SELECT count(p) FROM Platz AS p", Platz.class);
        return  query.getResultList();
    }

    @Transactional
    public void blockHinzufuegen(Stadion stadion, Block block) {
        List<Block> tempBloecke = stadion.getBloecke();
        tempBloecke.add(block);
        stadion.setBloecke(tempBloecke);
        entityManager.merge(stadion);
        logger.log(Level.INFO, "Block und Platz angelegt");
    }

    public List<Kategorie> findeKategorienNachStadion(Stadion stadion) {
        TypedQuery<Kategorie> query = entityManager.createQuery("SELECT k FROM Kategorie AS k where stadionKategorie = :stadion and stadionKategorie = :stadion", Kategorie.class);
        query.setParameter("stadion", stadion);
        return query.getResultList();
    }

    public Boolean pruefeKategorieNrUndNameVorhanden(Kategorie kategorie, Stadion stadion) {
        TypedQuery<Kategorie> query = entityManager.createQuery("SELECT k FROM Kategorie AS k where stadionKategorie = :stadion", Kategorie.class);
        query.setParameter("stadion", stadion);
        List<Kategorie> tempKategorie = query.getResultList();
        for (Kategorie item : tempKategorie) {
            if ((kategorie.getName().equals(item.getName()) && kategorie.getKategorienr() == item.getKategorienr()) ||  (kategorie.getKategorienr() == item.getKategorienr()) || (kategorie.getName().equals(item.getName()))){
                return true;
            }
        }
        return false;
    }

    @Transactional
    public Kategorie kategorieAnlegen(Kategorie kategorie, Stadion stadion) {
        try{
            entityManager.persist(kategorie);
            logger.log(Level.INFO, "Kategorie " + kategorie.toString() + " angelegt");
            return kategorie;
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean pruefeObBlockPlatzDatenSchonVorhanden(Block block) {
        TypedQuery<Block> query = entityManager.createQuery("SELECT b FROM Block AS b where block_id != :block_id and stadionBlock = :stadion", Block.class);
        //Abfrage mit Stadion_id um gleiches zu bearbeitendes Stadion zu ignorieren, da ja Daten gleich sind
        query.setParameter("block_id", block.getBlock_id());
        query.setParameter("stadion", block.getStadionBlock());
        List<Block> queryResult = query.getResultList();
        if (queryResult.size() > 0) {
            for (Block item : queryResult) {
                if (block.getName().equals(item.getName())){
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }


    public Boolean pruefeObKategorieDatenSchonVorhanden(Kategorie kategorie) {
        TypedQuery<Kategorie> query = entityManager.createQuery("SELECT k FROM Kategorie AS k where kategorie_id != :kategorie", Kategorie.class);
        //Abfrage mit Stadion_id um gleiches zu bearbeitendes Stadion zu ignorieren, da ja Daten gleich sind
        query.setParameter("kategorie", kategorie.getKategorie_id());
        List<Kategorie> queryResult = query.getResultList();
        Boolean vorhandenFlag = false;
        if (queryResult.size() > 0) {
            for (Kategorie item : queryResult) {
                //Prüfen ob Daten schon vorhanden (Name des Stadions oder selbe Adresse)
                if (kategorie.getName().equals(item.getName()) && kategorie.getStadionKategorie().getStadion_id() == item.getStadionKategorie().getStadion_id() && kategorie.getKategorienr() == item.getKategorienr()){
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Transactional
    public Block aendernBlockPlatz(Block zuBearbeiten) {
        try {
        Block temp = entityManager.find(Block.class, zuBearbeiten.getBlock_id());
            temp.setName(zuBearbeiten.getName());
            temp.setAusrichtung(zuBearbeiten.getAusrichtung());
            temp.setKategorie(zuBearbeiten.getKategorie());
            logger.log(Level.INFO, "Block " + temp.toString() + " geändert");
            return entityManager.merge(temp);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public Kategorie aendernKategorie(Kategorie zuBearbeiten) {
        try {
        Kategorie temp = entityManager.find(Kategorie.class, zuBearbeiten.getKategorie_id());
            temp.setName(zuBearbeiten.getName());
            temp.setPreis(zuBearbeiten.getPreis());
            temp.setStehplatz(zuBearbeiten.getStehplatz());
            temp.setKategorienr(zuBearbeiten.getKategorienr());
            logger.log(Level.INFO, "Kategorie " + temp.toString() + " geändert");
            return entityManager.merge(temp);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    //Block sollte nur gelöscht werden können wenn noch keine Buchung auf das Stadion besteht
    public Block loescheBlockPlatz(Block zuLoeschen) {
        try {
        Block temp = entityManager.find(Block.class, zuLoeschen.getBlock_id());
            TypedQuery<Buchung> query = entityManager.createQuery("SELECT b FROM Buchung AS b where stadion = :stadion", Buchung.class);
            query.setParameter("stadion", zuLoeschen.getStadionBlock());
            if (query.getResultList().size() == 0) {
                entityManager.remove(temp);
                logger.log(Level.INFO, "Block " + temp.toString() + " gelöscht");
                return temp;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean pruefeObBlockPlatzfuerKategorieVorhanden(Kategorie kategorie) {
        TypedQuery<Block> query = entityManager.createQuery("SELECT b FROM Block AS b where kategorie = :kategorie", Block.class);
        query.setParameter("kategorie", kategorie);
        List<Block> queryResult = query.getResultList();
        if (queryResult.size() > 0) {
            return true;
        } else {
            return false;
        }
    }


    @Transactional
    public Kategorie loescheKategorie(Kategorie zuLoeschen) {
        try {
        Kategorie temp = entityManager.find(Kategorie.class, zuLoeschen.getKategorie_id());
            entityManager.remove(temp);
            logger.log(Level.INFO, "Kategorie " + temp.toString() + " gelöscht");
            return temp;
        } catch (Exception e) {
            return null;
        }
    }

}
