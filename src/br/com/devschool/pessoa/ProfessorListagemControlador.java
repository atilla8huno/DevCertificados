package br.com.devschool.pessoa;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.entidade.Pessoa;
import br.com.devschool.enumeradores.EnumFiltroProfessor;
import br.com.devschool.pessoa.servico.PessoaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.IListagemControlador;
import br.com.devschool.util.SpringContainer;

/**
 * @author JOSEMAR
 * @date 03/01/2012
 */
@Component("professorListagemControlador")
@Scope("session")
public class ProfessorListagemControlador extends ControladorGenerico<Pessoa> implements IListagemControlador {

	private Pessoa entidade;
	private List<Pessoa> lista;

	@Autowired
	private PessoaServico servico;

	/**
	 * Este método prepara o objeto selecionado para fazer a edição do mesmo na
	 * tela de cadastro de professor
	 * 
	 * @author JOSEMAR
	 * @date 04/01/2013
	 * @return String - Página de Cadastro de Professor
	 */
	@Override
	public String selecionar() {
		return "/paginas/adm/professorCadastro.jsf?faces-redirect=true";
	}

	/**
	 * Este método prepara a tela de cadastro de professor para entrar na mesma,
	 * para um novo cadastro de professor
	 * 
	 * @author JOSEMAR
	 * @date 04/01/2013
	 * @return String - Página Cadastro de Professor
	 */
	@Override
	public String novo() {
		try {
			return ProfessorCadastroControlador.getInstancia().entrar();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método exclui o objeto de professor e atualiza a lista de professores
	 * 
	 * @author JOSEMAR
	 * @date 04/01/2013
	 * @return String - Página Cadastro de Professor
	 */
	@Override
	public String excluir() {
		try {
			getService().desativarAtivarPessoa(getEntidade());
			setLista(listarProfessores());
			setarMensagemExclusaoSucesso();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método prepara para entrar na tela de listagem de professor, executa
	 * o método para atualizar a listagem.
	 * 
	 * @author JOSEMAR
	 * @date 04/01/2013
	 * @return String - Página Listagem de Professor
	 */
	@Override
	public String entrar() {
		try {
			setLista(listarProfessores());

			return "/paginas/adm/professorListagem.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método prepara para entrar na tela de listagem de professor, executa
	 * o método para atualizar a listagem.
	 * 
	 * @author JOSEMAR
	 * @date 04/01/2013
	 * @return String - Página Listagem de Professor
	 */
	public String entrarTurmasDoProfessor() {
		try {
			setLista(listarProfessores());

			return "/paginas/professor/professores.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método analisa o parâmetro recebido da página
	 * e pesquisa entre a lista de professores ativos
	 * retornando uma List com sugestões de professores (AutoComplete)
	 * 
	 * @author 	ATILLA
	 * @date	12/01/2013
	 * @param 	String
	 * @return	List<Pessoa> - Sugestões de Disciplinas
	 */
	public List<Pessoa> completeProfessores(String query) {
        List<Pessoa> sugestoes = new ArrayList<Pessoa>();
        
        for(Pessoa p : lista) {
        	if(p.getNome().startsWith(query))
        		sugestoes.add(p);
        }
        
        return sugestoes;
	}

	public StreamedContent mostrarImagem() {
		try {
			return new DefaultStreamedContent(new ByteArrayInputStream(getLista().get(0).getAssinaturaDigital()));
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return null;
	}

	private List<Pessoa> listarProfessores() {
		try {
			return getService().listarProfessores(EnumFiltroProfessor.TODOS);
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return new ArrayList<Pessoa>();
	}

	public List<Pessoa> listarProfessoresAtivos() throws Exception {
		try {
			return getService().listarProfessores(EnumFiltroProfessor.ATIVO);
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return new ArrayList<Pessoa>();
	}

	@Override
	public Pessoa getEntidade() {
		if (entidade == null) {
			entidade = new Pessoa();
		}
		return entidade;
	}

	@Override
	public void setEntidade(Pessoa entidade) {
		this.entidade = entidade;
	}

	public List<Pessoa> getLista() {
		if (lista == null) {
			lista = new ArrayList<Pessoa>();
		}
		return lista;
	}

	public void setLista(List<Pessoa> lista) {
		this.lista = lista;
	}

	public static ProfessorListagemControlador getInstancia() {
		return (ProfessorListagemControlador) SpringContainer.getInstancia().getBean("professorListagemControlador");
	}

	@Override
	public String limpar() {
		return PAGINA_CORRENTE;
	}

	/**
	 * @return PessoaServico
	 */
	@Override
	protected PessoaServico getService() {
		return servico;
	}
}
