package br.com.devschool.disciplina.servico;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.devschool.entidade.Disciplina;
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.PersistenciaException;

/**
 * @author	ATILLA
 * @date	12/09/2012
 * 
 */
@Repository
public class DisciplinaDAO extends DAOGenerico<Disciplina>{

	/**
	 * Esse método recebe um objeto de Disciplina como argumento
	 * e o desativa, alterando seu status para false no BD.
	 * 
	 * @author 	WENDEL
	 * @date	29/12/2012
	 * @param	Objeto da Entidade
	 * @return 	Objeto Disciplina
	 * @throws PersistenciaException  
	 */
	protected Disciplina desativarAtivarDisciplina(Disciplina entidade) throws PersistenciaException {
		try {
			if(entidade.isStatus()){
				entidade.setStatus(false);
			} else {
				entidade.setStatus(true);
			}
			
			return (Disciplina) entityManager.merge(entidade);
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
	
	/**
	 * Este método faz uma pesquisa dos professores e retorna uma lista de
	 * objetos com os resultados.
	 * 
	 * @author	ATILLA
	 * @date	10/01/2013
	 * @return	List
	 */
	@SuppressWarnings("unchecked")
	protected List<Disciplina> listarDisciplinasAtivas() throws PersistenciaException {
		try {
			return (List<Disciplina>) entityManager.createQuery("SELECT d FROM Disciplina d WHERE d.status = true").getResultList();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
}
