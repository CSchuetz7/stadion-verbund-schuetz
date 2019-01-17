package de.stadionVerbundSchuetz.converter;

import de.stadionVerbundSchuetz.entity.Stadion;
import de.stadionVerbundSchuetz.service.StadionServiceIF;
import de.stadionVerbundSchuetz.ui.model.StadionModel;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ApplicationScoped
public class StadionKonvertierer implements Converter {
    @Inject
    StadionServiceIF stadionService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null){
            return "";
        }
        Stadion stadion = stadionService.findeStadion(Long.parseLong(value));
        if(stadion == null){
            return "";
        }
        return stadion;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value == null){
            return null;
        }
        if(!value.getClass().equals(Stadion.class)){
            return null;
        }
        long id = ((Stadion)value).getStadion_id();
        return Long.toString(id);
    }
}
