package br.com.devschool.persistencia;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.devschool.entidade.MatrizCurricular;
import br.com.devschool.entidade.MatrizCurricularDisciplina;
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.PersistenciaException;

/**
 * @author	ATILLA
 * @date	12/09/2012
 */
@Repository
public class MatrizCurricularDisciplinaDAO extends DAOGenerico<MatrizCurricularDisciplina>{
	
	@SuppressWarnings("unchecked")
	public List<MatrizCurricularDisciplina> listarPorMatrizCurricular(MatrizCurricular matrizCurricular) throws PersistenciaException {
		try {
			Query query = entityManager.createQuery("FROM MatrizCurricularDisciplina m WHERE m.matrizCurricular.id = :id");
			query.setParameter("id", matrizCurricular.getId());
			
			return (List<MatrizCurricularDisciplina>) query.getResultList();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
}
