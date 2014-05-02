package br.com.devschool.pagamento.servico;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.devschool.entidade.AlunoTurma;
import br.com.devschool.entidade.Pagamento;
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.PersistenciaException;

/**
 * @author	ATILLA
 * @date	12/09/2012
 * 
 */
@Repository
public class PagamentoDAO extends DAOGenerico<Pagamento>{
	
	/**
	 * Este método retorna os pagamentos referentes a um aluno
	 * 
	 * @author WENDEL
	 * @param alunoTurma - AlunoTurma a pesquisar
	 * @return List<Pagamento> - Lista de pagamentos 
	 * @throws PersistenciaException 
	 */
	@SuppressWarnings("unchecked")
	protected List<Pagamento> listarPorAlunoTurma(AlunoTurma alunoTurma) throws PersistenciaException {
		try {
			Query query = entityManager.createQuery("FROM Pagamento p WHERE p.alunoTurma.id = :idAlunoTurma");
			query.setParameter("idAlunoTurma", alunoTurma.getId());
			
			return (List<Pagamento>) query.getResultList();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
}
