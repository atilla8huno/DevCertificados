package br.com.devschool.pagamento;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.curso.servico.CursoServico;
import br.com.devschool.entidade.AlunoTurma;
import br.com.devschool.entidade.Curso;
import br.com.devschool.entidade.Pagamento;
import br.com.devschool.entidade.Turma;
import br.com.devschool.matricula.servico.AlunoTurmaServico;
import br.com.devschool.turma.servico.TurmaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.IListagemControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

@Component("pagamentoListagemControlador")
@Scope("session")
public class PagamentoListagemControlador extends ControladorGenerico<Pagamento> implements IListagemControlador {

	@Autowired
	private CursoServico cursoServico;
	@Autowired
	private TurmaServico turmaServico;
	@Autowired
	private AlunoTurmaServico matriculaServico;

	private Turma turma;
	private Curso curso;
	private List<AlunoTurma> listaAlunos;
	private List<Curso> listaCursos;
	private List<Turma> listaTurma;
	private boolean abreListaCurso;

	@Override
	protected ServicoGenerico<Pagamento> getService() {
		return null;
	}

	/**
	 * Este método lista os alunos referente a turma
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	public void listarAlunos() {
		try {
			setListaAlunos(matriculaServico.listarPorTurma(getTurma()));
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}

	@Override
	public Pagamento getEntidade() {
		return null;
	}

	@Override
	public void setEntidade(Pagamento entidade) {
	}

	/**
	 * Este método prepara a tela de pagamento listagem para entrar na mesma
	 * 
	 * @author WENDEL
	 * @return String - Página de listagem de pagamentos
	 */
	@Override
	public String entrar() {
		try {
			limpar();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return "/paginas/adm/pagamentoListagem.jsf?faces-redirect=true";
	}

	/**
	 * Este método prepara a consulta de cursos
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	public String consultarCursos() {
		try {
			setListaCursos(cursoServico.listarTodos());
			setAbreListaCurso(true);
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método limpa a tela de listagem de pagamentos
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	@Override
	public String limpar() {
		try {
			setTurma(new Turma());
			setListaAlunos(new ArrayList<AlunoTurma>());
			setListaCursos(new ArrayList<Curso>());
			setListaTurma(new ArrayList<Turma>());
			setAbreListaCurso(false);
			setCurso(new Curso());
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	@Override
	public String novo() {
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método seleciona um aluno e prepara para entrar na tela de cadastro de pagamento
	 * 
	 * @author WENDEL
	 * @return String - Página de Cadastro de Pagamento
	 */
	@Override
	public String selecionar() {
		try {
			return PagamentoCadastroControlador.getInstancia().entrar();
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
	public String selecionarCurso() {
		try {
			setAbreListaCurso(false);
			setListaTurma(turmaServico.listarPorCurso(getCurso()));
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
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

	public List<AlunoTurma> getListaAlunos() {
		if (listaAlunos == null) {
			listaAlunos = new ArrayList<AlunoTurma>();
		}
		return listaAlunos;
	}

	public void setListaAlunos(List<AlunoTurma> listaAlunos) {
		this.listaAlunos = listaAlunos;
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

	public List<Turma> getListaTurma() {
		if (listaTurma == null) {
			listaTurma = new ArrayList<Turma>();
		}
		return listaTurma;
	}

	public void setListaTurma(List<Turma> listaTurma) {
		this.listaTurma = listaTurma;
	}

	public boolean isAbreListaCurso() {
		return abreListaCurso;
	}

	public void setAbreListaCurso(boolean abreListaCurso) {
		this.abreListaCurso = abreListaCurso;
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
	
	/**
	 * @return PagamentoListagemControlador
	 */
	public static PagamentoListagemControlador getInstancia() {
		return (PagamentoListagemControlador) SpringContainer.getInstancia().getBean("pagamentoListagemControlador");
	}
}
