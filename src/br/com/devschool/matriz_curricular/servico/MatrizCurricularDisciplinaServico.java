package br.com.devschool.matriz_curricular.servico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.devschool.entidade.MatrizCurricular;
import br.com.devschool.entidade.MatrizCurricularDisciplina;
import br.com.devschool.persistencia.MatrizCurricularDisciplinaDAO;
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.NegocioException;
import br.com.devschool.util.PersistenciaException;
import br.com.devschool.util.ServicoGenerico;

/**
 * @author	ATILLA
 * @date	13/09/2012
 */
@Service
public class MatrizCurricularDisciplinaServico extends ServicoGenerico<MatrizCurricularDisciplina>{

	@Autowired
	private MatrizCurricularDisciplinaDAO dao;

	@Override
	protected DAOGenerico<MatrizCurricularDisciplina> getDAO() {
		return dao;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<MatrizCurricularDisciplina> listarPorMatrizCurricular(MatrizCurricular matrizCurricular) throws PersistenciaException {
		return dao.listarPorMatrizCurricular(matrizCurricular);
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
	public MatrizCurricularDisciplina salvarOuAtualizar(MatrizCurricularDisciplina entidade) throws PersistenciaException, NegocioException {
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
	protected boolean verificarEntidade(MatrizCurricularDisciplina entidade) throws NegocioException {
		if(entidade == null) {
			throw new NegocioException("negocio.validacao.objetoNulo");
		} else if (entidade.getCargaHoraria().intValue() <= 0 || entidade.getMatrizCurricular().isTransient() || entidade.getDisciplina().isTransient()){
			throw new NegocioException("negocio.validacao.campoObrigatorio");
		} else {
			return true;
		}
	}
}
