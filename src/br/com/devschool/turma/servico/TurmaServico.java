package br.com.devschool.turma.servico;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.devschool.entidade.AlunoTurma;
import br.com.devschool.entidade.Curso;
import br.com.devschool.entidade.MatrizCurricularDisciplina;
import br.com.devschool.entidade.Pessoa;
import br.com.devschool.entidade.Turma;
import br.com.devschool.enumeradores.EnumStatusTurma;
import br.com.devschool.matriz_curricular.servico.MatrizCurricularDisciplinaServico;
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.GeradorRelatorio;
import br.com.devschool.util.NegocioException;
import br.com.devschool.util.PersistenciaException;
import br.com.devschool.util.RelatorioException;
import br.com.devschool.util.ServicoGenerico;

/**
 * @author	ATILLA
 * @date	13/09/2012
 */
@Service
public class TurmaServico extends ServicoGenerico<Turma>{

	@Autowired
	private TurmaDAO dao;
	@Autowired
	private MatrizCurricularDisciplinaServico matrizCurricularDisciplinaServico;
	@Autowired
	private GeradorRelatorio geradorRelatorio;

	@Override
	protected DAOGenerico<Turma> getDAO() {
		return dao;
	}
	
	/**
	 * Este método lista todas as turmas relacionadas a um curso
	 * 
	 * @author 	WENDEL NUNES DONIZETE
	 * @date	16/01/2013
	 * @param	curso | Curso ao qual irá fazer a pesquisa
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<Turma> listarPorCurso(Curso curso) throws PersistenciaException {
		return dao.listarPorCurso(curso);
	}
	
	/**
	 * Este método lista todas as turmas relacionadas a um professor
	 * 
	 * @author 	ATILLA
	 * @date	21/03/2013
	 * @param	Pessoa - Professor
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<Turma> listarPorProfessor(Pessoa professor) throws PersistenciaException {
		return dao.listarPorProfessor(professor);
	}
	
	/**
	 * Este método lista todas as turmas não concluídas
	 * 
	 * @author 	ATILLA
	 * @date	15/04/2013
	 * @throws 	PersistenciaException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<Turma> listarTurmasEmAberto() throws PersistenciaException {
		return dao.listarTurmasEmAberto();
	}
	
	/**
	 * Este método lista todas as turmas relacionadas a um curso e professor
	 * 
	 * @author 	WENDEL NUNES DONIZETE
	 * @date	16/01/2013
	 * @param	curso | Curso ao qual irá fazer a pesquisa
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<Turma> listarPorCursoProfessor(Curso curso, Pessoa pessoa) throws PersistenciaException {
		return dao.listarPorCursoProfessor(curso, pessoa);
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
	public Turma salvarOuAtualizar(Turma entidade) throws PersistenciaException, NegocioException {
		verificarEntidade(entidade);
		for (Turma t : entidade.getPessoa().getTurmas()) {
			verificarDatasDasTurmas(t, entidade);
		}
		
		return super.salvarOuAtualizar(entidade);	
	}
	
	/**
	 * Este método recebe uma turma como parâmetro e gera os certificados para ela.
	 * 
	 * @author 	ATILLA
	 * @param 	Turma
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	public byte[] gerarCertificadosDaTurma(Turma turma) throws Exception {
		verificarEntidade(turma);
		
		try {
			//Evita exception de objeto detached
			turma = procurarPorCodigo(turma.getId());
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			List<Map> itens 				= new ArrayList<Map>();
			Set<AlunoTurma> alunos 			= turma.getAlunoTurmas();

			if(alunos != null && alunos.size() > 0){
				parametros.put("id_turma", turma.getId());
				
				for(AlunoTurma alunoTurma : alunos){
					Map<String, Object> item = new HashMap<String, Object>();
					
					item.put("codigo", 					alunoTurma.getId());
					item.put("nome_aluno", 				alunoTurma.getPessoa().getNome());
					item.put("titulo", 					turma.getMatrizCurricular().getCurso().getTitulo());
					item.put("nome_professor", 			turma.getPessoa().getNome());
					item.put("assinatura_digital", 		turma.getPessoa().getAssinaturaDigital());
					item.put("atividade_profissional", 	turma.getPessoa().getAtividadeProfissional());
					item.put("formacao", 				turma.getPessoa().getFormacao());
					item.put("data_inicio", 			turma.getDataRealInicio());
					item.put("data_fim", 				turma.getDataRealFim());
					item.put("porcentagem_frequencia", 	alunoTurma.getPorcentagemFrequencia());
					item.put("porcentagem_aproveitamento",alunoTurma.getPorcentagemAproveitamento());
					
					Integer total = 0;
					if (turma.getMatrizCurricular() != null) {
						List<MatrizCurricularDisciplina> matrizCurricularDisciplinas = 
								matrizCurricularDisciplinaServico.listarPorMatrizCurricular(turma.getMatrizCurricular());
					    for (MatrizCurricularDisciplina m : matrizCurricularDisciplinas) {
					    	total += (m.getCargaHoraria() != null ? m.getCargaHoraria() : 0);
					    }
					}
					item.put("total_carga_horaria", total);
					
					itens.add(item);
				}
			} else {
				throw new NegocioException("listaSemAlunos");
			}
			
			turma.setCertificados(geradorRelatorio.geraRelatorio("Certificado.jasper", itens, parametros));
			
			return turma.getCertificados();
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}
	
	/**
	 * Este método recebe uma turma como parâmetro e gera os certificados para ela.
	 * 
	 * @author 	ATILLA
	 * @param 	Turma
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	public byte[] gerarCertificadoDoProfessor(Turma turma) throws Exception {
		verificarCertificado(turma);
		
		try {
			//Evita exception de objeto detached
			turma = procurarPorCodigo(turma.getId());
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			List<Map> itens = new ArrayList<Map>();

			parametros.put("id_turma", turma.getId());
			
			Map<String, Object> item = new HashMap<String, Object>();
			
			item.put("codigo", 					turma.getId());
			item.put("titulo", 					turma.getMatrizCurricular().getCurso().getTitulo());
			item.put("nome_professor", 			turma.getPessoa().getNome());
			item.put("assinatura_digital", 		turma.getPessoa().getAssinaturaDigital());
			item.put("atividade_profissional", 	turma.getPessoa().getAtividadeProfissional());
			item.put("formacao", 				turma.getPessoa().getFormacao());
			item.put("data_inicio", 			turma.getDataRealInicio());
			item.put("data_fim", 				turma.getDataRealFim());
			
			Integer total = 0;
			if (turma.getMatrizCurricular() != null) {
				List<MatrizCurricularDisciplina> matrizCurricularDisciplinas = 
						matrizCurricularDisciplinaServico.listarPorMatrizCurricular(turma.getMatrizCurricular());
			    for (MatrizCurricularDisciplina m : matrizCurricularDisciplinas) {
			    	total += (m.getCargaHoraria() != null ? m.getCargaHoraria() : 0);
			    }
			}
			item.put("total_carga_horaria", total);
			
			itens.add(item);
			
			turma.setCertificados(geradorRelatorio.geraRelatorio("CertificadoProfessor.jasper", itens, parametros));
			
			return turma.getCertificados();
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}
	
	/**
	 * Este método verifica se o certificado porerá ser disponibilizado para download
	 * 
	 * @author 	ATILLA
	 * @date	13/04/2013
	 * @param	AlunoTurma entidade
	 * @throws 	NegocioException
	 * @return	boolean
	 */
	protected boolean verificarCertificado(Turma entidade) throws NegocioException {
		verificarEntidade(entidade);
		
		if (!entidade.getStatus().equals(EnumStatusTurma.CONCLUIDA)){
			throw new NegocioException("turmaNaoConcluida");
		} else {
			return true;
		}
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
	protected boolean verificarEntidade(Turma entidade) throws NegocioException {
		if(entidade == null) {
			throw new NegocioException("negocio.validacao.objetoNulo");
		} else if (entidade.getNome().isEmpty() || entidade.getPessoa().isTransient() || entidade.getMatrizCurricular().isTransient() ||
				entidade.getDataRealInicio() == null || entidade.getDataRealFim() == null){
			throw new NegocioException("negocio.validacao.campoObrigatorio");
		} else {
			return true;
		}
	}
	
	/**
	 * Este método verifica se o turno e as datas de inicio e fim das turmas recebidas como parâmentro se chocam.
	 * 
	 * @author	ATILLA
	 * @param	Turma t1, Turma t2
	 * @date	20/04/2013
	 * @throws	NegocioException
	 */
	public void verificarDatasDasTurmas(Turma t1, Turma t2) throws NegocioException {
		if (t1.getMatrizCurricular().getTurno().equals(t2.getMatrizCurricular().getTurno())
				&& ((!t1.getStatus().equals(EnumStatusTurma.CONCLUIDA) && !t1.getStatus().equals(EnumStatusTurma.CANCELADA)) 
						&& (!t2.getStatus().equals(EnumStatusTurma.CONCLUIDA) && !t2.getStatus().equals(EnumStatusTurma.CANCELADA)))
				&& ((t1.getDataRealFim().after(t2.getDataRealInicio()) || t1.getDataRealFim().equals(t2.getDataRealInicio()))
				|| (t1.getDataRealInicio().before(t2.getDataRealFim()) || t1.getDataRealInicio().equals(t2.getDataRealFim())))) {
			throw new NegocioException("negocio.matricula.campoDataMenorOuMaior");
		}
	}
	
	/**
	 * Este método recebe filtros e exibe relatório de turmas.
	 * 
	 * @param Date dataInicio
	 * @param Date dataFim
	 * @param Pessoa professor
	 * @param EnumStatusTurma status
	 * @return byte[]
	 * @throws NegocioException, RelatorioException
	 */
	@SuppressWarnings("rawtypes")
	public byte[] gerarRelatorioTurmas(Date dataInicio, Date dataFim, Pessoa professor, EnumStatusTurma status) throws NegocioException, RelatorioException {
		try {
			Map<String, Object> parametros = new HashMap<String, Object>();
			List<Map> itens = new ArrayList<Map>();
			
			List<Turma> turmas = dao.listarTurmasPorFiltros(dataInicio, dataFim, professor, status);
			
			for (Turma turma : turmas) {
				try {
					Map<String, Object> item = new HashMap<String, Object>();
					
					item.put("nome", 		turma.getNome());
					item.put("codigo", 		turma.getIdTurma());
					item.put("data_inicio",	turma.getDataRealInicio());
					item.put("data_fim", 	turma.getDataRealFim());
					item.put("professor", 	turma.getPessoa().getNome());
					item.put("status", 		turma.getStatus().getDescricao());
					
					itens.add(item);
				} catch (Exception e) {
					throw new RelatorioException(e);
				}
			}
			return geradorRelatorio.geraRelatorio("RelatorioTurmas.jasper", itens, parametros);
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}
}
