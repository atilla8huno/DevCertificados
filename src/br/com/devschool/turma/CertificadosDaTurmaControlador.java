package br.com.devschool.turma;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.entidade.AlunoTurma;
import br.com.devschool.entidade.MatrizCurricular;
import br.com.devschool.entidade.Turma;
import br.com.devschool.matricula.servico.AlunoTurmaServico;
import br.com.devschool.matriz_curricular.servico.MatrizCurricularServico;
import br.com.devschool.turma.servico.TurmaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.IListagemControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

/**
 * @author ATILLA
 */
@Component("certificadosDaTurmaControlador")
@Scope("session")
public class CertificadosDaTurmaControlador extends ControladorGenerico<Turma> implements IListagemControlador {

	@Autowired
	private TurmaServico turmaServico;
	@Autowired
	private AlunoTurmaServico alunoTurmaServico;
	@Autowired
	private MatrizCurricularServico matrizCurricularServico;
	
	private Turma turma;
	private MatrizCurricular matrizCurricular;
	private List<MatrizCurricular> cursos;
	private List<Turma> turmas;
	private List<AlunoTurma> alunos;
	private List<AlunoTurma> alunosSelecionados;
	
	@Override
	public String novo() {
		try {
			limpar();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	@Override
	public String selecionar() {
		return PAGINA_CORRENTE;
	}

	@Override
	protected ServicoGenerico<Turma> getService() {
		return turmaServico;
	}

	@Override
	public Turma getEntidade() {
		if (turma == null) {
			turma = new Turma();
		}
		return turma;
	}

	@Override
	public void setEntidade(Turma entidade) {
		this.turma = entidade;
	}

	@Override
	public String entrar() {
		try {
			limpar();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return "/paginas/adm/certificadosDaTurma.jsf?faces-redirect=true";
	}

	@Override
	public String limpar() {
		try {
			turma	= new Turma();
			matrizCurricular = new MatrizCurricular();
			cursos	= new ArrayList<MatrizCurricular>();
			turmas	= new ArrayList<Turma>();
			alunos	= new ArrayList<AlunoTurma>();
			alunosSelecionados = new ArrayList<AlunoTurma>();
		
			cursos = matrizCurricularServico.listarTodos();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método atualiza a lista de alunos (AlunoTurma) de acordo com a turma selecionada.
	 * 
	 * @author	ATILLA
	 * @date	27/03/2013
	 */
	public void atualizaComboAlunos(){
		try {
			alunosSelecionados.clear();
			alunos.clear();
			if(turma != null && !turma.isTransient()) {
				turma = turmaServico.procurarPorCodigo(turma.getId());
				alunos.addAll(turma.getAlunoTurmas());
			}
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}
	
	/**
	 * Este método atualiza a lista de turmas (AlunoTurma) de acordo com o aluno selecionado.
	 * 
	 * @author	ATILLA
	 * @date	27/03/2013
	 */
	public void atualizaComboTurmas(){
		try {
			turmas.clear();
			if(matrizCurricular != null && !matrizCurricular.isTransient()) {
				matrizCurricular = matrizCurricularServico.procurarPorCodigo(matrizCurricular.getId());
				turmas.addAll(matrizCurricular.getTurmas());
			}
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}
	
	/**
	 * Este método gera o certificado dos alunos e disponibiliza para exibição/download.
	 * 
	 * @author ATILLA
	 */
	public void emitirCertificado(){
		try {
			if(alunosSelecionados != null && alunosSelecionados.size() > 0) {
				exibirRelatorioSessao(alunoTurmaServico.gerarCertificadosDosAlunos(alunosSelecionados));
			} else {
				setarMensagemInfo("Ops! Certificado não encontrado.");
			}
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}
	
	/**
	 * @return CertificadosDaTurmaControlador
	 */
	public static CertificadosDaTurmaControlador getInstancia() {
		return (CertificadosDaTurmaControlador) SpringContainer.getInstancia().getBean("certificadosDaTurmaControlador");
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

	public List<MatrizCurricular> getCursos() {
		if (cursos == null) {
			cursos = new ArrayList<MatrizCurricular>();
		}
		return cursos;
	}

	public void setCursos(List<MatrizCurricular> cursos) {
		this.cursos = cursos;
	}

	public List<Turma> getTurmas() {
		if (turmas == null) {
			turmas = new ArrayList<Turma>();
		}
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public List<AlunoTurma> getAlunos() {
		if (alunos == null) {
			alunos = new ArrayList<AlunoTurma>();
		}
		return alunos;
	}

	public void setAlunos(List<AlunoTurma> alunos) {
		this.alunos = alunos;
	}

	public List<AlunoTurma> getAlunosSelecionados() {
		if (alunosSelecionados == null) {
			alunosSelecionados = new ArrayList<AlunoTurma>();
		}
		return alunosSelecionados;
	}

	public void setAlunosSelecionados(List<AlunoTurma> alunosSelecionados) {
		this.alunosSelecionados = alunosSelecionados;
	}

	public MatrizCurricular getMatrizCurricular() {
		if (matrizCurricular == null) {
			matrizCurricular = new MatrizCurricular();
		}
		return matrizCurricular;
	}

	public void setMatrizCurricular(MatrizCurricular matrizCurricular) {
		this.matrizCurricular = matrizCurricular;
	}
}
