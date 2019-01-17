package de.StadionverbandSchuetz.service;

import de.StadionverbandSchuetz.entity.Block;
import de.StadionverbandSchuetz.entity.Kategorie;
import de.StadionverbandSchuetz.entity.Platz;
import de.StadionverbandSchuetz.entity.Stadion;

import java.io.Serializable;
import java.util.List;

public interface SitzplatzServiceIF extends Serializable {

    public Kategorie findeKategorie(long kategorieNr);


}
