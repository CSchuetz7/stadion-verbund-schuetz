package de.stadionVerbundSchuetz.service;

import de.stadionVerbundSchuetz.entity.Stadion;

import java.io.Serializable;

public interface StadionServiceIF extends Serializable {

     Stadion findeStadion(long stadionNr);

}
