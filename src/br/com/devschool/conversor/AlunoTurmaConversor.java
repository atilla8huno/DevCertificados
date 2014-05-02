package br.com.devschool.conversor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.entidade.AlunoTurma;
import br.com.devschool.matricula.servico.AlunoTurmaServico;

@FacesConverter(value = "alunoTurmaConversor")
@Component("alunoTurmaConversor")
@Scope("session")
public class AlunoTurmaConversor implements Converter {

	@Autowired
	private AlunoTurmaServico servico;
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String id) {
		if(id.trim().equals("")){
			return null;
		} else {
			AlunoTurma alunoTurma = null;
			try{
				alunoTurma = servico.procurarPorCodigo(new Integer(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return alunoTurma;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object pessoaArg) {
		if(pessoaArg == null || pessoaArg.equals("")){
			return "";
		} else {
			try{
				AlunoTurma alunoTurma = (AlunoTurma) pessoaArg;
				return String.valueOf(alunoTurma.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}
	}
}
