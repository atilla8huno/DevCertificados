package br.com.devschool.curso;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.controle.LoginControlador;
import br.com.devschool.entidade.AlunoTurma;
import br.com.devschool.entidade.MatrizCurricular;
import br.com.devschool.entidade.MatrizCurricularDisciplina;
import br.com.devschool.entidade.Turma;
import br.com.devschool.matriz_curricular.servico.MatrizCurricularServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.ServicoGenerico;

@Component("detalhesCursoControlador")
@Scope("session")
public class DetalhesCursoControlador extends ControladorGenerico<MatrizCurricular> {

	@Autowired
	private MatrizCurricularServico matrizCurricularServico;
	private MatrizCurricular entidade;
	private List<Turma> listaTurma;
	private List<MatrizCurricularDisciplina> listaDisciplina;
	private int cargaHoraria;
	
	@Override
	protected ServicoGenerico<MatrizCurricular> getService() {
		return null;
	}

	@Override
	public MatrizCurricular getEntidade() {
		if(entidade == null){
			entidade = new MatrizCurricular();
		}
		return entidade;
	}

	@Override
	public void setEntidade(MatrizCurricular entidade) {
		this.entidade = entidade;
	}

	@Override
	public String entrar() {
		getEntidade();
		getListaTurma().clear();
		getListaTurma().addAll(getEntidade().getTurmas());
		removeTurmasMatriculadas();
		getListaDisciplina().clear();
		getListaDisciplina().addAll(getEntidade().getMatrizCurricularDisciplinas());
		cargaHoraria = 0;
		for(MatrizCurricularDisciplina matrizCurricularDisciplina : getListaDisciplina()){
			cargaHoraria += matrizCurricularDisciplina.getCargaHoraria();
		}
		
		return "/paginas/aluno/detalhesCurso.jsf?faces-redirect=true";
	}
	
	private void removeTurmasMatriculadas(){
		for (AlunoTurma alunoTurma : LoginControlador.getInstancia().getUsuarioSessao().getAlunoTurmas()) {
			if(getListaTurma().contains(alunoTurma.getTurma())){
				getListaTurma().remove(alunoTurma.getTurma());
			}
		}
	}

	@Override
	public String limpar() {
		return PAGINA_CORRENTE;
	}

	public MatrizCurricularServico getMatrizCurricularServico() {
		return matrizCurricularServico;
	}

	public void setMatrizCurricularServico(
			MatrizCurricularServico matrizCurricularServico) {
		this.matrizCurricularServico = matrizCurricularServico;
	}

	public List<Turma> getListaTurma() {
		if(listaTurma == null){
			listaTurma = new ArrayList<Turma>();
		}
		return listaTurma;
	}

	public void setListaTurma(List<Turma> listaTurma) {
		this.listaTurma = listaTurma;
	}

	public List<MatrizCurricularDisciplina> getListaDisciplina() {
		if(listaDisciplina == null){
			listaDisciplina = new ArrayList<MatrizCurricularDisciplina>();
		}
		return listaDisciplina;
	}

	public void setListaDisciplina(List<MatrizCurricularDisciplina> listaDisciplina) {
		this.listaDisciplina = listaDisciplina;
	}

	public int getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(int cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}
}
