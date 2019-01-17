
package de.wsdl.ticketEckert;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.wsdl.ticketEckert package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ErstelleTicket_QNAME = new QName("http://service.ticketeckert.de/", "erstelleTicket");
    private final static QName _ErstelleTicketResponse_QNAME = new QName("http://service.ticketeckert.de/", "erstelleTicketResponse");
    private final static QName _ErstelleTickets_QNAME = new QName("http://service.ticketeckert.de/", "erstelleTickets");
    private final static QName _ErstelleTicketsResponse_QNAME = new QName("http://service.ticketeckert.de/", "erstelleTicketsResponse");
    private final static QName _Ticket_QNAME = new QName("http://service.ticketeckert.de/", "ticket");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.wsdl.ticketEckert
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ErstelleTicket }
     * 
     */
    public ErstelleTicket createErstelleTicket() {
        return new ErstelleTicket();
    }

    /**
     * Create an instance of {@link ErstelleTicketResponse }
     * 
     */
    public ErstelleTicketResponse createErstelleTicketResponse() {
        return new ErstelleTicketResponse();
    }

    /**
     * Create an instance of {@link ErstelleTickets }
     * 
     */
    public ErstelleTickets createErstelleTickets() {
        return new ErstelleTickets();
    }

    /**
     * Create an instance of {@link ErstelleTicketsResponse }
     * 
     */
    public ErstelleTicketsResponse createErstelleTicketsResponse() {
        return new ErstelleTicketsResponse();
    }

    /**
     * Create an instance of {@link Ticket }
     * 
     */
    public Ticket createTicket() {
        return new Ticket();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ErstelleTicket }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ErstelleTicket }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.ticketeckert.de/", name = "erstelleTicket")
    public JAXBElement<ErstelleTicket> createErstelleTicket(ErstelleTicket value) {
        return new JAXBElement<ErstelleTicket>(_ErstelleTicket_QNAME, ErstelleTicket.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ErstelleTicketResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ErstelleTicketResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.ticketeckert.de/", name = "erstelleTicketResponse")
    public JAXBElement<ErstelleTicketResponse> createErstelleTicketResponse(ErstelleTicketResponse value) {
        return new JAXBElement<ErstelleTicketResponse>(_ErstelleTicketResponse_QNAME, ErstelleTicketResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ErstelleTickets }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ErstelleTickets }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.ticketeckert.de/", name = "erstelleTickets")
    public JAXBElement<ErstelleTickets> createErstelleTickets(ErstelleTickets value) {
        return new JAXBElement<ErstelleTickets>(_ErstelleTickets_QNAME, ErstelleTickets.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ErstelleTicketsResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ErstelleTicketsResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.ticketeckert.de/", name = "erstelleTicketsResponse")
    public JAXBElement<ErstelleTicketsResponse> createErstelleTicketsResponse(ErstelleTicketsResponse value) {
        return new JAXBElement<ErstelleTicketsResponse>(_ErstelleTicketsResponse_QNAME, ErstelleTicketsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ticket }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Ticket }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.ticketeckert.de/", name = "ticket")
    public JAXBElement<Ticket> createTicket(Ticket value) {
        return new JAXBElement<Ticket>(_Ticket_QNAME, Ticket.class, null, value);
    }

}
