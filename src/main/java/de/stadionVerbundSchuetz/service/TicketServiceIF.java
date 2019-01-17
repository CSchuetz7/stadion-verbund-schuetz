package de.StadionverbandSchuetz.service;

import de.StadionverbandSchuetz.entity.Buchung;

import java.io.Serializable;

public interface TicketServiceIF extends Serializable {

  void erstelleTicket(Buchung buchung);

}
