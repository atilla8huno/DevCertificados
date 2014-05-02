package br.com.devschool.curso.servico;

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
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.PersistenciaException;

/**
 * @author	ATILLA
 * @date	12/09/2012
 * 
 */
@Repository
public class CursoDAO extends DAOGenerico<Curso>{
	
	/**
	 * Este método lista cursos referente a um professor
	 * 
	 * @author WENDEL
	 * @param pessoa - Professor a pesquisar
	 * @return List<Curso> - Lista de Curso
	 */
	@SuppressWarnings("unchecked")
	protected List<Curso> listarPorProfessor(Pessoa pessoa) throws PersistenciaException {
		try {
			Query query = entityManager.createQuery("SELECT DISTINCT(t.matrizCurricular.curso) FROM Turma t WHERE t.pessoa.id = :idPessoa");
			query.setParameter("idPessoa", pessoa.getId());
			
			return (List<Curso>) query.getResultList();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
	
	/**
	 * Este método lista todos cursos ativos ou inativos.
	 * 
	 * @param Boolean status
	 * @return List<Curso>
	 * @throws PersistenciaException
	 */
	@SuppressWarnings("all")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Curso> listarCursosPorFiltros(Boolean status) throws PersistenciaException {
		try {
			Criteria criteria = createCriteria().addOrder(Order.asc("titulo"));
			
			if (status != null)
				criteria.add(Restrictions.eq("status", status));
			
			return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
}
