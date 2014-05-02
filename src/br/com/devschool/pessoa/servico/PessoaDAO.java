package br.com.devschool.pessoa.servico;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.devschool.entidade.Pessoa;
import br.com.devschool.enumeradores.EnumFiltroProfessor;
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.NegocioException;
import br.com.devschool.util.PersistenciaException;

/**
 * @author ATILLA
 * @date 12/09/2012
 */
@Repository
public class PessoaDAO extends DAOGenerico<Pessoa> {
    
    private static String FILTROS ;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	protected Pessoa buscaPor(String login, String senha) throws PersistenciaException, NegocioException {
		try {
			Pessoa usuario = (Pessoa) createCriteria().add(Restrictions.eq("email", login)).add(Restrictions.eq("senha", senha)).uniqueResult(); 
			if(usuario == null) {
				throw new NegocioException("negocio.validacao.loginInvalido");
			}
			
			return usuario;
		} catch (NegocioException e) {
			throw e;
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}

	/**
	 * Este método faz uma pesquisa dos professores e retorna uma lista de
	 * objetos com os resultados.
	 * 
	 * @author	Josemar
	 * @date	09/01/2013
	 * @return	List
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	protected List<Pessoa> listarProfessores(EnumFiltroProfessor filtroProfessorEnum) throws PersistenciaException {
		try {
	    	FILTROS = "";
	    	if(filtroProfessorEnum.equals(EnumFiltroProfessor.ATIVO)) {
	    	    FILTROS = "AND p.status = true";
	    	} else if(filtroProfessorEnum.equals(EnumFiltroProfessor.INATIVO)){
	    		FILTROS = "AND p.status = false";
	    	}
			return (List<Pessoa>) entityManager.createQuery("SELECT p FROM Pessoa p WHERE p.tipoProfessor = true "+ FILTROS).getResultList();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
	
	/**
	 * Este método lista todas pessoas de acordo com parâmetros recebidos.
	 * 
	 * @param Boolean tipoAluno
	 * @param Boolean tipoProfessor
	 * @param Boolean tipoAdm
	 * @param Boolean status
	 * @param Date dataInicio
	 * @param Date dataFim
	 * @return List<Pessoa>
	 * @throws PersistenciaException
	 */
	@SuppressWarnings("all")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Pessoa> listarPessoasPorFiltros(Boolean tipoAluno, Boolean tipoProfessor, Boolean tipoAdm, Boolean status, Date dataInicio, Date dataFim) throws PersistenciaException {
		try {
			Criteria criteria = createCriteria().addOrder(Order.asc("dataCadastro"));
			
			if (tipoAluno != null)
				criteria.add(Restrictions.eq("tipoAluno", tipoAluno));
			
			if (tipoProfessor != null)
				criteria.add(Restrictions.eq("tipoProfessor", tipoProfessor));
			
			if (tipoAdm != null)
				criteria.add(Restrictions.eq("tipoAdm", tipoAdm));
			
			if (status != null)
				criteria.add(Restrictions.eq("status", status));
			
			if (dataInicio != null && dataFim != null)
				criteria.add(Restrictions.between("dataCadastro", dataInicio, dataFim));
			
			return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
}
