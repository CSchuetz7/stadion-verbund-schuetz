package de.stadionVerbundSchuetz.entity;

import de.stadionVerbundSchuetz.ui.model.StadionModel;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name="Buchung")
public class Buchung implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Getter
  @Setter
  private long buchung_id;
  @Getter
  @Setter
  private long spielid;
  @Getter
  @Setter
  @Temporal(TemporalType.DATE)
  private Date spieldatum;
  @Getter
  @Setter
  @Temporal(TemporalType.DATE)
  private Date buchungdatum;

  @Getter
  @Setter
  private Boolean ticketErstellt;

  @Getter
  @Setter
  @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.MERGE)
  private Benutzer benutzerId;

  @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.MERGE)
  @Getter
  @Setter
  private Stadion stadion;

  @Inject
  StadionModel stadionModel;

  public Buchung() {
  }

  public Buchung(long spielid, Date spieldatum, Date buchungdatum, Stadion stadion, Benutzer benutzerId, Boolean ticketErstellt) {
    this.spielid = spielid;
    this.spieldatum = spieldatum;
    this.buchungdatum = buchungdatum;
    this.stadion = stadion;
    this.benutzerId = benutzerId;
    this.ticketErstellt = ticketErstellt;
  }

  public Buchung(long spielid, Date spieldatum, Stadion stadion) {
    this.spielid = spielid;
    this.spieldatum = spieldatum;
    this.buchungdatum = new Date();
    this.stadion = stadion;
    this.benutzerId = null;
    this.ticketErstellt = false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Buchung buchung = (Buchung) o;
    return buchung_id == buchung.buchung_id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(buchung_id);
  }
}
