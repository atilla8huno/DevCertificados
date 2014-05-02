package br.com.devschool.turma.servico;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.devschool.entidade.Curso;
import br.com.devschool.entidade.Pessoa;
import br.com.devschool.entidade.Turma;
import br.com.devschool.enumeradores.EnumStatusTurma;
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.PersistenciaException;

/**
 * @author	ATILLA
 * @date	12/09/2012
 * 
 */
@Repository
public class TurmaDAO extends DAOGenerico<Turma>{
	
	/**
	 * Este método lista as turmas referente a um curso
	 * 
	 * @author WENDEL
	 * @param curso - Curso a pesquisar
	 * @return List<Turma> - Lista de turmas referente ao curso pesquisado
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected List<Turma> listarPorCurso(Curso curso) throws PersistenciaException {
		try {
			Query query = entityManager.createQuery("FROM Turma t WHERE t.matrizCurricular.curso.id = :idCurso ORDER BY t.nome");
			query.setParameter("idCurso", curso.getId());
			
			return query.getResultList();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
	
	/**
	 * Este método lista turmas referente a um curso e um professor
	 * 
	 * @author WENDEL
	 * @param curso - Curso a pesquisar
	 * @param pessoa - Professor a pesquiasr
	 * @return Lista<Turma> - Lista de turmas
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected List<Turma> listarPorCursoProfessor(Curso curso,Pessoa pessoa) throws PersistenciaException {
		try {
			Query query = entityManager.createQuery("FROM Turma t WHERE t.matrizCurricular.curso.id = :idCurso AND t.pessoa.id = :idPessoa ORDER BY t.nome");
			query.setParameter("idCurso", curso.getId());
			query.setParameter("idPessoa", pessoa.getId());
			
			return query.getResultList();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
	
	/**
	 * Este método lista todas as turmas relacionadas a um professor
	 * 
	 * @author 	ATILLA
	 * @date	21/03/2013
	 * @param	Pessoa - Professor
	 * @throws 	PersistenciaException
	 */
	@SuppressWarnings("unchecked")
	protected List<Turma> listarPorProfessor(Pessoa professor) throws PersistenciaException {
		try {
			Query query = entityManager.createQuery("FROM Turma t WHERE t.pessoa.id = :idPessoa ORDER BY t.nome");
			query.setParameter("idPessoa", professor.getId());
			
			return query.getResultList();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}

	/**
	 * Este método lista todas as turmas não concluídas
	 * 
	 * @author 	ATILLA
	 * @date	15/04/2013
	 * @throws 	PersistenciaException
	 */
	@SuppressWarnings("unchecked")
	protected List<Turma> listarTurmasEmAberto() throws PersistenciaException {
		try {
			Query query = entityManager.createQuery("FROM Turma t WHERE t.status <> '"+EnumStatusTurma.CANCELADA+"' AND t.status <> '"+EnumStatusTurma.CONCLUIDA+"'");
			
			return query.getResultList();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
	
	/**
	 * Este método lista todas turmas de acordo com filtros recebidos.
	 * 
	 * @param Date dataInicio
	 * @param Date dataFim
	 * @param Pessoa professor
	 * @param EnumStatusTurma status
	 * @return List<Turma>
	 * @throws PersistenciaException
	 */
	@SuppressWarnings("all")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Turma> listarTurmasPorFiltros(Date dataInicio, Date dataFim, Pessoa professor, EnumStatusTurma status) throws PersistenciaException {
		try {
			Criteria criteria = createCriteria().addOrder(Order.asc("nome"));
			
			if (dataInicio != null && dataFim != null)
				criteria.add(Restrictions.between("dataRealInicio", dataInicio, dataFim))
						.add(Restrictions.between("dataRealFim", dataInicio, dataFim));
			
			if (professor != null)
				criteria.add(Restrictions.eq("pessoa", professor));
			
			if (status != null)
				criteria.add(Restrictions.eq("status", status));
			
			return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
}
