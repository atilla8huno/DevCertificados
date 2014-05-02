package br.com.devschool.disciplina.servico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.devschool.entidade.Disciplina;
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
public class DisciplinaServico extends ServicoGenerico<Disciplina>{

	@Autowired
	private DisciplinaDAO dao;

	@Override
	protected DAOGenerico<Disciplina> getDAO() {
		return dao;
	}
	
	/**
	 * Esse método recebe um objeto de Disciplina como argumento
	 * e o desativa, alterando seu status para false no BD.
	 * 
	 * @author 	WENDEL
	 * @date	30/12/2012
	 * @param	Obsjeto da Entidade
	 * @return 	Objeto Disciplina
	 * @throws PersistenciaException  
	 */
	@Transactional(propagation= Propagation.REQUIRED, rollbackFor = Exception.class)
	public Disciplina desativarAtivarDisciplina(Disciplina entidade) throws PersistenciaException {
		return dao.desativarAtivarDisciplina(entidade);
	}
	
	/**
	 * Este método faz uma pesquisa dos professores e retorna uma lista de
	 * objetos com os resultados.
	 * 
	 * @author	ATILLA
	 * @date	10/01/2013
	 * @return	List
	 * @throws PersistenciaException 
	 */
	public List<Disciplina> listarDisciplinasAtivas() throws PersistenciaException {
		return dao.listarDisciplinasAtivas();
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
	public Disciplina salvarOuAtualizar(Disciplina entidade) throws PersistenciaException, NegocioException {
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
	protected boolean verificarEntidade(Disciplina entidade) throws NegocioException {
		if(entidade == null) {
			throw new NegocioException("negocio.validacao.objetoNulo");
		} else if (entidade.getTitulo().isEmpty()){
			throw new NegocioException("negocio.validacao.campoObrigatorio");
		} else {
			return true;
		}
	}
}
