package br.com.devschool.conversor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.entidade.Nivel;
import br.com.devschool.nivel.servico.NivelServico;

/**
 * @author 	ATILLA
 * @date 	07/01/2013
 * 
 */
@FacesConverter(value="nivelConversor")
@Component("nivelConversor")
@Scope("session")
public class NivelConversor implements Converter {

	@Autowired
	private NivelServico servico;
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String id) {
		if(id.trim().equals("")){
			return null;
		} else {
			Nivel nivel = null;
			try{
				nivel = servico.procurarPorCodigo(new Integer(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return nivel;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object nivelArg) {
		if(nivelArg == null || nivelArg.equals("")){
			return "";
		} else {
			try{
				Nivel nivel = (Nivel) nivelArg;
				return String.valueOf(nivel.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}
	}
}
