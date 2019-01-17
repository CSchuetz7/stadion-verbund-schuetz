package de.StadionverbandSchuetz.ui.model;

import de.StadionverbandSchuetz.converter.StadionKonvertierer;
import de.StadionverbandSchuetz.entity.*;
import de.StadionverbandSchuetz.exceptions.KonnteNichtGeloeschtWerdenFehler;
import de.StadionverbandSchuetz.service.StadionService;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@SessionScoped
public class StadionModel implements Serializable {

    @Inject
    private StadionService stadionService;

    @Inject
    private transient Logger logger;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Boolean inBearbeitung;

    @Getter
    @Setter
    private String strassenname;
    @Getter
    @Setter
    private String hausnummer;
    @Getter
    @Setter
    private String plz;
    @Getter
    @Setter
    private String ort;

    @Getter
    @Setter
    private Platz platz;

    @Getter
    @Setter
    private List<Stadion> stadionListe;

    @Getter
    @Setter
    private Stadion stadion;

    @Inject
    @Getter
    @Setter
    StadionKonvertierer stadionTypKonv;
    @Getter
    @Setter
    private int kategorienr;
    @Getter
    @Setter
    private Boolean stehplatz;
    @Getter
    @Setter
    private String kategorieName;
    @Getter
    @Setter
    private double preis;
    @Getter
    @Setter
    private String blockName;
    @Getter
    @Setter
    private String ausrichtung;

    @Getter
    @Setter
    private int anzahlPlaetzeProReihe;
    @Getter
    @Setter
    private int anzahlReihe;

    public int AnzahlPlaetze() {
        return (this.anzahlReihe * this.anzahlPlaetzeProReihe);
    }

    public void initialisieren() {
        this.stadionListe = stadionService.findeAlleStadien();
    }

    public void aktualisieren() {
        this.stadionListe = stadionService.findeAlleStadien();
    }
    // Action-Methoden

    public String anlegen() {
        try {
            Adresse tempAdresse = new Adresse(this.strassenname, this.hausnummer, this.plz, this.ort);
            Stadion tempStadion = new Stadion(this.name, tempAdresse);
            if (!stadionService.pruefeObStadionDatenSchonVorhanden(tempStadion)) {
                if (this.stadionService.anlegen(tempStadion) != null) {
                    aktualisieren();
                    return "angelegt";
                } else {
                    FacesContext.getCurrentInstance().addMessage("StadionAnlegenFenster", new FacesMessage("Füllen Sie bitte alle Eingabefelder aus!"));
                    return null;
                }
            } else {
                FacesContext.getCurrentInstance().addMessage("StadionAnlegenFenster", new FacesMessage("Stadion mit diesen Daten ist schon vorhanden"));
                return null;
            }
        } catch (Exception e){
            return null;
        }
    }


    public List<Stadion> findeAlleStadien() {
        try {
            return stadionService.findeAlleStadien();
        } catch (Exception e) {
            logger.log(Level.INFO, "Exception: " + e.toString());
            return null;
        }
    }

    public int findePlatzAnzahl(Stadion stadion){
        return stadionService.findePlatzAnzahl(stadion);
    }

    public void inAenderungVorbereitenStadion(Stadion stadion) {
        this.inBearbeitung = true;
        this.name = stadion.getName();
        this.strassenname = stadion.getAdresse().getStrassenname();
        this.hausnummer = stadion.getAdresse().getHausnummer();
        this.plz = stadion.getAdresse().getPlz();
        this.ort = stadion.getAdresse().getOrt();
        this.stadion = stadion;
    }

    public void aendereStadionAbbrechen() {
    this.inBearbeitung = false;
    }

    public void aendereStadion() {
        if (!this.name.equals("") && !this.strassenname.equals("") && !this.hausnummer.equals("") && !this.plz.equals("") && !this.ort.equals("")) {
            this.inBearbeitung = false;
            Adresse tempAdresse = new Adresse(this.strassenname, this.hausnummer, this.plz, this.ort);
            Stadion tempStadion = stadion;
            tempStadion.setName(this.name);
            tempStadion.setAdresse(tempAdresse);
            if (!stadionService.pruefeObStadionDatenSchonVorhanden(tempStadion)) {
                stadionService.aendern(tempStadion);
                aktualisieren();
            } else {
                aktualisieren();
                FacesContext.getCurrentInstance().addMessage("StadionAnlegenFenster", new FacesMessage("Ein Stadion mit diesen Daten ist schon vorhanden, eine Bearbeitung ist mit diesen Daten nicht möglich"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("StadionAnlegenFenster", new FacesMessage("Füllen Sie bitte alle Eingabefelder aus!"));
        }
    }

    public String loescheStadion(Stadion stadion) {
        try {
            if (!stadionService.pruefeObBuchungfuerStadionVorhanden(stadion)) {
                if (stadionService.loeschen(stadion) != null) {
                    aktualisieren();
                    return "stadion";
                } else
                    throw new KonnteNichtGeloeschtWerdenFehler();
            } else {
                FacesContext.getCurrentInstance().addMessage("StadionAnlegenFenster", new FacesMessage("Für das Stadion " + stadion.getName() + " ist schon eine Buchung vorhanden, wenn Sie das Stadion löschen wollen löschen Sie zuerst die dazugehörige Buchung"));
                return null;
            }
        } catch (Exception e) {
            logger.log(Level.INFO, e.toString());
            return null;
        }
    }

    public String erzeugeZufallsStadion() {
        this.name = "Stadion-" + (int)(Math.random() * 100000);
        this.strassenname = "Hauptstraße " + (int)(Math.random() * 100000);
        this.hausnummer = (int)(Math.random() * 100000) + " a";
        this.plz = ""+((int)(Math.random() * 100000))+"";
        this.ort = "Regensburg";
        anlegen();
        return "stadion";
    }


}
