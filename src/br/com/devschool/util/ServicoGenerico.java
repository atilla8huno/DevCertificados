package br.com.devschool.util;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author	ATILLA
 * @date	12/09/2012
 * 
 */
public abstract class ServicoGenerico <T extends EntidadeGeral> {

	protected abstract DAOGenerico<T> getDAO();
	protected abstract boolean verificarEntidade(T entidade) throws NegocioException;
	
	/**
	 * Este método salva ou atualiza o objeto na base de dados
	 * 
	 * @author 	ATILLA
	 * @date	24/09/2012
	 * @return	Objeto da Entidade
	 * @param	Objeto da Entidade
	 * @throws NegocioException 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public T salvarOuAtualizar(T entidade) throws PersistenciaException, NegocioException {
		return (T) getDAO().salvarOuAtualizar(entidade);
	}
	
	/**
	 * Este método exclui o objeto na base de dados
	 * <b>Obs: Cuidado ao usar este método!!</b>
	 * 
	 * @author 	ATILLA
	 * @date	24/09/2012
	 * @param	Objeto da Entidade
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(T entidade) throws PersistenciaException {
		getDAO().excluir(entidade);
	}
	
	/**
	 * Este método procura um registro na base de dados
	 * de acordo com o código passado como argumento.
	 * 
	 * @author 	ATILLA
	 * @date	24/09/2012
	 * @return	Objeto da Entidade
	 * @param	Integer - Código a ser pesquisado
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public T procurarPorCodigo(Integer codigo) throws PersistenciaException {
		return (T) getDAO().procurarPorCodigo(codigo);
	}
	
	/**
	 * Este método faz uma pesquisa na base de dados
	 * e retorna uma lista de objetos com os resultados.
	 * <b>Obs: Cuidado ao usar este método!!</b>
	 * 
	 * @author 	ATILLA
	 * @date	24/09/2012
	 * @return	List
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<T> listarTodos() throws PersistenciaException {
		return (List<T>) getDAO().listarTodos();
	}
}
