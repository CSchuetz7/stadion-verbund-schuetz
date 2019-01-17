package de.stadionVerbundSchuetz.ui.model;

import de.stadionVerbundSchuetz.converter.StadionKonvertierer;
import de.stadionVerbundSchuetz.entity.Buchung;
import de.stadionVerbundSchuetz.entity.Stadion;
import de.stadionVerbundSchuetz.service.BuchungService;
import de.stadionVerbundSchuetz.service.TicketDummyService;
import de.stadionVerbundSchuetz.service.TicketServiceIF;
import de.stadionVerbundSchuetz.utils.Util;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@SessionScoped
public class BuchungModel implements Serializable {

    @Inject
    BuchungService buchungService;

    @Inject
    StadionModel stadionService;

    @Inject
    TicketServiceIF ticketServiceIF;

    @Inject
    Logger logger;

    @Getter
    @Setter
    private long spielid;

    @Getter
    @Setter
    private List<Buchung> buchungListe;

    @Getter
    @Setter
    private Date spieldatum;

    @Getter
    @Setter
    private Date buchungdatum;
    @Getter
    @Setter
    private Stadion stadion;

    @Inject
    @Getter
    @Setter
    StadionKonvertierer stadionTypKonv;

    @Getter
    @Setter
    private List<Stadion> stadionListe;

    public void initialisieren() {
        if (stadionService.getStadionListe() != null) {
            this.stadionListe = stadionService.getStadionListe();
        } else {
            this.stadionListe = stadionService.findeAlleStadien();
        }
        this.buchungListe = buchungService.findeBuchungFuerBenutzer(Util.findeBenutzer());
    }

    public void aktualisiereBuchung() {
        this.buchungListe = buchungService.findeBuchungFuerBenutzer(Util.findeBenutzer());
    }

    public String ticketErstellen(Buchung buchung){
        try{
            ticketServiceIF.erstelleTicket(buchung);
            aktualisiereBuchung();
            return "buchungAnlegen";
        }catch (Exception e){
            logger.log(Level.INFO, "Exception: " + e.toString());
            return null;
        }
    }

    public String anlegen() {
        if (this.stadion != null &&
                this.spieldatum != null &&
                this.spielid >= 0) {
            if (buchungService.pruefeSpielDatumVerfuegbar(this.spieldatum, this.stadion).size() == 0) {
                if (buchungService.pruefeSpielIdVorhanden(this.spielid).size() == 0) {
                    if (this.spieldatum.compareTo(new Date()) >= 0) {
                        Buchung tempBuchung = new Buchung(this.spielid, this.spieldatum, new Date(), this.stadion, Util.findeBenutzer(), false);
                        buchungService.anlegen(tempBuchung);
                        aktualisiereBuchung();
                        return "angelegt";
                    } else {
                        FacesContext.getCurrentInstance().addMessage("buchungAnlegenForm:AnlegenId", new FacesMessage("Spieldatum liegt in der Vergangenheit"));
                        return null;
                    }
                } else {
                    // Spiel-Id ist unique, da hier der Bezug auf das Spiel gemacht wird (Wettbüro, Ticketverkauf)
                    FacesContext.getCurrentInstance().addMessage("buchungAnlegenForm:AnlegenId", new FacesMessage("Diese Spiel-Id ist bereits vorhanden"));
                    return null;
                }
            } else {
                FacesContext.getCurrentInstance().addMessage("buchungAnlegenForm:AnlegenId", new FacesMessage("Dieses Spieldatum in diesem Stadion ist nicht mehr verfügbar"));
                return null;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("buchungAnlegenForm:AnlegenId", new FacesMessage("Füllen Sie bitte alle Eingabefelder aus!"));
            return null;
        }
    }

    public List<Buchung> findeBuchungFuerBenutzer() {
        return buchungService.findeBuchungFuerBenutzer(Util.findeBenutzer());
    }

    public String loescheBuchung (Buchung buchung){
        buchungService.loeschen(buchung);
        aktualisiereBuchung();
        return "buchung";
    }

}
