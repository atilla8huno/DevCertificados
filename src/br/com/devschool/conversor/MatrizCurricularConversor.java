package br.com.devschool.conversor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.entidade.MatrizCurricular;
import br.com.devschool.matriz_curricular.servico.MatrizCurricularServico;

/**
 * @author 	ATILLA
 * @date 	07/01/2013
 * 
 */
@FacesConverter(value="matrizCurricularConversor")
@Component("matrizCurricularConversor")
@Scope("session")
public class MatrizCurricularConversor implements Converter {

	@Autowired
	private MatrizCurricularServico servico;
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String id) {
		if(id.trim().equals("")){
			return null;
		} else {
			MatrizCurricular matrizCurricular = null;
			try{
				matrizCurricular = servico.procurarPorCodigo(new Integer(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return matrizCurricular;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object matrizCurricularArg) {
		if(matrizCurricularArg == null || matrizCurricularArg.equals("")){
			return "";
		} else {
			try{
				MatrizCurricular matrizCurricular = (MatrizCurricular) matrizCurricularArg;
				return String.valueOf(matrizCurricular.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}
	}
}
