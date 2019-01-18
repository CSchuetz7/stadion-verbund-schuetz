package de.stadionVerbundSchuetz.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlTransient;

@Embeddable
public class Adresse implements Serializable {

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

  public Adresse(){};

  public Adresse(String strassenname, String hausnummer, String plz, String ort) {
    this.strassenname = strassenname;
    this.hausnummer = hausnummer;
    this.plz = plz;
    this.ort = ort;
  }

  @Override
  public String toString() {
    return strassenname+" "+hausnummer+", "+plz+" "+ort;
  }
}
