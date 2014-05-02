package br.com.devschool.conversor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.entidade.Turno;
import br.com.devschool.turno.servico.TurnoServico;

/**
 * @author 	ATILLA
 * @date 	07/01/2013
 * 
 */
@FacesConverter(value="turnoConversor")
@Component("turnoConversor")
@Scope("session")
public class TurnoConversor implements Converter {

	@Autowired
	private TurnoServico servico;
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String id) {
		if(id.trim().equals("")){
			return null;
		} else {
			Turno turno = null;
			try{
				turno = servico.procurarPorCodigo(new Integer(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return turno;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object turnoArg) {
		if(turnoArg == null || turnoArg.equals("")){
			return "";
		} else {
			try{
				Turno turno = (Turno) turnoArg;
				return String.valueOf(turno.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}
	}
}
