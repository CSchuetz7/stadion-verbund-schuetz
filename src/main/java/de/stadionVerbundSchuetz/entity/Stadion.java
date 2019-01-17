package de.stadionVerbundSchuetz.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "Stadion")
public class Stadion implements Serializable {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long stadion_id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Adresse adresse;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany( mappedBy = "stadionBlock", fetch = FetchType.LAZY, cascade = CascadeType.MERGE, orphanRemoval = true)
    @Getter
    @Setter
    @XmlTransient
    private List<Block> bloecke;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany( mappedBy = "stadionPlatz",fetch = FetchType.LAZY, orphanRemoval = true)
    @Getter
    @Setter
    @XmlTransient
    private List<Platz> plaetze;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany( mappedBy = "stadionKategorie",fetch = FetchType.LAZY, orphanRemoval = true)
    @Getter
    @Setter
    @XmlTransient
    private List<Kategorie> kategorien;

    public Stadion() {
    }

    public Stadion(String name, Adresse adresse) {
        this.name = name;
        this.adresse = adresse;
    }

    public Stadion(long stadion_id, String name, Adresse adresse) {
        this.stadion_id = stadion_id;
        this.name = name;
        this.adresse = adresse;
    }

    public Stadion(String name) {
        this.name = name;
    }

    public Stadion(String name, Adresse adresse, List<Block> bloecke) {
        this.name = name;
        this.adresse = adresse;
        this.bloecke = bloecke;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stadion stadion = (Stadion) o;

        return stadion_id == stadion.stadion_id;
    }

    @Override
    public int hashCode() {
        return (int) (stadion_id ^ (stadion_id >>> 32));
    }
}
