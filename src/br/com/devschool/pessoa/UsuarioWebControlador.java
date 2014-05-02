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
import br.com.devschool.util.SpringContainer;

@Component("usuarioWebControlador")
@Scope("session")
public class UsuarioWebControlador extends ControladorGenerico<Pessoa> implements ICadastroControlador {

	private Pessoa usuario;

	@Autowired
	private PessoaServico servico;

	public Pessoa getUsuario() {
		if (usuario == null) {
			usuario = new Pessoa();
		}
		return usuario;
	}

	/**
	 * Este método cancela o cadastro ou edição de professor, e prepara tela de
	 * listagem de professor para voltar a mesma
	 * 
	 * @author JOSEMAR
	 * @date 04/01/2013
	 * @return String - Página Listagem de Professor
	 */
	@Override
	public String cancelar() {
		try {
			if (LoginControlador.getInstancia().getUsuarioSessao() == null)
				return "/paginas/login.jsf?faces-redirect=true";
			else
				return "/paginas/aluno/indexAluno.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método prepara a tela para realizar um novo cadastro
	 * 
	 * @author JOSEMAR
	 * @date 04/01/2013
	 * @return String - Página Cadastro de Aluno
	 */
	@Override
	public String entrar() {
		try {
			limpar();
			return "/paginas/aluno/usuarioWebCadastro.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método prepara a tela para realizar uma atualização cadastral
	 * 
	 * @author	ATILLA
	 * @date	09/04/2013
	 * @return	String - Página Cadastro de Pessoa
	 */
	public String entrarAlterarDados() {
		try {
			return "/paginas/aluno/usuarioWebCadastro.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método limpa tela de cadastro de professor para ficar disponível
	 * para realizar um novo cadastro
	 * 
	 * @author JOSEMAR
	 * @date 04/01/2013
	 * @return String - Página Cadastro de Disciplina
	 */
	@Override
	public String limpar() {
		try {
			setEntidade(new Pessoa());
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método salva ou atualiza uma professor, e prepara a tela de listagem
	 * de professor para voltar a mesma.
	 * 
	 * @author JOSEMAR
	 * @date 05/01/2013
	 * @return String - Página Listagem de Professor
	 */
	@Override
	public String salvar() {
		try {
			getEntidade().setTipoAluno(true);
			if (getEntidade().getSenha() != null && !getEntidade().getSenha().isEmpty()) {
				getEntidade().setSenha(Ferramentas.md5Critografia(getEntidade().getSenha()));
			}
			
			setEntidade(servico.salvarOuAtualizar(getEntidade()));
			limpar();
			setarMensagemInclusaoSucesso();
			
			return cancelar();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método atualiza uma pessoa e retorna para o index
	 * 
	 * @author	ATILLA
	 * @date	09/04/2013
	 * @return	String - Index
	 */
	public String alterar() {
		try {
			setEntidade(servico.salvarOuAtualizar(getEntidade()));
			limpar();
			setarMensagemAlteracaoSucesso();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	@Override
	protected ServicoGenerico<Pessoa> getService() {
		return servico;
	}

	@Override
	public Pessoa getEntidade() {
		if (usuario == null) {
			usuario = new Pessoa();
		}
		return usuario;
	}

	@Override
	public void setEntidade(Pessoa entidade) {
		this.usuario = entidade;
	}

	public static UsuarioWebControlador getInstancia() {
		return (UsuarioWebControlador) SpringContainer.getInstancia().getBean("usuarioWebControlador");
	}
}
