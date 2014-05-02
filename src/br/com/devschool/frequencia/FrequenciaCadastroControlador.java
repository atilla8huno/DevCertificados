package br.com.devschool.frequencia;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.devschool.aula.servico.AulaServico;
import br.com.devschool.controle.LoginControlador;
import br.com.devschool.curso.servico.CursoServico;
import br.com.devschool.entidade.Aula;
import br.com.devschool.entidade.Curso;
import br.com.devschool.entidade.FrequenciaAluno;
import br.com.devschool.entidade.Turma;
import br.com.devschool.frequencia.servico.FrequenciaAlunoServico;
import br.com.devschool.turma.servico.TurmaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.ICadastroControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

@Controller
@Scope("session")
public class FrequenciaCadastroControlador extends ControladorGenerico<FrequenciaAluno> implements ICadastroControlador {

	@Autowired
	private CursoServico cursoServico;
	@Autowired
	private TurmaServico turmaServico;
	@Autowired
	private AulaServico aulaServico;
	@Autowired
	private FrequenciaAlunoServico servico;

	private Curso curso;
	private Turma turma;
	private Aula aula;
	private List<Curso> listaCursos;
	private List<Turma> listaTurmas;
	private List<Aula> listaAulas;
	private List<FrequenciaAluno> listaFrequenciaAlunos;
	private boolean abreListaCurso;

	/**
	 * Este método cancela o cadastro de frequência
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
	 * Este método salva a listagem de frequencia
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	@Override
	public String salvar() {
		try {
			servico.salvarFrequencia(getListaFrequenciaAlunos());
			servico.atualizaFrequenciaTurma(getTurma());
			limpar();
			setarMensagemInclusaoSucesso();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método atualiza a combo aula, referente a turma escolhida
	 * 
	 * @author WENDEL
	 */
	public void atualizaComboAula() {
		try {
			if (getTurma() != null) {
				setListaAulas(aulaServico.listarAulaPorTurma(getTurma()));
			} else {
				setListaAulas(new ArrayList<Aula>());
			}
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}
	
	/**
	 * Este método atualiza a combo frequencia, referente a aula escolhida
	 * 
	 * @author WENDEL
	 */
	public void atualizaListaFrequencia(){
		try {
			if(getAula() != null){
				setListaFrequenciaAlunos(servico.listaFrequencia(getTurma(), getAula()));
			} else {
				setListaFrequenciaAlunos(new ArrayList<FrequenciaAluno>());
			}
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
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
			setListaTurmas(turmaServico.listarPorCursoProfessor(getCurso(),
					LoginControlador.getInstancia().getUsuarioSessao()));
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
	 * @return ServicoGenerico<FrequenciaAluno>
	 */
	@Override
	protected ServicoGenerico<FrequenciaAluno> getService() {
		return servico;
	}

	/**
	 * Este método entra na página de cadastro de frequencia
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	@Override
	public String entrar() {
		try {
			limpar();
			return "/paginas/professor/frequenciaCadastro.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método limpa a página de cadastro frequencia
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
			setCurso(new Curso());
			setTurma(new Turma());
			setAula(new Aula());
			setListaFrequenciaAlunos(new ArrayList<FrequenciaAluno>());
			setListaAulas(new ArrayList<Aula>());
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

	public boolean isAbreListaCurso() {
		return abreListaCurso;
	}

	public void setAbreListaCusro(boolean abreListaCusro) {
		this.abreListaCurso = abreListaCusro;
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

	public Aula getAula() {
		if (aula == null) {
			aula = new Aula();
		}
		return aula;
	}

	public void setAula(Aula aula) {
		this.aula = aula;
	}

	@Override
	public FrequenciaAluno getEntidade() {
		return null;
	}

	@Override
	public void setEntidade(FrequenciaAluno entidade) {
	}

	public List<FrequenciaAluno> getListaFrequenciaAlunos() {
		if(listaFrequenciaAlunos == null){
			listaFrequenciaAlunos = new ArrayList<FrequenciaAluno>();
		}
		return listaFrequenciaAlunos;
	}

	public void setListaFrequenciaAlunos(List<FrequenciaAluno> listaFrequenciaAlunos) {
		this.listaFrequenciaAlunos = listaFrequenciaAlunos;
	}
	
	/**
	 * @return FrequenciaCadastroControlador
	 */
	public static FrequenciaCadastroControlador getInstancia() {
		return (FrequenciaCadastroControlador) SpringContainer.getInstancia().getBean("frequenciaCadastroControlador");
	}
}
