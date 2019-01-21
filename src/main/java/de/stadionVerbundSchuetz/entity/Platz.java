package de.stadionVerbundSchuetz.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name="Platz")
@XmlTransient
public class Platz implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long platz_id;
  @Getter
  @Setter
  private int anzahlSitzeReihe;

  @Getter
  @Setter
  private int anzahlReihe;

  @ManyToOne(fetch = FetchType.EAGER)
  @Getter
  @Setter
  private Stadion stadionPlatz;


  public Platz(){}

  public Platz(int anzahlSitzeReihe, int anzahlReihe, Stadion stadion) {
    this.anzahlSitzeReihe = anzahlSitzeReihe;
    this.anzahlReihe = anzahlReihe;
    this.stadionPlatz = stadion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Platz platz = (Platz) o;
    return platz_id == platz.platz_id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(platz_id);
  }
}
