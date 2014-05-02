package br.com.devschool.pessoa;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.controle.LoginControlador;
import br.com.devschool.entidade.Mensagem;
import br.com.devschool.entidade.Pessoa;
import br.com.devschool.pagamento.servico.PagamentoServico;
import br.com.devschool.pessoa.servico.PessoaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.ICadastroControlador;
import br.com.devschool.util.ServicoGenerico;

@Component("reativarContaControlador")
@Scope("request")
public class ReativarContaControlador extends ControladorGenerico<Pessoa> implements ICadastroControlador {

	@Autowired
	private PessoaServico pessoaServico;
	
	@Autowired
	private PagamentoServico pgtoServico; 
	
	private List<Pessoa> listaDeAlunos;
	private Pessoa usuario;
	private Pessoa aluno;
	
	/**
	 * Este método reativa a conta do usuário, passando para status ATIVO (true)
	 * 
	 * @author	ATILLA
	 * @date	11/04/2013
	 * @return	String - Página corrente
	 */
	public String ativarDesativarContaADM(){
		try {
			getAluno();
			aluno = pessoaServico.desativarAtivarPessoaADM(aluno);
			setarMensagemInfo(Mensagem.get("controle.conta.reativacao", usuario.getNome()));
			limpar();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método reativa a conta do usuário, passando para status ATIVO (true)
	 * 
	 * @author	ATILLA
	 * @date	11/04/2013
	 * @return	String - Index aluno
	 */
	public String reativarConta(){
		try {
			getUsuario();
			usuario = pessoaServico.desativarAtivarPessoa(usuario);
			setarMensagemInfo(Mensagem.get("controle.conta.reativacao", usuario.getNome()));
			
			return "/paginas/aluno/indexAluno.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return cancelar();
	}
	
	/**
	 * Este método analisa o parâmetro recebido da página
	 * e pesquisa entre a lista de alunos, retornando uma 
	 * List com sugestões de alunos (AutoComplete)
	 * 
	 * @author 	ATILLA
	 * @date	14/04/2013
	 * @param 	String
	 * @return	List<Pessoa> - Sugestões de Alunos
	 */
	public List<Pessoa> completeAlunos(String query) {
        List<Pessoa> sugestoes = new ArrayList<Pessoa>();
        
        for(Pessoa p : getListaDeAlunos()) {
        	if(p.getNome().startsWith(query))
        		sugestoes.add(p);
        }
        
        return sugestoes;
	}
	
	public String verificaPendencias(){
		try {
			pgtoServico.verificarPagamentosPendentes(aluno);
		} catch (Exception e) {
			setarMensagemAviso(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método retorna o index do Aluno
	 */
	@Override
	public String cancelar() {
		try {
			return LoginControlador.getInstancia().sair();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return "/paginas/login.jsf?faces-redirect=true";
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
		return "/paginas/aluno/reativarConta.jsf?faces-redirect=true";
	}
	
	public String entrarModuloADM() {
		limpar();
		return "/paginas/adm/ativarDesativarConta.jsf?faces-redirect=true";
	}

	@Override
	public String limpar() {
		try {
			aluno = new Pessoa();
			listaDeAlunos = pessoaServico.listarTodos();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	public Pessoa getUsuario() {
		if (usuario == null || usuario.isTransient()) {
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

	public List<Pessoa> getListaDeAlunos() {
		if (listaDeAlunos == null) {
			try {
				listaDeAlunos = pessoaServico.listarTodos();
			} catch (Exception e) {
				setarMensagemErro(e.getMessage());
			}
		}
		return listaDeAlunos;
	}

	public void setListaDeAlunos(List<Pessoa> listaDeAlunos) {
		this.listaDeAlunos = listaDeAlunos;
	}

	public Pessoa getAluno() {
		if (aluno == null) {
			aluno = new Pessoa();
		}
		return aluno;
	}

	public void setAluno(Pessoa aluno) {
		this.aluno = aluno;
	}
}
