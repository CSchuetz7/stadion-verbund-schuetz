package de.stadionVerbundSchuetz.ui.model;

import de.stadionVerbundSchuetz.entity.Adresse;
import de.stadionVerbundSchuetz.entity.Benutzer;
import de.stadionVerbundSchuetz.service.BenutzerService;
import de.stadionVerbundSchuetz.utils.Util;
import lombok.Getter;
import lombok.Setter;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
@SessionScoped
public class BenutzerModel implements Serializable {

    @Inject
    BenutzerService benutzerService;

    @Inject
    private transient Logger logger;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String passwort;

    @Getter
    @Setter
    private String passwort2;

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
    private Benutzer benutzer;

    @Getter
    @Setter
    private Pattern pattern;

    @Getter
    @Setter
    private Matcher matcher;

    @Getter
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public String pruefeAnmeldeinfo() {
        this.benutzer = benutzerService.anmelden(email, passwort);
        if (this.benutzer != null && this.benutzer.getEmail() != null) {
            HttpSession session = Util.findeSitzung();
            session.setAttribute("benutzer", this.benutzer);
            return "profil";
        } else {
            FacesContext.getCurrentInstance().addMessage("AnmeldeFenster:AnmeldeId", new FacesMessage("Ungültige E-Mail oder Passwort! Bitte versuchen Sie es erneut!"));
            return "anmelden";
        }
    }

    public String abmelden() {
        HttpSession session = Util.findeSitzung();
        session.invalidate();
        this.benutzer = null;
        return "anmelden";
    }
    //Methoden zum schnellen Erzeugen eines Admins für Devs
    public String erzeugeAdmin(){
        this.email = "admin@StadionverbundSchuetz.de";
        this.name = "Mitarbeiter";
        this.strassenname = "Hauptstraße";
        this.hausnummer = "32";
        this.plz = "93053";
        this.ort = "Regensburg";
        this.passwort = "123";
        this.passwort2 = "123";
        anmelden();
        return "profil";
    }
    //Methoden zum schnellen Erzeugen eines Gasts für Devs
    public String erzeugeGast(){
        this.email = "gast@gmail.com";
        this.name = "Sportverband Test";
        this.strassenname = "Hauptstraße";
        this.hausnummer = "32";
        this.plz = "93024";
        this.ort = "Regensburg";
        this.passwort = "123";
        this.passwort2 = "123";
        return anmelden();
    }

    public String anmelden() {
        if (!this.email.equals("") &&
                !this.name.equals("") &&
                !this.passwort.equals("") &&
                !this.passwort2.equals("") &&
                !this.hausnummer.equals("") &&
                !this.strassenname.equals("") &&
                !this.ort.equals("")) {
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(this.email);
            if (matcher.matches()) {
                if (this.passwort.equals(this.passwort2)) {
                    Adresse tempAdresse = new Adresse(this.strassenname, this.hausnummer, this.plz, this.ort);
                    this.benutzer = benutzerService.anlegen(this.name, tempAdresse, this.passwort, this.email);
                    if (this.benutzer != null) {
                        this.benutzer = benutzerService.anmelden(benutzer.getEmail(), this.passwort);
                        if (this.benutzer != null && this.benutzer.getEmail() != null) {
                            HttpSession session = Util.findeSitzung();
                            session.setAttribute("benutzer", this.benutzer);
                            return "profil";
                        } else {
                            return "anmelden";
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage("AnmeldeFenster:AnmeldeId", new FacesMessage("Email Adresse ist schon vorhanden. Benutzen Sie eine andere!"));
                        return null;
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage("AnmeldeFenster:AnmeldeId", new FacesMessage("Passwörter stimmen nicht überein"));
                    return null;
                }
            } else {
                FacesContext.getCurrentInstance().addMessage("AnmeldeFenster:AnmeldeId", new FacesMessage("Ungültiges Email Format"));
                return null;
            }
        }
        FacesContext.getCurrentInstance().addMessage("AnmeldeFenster:AnmeldeId", new FacesMessage("Füllen Sie bitte alle Eingabefelder aus!"));
        return null;
    }


    public boolean adminIstAngemeldet() {
        if (this.benutzer != null) {
            if (this.benutzer.getEmail().equals("admin@StadionverbundSchuetz.de")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean istAngemeldet() {
        return this.benutzer != null;
    }
}


