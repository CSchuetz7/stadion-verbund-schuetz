
package de.wsdl.ticketEckert;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für generatedIdEntity complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="generatedIdEntity"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://service.ticketeckert.de/}singleIdEntity"&gt;
 *       &lt;sequence&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "generatedIdEntity")
@XmlSeeAlso({
    Ticket.class
})
public abstract class GeneratedIdEntity
    extends SingleIdEntity
{


}
