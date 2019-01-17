package de.stadionVerbundSchuetz.service;

import de.stadionVerbundSchuetz.entity.Buchung;

import java.io.Serializable;

public interface TicketServiceIF extends Serializable {

  void erstelleTicket(Buchung buchung);

}
