package de.StadionverbandSchuetz.converter;

import de.StadionverbandSchuetz.entity.Kategorie;
import de.StadionverbandSchuetz.service.SitzplatzServiceIF;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ApplicationScoped
public class KategorieKonvertierer implements Converter {

    @Inject
    SitzplatzServiceIF sitzplatzService;


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null){
            return "";
        }
        Kategorie kategorie = sitzplatzService.findeKategorie(Long.parseLong(value));
        if(kategorie == null){
            return "";
        }
        return kategorie;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value == null){
            return null;
        }
        if(!value.getClass().equals(Kategorie.class)){
            return null;
        }
        long id = ((Kategorie)value).getKategorie_id();
        return Long.toString(id);
    }
}
