package br.com.devschool.persistencia;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.devschool.entidade.AlunoTurma;
import br.com.devschool.entidade.Pessoa;
import br.com.devschool.entidade.Turma;
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.PersistenciaException;

/**
 * @author	ATILLA
 * @date	12/09/2012
 * 
 */
@Repository
public class AlunoTurmaDAO extends DAOGenerico<AlunoTurma> {
	
	/**
	 * Este método pesquisa todos os registros na tabela AlunoTurma onde o aluno for igual ao parâmentro.
	 * 
	 * @author 	ATILLA
	 * @param 	Pessoa aluno
	 * @return 	List<AlunoTurma>
	 * @throws 	PersistenciaException
	 * @date	20/03/2013
	 */
	@SuppressWarnings("unchecked")
	public List<AlunoTurma> listarPorAluno(Pessoa aluno) throws PersistenciaException {
		try {
			Query query = entityManager.createQuery("FROM AlunoTurma a WHERE a.pessoa.id = :idPessoa");
			query.setParameter("idPessoa", aluno.getId());
			
			return query.getResultList();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
	
	/**
	 * Este método pesquisa todos os registros na tabela AlunoTurma onde o aluno e a turma 
	 * forem iguais aos parâmentros.
	 * 
	 * @author 	ATILLA
	 * @param 	Pessoa aluno
	 * @param	Turma turma
	 * @return 	List<AlunoTurma>
	 * @throws 	PersistenciaException
	 * @date	25/03/2013
	 */
	@SuppressWarnings("unchecked")
	public List<AlunoTurma> listarPorAlunoETurma(Pessoa aluno, Turma turma) throws PersistenciaException {
		try {
			Query query = entityManager.createQuery("FROM AlunoTurma a WHERE a.pessoa.id = :idPessoa AND a.turma.id = :idTurma");
			query.setParameter("idPessoa",	aluno.getId());
			query.setParameter("idTurma",	turma.getId());
			
			return query.getResultList();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
	

	/**
	 * Este método verifica se a pessoa já está matriculada em uma turma
	 * 
	 * @author WENDEL
	 * @param pessoa - Pessoa a pesquisar
	 * @param turma - Turma a pesquisar
	 * @return boolean | True - Matriculado, False - Não Matriculado
	 * @throws PersistenciaException
	 */
	public boolean verificaMatricula(Pessoa pessoa, Turma turma) throws PersistenciaException {
		try {
			Query query = entityManager.createQuery("FROM AlunoTurma a WHERE a.pessoa.id = :idPessoa AND a.turma.id = :idTurma");
			query.setParameter("idPessoa", pessoa.getId());
			query.setParameter("idTurma", turma.getId());
			
			if(query.getResultList().size() > 0){
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
	
	/**
	 * Este método lista matriculas relacionado a uma turma
	 * 
	 * @author WENDEL
	 * @param turma | Turma a pesquisar
	 * @return List<AlunoTurma> - Lista de mátriculas
	 * @throws PersistenciaException
	 */
	@SuppressWarnings("unchecked")
	public List<AlunoTurma> listarPorTurma(Turma turma) throws PersistenciaException {
		try {
			Query query = entityManager.createQuery("FROM AlunoTurma a WHERE a.turma.id = :idTurma ORDER BY a.pessoa.nome");
			query.setParameter("idTurma", turma.getId());
			return (List<AlunoTurma>) query.getResultList();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
	
	/**
	 * Este método lista todas matriculas de acordo com parâmetros recebidos.
	 * 
	 * @param Pessoa aluno
	 * @param Turma turma
	 * @return List<AlunoTurma>
	 * @throws PersistenciaException
	 */
	@SuppressWarnings("all")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<AlunoTurma> listarMatriculasPorFiltros(Pessoa aluno, Turma turma) throws PersistenciaException {
		try {
			Criteria criteria = createCriteria();
			
			if (aluno != null)
				criteria.add(Restrictions.eq("pessoa", aluno));
			
			if (turma != null)
				criteria.add(Restrictions.eq("turma", turma));
			
			return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
}
