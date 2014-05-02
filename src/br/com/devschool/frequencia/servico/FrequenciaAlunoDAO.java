package br.com.devschool.frequencia.servico;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.devschool.entidade.AlunoTurma;
import br.com.devschool.entidade.Aula;
import br.com.devschool.entidade.FrequenciaAluno;
import br.com.devschool.entidade.Turma;
import br.com.devschool.matricula.servico.AlunoTurmaServico;
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.PersistenciaException;
import br.com.devschool.util.SpringContainer;

/**
 * @author	ATILLA
 * @date	12/09/2012
 * 
 */
@Repository
public class FrequenciaAlunoDAO extends DAOGenerico<FrequenciaAluno>{
	
	/**
	 * @author WENDEL
	 * @param Turma turma
	 * @param Aula aula
	 * @return List<FrequenciaAluno>
	 * @throws PersistenciaException
	 */
	protected List<FrequenciaAluno> listaFrequencia(Turma turma, Aula aula) throws PersistenciaException {
		try {
			AlunoTurmaServico matriculaServico = (AlunoTurmaServico) SpringContainer.getInstancia().getBean("alunoTurmaServico");
			List<AlunoTurma> listaAlunos = matriculaServico.listarPorTurma(turma);
			List<FrequenciaAluno> listaFrequencia  = new ArrayList<FrequenciaAluno>();
			for(AlunoTurma alunoTurma : listaAlunos){
				listaFrequencia.add(procuraFrequenciaAluno(alunoTurma, aula));
			}
			return listaFrequencia;
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
	
	/**
	 * Este método procura a frequência de um aluno
	 * 
	 * @author WENDEL
	 * @param AlunoTurma alunoTurma
	 * @param Aula aula
	 * @return FrequenciaAluno
	 * @throws PersistenciaException
	 */
	@SuppressWarnings("unchecked")
	private FrequenciaAluno procuraFrequenciaAluno(AlunoTurma alunoTurma, Aula aula) throws PersistenciaException {
		try {
			Query query = entityManager.createQuery("SELECT f FROM FrequenciaAluno f WHERE f.alunoTurma.id = :idAlunoTurma AND f.aula.id = :idAula");
			query.setParameter("idAlunoTurma", alunoTurma.getId());
			query.setParameter("idAula", aula.getId());
			List<FrequenciaAluno> lista = (List<FrequenciaAluno>) query.getResultList();
			if(lista.size()==0){
				FrequenciaAluno frequenciaAluno = new FrequenciaAluno();
				frequenciaAluno.setAlunoTurma(alunoTurma);
				frequenciaAluno.setAula(aula);
				frequenciaAluno.setFalta(false);
				
				return frequenciaAluno;
			}
			return lista.get(0);
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
	
	/**
	 * Este método atualiza a frequência de uma turma
	 * 
	 * @param Turma turma
	 * @throws PersistenciaException
	 */
	protected void atualizaFrequenciaTurma(Turma turma) throws PersistenciaException{
		try {
			Query query = entityManager.createNativeQuery("SELECT atualizafrequenciaturma(:idTurma)");
			query.setParameter("idTurma", turma.getId());
			query.getSingleResult();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
}
