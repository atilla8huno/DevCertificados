package br.com.devschool.disciplina;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.disciplina.servico.DisciplinaServico;
import br.com.devschool.entidade.Disciplina;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.IListagemControlador;
import br.com.devschool.util.SpringContainer;

/**
 * @author ATILLA
 * @date 10/09/2012
 */
@Component("disciplinaListagemControlador")
@Scope("session")
public class DisciplinaListagemControlador extends ControladorGenerico<Disciplina> implements IListagemControlador {

	private Disciplina entidade;
	private List<Disciplina> lista;

	@Autowired
	private DisciplinaServico servico;

	/**
	 * Este método prepara o objeto selecionado para fazer a edição do mesmo na tela de cadastro de disciplina
	 * 
	 * @author	WENDEL
	 * @date	03/01/2013
	 * @return	String - Página de Cadastro de Disciplina
	 */
	@Override
	public String selecionar() {
		return "/paginas/adm/disciplinaCadastro.jsf?faces-redirect=true";
	}

	/**
	 * Este método prepara para entrar na tela de listagem de disciplina, executa o método
	 * para atualizar a listagem.
	 * 
	 * @author	WENDEL
	 * @date	03/01/2013
	 * @return	String - Página de Cadastro de Disciplina
	 */
	@Override
	public String entrar() {
		try {
			setLista(listarTodos());
			return "/paginas/adm/disciplinaListagem.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	@Override
	public String limpar() {
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método prepara a tela de cadastro de disciplina para entrar na mesma,
	 * para um novo cadastro de disciplina
	 * 
	 * @author 	WENDEL
	 * @date	03/01/2013
	 * @return	String - Página Cadastro de Disciplina
	 */
	@Override
	public String novo() {
		try {
			return DisciplinaCadastroControlador.getInstancia().entrar();			
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método exclui o objeto de disciplina e atualiza a lista de disciplina
	 * 
	 * @author 	WENDEL
	 * @date	03/01/2013
	 * @return	String - Página Cadastro de Disciplina
	 */
	@Override
	public String excluir() {
		try {
			getService().desativarAtivarDisciplina(getEntidade());
			setLista(getService().listarTodos());
			setarMensagemExclusaoSucesso();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * @return DisciplinaServico
	 */
	@Override
	protected DisciplinaServico getService() {
		return servico;
	}

	@Override
	public Disciplina getEntidade() {
		if (entidade == null) {
			entidade = new Disciplina();
		}
		return entidade;
	}

	@Override
	public void setEntidade(Disciplina entidade) {
		this.entidade = entidade;
	}

	public List<Disciplina> getLista() {
		if (lista == null) {
			lista = new ArrayList<Disciplina>();
		}
		return lista;
	}

	public void setLista(List<Disciplina> lista) {
		this.lista = lista;
	}

	/**
	 * @return DisciplinaListagemControlador
	 */
	public static DisciplinaListagemControlador getInstancia() {
		return (DisciplinaListagemControlador) SpringContainer.getInstancia().getBean("disciplinaListagemControlador");
	}
}
