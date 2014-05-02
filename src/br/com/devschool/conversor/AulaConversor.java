package br.com.devschool.conversor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.aula.servico.AulaServico;
import br.com.devschool.entidade.Aula;

@FacesConverter(value = "aulaConversor")
@Component("aulaConversor")
@Scope("session")
public class AulaConversor implements Converter {

	@Autowired
	private AulaServico servico;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String id) {
		if (id.trim().equals("")) {
			return null;
		} else {
			Aula aula = null;
			try {
				aula = servico.procurarPorCodigo(Integer.valueOf(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return aula;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object entidade) {
		try {
			if (entidade != null 
					&& entidade instanceof Aula
						&& !((Aula)entidade).isTransient()) {
				Aula aula = (Aula) entidade;
				return aula.getId().toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
