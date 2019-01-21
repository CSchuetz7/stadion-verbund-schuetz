package de.stadionVerbundSchuetz.service;
import de.stadionVerbundSchuetz.entity.Kategorie;

import java.io.Serializable;


public interface SitzplatzServiceIF extends Serializable {

     Kategorie findeKategorie(long kategorieNr);


}
