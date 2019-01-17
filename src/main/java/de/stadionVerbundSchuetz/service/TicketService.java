package de.StadionverbandSchuetz.service;

import de.StadionverbandSchuetz.entity.Block;
import de.StadionverbandSchuetz.entity.Buchung;
import de.StadionverbandSchuetz.entity.Platz;
import de.StadionverbandSchuetz.entity.Stadion;
import de.StadionverbandSchuetz.exceptions.KeineRueckmeldungBeimTicketVersenden;
import de.wsdl.ticketEckert.Ticket;
import de.wsdl.ticketEckert.TicketauftragService;
import de.wsdl.ticketEckert.TicketauftragServiceService;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
@Alternative
public class TicketService implements TicketServiceIF, Serializable {

    @Inject
    BuchungService buchungService;

    @Resource
    private UserTransaction userTransaction;

    ExecutorService myExecutor = Executors.newCachedThreadPool();

    @Override
    public void erstelleTicket(Buchung buchung) {
        myExecutor.execute(new Runnable() {
            public void run() {
                erstelleTicketMethod(buchung);
            }
        });
    }

    @Inject
    private Logger logger;

    @PersistenceContext(unitName = "stadionVerbundPU")
    private EntityManager entityManager;

    public void erstelleTicketMethod(Buchung buchung) {
        try {
            TicketauftragServiceService ticketauftragService = new TicketauftragServiceService();
            TicketauftragService stub = ticketauftragService.getTicketauftragServicePort();
            de.wsdl.ticketEckert.Ticket ticket = new de.wsdl.ticketEckert.Ticket();
            Stadion stadion = buchung.getStadion();
            Stadion stadionTemp = entityManager.find(Stadion.class, stadion.getStadion_id());
            if (stadionTemp != null) {
                for (Block itemBlock : stadionTemp.getBloecke()) {
                    de.wsdl.ticketEckert.Ticket ticketTemp = new de.wsdl.ticketEckert.Ticket();
                    //Ticket in (INT) Cent anstatt (Double) Euro
                    int preisInCent = (int) (itemBlock.getKategorie().getPreis() * 100);
                    ticketTemp.setPreis(preisInCent);
                    ticketTemp.setKategorie(itemBlock.getKategorie().getName());
                    ticketTemp.setSpielId(buchung.getSpielid());
                    ticketTemp.setStadion(stadionTemp.getName());
                    //1 Sitzplatz, 2 Stehplatz
                    if (itemBlock.getKategorie().getStehplatz()) {
                        ticketTemp.setTickettyp(2);
                    } else {
                        ticketTemp.setTickettyp(1);
                    }
                    for (int reiheNr = 1; reiheNr <= itemBlock.getPlaetze().getAnzahlReihe(); reiheNr++) {
                        for (int sitzNr = 1; sitzNr <= itemBlock.getPlaetze().getAnzahlSitzeReihe(); sitzNr++) {
                            String platz = "ReiheNr: " + reiheNr + ", SitzNr: " + sitzNr;
                            ticketTemp.setPlatz(platz);
                            Ticket returnTicket = stub.erstelleTicket(ticketTemp);
                            if (returnTicket == null) {
                                throw new KeineRueckmeldungBeimTicketVersenden("Keine Rückmeldung beim Ticket versenden");
                            }
                        }
                    }
                }
                setzeTicketErstelltBeiBuchung(buchung, true);
            }
        } catch (Exception e) {
            logger.log(Level.INFO, "Exception: " + e.toString());
            myExecutor.shutdown();
        }
    }

    @Transactional
    public Buchung setzeTicketErstelltBeiBuchung(Buchung buchung, Boolean ticketErstellt){
        Buchung buchungTemp = entityManager.find(Buchung.class, buchung.getBuchung_id());
        if (buchungTemp != null) {
            buchungTemp.setTicketErstellt(ticketErstellt);
            try {
                userTransaction.begin();
                Buchung temp = entityManager.merge(buchungTemp);
                userTransaction.commit();
                return temp;
            } catch (Exception e){
                return null;
            }
        }
        else return null;
    }

}