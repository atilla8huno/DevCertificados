package br.com.devschool.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.devschool.entidade.Pessoa;
import br.com.devschool.pessoa.servico.PessoaServico;

@Service("autenticador")
public class AutenticadorImpl implements IAutenticador {
	
	@Autowired
	private PessoaServico pessoaServico;

	@Override
	public Pessoa autentica(String login, String senha) throws Exception {
		try {
			Pessoa pessoa = pessoaServico.buscaPor(login, senha);
			return pessoa;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @return the pessoaServico
	 */
	public PessoaServico getPessoaServico() {
		return pessoaServico;
	}

	/**
	 * @param pessoaServico the pessoaServico to set
	 */
	public void setPessoaServico(PessoaServico pessoaServico) {
		this.pessoaServico = pessoaServico;
	}
}
