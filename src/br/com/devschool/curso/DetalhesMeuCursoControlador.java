package br.com.devschool.curso;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.controle.LoginControlador;
import br.com.devschool.entidade.AlunoTurma;
import br.com.devschool.entidade.FrequenciaAluno;
import br.com.devschool.entidade.MatrizCurricular;
import br.com.devschool.entidade.MatrizCurricularDisciplina;
import br.com.devschool.entidade.Pagamento;
import br.com.devschool.matriz_curricular.servico.MatrizCurricularServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.ServicoGenerico;

@Component("detalhesMeuCursoControlador")
@Scope("session")
public class DetalhesMeuCursoControlador extends ControladorGenerico<MatrizCurricular> {

	@Autowired
	private MatrizCurricularServico matrizCurricularServico;
	private MatrizCurricular entidade;
	private List<AlunoTurma> listaAlunoTurma;
	private List<MatrizCurricularDisciplina> listaDisciplina;
	private AlunoTurma alunoTurma;
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
		listaTurmasAluno();
		getListaDisciplina().clear();
		getListaDisciplina().addAll(getEntidade().getMatrizCurricularDisciplinas());
		cargaHoraria = 0;
		for(MatrizCurricularDisciplina matrizCurricularDisciplina : getListaDisciplina()){
			cargaHoraria += matrizCurricularDisciplina.getCargaHoraria();
		}
		
		return "/paginas/aluno/detalhesMeuCurso.jsf";
	}
	
	private void listaTurmasAluno(){
		getListaTurma().clear();
		for (AlunoTurma alunoTurma : LoginControlador.getInstancia().getUsuarioSessao().getAlunoTurmas()) {
			if(getEntidade().equals(alunoTurma.getTurma().getMatrizCurricular())){
				getListaTurma().add(alunoTurma);
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

	public List<AlunoTurma> getListaTurma() {
		if(listaAlunoTurma == null){
			listaAlunoTurma = new ArrayList<AlunoTurma>();
		}
		return listaAlunoTurma;
	}

	public void setListaTurma(List<AlunoTurma> listaAlunoTurma) {
		this.listaAlunoTurma = listaAlunoTurma;
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

	public AlunoTurma getAlunoTurma() {
		if(this.alunoTurma == null){
			this.alunoTurma = new AlunoTurma();
		}
		return this.alunoTurma;
	}

	public void setAlunoTurma(AlunoTurma alunoTurma) {
		this.alunoTurma = alunoTurma;
	}
	
	public List<Pagamento> getPagamentos(){
		List<Pagamento> lista = new ArrayList<Pagamento>();
		if(!getAlunoTurma().isTransient()){
			lista.addAll(getAlunoTurma().getPagamentos());
		}
		return lista;
	}
	
	public List<FrequenciaAluno> getFrequencia(){
		List<FrequenciaAluno> lista = new ArrayList<FrequenciaAluno>();
		
		if(!getAlunoTurma().isTransient()){
			lista.addAll(getAlunoTurma().getFrequenciaAlunos());
		}
		
		return lista;
	}
}
