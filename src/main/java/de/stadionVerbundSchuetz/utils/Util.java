package de.stadionVerbundSchuetz.utils;

import de.stadionVerbundSchuetz.entity.Benutzer;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Util {

    public static Benutzer findeBenutzer() {
        HttpSession session = findeSitzung();
        if (session != null)
            return (Benutzer) session.getAttribute("benutzer");
        else
            return null;
    }

    /*
    public static HttpServletRequest findeAnfrage() {
        return (HttpServletRequest) FacesContext.
                getCurrentInstance().
                getExternalContext().getRequest();
    }*/

    /*
    public static String findeBenutzername() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return session.getAttribute("benutzer").toString();
    } */

    public static HttpSession findeSitzung() {
        return (HttpSession)
                FacesContext.
                        getCurrentInstance().
                        getExternalContext().
                        getSession(false);
    }


}