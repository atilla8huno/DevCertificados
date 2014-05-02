package br.com.devschool.frequencia.servico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.devschool.entidade.Aula;
import br.com.devschool.entidade.FrequenciaAluno;
import br.com.devschool.entidade.Turma;
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
public class FrequenciaAlunoServico extends ServicoGenerico<FrequenciaAluno>{

	@Autowired
	private FrequenciaAlunoDAO dao;
	
	@Override
	protected DAOGenerico<FrequenciaAluno> getDAO() {
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
	public FrequenciaAluno salvarOuAtualizar(FrequenciaAluno entidade) throws PersistenciaException, NegocioException {
		verificarEntidade(entidade);
		
		return super.salvarOuAtualizar(entidade);
	}
	
	/**
	 * Este método lista as frequências de uma turma, referente a uma aula
	 * 
	 * @author Wendel Nunes Donizete
	 * @since 19/03/2013
	 * @param turma - Turma a pesquisar
	 * @param aula - Aula a pesquisar
	 * @return List<FrequenciaAluno> - Lista de frequência da turma
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<FrequenciaAluno> listaFrequencia(Turma turma, Aula aula) throws PersistenciaException {
		return dao.listaFrequencia(turma, aula);
	}
	
	
	/**
	 * Este método salva as frequências de uma turma
	 * 
	 * @author Wendel Nunes Donizete
	 * @since 19/03/2013
	 * @param frequencias - Lista de frequências a salvar
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void salvarFrequencia(List<FrequenciaAluno> frequencias) throws PersistenciaException, NegocioException {
		for(FrequenciaAluno f : frequencias){
			verificarEntidade(f);
			super.salvarOuAtualizar(f);
		}
	}
	
	/**
	 * Este método atualiza a frequência de uma turma
	 * 
	 * @param Turma turma
	 * @throws PersistenciaException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void atualizaFrequenciaTurma(Turma turma) throws PersistenciaException{
		dao.atualizaFrequenciaTurma(turma);
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
	protected boolean verificarEntidade(FrequenciaAluno entidade) throws NegocioException {
		if(entidade == null) { 
			throw new NegocioException("negocio.validacao.objetoNulo");
		} else if (entidade.getAlunoTurma().isTransient() || entidade.getAula().isTransient() || entidade.getFalta() == null){
			throw new NegocioException("negocio.validacao.campoObrigatorio");
		} else {
			return true;
		}
	}
}
