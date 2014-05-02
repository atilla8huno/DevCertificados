package br.com.devschool.matricula.servico;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.devschool.entidade.AlunoTurma;
import br.com.devschool.entidade.FrequenciaAluno;
import br.com.devschool.entidade.MatrizCurricularDisciplina;
import br.com.devschool.entidade.Pagamento;
import br.com.devschool.entidade.Pessoa;
import br.com.devschool.entidade.Turma;
import br.com.devschool.enumeradores.EnumFormaDePagamento;
import br.com.devschool.enumeradores.EnumStatusAlunoTurma;
import br.com.devschool.enumeradores.EnumStatusTurma;
import br.com.devschool.matriz_curricular.servico.MatrizCurricularDisciplinaServico;
import br.com.devschool.pagamento.servico.PagamentoServico;
import br.com.devschool.persistencia.AlunoTurmaDAO;
import br.com.devschool.turma.servico.TurmaServico;
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
public class AlunoTurmaServico extends ServicoGenerico<AlunoTurma>{

	@Autowired
	private AlunoTurmaDAO dao;
	@Autowired
	private MatrizCurricularDisciplinaServico matrizCurricularDisciplinaServico;
	@Autowired
	private GeradorRelatorio geradorRelatorio;
	@Autowired
	private PagamentoServico pagamentoServico;
	@Autowired
	private TurmaServico turmaServico;
	
	@Override
	protected DAOGenerico<AlunoTurma> getDAO() {
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
	public AlunoTurma salvarOuAtualizar(AlunoTurma entidade) throws PersistenciaException, NegocioException {
		verificarEntidade(entidade);
		if (entidade.isTransient()) {
			verificarDatas(entidade);
			pagamentoServico.verificarPagamentosPendentes(entidade.getPessoa());
		}
		return super.salvarOuAtualizar(entidade);
	}
	
	/**
	 * Este método salva ou atualiza o objeto na base de dados
	 * 
	 * @author 	WENDEL
	 * @date	24/09/2012
	 * @return	Objeto da Entidade
	 * @param	Objeto da Entidade
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public AlunoTurma salvar(AlunoTurma entidade, EnumFormaDePagamento formaPagamento, Integer qtde, Date dataVencimento) throws PersistenciaException, NegocioException {
		entidade.setStatus(EnumStatusAlunoTurma.EM_ANDAMENTO);
		verificarEntidade(entidade);
		entidade = this.salvarOuAtualizar(entidade);
		geraPagamentos(entidade, formaPagamento, qtde,dataVencimento);
		return entidade;
	}

	/**
	 * Este método gera os pagamentos referente a matricula do aluno
	 * 
	 * @author WENDEL
	 * @date 29/04/2013
	 * @param entidade
	 * @param formaPagamento
	 * @param qtde
	 * @throws PersistenciaException
	 * @throws NegocioException
	 */
	private void geraPagamentos(AlunoTurma entidade,
			EnumFormaDePagamento formaPagamento, Integer qtde, Date dataVencimento)
			throws PersistenciaException, NegocioException {
		
		for(int i = 0; i<qtde; i++){
			Pagamento pagamento = new Pagamento();
			pagamento.setValor(entidade.getTurma().getMatrizCurricular().getCurso().getInvestimento().divide(new BigDecimal(qtde)));
			pagamento.setAlunoTurma(entidade);
			pagamento.setFormaPagamento(formaPagamento);
			pagamento.setParcela(i+1);
			pagamento.setObservacao("Pagamento referente a mátricula no curso "
											+entidade.getTurma().getMatrizCurricular().getCurso().getTitulo()+", " +
													"nível "+entidade.getTurma().getMatrizCurricular().getNivel().getDescricao()+", "+
														"turma "+entidade.getTurma().getNome()+", "+
															"turno "+entidade.getTurma().getMatrizCurricular().getTurno().getDescricao()+". "+
																"("+(i+1)+"º Parcela)");
			
			if(qtde>1){
				GregorianCalendar data = new GregorianCalendar();
				data.setTime(dataVencimento);
				data.add(GregorianCalendar.MONTH, i);
				pagamento.setDataVencimento(data.getTime());
			} else {
				pagamento.setDataVencimento(new Date());
			}
			
			pagamentoServico.salvarOuAtualizar(pagamento);
		}
		
	}
	
	/**
	 * Este método pesquisa todos os registros na tabela AlunoTurma onde o aluno for igual ao parâmentro.
	 * 
	 * @author 	ATILLA
	 * @param 	Pessoa aluno
	 * @return 	List<AlunoTurma>
	 * @throws 	Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AlunoTurma> listarPorAluno(Pessoa aluno) throws PersistenciaException {
		return dao.listarPorAluno(aluno);
	}
	
	/**
	 * Este método pesquisa todos os registros na tabela AlunoTurma onde o aluno e a turma 
	 * forem iguais aos parâmentros.
	 * 
	 * @author 	ATILLA
	 * @param 	Pessoa aluno
	 * @return 	List<AlunoTurma>
	 * @throws 	Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public AlunoTurma procurarPorAlunoETurma(Pessoa aluno, Turma turma) throws PersistenciaException, NegocioException {
		if(aluno == null || turma == null){
			throw new NegocioException("negocio.validacao.objetoNulo");
		} else {
			return dao.listarPorAlunoETurma(aluno, turma).get(0);
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
	protected boolean verificarEntidade(AlunoTurma entidade) throws NegocioException {
		if(entidade == null){
			throw new NegocioException("negocio.validacao.objetoNulo");
		} else if (entidade.getPessoa().isTransient() || entidade.getTurma().isTransient()){
			throw new NegocioException("negocio.validacao.campoObrigatorio");
		} else {
			return true;
		}
	}
	
	/**
	 * Este método verifica se o aluno já está matriculado em um curso com a mesma data e turno
	 * 
	 * @author 	ATILLA
	 * @date	24/09/2012
	 * @param	Objeto da Entidade
	 * @throws 	NegocioException 
	 */
	protected boolean verificarDatas(AlunoTurma entidade) throws NegocioException {
		verificarEntidade(entidade);
		
		for (AlunoTurma at : entidade.getPessoa().getAlunoTurmas())
			turmaServico.verificarDatasDasTurmas(at.getTurma(), entidade.getTurma());
		
		return true;
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
	protected boolean verificarCertificado(AlunoTurma entidade) throws NegocioException {
		verificarEntidade(entidade);
		
		if (entidade.getStatus().equals(EnumStatusAlunoTurma.REPROVADO)){
			throw new NegocioException("alunoReprovado");
		} else if (entidade.equals(EnumStatusAlunoTurma.EM_ANDAMENTO)){
			throw new NegocioException("alunoNaoAprovado");
		} else if (!entidade.getTurma().getStatus().equals(EnumStatusTurma.CONCLUIDA)){
			throw new NegocioException("turmaNaoConcluida");
		} else {
			return true;
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
	public byte[] gerarCertificadoDoAluno(AlunoTurma alunoTurma) throws RelatorioException, NegocioException {
		verificarCertificado(alunoTurma);
		try {
			//Evita exception de objeto detached
			alunoTurma = procurarPorCodigo(alunoTurma.getId());
			
			Map<String, Object> paramentros = new HashMap<String, Object>();
			List<Map> itens 				= new ArrayList<Map>();
			Pessoa aluno 					= alunoTurma.getPessoa();
			
			if(aluno != null && !aluno.isTransient()){
				paramentros.put("id_aluno_turma", alunoTurma.getId());
				Map<String, Object> item = new HashMap<String, Object>();
				
				item.put("codigo", 					alunoTurma.getId());
				item.put("nome_aluno", 				alunoTurma.getPessoa().getNome());
				item.put("titulo", 					alunoTurma.getTurma().getMatrizCurricular().getCurso().getTitulo());
				item.put("nome_professor", 			alunoTurma.getTurma().getPessoa().getNome());
				item.put("assinatura_digital", 		alunoTurma.getTurma().getPessoa().getAssinaturaDigital());
				item.put("atividade_profissional", 	alunoTurma.getTurma().getPessoa().getAtividadeProfissional());
				item.put("formacao", 				alunoTurma.getTurma().getPessoa().getFormacao());
				item.put("data_inicio", 			alunoTurma.getTurma().getDataRealInicio());
				item.put("data_fim", 				alunoTurma.getTurma().getDataRealFim());
				item.put("porcentagem_frequencia", 	alunoTurma.getPorcentagemFrequencia());
				item.put("porcentagem_aproveitamento",alunoTurma.getPorcentagemAproveitamento());
				
				Integer total = 0;
				if (alunoTurma.getTurma().getMatrizCurricular() != null) {
					List<MatrizCurricularDisciplina> matrizCurricularDisciplinas = 
							matrizCurricularDisciplinaServico.listarPorMatrizCurricular(alunoTurma.getTurma().getMatrizCurricular());
				    for (MatrizCurricularDisciplina m : matrizCurricularDisciplinas) {
				    	total += (m.getCargaHoraria() != null ? m.getCargaHoraria() : 0);
				    }
				}
				item.put("total_carga_horaria", total);
				
				itens.add(item);
			} else {
				throw new Exception("A turma não possui alunos para gerar os certificados. Verifique!!!");
			}
			
			alunoTurma.setCertificado(geradorRelatorio.geraRelatorio("Certificado.jasper", itens, paramentros));
			
			return alunoTurma.getCertificado();
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}
	
	/**
	 * Este método recebe lista de alunos (matriculas/aluno_turma) como parâmetro e gera os certificados.
	 * 
	 * @author 	ATILLA
	 * @param 	List<AlunoTurma>
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	public byte[] gerarCertificadosDosAlunos(List<AlunoTurma> alunos) throws NegocioException, RelatorioException {
		if(alunos == null) {
			throw new NegocioException("negocio.validacao.objetoNulo");
		} else if (alunos.size() <= 0) {
			throw new NegocioException("listaSemAlunos");
		} else {
			try {
				Map<String, Object> parametros = new HashMap<String, Object>();
				List<Map> itens = new ArrayList<Map>();
				Turma turma	= null;
	
				for (AlunoTurma alunoTurma : alunos) {
					try {
						verificarCertificado(alunoTurma);
						
						Map<String, Object> item = new HashMap<String, Object>();
						turma = alunoTurma.getTurma();
						
						item.put("id_aluno_turma", 			alunoTurma.getId());
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
					} catch (Exception e) {
						throw new RelatorioException(e);
					}
				}
				turma.setCertificados(geradorRelatorio.geraRelatorio("Certificado.jasper", itens, parametros));
			
				return turma.getCertificados();
			} catch (Exception e) {
				throw new RelatorioException(e);
			}
		}
	}
	
	/**
	 * Este método verifica se a pessoa já esta matrículado em uma turma
	 * 
	 * @author 	WENDEL
	 * @date	15/01/2013
	 * @param	pessoa - Pessoa ao qual quer verificar
	 * @param	turma - Turma ao qual quer verificar
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean verificaMatricula(Pessoa pessoa, Turma turma) throws PersistenciaException {
		return dao.verificaMatricula(pessoa, turma);
	}
	
	/**
	 * Este método lista matrículas relacionada a uma turma
	 * 
	 * @author WENDEL
	 * @param turma | Turma a pesquisar
	 * @return List<AlunoTurma> - Lista de matrículas
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<AlunoTurma> listarPorTurma(Turma turma) throws PersistenciaException {
		return dao.listarPorTurma(turma);
	}
	
	/**
	 * Este método salva ou atualiza o notas na base de dados
	 * 
	 * @author 	WENDEL
	 * @date	20/03/2013
	 * @return	Lista de Objeto da Entidade
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void salvarOuAtualizar(List<AlunoTurma> listaAlunosTurmas) throws NegocioException, PersistenciaException {
		for(AlunoTurma a : listaAlunosTurmas){
			if (a.isTransient()) {
				verificarDatas(a);
				pagamentoServico.verificarPagamentosPendentes(a.getPessoa());
			}
			super.salvarOuAtualizar(a);
		}
	}

	/**
	 * Este método salva ou atualiza o notas na base de dados
	 * 
	 * @author 	ATILLA
	 * @date	15/04/2013
	 * @return	Lista de Objeto da Entidade
	 * @throws	NegocioException, PersistenciaException 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void transferirAluno(Turma turma, AlunoTurma alunoTurma) throws NegocioException, PersistenciaException {
		verificarEntidade(alunoTurma);
		if (turma == null || turma.isTransient()) {
			throw new NegocioException("negocio.validacao.objetoNulo");
		}
		
		alunoTurma.setTurma(turma);
		salvarOuAtualizar(alunoTurma);
	}
	
	/**
	 * Este método recebe filtros e emite relatório de notas.
	 * 
	 * @param Pessoa aluno
	 * @param Turma turma
	 * @return byte[]
	 * @throws NegocioException, RelatorioException
	 */
	@SuppressWarnings("rawtypes")
	public byte[] gerarRelatorioNotas(Pessoa aluno, Turma turma) throws NegocioException, RelatorioException {
		try {
			Map<String, Object> parametros = new HashMap<String, Object>();
			List<Map> itens = new ArrayList<Map>();
			
			List<AlunoTurma> matriculas = dao.listarMatriculasPorFiltros(aluno, turma);
			
			for (AlunoTurma matricula : matriculas) {
				try {
					Map<String, Object> item = new HashMap<String, Object>();
					
					item.put("nome_aluno",		matricula.getPessoa().getNome());
					item.put("nome_curso",		matricula.getTurma().getMatrizCurricular().getCurso().getTitulo());
					item.put("nome_turma",		matricula.getTurma().getNome());
					item.put("data_inicio", 	matricula.getTurma().getDataRealInicio());
					item.put("data_fim", 		matricula.getTurma().getDataRealFim());
					item.put("aproveitamento", 	matricula.getPorcentagemAproveitamento());
					
					itens.add(item);
				} catch (Exception e) {
					throw new RelatorioException(e);
				}
			}
			return geradorRelatorio.geraRelatorio("RelatorioNotas.jasper", itens, parametros);
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}
	
	/**
	 * Este método recebe filtros e emite relatório de frequências.
	 * 
	 * @param Pessoa aluno
	 * @param Turma turma
	 * @return byte[]
	 * @throws NegocioException, RelatorioException
	 */
	@SuppressWarnings("rawtypes")
	public byte[] gerarRelatorioFrequencias(Pessoa aluno, Turma turma) throws NegocioException, RelatorioException {
		try {
			Map<String, Object> parametros = new HashMap<String, Object>();
			List<Map> itens = new ArrayList<Map>();
			
			List<AlunoTurma> matriculas = dao.listarMatriculasPorFiltros(aluno, turma);
			
			for (AlunoTurma matricula : matriculas) {
				try {
					Map<String, Object> item = new HashMap<String, Object>();
					
					item.put("nome_aluno",		matricula.getPessoa().getNome());
					item.put("nome_curso",		matricula.getTurma().getMatrizCurricular().getCurso().getTitulo());
					item.put("nome_turma",		matricula.getTurma().getNome());
					item.put("data_inicio", 	matricula.getTurma().getDataRealInicio());
					item.put("data_fim", 		matricula.getTurma().getDataRealFim());
					item.put("aproveitamento", 	matricula.getPorcentagemAproveitamento());
					
					//Adiciona as aulas da matricula
					obterAulas(matricula, item);
					
					itens.add(item);
				} catch (Exception e) {
					throw new RelatorioException(e);
				}
			}
			return geradorRelatorio.geraRelatorio("RelatorioFrequencias.jasper", itens, parametros);
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}
	
	/**
	 * Este método recebe uma matricula e adiciona as aulas com presenças.
	 * 
	 * @param 	AlunoTurma matricula
	 * @param 	Map<String, Object> item
	 * @throws 	NegocioException
	 */
	@SuppressWarnings("rawtypes")
	private void obterAulas(AlunoTurma matricula, Map<String, Object> item) throws NegocioException {
		try {
			List<Map> aulas = new ArrayList<Map>();
			
			for (FrequenciaAluno frequencia : matricula.getFrequenciaAlunos()) {
				Map<String, Object> aula = new HashMap<String, Object>();
				
				aula.put("data_aula",	frequencia.getAula().getDataAula());
				aula.put("presenca", 	frequencia.getFalta());
				
				aulas.add(aula);
			}
			
			JRBeanCollectionDataSource aulasBCDS = new JRBeanCollectionDataSource(aulas);
			item.put("aulas", aulasBCDS);
		} catch (Exception e) {
			throw new NegocioException("erro.geral");
		}
	}
}
