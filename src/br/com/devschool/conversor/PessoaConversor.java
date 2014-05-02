package br.com.devschool.conversor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.pessoa.servico.PessoaServico;
import br.com.devschool.entidade.Pessoa;

/**
 * @author 	ATILLA
 * @date 	07/01/2013
 * 
 */
@FacesConverter(value="pessoaConversor")
@Component("pessoaConversor")
@Scope("session")
public class PessoaConversor implements Converter {

	@Autowired
	private PessoaServico servico;
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String id) {
		if(id.trim().equals("")){
			return null;
		} else {
			Pessoa pessoa = null;
			try{
				pessoa = servico.procurarPorCodigo(new Integer(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return pessoa;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object pessoaArg) {
		if(pessoaArg == null || pessoaArg.equals("")){
			return "";
		} else {
			try{
				Pessoa pessoa = (Pessoa) pessoaArg;
				return String.valueOf(pessoa.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}
	}
}
