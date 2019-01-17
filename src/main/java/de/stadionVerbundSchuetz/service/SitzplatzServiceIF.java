package de.stadionVerbundSchuetz.service;

import de.stadionVerbundSchuetz.entity.Block;
import de.stadionVerbundSchuetz.entity.Kategorie;
import de.stadionVerbundSchuetz.entity.Platz;
import de.stadionVerbundSchuetz.entity.Stadion;

import java.io.Serializable;
import java.util.List;

public interface SitzplatzServiceIF extends Serializable {

    public Kategorie findeKategorie(long kategorieNr);


}
