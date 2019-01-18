package de.stadionVerbundSchuetz.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name="Block")
@XmlTransient
public class Block implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Getter
  private long block_id;
  @Getter
  @Setter
  private String name;
  @Getter
  @Setter
  //private Enum<Ausrichtung> ausrichtung;
  private String ausrichtung;

  @Getter
  @Setter
  @OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true)
  private Platz plaetze;

  @ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.DETACH)
  @Getter
  @Setter
  private Kategorie kategorie;

  @ManyToOne(fetch = FetchType.EAGER)
  @Getter
  @Setter
  private Stadion stadionBlock;

  public Block(){
  }

  public Block(String name, String ausrichtung, Platz plaetze, Kategorie kategorie, Stadion stadionBlock) {
    this.name = name;
    this.ausrichtung = ausrichtung;
    this.plaetze = plaetze;
    this.kategorie = kategorie;
    this.stadionBlock = stadionBlock;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Block block = (Block) o;
    return block_id == block.block_id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(block_id);
  }
}
