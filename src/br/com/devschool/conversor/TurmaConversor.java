package br.com.devschool.conversor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.entidade.Turma;
import br.com.devschool.turma.servico.TurmaServico;

@FacesConverter(value = "turmaConversor")
@Component("turmaConversor")
@Scope("session")
public class TurmaConversor implements Converter {

	@Autowired
	private TurmaServico servico;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String id) {
		if (id.trim().equals("")) {
			return null;
		} else {
			Turma turma = null;
			try {
				turma = servico.procurarPorCodigo(Integer.valueOf(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return turma;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object entidade) {
		try {
			if (entidade != null 
					&& entidade instanceof Turma
						&& !((Turma)entidade).isTransient()) {
				Turma turma = (Turma) entidade;
				return turma.getId().toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
