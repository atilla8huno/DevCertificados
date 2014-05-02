package br.com.devschool.pessoa;

import javax.faces.bean.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.devschool.controle.LoginControlador;
import br.com.devschool.entidade.Mensagem;
import br.com.devschool.entidade.Pessoa;
import br.com.devschool.pessoa.servico.PessoaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.Ferramentas;
import br.com.devschool.util.ICadastroControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

@Component("configuracaoControlador")
@ViewScoped
public class ConfiguracaoControlador extends ControladorGenerico<Pessoa> implements ICadastroControlador {

	@Autowired
	private PessoaServico pessoaServico;
	
	private String senha, senhaConfirmacao;
	
	private Pessoa usuario;
	
	/**
	 * Este método desativa a conta do usuário logado caso a confirmação por senha seja positiva.
	 * 
	 * @author	ATILLA
	 * @date	13/04/2013
	 * @return	String - Página de login
	 */
	public String desativarConta(){
		try {
			if (!senha.equals(senhaConfirmacao)) {
				setarMensagemErro("A senha não confere com a confirmação. Verifique!");
				limpar();
				return PAGINA_CORRENTE;
			}
			
			if (getUsuario() == null || getUsuario().isTransient()) {
				usuario = LoginControlador.getInstancia().getUsuarioSessao();
			}
			//Evita exception de objeto detached
			usuario = pessoaServico.procurarPorCodigo(getUsuario().getId());
			
			usuario = pessoaServico.buscaPor(getUsuario().getEmail(), Ferramentas.md5Critografia(senha));
			usuario = pessoaServico.desativarAtivarPessoa(usuario);
			
			setarMensagemInfo(Mensagem.get("controle.conta.desativacao", usuario.getNome()));
			
			return LoginControlador.getInstancia().sair();
		} catch (Exception e) {
			if (e.getMessage().equals(Mensagem.get("negocio.validacao.loginInvalido"))) {
				setarMensagemErro("Senha inválida! Verifique.");
			} else {
				setarMensagemErro(e.getMessage());
			}
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método salva os dados do usuário
	 * 
	 * @author	ATILLA
	 * @date	13/04/2013
	 * @return	String - Index do Aluno
	 */
	public String salvar(){
		try {
			usuario = pessoaServico.salvarOuAtualizar(usuario);
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
		return "/paginas/aluno/configuracao.jsf?faces-redirect=true";
	}

	@Override
	public String limpar() {
		try {
			senha = "";
			senhaConfirmacao = "";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSenhaConfirmacao() {
		return senhaConfirmacao;
	}

	public void setSenhaConfirmacao(String senhaConfirmacao) {
		this.senhaConfirmacao = senhaConfirmacao;
	}
	
	public static ConfiguracaoControlador getInstancia() {
		return (ConfiguracaoControlador) SpringContainer.getInstancia().getBean("configuracaoControlador");
	}
}
