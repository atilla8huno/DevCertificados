package br.com.devschool.aula;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.aula.servico.AulaServico;
import br.com.devschool.controle.LoginControlador;
import br.com.devschool.curso.servico.CursoServico;
import br.com.devschool.entidade.Aula;
import br.com.devschool.entidade.Curso;
import br.com.devschool.entidade.Turma;
import br.com.devschool.turma.servico.TurmaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.ICadastroControlador;
import br.com.devschool.util.ServicoGenerico;

@Component("planoAulaCadastroControlador")
@Scope("session")
public class PlanoAulaCadastroControlador extends ControladorGenerico<Aula> implements ICadastroControlador {

	@Autowired
	private CursoServico cursoServico;
	@Autowired
	private TurmaServico turmaServico;
	@Autowired
	private AulaServico servico;

	private Curso curso;
	private Turma turma;
	private Aula aulaSelecionada;
	private List<Curso> listaCursos;
	private List<Turma> listaTurmas;
	private List<Aula> listaAulas;
	private boolean abreListaCurso;
	
	/**
	 * Este método cancela o cadastro de plano de aula
	 * 
	 * @author WENDEL
	 * @return String - Página Corrente
	 */
	@Override
	public String cancelar() {
		try {
			limpar();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método salva a listagem de plano de aula
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	@Override
	public String salvar() {
		try {
			servico.salvarPlanosDeAula(getListaAulas());
			limpar();
			setarMensagemInclusaoSucesso();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método seleciona um curso na listagem de curso
	 * 
	 * @author WENDEL
	 * @return String - Pagina corente
	 */
	public String selecionarCurso() {
		try {
			setAbreListaCusro(false);
			setListaTurmas(turmaServico.listarPorCursoProfessor(getCurso(), LoginControlador.getInstancia().getUsuarioSessao()));
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método abre a listagem de curso
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	public String abreListaCurso() {
		try {
			setAbreListaCusro(true);
			setListaCursos(cursoServico.listarPorProfessor(LoginControlador.getInstancia().getUsuarioSessao()));
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método limpa a aula selecionada na listagem de plano de aula
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	public String limparAulaSelecionada(){
		try {
			aulaSelecionada.setPlanoDeAula("");
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * @return ServicoGenerico<Aula>
	 */
	@Override
	protected ServicoGenerico<Aula> getService() {
		return servico;
	}

	@Override
	public Aula getEntidade() {
		if (aulaSelecionada == null) {
			aulaSelecionada = new Aula();
		}
		return aulaSelecionada;
	}

	@Override
	public void setEntidade(Aula entidade) {
		aulaSelecionada = entidade;
	}
	
	/**
	 * Este método lista as aulas pela turma
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	public String listar(){
		try {
			setListaAulas(servico.listarAulaPorTurma(getTurma()));
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método entra na página de cadastro de aula
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	@Override
	public String entrar() {
		try {
			limpar();
			return "/paginas/professor/planoAulaCadastro.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método limpa a página de cadastro e curso
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	@Override
	public String limpar() {
		try {
			setAbreListaCusro(false);
			setListaCursos(new ArrayList<Curso>());
			setListaTurmas(new ArrayList<Turma>());
			setListaAulas(new ArrayList<Aula>());
			setCurso(new Curso());
			setTurma(new Turma());
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
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

	public Turma getTurma() {
		if (turma == null) {
			turma = new Turma();
		}
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public List<Curso> getListaCursos() {
		if (listaCursos == null) {
			listaCursos = new ArrayList<Curso>();
		}
		return listaCursos;
	}

	public void setListaCursos(List<Curso> listaCursos) {
		this.listaCursos = listaCursos;
	}

	public List<Turma> getListaTurmas() {
		if (listaTurmas == null) {
			listaTurmas = new ArrayList<Turma>();
		}
		return listaTurmas;
	}

	public void setListaTurmas(List<Turma> listaTurmas) {
		this.listaTurmas = listaTurmas;
	}

	public List<Aula> getListaAulas() {
		if (listaAulas == null) {
			listaAulas = new ArrayList<Aula>();
		}
		return listaAulas;
	}

	public void setListaAulas(List<Aula> listaAulas) {
		this.listaAulas = listaAulas;
	}

	public Aula getAulaSelecionada() {
		if (aulaSelecionada == null) {
			aulaSelecionada = new Aula();
		}
		return aulaSelecionada;
	}

	public void setAulaSelecionada(Aula aulaSelecionada) {
		this.aulaSelecionada = aulaSelecionada;
	}

	public boolean isAbreListaCurso() {
		return abreListaCurso;
	}

	public void setAbreListaCusro(boolean abreListaCusro) {
		this.abreListaCurso = abreListaCusro;
	}
}
