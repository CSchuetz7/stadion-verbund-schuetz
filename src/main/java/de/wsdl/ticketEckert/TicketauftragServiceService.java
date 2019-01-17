
package de.wsdl.ticketEckert;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.3.1-SNAPSHOT
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "TicketauftragServiceService", targetNamespace = "http://service.ticketeckert.de/", wsdlLocation = "http://im-lamport:8080/TicketEckert-0.1/TicketauftragService?WSDL")
public class TicketauftragServiceService
    extends Service
{

    private final static URL TICKETAUFTRAGSERVICESERVICE_WSDL_LOCATION;
    private final static WebServiceException TICKETAUFTRAGSERVICESERVICE_EXCEPTION;
    private final static QName TICKETAUFTRAGSERVICESERVICE_QNAME = new QName("http://service.ticketeckert.de/", "TicketauftragServiceService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://im-lamport:8080/TicketEckert-0.1/TicketauftragService?WSDL");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        TICKETAUFTRAGSERVICESERVICE_WSDL_LOCATION = url;
        TICKETAUFTRAGSERVICESERVICE_EXCEPTION = e;
    }

    public TicketauftragServiceService() {
        super(__getWsdlLocation(), TICKETAUFTRAGSERVICESERVICE_QNAME);
    }

    public TicketauftragServiceService(WebServiceFeature... features) {
        super(__getWsdlLocation(), TICKETAUFTRAGSERVICESERVICE_QNAME, features);
    }

    public TicketauftragServiceService(URL wsdlLocation) {
        super(wsdlLocation, TICKETAUFTRAGSERVICESERVICE_QNAME);
    }

    public TicketauftragServiceService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, TICKETAUFTRAGSERVICESERVICE_QNAME, features);
    }

    public TicketauftragServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public TicketauftragServiceService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns TicketauftragService
     */
    @WebEndpoint(name = "TicketauftragServicePort")
    public TicketauftragService getTicketauftragServicePort() {
        return super.getPort(new QName("http://service.ticketeckert.de/", "TicketauftragServicePort"), TicketauftragService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns TicketauftragService
     */
    @WebEndpoint(name = "TicketauftragServicePort")
    public TicketauftragService getTicketauftragServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://service.ticketeckert.de/", "TicketauftragServicePort"), TicketauftragService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (TICKETAUFTRAGSERVICESERVICE_EXCEPTION!= null) {
            throw TICKETAUFTRAGSERVICESERVICE_EXCEPTION;
        }
        return TICKETAUFTRAGSERVICESERVICE_WSDL_LOCATION;
    }

}