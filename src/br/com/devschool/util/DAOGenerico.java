package br.com.devschool.util;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;

/**
 * @author	ATILLA
 * @author	WENDEL
 * @date	12/09/2012
 * 
 */
public abstract class DAOGenerico <T extends EntidadeGeral> {

	private Class<T> classeDaEntidade;

	@PersistenceContext
	protected EntityManager entityManager;
	
	/**
	 * Este método salva ou atualiza o objeto na base de dados
	 * 
	 * @author 	ATILLA
	 * @author 	WENDEL
	 * @date	12/09/2012
	 * @return	Objeto da Entidade
	 * @param	Objeto da Entidade
	 */
	protected T salvarOuAtualizar(T entidade) throws PersistenciaException {
		try{	
			return (T) entityManager.merge(entidade);
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
	
	protected Criteria createCriteria() throws PersistenciaException {
		try {
			Session session = ((Session) entityManager.getDelegate());
			return session.createCriteria(getClasseDaEntidade());
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
	
	/**
	 * Este método exclui o objeto na base de dados
	 * <b>Obs: Cuidado ao usar este método!!</b>
	 * 
	 * @author 	ATILLA
	 * @author 	WENDEL
	 * @date	12/09/2012
	 * @param	Objeto da Entidade
	 */
	protected void excluir(T entidade) throws PersistenciaException {
		try{
			entidade = procurarPorCodigo(entidade.getId());
			entityManager.remove(entidade);
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
	
	/**
	 * Este método procura um registro na base de dados
	 * de acordo com o código passado como argumento.
	 * 
	 * @author 	ATILLA
	 * @author 	WENDEL
	 * @date	12/09/2012
	 * @return	Objeto da Entidade
	 * @param	Integer - Código a ser pesquisado
	 */
	protected T procurarPorCodigo(Integer codigo) throws PersistenciaException {
		try{
			return (T) entityManager.find(getClasseDaEntidade(), codigo);
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
	
	/**
	 * Este método faz uma pesquisa na base de dados
	 * e retorna uma lista de objetos com os resultados.
	 * <b>Obs: Cuidado ao usar este método!!</b>
	 * 
	 * @author 	ATILLA
	 * @author 	WENDEL
	 * @date	12/09/2012
	 * @return	List
	 */
	@SuppressWarnings("unchecked")
	protected List<T> listarTodos() throws PersistenciaException {
		try{
			return (List<T>) entityManager.createQuery("from "+getClasseDaEntidade().getSimpleName()).getResultList();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
	
	/**
	 * Método construtor
	 */
	@SuppressWarnings("unchecked")
	public DAOGenerico() {
		setClasseDaEntidade((Class<T>) getClasseGenerica(this.getClass()));
	}
	
	public static Class<?> getClasseGenerica(Class<?> classe) {
		if (classe == null)
			return null;

		if (classe.getGenericSuperclass() instanceof ParameterizedType)
			return (Class<?>) ((ParameterizedType) classe.getGenericSuperclass()).getActualTypeArguments()[0];

		return null;
	}
	
	public Class<T> getClasseDaEntidade() {
		return classeDaEntidade;
	}

	public void setClasseDaEntidade(Class<T> classeDaEntidade) {
		this.classeDaEntidade = classeDaEntidade;
	}
}
