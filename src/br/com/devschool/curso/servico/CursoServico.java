package br.com.devschool.curso.servico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.devschool.entidade.Curso;
import br.com.devschool.entidade.Pessoa;
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.GeradorRelatorio;
import br.com.devschool.util.NegocioException;
import br.com.devschool.util.PersistenciaException;
import br.com.devschool.util.RelatorioException;
import br.com.devschool.util.ServicoGenerico;

/**
 * @author	ATILLA
 * @date	13/09/2012
 * 
 */
@Service
public class CursoServico extends ServicoGenerico<Curso> {

	@Autowired
	private CursoDAO dao;
	@Autowired
	private GeradorRelatorio geradorRelatorio;

	@Override
	protected DAOGenerico<Curso> getDAO() {
		return dao;
	}

	/**
	 * Este método salva ou atualiza o objeto na base de dados
	 * 
	 * @author 	ATILLA
	 * @date	24/09/2012
	 * @return	Objeto da Entidade
	 * @param	Objeto da Entidade
	 * @throws 	PersistenciaException, NegocioException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Curso salvarOuAtualizar(Curso entidade) throws PersistenciaException, NegocioException {
		verificarEntidade(entidade);
		
		return super.salvarOuAtualizar(entidade);
	}
	
	/**
	 * Este método lista cursos referente a um professor
	 * 
	 * @author WENDEL
	 * @param pessoa - Professor a pesquisar
	 * @return List<Curso> - Lista de Curso
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<Curso> listarPorProfessor(Pessoa pessoa) throws PersistenciaException {
		return dao.listarPorProfessor(pessoa);
	}

	/**
	 * Este método faz uma validação do objeto de acordo
	 * com as regras de negócio
	 * 
	 * @author 	ATILLA
	 * @date	24/09/2012
	 * @param	Objeto da Entidade
	 */
	@Override
	protected boolean verificarEntidade(Curso entidade) throws NegocioException {
		if(entidade == null) {
			throw new NegocioException("negocio.validacao.objetoNulo");
		} else if (entidade.getTitulo().isEmpty() 		|| entidade.getDescricao().isEmpty()
				|| entidade.getPublicoAlvo().isEmpty() 	|| entidade.getPreRequisito().isEmpty()
				|| entidade.getAlunoAposCurso().isEmpty()) {
			throw new NegocioException("negocio.validacao.campoObrigatorio");
		} else {
			return true;
		}
	}
	
	/**
	 * Este método recebe um status e emite relatório de cursos.
	 * 
	 * @param 	Boolean status
	 * @return	byte[]
	 * @throws 	NegocioException, RelatorioException
	 */
	@SuppressWarnings("rawtypes")
	public byte[] gerarRelatorioCursos(Boolean status) throws NegocioException, RelatorioException {
		try {
			Map<String, Object> parametros = new HashMap<String, Object>();
			List<Map> itens = new ArrayList<Map>();
			
			List<Curso> cursos = dao.listarCursosPorFiltros(status);
			
			for (Curso curso : cursos) {
				try {
					Map<String, Object> item = new HashMap<String, Object>();
					
					item.put("titulo", 			curso.getTitulo());
					item.put("descricao", 		curso.getDescricao());
					item.put("publico_alvo", 	curso.getPublicoAlvo());
					item.put("pre_requisito", 	curso.getPreRequisito());
					item.put("aluno_apos_curso",curso.getAlunoAposCurso());
					item.put("investimento", 	curso.getInvestimento());
					item.put("status",			curso.isStatus());
					
					itens.add(item);
				} catch (Exception e) {
					throw new RelatorioException(e);
				}
			}
			return geradorRelatorio.geraRelatorio("RelatorioCursos.jasper", itens, parametros);
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}
}
