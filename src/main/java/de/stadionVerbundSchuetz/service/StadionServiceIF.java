package de.StadionverbandSchuetz.service;

import de.StadionverbandSchuetz.entity.Stadion;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import java.io.Serializable;

public interface StadionServiceIF extends Serializable {

     Stadion findeStadion(long stadionNr);

}
