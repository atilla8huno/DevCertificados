package br.com.devschool.matriz_curricular.servico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.devschool.entidade.Curso;
import br.com.devschool.entidade.MatrizCurricular;
import br.com.devschool.entidade.MatrizCurricularDisciplina;
import br.com.devschool.entidade.Nivel;
import br.com.devschool.entidade.Turno;
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
public class MatrizCurricularServico extends ServicoGenerico<MatrizCurricular>{

	@Autowired
	private MatrizCurricularDAO dao;
	@Autowired
	private MatrizCurricularDisciplinaServico matrizServico;
	@Autowired
	private GeradorRelatorio geradorRelatorio;

	@Override
	protected DAOGenerico<MatrizCurricular> getDAO() {
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
	public MatrizCurricular salvarOuAtualizar(MatrizCurricular entidade) throws PersistenciaException, NegocioException {
		verificarEntidade(entidade);
		
		Set<MatrizCurricularDisciplina> disciplinas = entidade.getMatrizCurricularDisciplinas();
		entidade = super.salvarOuAtualizar(entidade);
		
		for(MatrizCurricularDisciplina mcd : disciplinas){
			mcd.setMatrizCurricular(entidade);
			matrizServico.salvarOuAtualizar(mcd);
		}
		
		return entidade;
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
	protected boolean verificarEntidade(MatrizCurricular entidade) throws NegocioException {
		if(entidade == null) {
			throw new NegocioException("negocio.validacao.objetoNulo");
		} else if (entidade.getCurso().isTransient() || entidade.getTurno().isTransient() || entidade.getNivel().isTransient()){
			throw new NegocioException("negocio.validacao.campoObrigatorio");
		} else {
			return true;
		}
	}
	
	/**
	 * Este método recebe filtros e emite relatório de matrizes curriculares.
	 * 
	 * @param Curso curso
	 * @param Nivel nivel
	 * @param Turno turno
	 * @return List<MatrizCurricular>
	 * @throws PersistenciaException
	 */
	@SuppressWarnings("rawtypes")
	public byte[] gerarRelatorioMatrizesCurriculares(Curso curso, Nivel nivel, Turno turno) throws NegocioException, RelatorioException {
		try {
			Map<String, Object> parametros = new HashMap<String, Object>();
			List<Map> itens = new ArrayList<Map>();
			
			List<MatrizCurricular> matrizes = dao.listarMatrizesPorFiltros(curso, nivel, turno);
			
			for (MatrizCurricular matrizCurricular : matrizes) {
				try {
					Map<String, Object> item = new HashMap<String, Object>();
					
					item.put("curso", matrizCurricular.getCurso().getTitulo());
					item.put("nivel", matrizCurricular.getNivel().getDescricao());
					item.put("turno", matrizCurricular.getTurno().getDescricao());
					
					//Adiciona as disciplinas da matriz no item
					obterDisciplinas(matrizCurricular, item);
					
					itens.add(item);
				} catch (Exception e) {
					throw new RelatorioException(e);
				}
			}
			return geradorRelatorio.geraRelatorio("RelatorioMatrizesCurriculares.jasper", itens, parametros);
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}
	
	/**
	 * Este método recebe uma matriz curricular e adiciona as disciplinas com carga horária.
	 * 
	 * @param MatrizCurricular matrizCurricular
	 * @param Map<String, Object> item
	 * @throws NegocioException
	 */
	@SuppressWarnings("rawtypes")
	private void obterDisciplinas(MatrizCurricular matrizCurricular, Map<String, Object> item) throws NegocioException {
		try {
			List<Map> disciplinas = new ArrayList<Map>();
			
			for (MatrizCurricularDisciplina matrizDisciplina : matrizCurricular.getMatrizCurricularDisciplinas()) {
				
				Map<String, Object> disciplina = new HashMap<String, Object>();
				
				disciplina.put("titulo", 		matrizDisciplina.getDisciplina().getTitulo());
				disciplina.put("carga_horaria", matrizDisciplina.getCargaHoraria());
				
				disciplinas.add(disciplina);
			}
			
			JRBeanCollectionDataSource disciplinasBCDS = new JRBeanCollectionDataSource(disciplinas);
			item.put("disciplinas", disciplinasBCDS);
		} catch (Exception e) {
			throw new NegocioException("erro.geral");
		}
	}
}
