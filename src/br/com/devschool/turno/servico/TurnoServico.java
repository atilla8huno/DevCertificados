package br.com.devschool.turno.servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.devschool.entidade.Turno;
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.NegocioException;
import br.com.devschool.util.PersistenciaException;
import br.com.devschool.util.ServicoGenerico;

/**
 * @author	ATILLA
 * @date	13/09/2012
 * 
 */
@Service
public class TurnoServico extends ServicoGenerico<Turno>{

	@Autowired
	private TurnoDAO dao;

	@Override
	protected DAOGenerico<Turno> getDAO() {
		return dao;
	}
	
	/**
	 * Este método salva ou atualiza o objeto na base de dados
	 * 
	 * @author 	ATILLA
	 * @date	24/09/2012
	 * @return	Objeto da Entidade
	 * @param	Objeto da Entidade
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Turno salvarOuAtualizar(Turno entidade) throws PersistenciaException, NegocioException {
		verificarEntidade(entidade);
		
		return super.salvarOuAtualizar(entidade);
	}

	/**
	 * Este método faz uma validação do objeto de acordo
	 * com as regras de negócio
	 * 
	 * @author 	ATILLA
	 * @date	24/09/2012
	 * @param	Objeto da Entidade
	 * @throws 	NegocioException 
	 */
	@Override
	protected boolean verificarEntidade(Turno entidade) throws NegocioException {
		if(entidade == null) {
			throw new NegocioException("negocio.validacao.objetoNulo");
		} else if (entidade.getDescricao().isEmpty() || entidade.getSigla().isEmpty()){
			throw new NegocioException("negocio.validacao.campoObrigatorio");
		} else {
			return true;
		}
	}
}
