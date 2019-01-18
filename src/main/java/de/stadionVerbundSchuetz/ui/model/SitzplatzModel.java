package de.stadionVerbundSchuetz.ui.model;

import de.stadionVerbundSchuetz.converter.KategorieKonvertierer;
import de.stadionVerbundSchuetz.converter.StadionKonvertierer;
import de.stadionVerbundSchuetz.entity.Block;
import de.stadionVerbundSchuetz.entity.Kategorie;
import de.stadionVerbundSchuetz.entity.Platz;
import de.stadionVerbundSchuetz.entity.Stadion;
import de.stadionVerbundSchuetz.service.SitzplatzService;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@SessionScoped
public class SitzplatzModel implements Serializable {

    @Inject
    SitzplatzService sitzplatzService;

    @Inject
    StadionModel stadionService;

    @Inject
    private transient Logger logger;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Boolean inBearbeitungKategorie;

    @Getter
    @Setter
    private Boolean inBearbeitungBlockPlatz;

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
    private List<Kategorie> kategorieListe;

    @Getter
    @Setter
    private List<Block> blockListe;

    @Getter
    @Setter
    private List<Kategorie> kategorieStadionListe;

    @Getter
    @Setter
    private Stadion stadion;

    @Inject
    @Getter
    @Setter
    StadionKonvertierer stadionTypKonv;

    @Inject
    @Getter
    @Setter
    KategorieKonvertierer kategorieTypKonv;

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
    private double preis = 1.0;
    @Getter
    @Setter
    private String blockName;
    @Getter
    @Setter
    private String ausrichtung;

    @Setter
    @Getter
    private Kategorie kategorie;

    @Setter
    @Getter
    private Block block;

    @Getter
    @Setter
    private int anzahlPlaetzeProReihe;
    @Getter
    @Setter
    private int anzahlReihe;

    public void initialisieren() {
        if (this.stadionService.getStadionListe() != null) {
            this.stadionListe = stadionService.getStadionListe();
        } else {
            this.stadionListe = stadionService.findeAlleStadien();
        }
            this.kategorieListe = sitzplatzService.findeAlleKategorien();
            this.blockListe = sitzplatzService.findeAlleBloecke();
    }

    private void aktualisiereKategorie() {
        this.kategorieListe = sitzplatzService.findeAlleKategorien();
    }
    private void aktualisiereBlockPlatz() {
        this.blockListe = sitzplatzService.findeAlleBloecke();
    }

    public String kategorieAnlegen() {
        if (this.stadion != null) {
            if (!this.kategorieName.equals("") && this.kategorienr >= 0 && this.preis > 0) {
                Kategorie tempKategorie = new Kategorie(this.kategorienr, this.stehplatz, this.kategorieName, this.preis, this.stadion);

                if (!sitzplatzService.pruefeKategorieNrUndNameVorhanden(tempKategorie, stadion)) {
                    if (sitzplatzService.kategorieAnlegen(tempKategorie, stadion) != null) {
                        return "angelegt";
                    } else {
                        return null;
                    }

                } else {
                    FacesContext.getCurrentInstance().addMessage("kategorieAnlegenForm:AnlegenId", new FacesMessage("Kategorie-Nummer oder Kategorie-Name für das Stadion ist schon vorhanden"));
                    return null;
                }
            } else {
                FacesContext.getCurrentInstance().addMessage("kategorieAnlegenForm:AnlegenId", new FacesMessage("Füllen Sie bitte alle Eingabefelder richtig aus!"));
                return null;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("kategorieAnlegenForm:AnlegenId", new FacesMessage("Legen Sie zuerst ein Stadion an"));
            return null;
        }
    }

    public void blockPlatzAnlegenStadionVeraenderung(Stadion stadion){
        this.kategorieStadionListe = sitzplatzService.findeKategorienNachStadion(stadion);
    }

    public String blockPlatzAnlegen() {
        if (this.stadion != null) {
            if (this.kategorie != null) {
                if (!this.blockName.equals("") && this.anzahlPlaetzeProReihe > 0 && this.anzahlReihe > 0) {
                    Kategorie tempKategorie = this.kategorie;
                    Platz tempPlatz = new Platz(anzahlPlaetzeProReihe, anzahlReihe, this.stadion);
                    Block tempBlock = new Block(this.blockName, this.ausrichtung, tempPlatz, tempKategorie, this.stadion);
                    if (!sitzplatzService.pruefeObBlockPlatzDatenSchonVorhanden(tempBlock)){
                        try {
                            this.sitzplatzService.blockHinzufuegen(this.stadion, tempBlock);
                        } catch (Exception e) {
                            logger.log(Level.INFO, "Exception: " + e.toString());
                            return null;
                        }
                    return "angelegt";
                } else {
                        FacesContext.getCurrentInstance().addMessage("blockPlatzAnlegenForm:AnlegenId", new FacesMessage("Ein Block mit diesem Namen für das Stadion ist schon vorhanden"));
                        return null;
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage("blockPlatzAnlegenForm:AnlegenId", new FacesMessage("Füllen Sie bitte alle Eingabefelder aus!"));
                    return null;
                }
            } else {
                FacesContext.getCurrentInstance().addMessage("blockPlatzAnlegenForm:AnlegenId", new FacesMessage("Legen Sie zuerst eine Kategorie an"));
                return null;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("blockPlatzAnlegenForm:AnlegenId", new FacesMessage("Legen Sie zuerst ein Stadion an"));
            return null;
        }
    }


    public List<Block> findeAlleBloecke() {
        try {
            return sitzplatzService.findeAlleBloecke();
        } catch (Exception e) {
            logger.log(Level.INFO, "Exception: " + e.toString());
            return null;
        }
    }

    public List<Kategorie> findeAlleKategorien() {
        try {
            return sitzplatzService.findeAlleKategorien();
        } catch (Exception e) {
            logger.log(Level.INFO, "Exception: " + e.toString());
            return null;
        }
    }

    public List<Kategorie> findeKategorienNachStadion(Stadion stadion) {
        try {
            return sitzplatzService.findeKategorienNachStadion(stadion);
        } catch (Exception e) {
            logger.log(Level.INFO, "Exception: " + e.toString());
            return null;
        }
    }

    public void inAenderungVorbereitenBlockPlatz(Block block) {
        this.inBearbeitungBlockPlatz = true;
        this.stadion = block.getStadionBlock();
        this.kategorie = block.getKategorie();
        this.blockName = block.getName();
        this.ausrichtung = block.getAusrichtung();
        this.anzahlPlaetzeProReihe = block.getPlaetze().getAnzahlSitzeReihe();
        this.anzahlReihe = block.getPlaetze().getAnzahlReihe();
        this.block = block;
    }

    public void aendereBlockPlatzAbbrechen() {
        this.inBearbeitungBlockPlatz = false;
    }

    public void aendereBlockPlatz() {
        if (!this.blockName.equals("") && this.kategorie != null) {
            this.inBearbeitungBlockPlatz = false;
            Block tempBlock = block;
            tempBlock.setStadionBlock(this.stadion);
            tempBlock.setKategorie(this.kategorie);
            tempBlock.setName(this.blockName);
            tempBlock.setAusrichtung(this.ausrichtung);
            if (!sitzplatzService.pruefeObBlockPlatzDatenSchonVorhanden(tempBlock)) {
                sitzplatzService.aendernBlockPlatz(tempBlock);
                aktualisiereBlockPlatz();
            } else {
                FacesContext.getCurrentInstance().addMessage("blockPlatzAnlegenForm:AnlegenId", new FacesMessage("Ein Block mit diesen Daten ist schon vorhanden, eine Bearbeitung ist mit diesen Daten nicht möglich"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("blockPlatzAnlegenForm:AnlegenId", new FacesMessage("Füllen Sie bitte alle Eingabefelder aus!"));
        }
    }

    public String loescheBlockPlatz(Block block) {
        sitzplatzService.loescheBlockPlatz(block);
        return "blockPlatzAnlegen";
    }


    public void inAenderungVorbereitenKategorie(Kategorie kategorie) {
        this.inBearbeitungKategorie = true;
        this.kategorienr = kategorie.getKategorienr();
        this.kategorieName = kategorie.getName();
        this.preis = kategorie.getPreis();
        this.stehplatz = kategorie.getStehplatz();
        this.stadion = kategorie.getStadionKategorie();
        this.kategorie = kategorie;
    }

    public void aendereKategorieAbbrechen() {
        this.inBearbeitungKategorie = false;
    }

    public void aendereKategorie() {
        if (!this.kategorieName.equals("") && this.kategorienr>=0 && this.preis > 0 && this.stadion != null) {
            this.inBearbeitungKategorie = false;
            Kategorie tempKategorie = kategorie;
            tempKategorie.setPreis(this.preis);
            tempKategorie.setName(this.kategorieName);
            tempKategorie.setStehplatz(this.stehplatz);
            tempKategorie.setKategorienr(this.kategorienr);
            if (!sitzplatzService.pruefeObKategorieDatenSchonVorhanden(tempKategorie)) {
                sitzplatzService.aendernKategorie(tempKategorie);
                aktualisiereKategorie();
            } else{
                FacesContext.getCurrentInstance().addMessage("kategorieAnlegenForm", new FacesMessage("Eine Kategorie mit diesen Daten ist schon vorhanden, eine Bearbeitung ist mit diesen Daten nicht möglich"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("kategorieAnlegenForm", new FacesMessage("Füllen Sie bitte alle Eingabefelder aus!"));
        }
    }

    public String loescheKategorie(Kategorie kategorie) {
        try {
            if (!sitzplatzService.pruefeObBlockPlatzfuerKategorieVorhanden(kategorie)) {
                sitzplatzService.loescheKategorie(kategorie);
                return "kategorieAnlegen";
            } else {
                FacesContext.getCurrentInstance().addMessage("kategorieAnlegenForm", new FacesMessage("Für die Kategorie " + kategorie.getName() + " ist schon ein Block&Platz vorhanden, wenn Sie das Stadion löschen wollen löschen Sie zuerst die dazugehörige Buchung"));
                return null;
            }
        } catch (Exception e){
            return null;
        }

    }

}
