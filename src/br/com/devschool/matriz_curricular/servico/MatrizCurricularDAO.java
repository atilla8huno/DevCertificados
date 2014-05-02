package br.com.devschool.matriz_curricular.servico;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.devschool.entidade.Curso;
import br.com.devschool.entidade.MatrizCurricular;
import br.com.devschool.entidade.Nivel;
import br.com.devschool.entidade.Turno;
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.PersistenciaException;

/**
 * @author ATILLA
 * @date 12/09/2012
 * 
 */
@Repository
public class MatrizCurricularDAO extends DAOGenerico<MatrizCurricular> {

	/**
	 * Este método lista todas matrizes curriculares de acordo com parâmetros recebidos.
	 * 
	 * @param Curso curso
	 * @param Nivel nivel
	 * @param Turno turno
	 * @return List<MatrizCurricular>
	 * @throws PersistenciaException
	 */
	@SuppressWarnings("all")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<MatrizCurricular> listarMatrizesPorFiltros(Curso curso, Nivel nivel, Turno turno) throws PersistenciaException {
		try {
			Criteria criteria = createCriteria();
			
			if (curso != null)
				criteria.add(Restrictions.eq("curso", curso));
			
			if (nivel != null)
				criteria.add(Restrictions.eq("nivel", nivel));
			
			if (turno != null)
				criteria.add(Restrictions.eq("turno", turno));
			
			return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
}
