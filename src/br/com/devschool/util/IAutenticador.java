package br.com.devschool.util;

import br.com.devschool.entidade.Pessoa;

public interface IAutenticador {
	public Pessoa autentica(String login, String senha) throws Exception;
}
