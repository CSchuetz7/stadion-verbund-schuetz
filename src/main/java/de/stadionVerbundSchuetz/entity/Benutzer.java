package de.StadionverbandSchuetz.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name="Benutzer")
public class Benutzer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long benutzer_id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    @Embedded
    private Adresse adresse;

    @Getter
    @Setter
    private String passwort;

    @Getter
    @Setter
    private String email;

    public Benutzer() {
    }

    public Benutzer(String name, Adresse adresse, String passwort, String email) {
        this.name = name;
        this.adresse = adresse;
        this.passwort = passwort;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Benutzer benutzer = (Benutzer) o;
        return benutzer_id == benutzer.benutzer_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(benutzer_id);
    }

}
