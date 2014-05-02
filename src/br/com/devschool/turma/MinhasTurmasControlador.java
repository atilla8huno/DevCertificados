package br.com.devschool.turma;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.controle.LoginControlador;
import br.com.devschool.entidade.MatrizCurricularDisciplina;
import br.com.devschool.entidade.Pessoa;
import br.com.devschool.entidade.Turma;
import br.com.devschool.matriz_curricular.servico.MatrizCurricularDisciplinaServico;
import br.com.devschool.turma.servico.TurmaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.Ferramentas;
import br.com.devschool.util.IListagemControlador;
import br.com.devschool.util.ServicoGenerico;

@Component("minhasTurmasControlador")
@Scope("session")
public class MinhasTurmasControlador extends ControladorGenerico<Turma> implements IListagemControlador {

	@Autowired
	private TurmaServico turmaServico;
	@Autowired
	private MatrizCurricularDisciplinaServico matrizCurricularDisciplinaServico;
	
	private Turma turma;
	private List<Turma> turmas;
	private Pessoa professor;
	
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
			professor = LoginControlador.getInstancia().getUsuarioSessao();
			for (Turma t : turmaServico.listarPorProfessor(professor)) {
		    	t.getMatrizCurricular().getCurso()
					.setMostraImagem(Ferramentas.mostraImagem(t.getMatrizCurricular().getCurso().getImagem(), t.getMatrizCurricular().getCurso()));
		    	turmas.add(t);
		    }
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return "/paginas/professor/minhasTurmas.jsf?faces-redirect=true";
	}

	/**
	 * Este método gera o certificado da turma e disponibiliza para exibição/download.
	 * 
	 * @author ATILLA
	 */
	public void emitirCertificado(){
		try {
			exibirRelatorioSessao(turmaServico.gerarCertificadosDaTurma(turma));
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}
	
	/**
	 * Este método gera o certificado do professore disponibiliza para exibição/download.
	 * 
	 * @author ATILLA
	 */
	public void emitirCertificadoProfessor(){
		try {
			exibirRelatorioSessao(turmaServico.gerarCertificadoDoProfessor(turma));
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}
	
	/**
     * Calcula carga horaria total
     * 
     * @return Integer
     */
    public Integer calculaCargaHoraria() {
    	Integer total = 0;
    	try {
    		if (turma != null && turma.getMatrizCurricular() != null) {
    			List<MatrizCurricularDisciplina> matrizCurricularDisciplinas = matrizCurricularDisciplinaServico.listarPorMatrizCurricular(turma.getMatrizCurricular());
    		    for (MatrizCurricularDisciplina m : matrizCurricularDisciplinas) {
    		    	total += (m.getCargaHoraria() != null ? m.getCargaHoraria() : 0);
    		    }
    		}
    	} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return total;
    }
	
	@Override
	public String limpar() {
		try {
			setEntidade(new Turma());
			turmas = new ArrayList<Turma>();
			if(professor == null || professor.isTransient()){
				professor = new Pessoa();
				professor.setTipoProfessor(true);
			}
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

	public List<Turma> getTurmas() {
		if (turmas == null) {
			turmas = new ArrayList<Turma>();
		}
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public Pessoa getProfessor() {
		if (professor == null) {
			professor = new Pessoa();
		}
		return professor;
	}

	public void setProfessor(Pessoa professor) {
		this.professor = professor;
	}
}
