package br.com.devschool.aula.servico;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.devschool.entidade.Aula;
import br.com.devschool.entidade.Turma;
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.PersistenciaException;

@Repository
public class AulaDAO extends DAOGenerico<Aula> {

	/**
	 * Este método retorna uma lista de aulas referente uma turma
	 * 
	 * @author WENDEL
	 * @since 26/01/2013
	 * @param turma - Turma a pesquisar
	 * @return List<Aula> - Lista de aulas da turma pesquisada
	 * @throws PersistenciaException
	 */
	@SuppressWarnings("unchecked")
	protected List<Aula> listarAulaPorTurma(Turma turma) throws PersistenciaException {
		try {
			Query query = entityManager.createQuery("FROM Aula a WHERE a.turma.id = :idTurma ORDER BY a.dataAula");
			query.setParameter("idTurma", turma.getId());
			
			return (List<Aula>) query.getResultList();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
}
