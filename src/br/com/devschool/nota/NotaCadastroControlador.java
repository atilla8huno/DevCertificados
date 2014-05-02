package br.com.devschool.nota;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.controle.LoginControlador;
import br.com.devschool.curso.servico.CursoServico;
import br.com.devschool.entidade.AlunoTurma;
import br.com.devschool.entidade.Curso;
import br.com.devschool.entidade.Turma;
import br.com.devschool.matricula.servico.AlunoTurmaServico;
import br.com.devschool.turma.servico.TurmaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.ICadastroControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

@Component("notaCadastroControlador")
@Scope("session")
public class NotaCadastroControlador extends ControladorGenerico<AlunoTurma>
		implements ICadastroControlador {

	@Autowired
	private CursoServico cursoServico;
	@Autowired
	private TurmaServico turmaServico;
	@Autowired
	private AlunoTurmaServico servico;

	private Curso curso;
	private Turma turma;
	private List<Curso> listaCursos;
	private List<Turma> listaTurmas;
	private List<AlunoTurma> listaAlunos;
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
			e.printStackTrace();
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
			if (getListaAlunos().size() > 0) {
				servico.salvarOuAtualizar(getListaAlunos());
			}
			limpar();
		} catch (Exception e) {
			e.printStackTrace();
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
			setListaTurmas(turmaServico.listarPorCursoProfessor(getCurso(),
					LoginControlador.getInstancia().getUsuarioSessao()));
		} catch (Exception e) {
			e.printStackTrace();
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
			setListaCursos(cursoServico.listarPorProfessor(LoginControlador
					.getInstancia().getUsuarioSessao()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return PAGINA_CORRENTE;
	}

	public void atualizaListaAlunos() {
		try {
			if (getTurma() != null) {
				setListaAlunos(servico.listarPorTurma(getTurma()));
			} else {
				setListaAlunos(new ArrayList<AlunoTurma>());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return ServicoGenerico<FrequenciaAluno>
	 */
	@Override
	protected ServicoGenerico<AlunoTurma> getService() {
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
			return "/paginas/professor/notaCadastro.jsf?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
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
			setListaAlunos(new ArrayList<AlunoTurma>());
			setCurso(new Curso());
			setTurma(new Turma());
		} catch (Exception e) {
			e.printStackTrace();
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

	/**
	 * @return FrequenciaCadastroControlador
	 */
	public static NotaCadastroControlador getInstancia() {
		return (NotaCadastroControlador) SpringContainer.getInstancia().getBean("frequenciaCadastroControlador");
	}

	public List<AlunoTurma> getListaAlunos() {
		if (listaAlunos == null) {
			listaAlunos = new ArrayList<AlunoTurma>();
		}
		return listaAlunos;
	}

	public void setListaAlunos(List<AlunoTurma> listaAlunos) {
		this.listaAlunos = listaAlunos;
	}

	@Override
	public void setEntidade(AlunoTurma entidade) { }

	@Override
	public AlunoTurma getEntidade() {
		return null;
	}
}
