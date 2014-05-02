package br.com.devschool.pessoa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.controle.LoginControlador;
import br.com.devschool.entidade.Pessoa;
import br.com.devschool.pessoa.servico.PessoaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.Ferramentas;
import br.com.devschool.util.ICadastroControlador;
import br.com.devschool.util.ServicoGenerico;

@Component("alterarSenhaControlador")
@Scope("request")
public class AlterarSenhaControlador extends ControladorGenerico<Pessoa> implements ICadastroControlador {

	@Autowired
	private PessoaServico pessoaServico;
	
	private String senhaAtual, senhaNova, senhaNovaConfirmacao;
	
	private Pessoa usuario;
	
	/**
	 * Este método verifica se as senhas estão corretas e altera a senha do usuário
	 * 
	 * @author	ATILLA
	 * @date	10/04/2013
	 * @return	String - Index aluno
	 */
	public String salvar(){
		try {
			if (!senhaNova.equals(senhaNovaConfirmacao)) {
				setarMensagemErro("A nova senha não confere com a confirmação. Verifique!");
				limpar();
				return PAGINA_CORRENTE;
			}
			
			if (usuario == null || usuario.isTransient()) {
				usuario = LoginControlador.getInstancia().getUsuarioSessao();
			} else {
				usuario = pessoaServico.procurarPorCodigo(getUsuario().getId());
			}
			
			usuario = pessoaServico.buscaPor(getUsuario().getEmail(), Ferramentas.md5Critografia(senhaAtual));
			usuario.setSenha(Ferramentas.md5Critografia(senhaNova));
			usuario = pessoaServico.salvarOuAtualizar(getEntidade());
			
			limpar();
			setarMensagemAlteracaoSucesso();
			
			return cancelar();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método retorna o index do Aluno
	 */
	@Override
	public String cancelar() {
		return "/paginas/aluno/indexAluno.jsf?faces-redirect=true";
	}

	@Override
	protected ServicoGenerico<Pessoa> getService() {
		return pessoaServico;
	}

	@Override
	public Pessoa getEntidade() {
		if (usuario == null) {
			try {
				usuario = LoginControlador.getInstancia().getUsuarioSessao();
			} catch (Exception e) {
				setarMensagemErro(e.getMessage());
			}
		}
		return usuario;
	}

	@Override
	public void setEntidade(Pessoa entidade) {
		this.usuario = entidade;
	}

	@Override
	public String entrar() {
		return "/paginas/aluno/alterarSenha.jsf?faces-redirect=true";
	}

	@Override
	public String limpar() {
		try {
			senhaAtual	= "";
			senhaNova	= "";
			senhaNovaConfirmacao = "";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	public String getSenhaAtual() {
		if (senhaAtual == null) {
			senhaAtual = "";
		}
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getSenhaNova() {
		if (senhaNova == null) {
			senhaNova = "";
		}
		return senhaNova;
	}

	public void setSenhaNova(String senhaNova) {
		this.senhaNova = senhaNova;
	}

	public String getSenhaNovaConfirmacao() {
		if (senhaNovaConfirmacao == null) {
			senhaNovaConfirmacao = "";
		}
		return senhaNovaConfirmacao;
	}

	public void setSenhaNovaConfirmacao(String senhaNovaConfirmacao) {
		this.senhaNovaConfirmacao = senhaNovaConfirmacao;
	}

	public Pessoa getUsuario() {
		if (usuario == null) {
			try {
				usuario = LoginControlador.getInstancia().getUsuarioSessao();
			} catch (Exception e) {
				setarMensagemErro(e.getMessage());
			}
		}
		return usuario;
	}

	public void setUsuario(Pessoa usuario) {
		this.usuario = usuario;
	}
}
