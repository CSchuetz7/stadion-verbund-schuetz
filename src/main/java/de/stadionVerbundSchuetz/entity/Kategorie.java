package de.StadionverbandSchuetz.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="Kategorie")
@XmlTransient
public class Kategorie implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Getter
  private long kategorie_id;
  @Getter
  @Setter
  private int kategorienr;
  @Getter
  @Setter
  private Boolean stehplatz;
  @Getter
  @Setter
  private String name;
  @Getter
  @Setter
  private double preis;

  @ManyToOne(fetch = FetchType.EAGER)
  @Getter
  @Setter
  private Stadion stadionKategorie;

  public Kategorie(){}

  public Kategorie(int kategorienr, Boolean stehplatz, String name, double preis, Stadion stadion) {
    this.kategorienr = kategorienr;
    this.stehplatz = stehplatz;
    this.name = name;
    this.preis = preis;
    this.stadionKategorie = stadion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Kategorie kategorie = (Kategorie) o;
    return kategorie_id == kategorie.kategorie_id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(kategorie_id);
  }
}

