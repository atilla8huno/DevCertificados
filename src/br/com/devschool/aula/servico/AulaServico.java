package br.com.devschool.aula.servico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.devschool.entidade.Aula;
import br.com.devschool.entidade.Turma;
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.NegocioException;
import br.com.devschool.util.PersistenciaException;
import br.com.devschool.util.ServicoGenerico;

@Service
public class AulaServico extends ServicoGenerico<Aula> {
	@Autowired
	private AulaDAO dao;
	
	/**
	 * Este método retorna uma lista de aulas referente uma turma
	 * 
	 * @author	WENDEL
	 * @since	26/01/2013
	 * @param	turma - Turma a pesquisar
	 * @return	List<Aula> - Lista de aulas da turma pesquisada
	 * @throws	PersistenciaException
	 */
	public List<Aula> listarAulaPorTurma(Turma turma) throws PersistenciaException {
		return dao.listarAulaPorTurma(turma);
	}
	
	/**
	 * Este método salva o plano de aula
	 * 
	 * @param planosDeAulas - Lista de plano de aula a salvar
	 * @throws PersistenciaException, NegocioException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void salvarPlanosDeAula(List<Aula> planosDeAulas) throws PersistenciaException, NegocioException {
		for (Aula aula : planosDeAulas) {
			verificarEntidade(aula);
			salvarOuAtualizar(aula);
		}
	}
	
	@Override
	protected DAOGenerico<Aula> getDAO() {
		return dao;
	}

	@Override
	protected boolean verificarEntidade(Aula entidade) throws NegocioException {
		if(entidade == null)
			throw new NegocioException("negocio.validacao.objetoNulo");
		else
			return true;
	}
}
