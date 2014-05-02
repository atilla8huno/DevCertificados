package br.com.devschool.conversor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.curso.servico.CursoServico;
import br.com.devschool.entidade.Curso;

/**
 * @author 	ATILLA
 * @date 	07/01/2013
 * 
 */
@FacesConverter(value="cursoConversor")
@Component("cursoConversor")
@Scope("session")
public class CursoConversor implements Converter {

	@Autowired
	private CursoServico servico;
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String id) {
		if(id.trim().equals("")){
			return null;
		} else {
			Curso curso = null;
			try{
				curso = servico.procurarPorCodigo(new Integer(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return curso;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object cursoArg) {
		if(cursoArg == null || cursoArg.equals("")){
			return "";
		} else {
			try{
				Curso curso = (Curso) cursoArg;
				return String.valueOf(curso.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}
	}
}
