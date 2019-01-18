package de.stadionVerbundSchuetz.ui.model;

import de.stadionVerbundSchuetz.entity.Adresse;
import de.stadionVerbundSchuetz.entity.Kategorie;
import de.stadionVerbundSchuetz.entity.Stadion;
import de.stadionVerbundSchuetz.service.AdministrationService;
import de.stadionVerbundSchuetz.service.BuchungService;
import de.stadionVerbundSchuetz.service.StadionService;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Named
@SessionScoped
public class AdministrationModel implements Serializable {

  @Inject
  AdministrationService administrationService;

  @Inject
  BuchungService buchungService;

  @Getter
  @Setter
  private String selectString;

  @Getter
  @Setter
  private List resultSelectString;

  public void loescheAlles() {
    administrationService.loescheAlles();
  }

  public void select(String s) {
    this.resultSelectString = administrationService.select(s);
    if (resultSelectString == null) {
      FacesContext.getCurrentInstance().addMessage("AdminFenster:SelectId", new FacesMessage("Es ist ein Fehler bei ihrem Select Befehl aufgetreten"));
    }
  }
  //Schnelle Erzeugung von sinnvollen Daten (Stadien, Kategorie, Block und Platz)
  public void erzeugeStadienDaten(){
    administrationService.erzeugeStadienDaten();
  }

  public void alleBuchungenLoeschen(){
    administrationService.alleBuchungenLöschen();
  }

  public void testBuchung() {
    try {

      SimpleDateFormat datumsFormat= new SimpleDateFormat("dd/MM/yyyy");
      Date d1= datumsFormat.parse("05/05/2019");
      Date d2= datumsFormat.parse("31/12/2035");

      Date zufallsDatum = new Date(ThreadLocalRandom.current()
              .nextLong(d1.getTime(), d2.getTime()));

      buchungService.bucheStadionAutomatisch((int)(Math.random()*10000), (int)((Math.random()*1)+1), zufallsDatum);
    } catch (Exception e) {
      System.out.println(e);
    }

  }
}
