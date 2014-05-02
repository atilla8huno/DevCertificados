package br.com.devschool.conversor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.disciplina.servico.DisciplinaServico;
import br.com.devschool.entidade.Disciplina;

/**
 * @author 	ATILLA
 * @date 	07/01/2013
 * 
 */
@FacesConverter(value="disciplinaConversor")
@Component("disciplinaConversor")
@Scope("session")
public class DisciplinaConversor implements Converter {

	@Autowired
	private DisciplinaServico servico;
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String id) {
		if(id.trim().equals("")){
			return null;
		} else {
			Disciplina disciplina = null;
			try{
				disciplina = servico.procurarPorCodigo(new Integer(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return disciplina;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object disciplinaArg) {
		if(disciplinaArg == null || disciplinaArg.equals("")){
			return "";
		} else {
			try{
				Disciplina disciplina = (Disciplina) disciplinaArg;
				return String.valueOf(disciplina.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}
	}
}
