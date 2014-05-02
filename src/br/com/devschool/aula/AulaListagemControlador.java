package br.com.devschool.aula;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.curso.servico.CursoServico;
import br.com.devschool.entidade.Aula;
import br.com.devschool.entidade.Curso;
import br.com.devschool.entidade.Turma;
import br.com.devschool.turma.servico.TurmaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.IListagemControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

@Component("aulaListagemControlador")
@Scope("session")
public class AulaListagemControlador extends ControladorGenerico<Aula> implements IListagemControlador {

	@Autowired
	private TurmaServico turmaServico;
	@Autowired
	private CursoServico cursoServico;
	
	private Curso curso;
	private List<Turma> listaTurmas;
	private List<Curso> listaCursos;
	private boolean abreListaCurso;
	
	@Override
	public String novo() {
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método seleciona uma turma na pagina de listagem de turmas
	 * 
	 * @author WENDEL
	 * @return String - Página de Cadastro de aula
	 */
	@Override
	public String selecionar() {
		return AulaCadastroControlador.getInstancia().entrar();
	}
	
	/**
	 * Este método lista turmas por curso
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	public String listarTurmas(){
		try {
			setListaTurmas(turmaServico.listarPorCurso(getCurso()));
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método seleciona um curso na listagem de curso
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	public String selecionarCurso(){
		try {
			setAbreListaCurso(false);
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este métdo abre a listagem de curso
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	public String abreListaCurso(){
		try {
			setAbreListaCurso(true);
			setListaCursos(cursoServico.listarTodos());
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	@Override
	protected ServicoGenerico<Aula> getService() {
		return null;
	}

	@Override
	public Aula getEntidade() {
		return null;
	}

	@Override
	public void setEntidade(Aula entidade) {
	}

	/**
	 * Este método entra na pagina de listagem de aula
	 * 
	 * @author WENDEL
	 * @return String - Página Listagem de aula
	 */
	@Override
	public String entrar() {
		limpar();
		return "/paginas/adm/aulaListagem.jsf?faces-redirect=true";
	}

	/**
	 * Este método limpa a tela de listagem de aula
	 * 
	 * @author WENDEL
	 * @return String - Página de listagem de aula
	 */
	@Override
	public String limpar() {
		try {
			setListaTurmas(new ArrayList<Turma>());
			setCurso(new Curso());
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	public List<Turma> getListaTurmas() {
		if(listaTurmas == null){
			listaTurmas = new ArrayList<Turma>();
		}
		return listaTurmas;
	}

	public void setListaTurmas(List<Turma> listaTurmas) {
		this.listaTurmas = listaTurmas;
	}

	public List<Curso> getListaCursos() {
		if(listaCursos == null){
			listaCursos = new ArrayList<Curso>();
		}
		return listaCursos;
	}

	public void setListaCursos(List<Curso> listaCursos) {
		this.listaCursos = listaCursos;
	}
	
	public Curso getCurso() {
		if (curso == null) {
			curso = new Curso();
		}
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	

	public boolean isAbreListaCurso() {
		return abreListaCurso;
	}

	public void setAbreListaCurso(boolean abreListaCurso) {
		this.abreListaCurso = abreListaCurso;
	}

	/**
	 * @return AulaListagemControlador
	 */
	public static AulaListagemControlador getInstancia() {
		return (AulaListagemControlador) SpringContainer.getInstancia().getBean("aulaListagemControlador");
	}
}
