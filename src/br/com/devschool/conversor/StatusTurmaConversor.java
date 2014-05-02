package br.com.devschool.conversor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.enumeradores.EnumStatusTurma;

@FacesConverter(value="statusTurmaConversor")
@Component("statusTurmaConversor")
@Scope("session")
public class StatusTurmaConversor implements Converter{

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent component, String value) {
		try {
			if(value != null && !value.toString().isEmpty()){
				return EnumStatusTurma.valueOf(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent component, Object value) {
		try {
			if(value != null && !value.toString().isEmpty()){
				return ((EnumStatusTurma) value).name();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
