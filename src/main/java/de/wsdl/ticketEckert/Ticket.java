
package de.wsdl.ticketEckert;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ticket complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ticket"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://service.ticketeckert.de/}generatedIdEntity"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="tickettyp" type="{http://service.ticketeckert.de/}tickettyp" minOccurs="0"/&gt;
 *         &lt;element name="platz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="preis" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="kategorie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="spiel_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="stadion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ticket", propOrder = {
    "tickettyp",
    "platz",
    "preis",
    "kategorie",
    "spielId",
    "stadion"
})
public class Ticket
    extends GeneratedIdEntity
{

    protected Integer tickettyp;
    protected String platz;
    protected int preis;
    protected String kategorie;
    @XmlElement(name = "spiel_id")
    protected long spielId;
    protected String stadion;

    /**
     * Ruft den Wert der tickettyp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTickettyp() {
        return tickettyp;
    }

    /**
     * Legt den Wert der tickettyp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTickettyp(Integer value) {
        this.tickettyp = value;
    }

    /**
     * Ruft den Wert der platz-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlatz() {
        return platz;
    }

    /**
     * Legt den Wert der platz-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlatz(String value) {
        this.platz = value;
    }

    /**
     * Ruft den Wert der preis-Eigenschaft ab.
     * 
     */
    public int getPreis() {
        return preis;
    }

    /**
     * Legt den Wert der preis-Eigenschaft fest.
     * 
     */
    public void setPreis(int value) {
        this.preis = value;
    }

    /**
     * Ruft den Wert der kategorie-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKategorie() {
        return kategorie;
    }

    /**
     * Legt den Wert der kategorie-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKategorie(String value) {
        this.kategorie = value;
    }

    /**
     * Ruft den Wert der spielId-Eigenschaft ab.
     * 
     */
    public long getSpielId() {
        return spielId;
    }

    /**
     * Legt den Wert der spielId-Eigenschaft fest.
     * 
     */
    public void setSpielId(long value) {
        this.spielId = value;
    }

    /**
     * Ruft den Wert der stadion-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStadion() {
        return stadion;
    }

    /**
     * Legt den Wert der stadion-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStadion(String value) {
        this.stadion = value;
    }

}
