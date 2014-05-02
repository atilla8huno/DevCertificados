package br.com.devschool.pessoa.servico;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.devschool.entidade.Pessoa;
import br.com.devschool.enumeradores.EnumFiltroProfessor;
import br.com.devschool.pagamento.servico.PagamentoServico;
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.GeradorRelatorio;
import br.com.devschool.util.NegocioException;
import br.com.devschool.util.PersistenciaException;
import br.com.devschool.util.RelatorioException;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

/**
 * @author ATILLA
 * @date 13/09/2012
 */
@Service
public class PessoaServico extends ServicoGenerico<Pessoa> {

	@Autowired
	private PessoaDAO dao;
	@Autowired
	private PagamentoServico pgtoServico;
	@Autowired
	private GeradorRelatorio geradorRelatorio;

	@Override
	public DAOGenerico<Pessoa> getDAO() {
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
	public Pessoa salvarOuAtualizar(Pessoa entidade) throws PersistenciaException, NegocioException {
		verificarEntidade(entidade);
		
		return super.salvarOuAtualizar(entidade);
	}
	
	/**
	 * Esse método recebe um objeto de Pessoa como argumento e o desativa/ativa,
	 * alterando seu status para false no BD.
	 * 
	 * @author	WENDEL, ÁTILLA
	 * @date	14/04/2013
	 * @param	Objeto da Entidade
	 * @return	Objeto Disciplina
	 * @throws	NegocioException 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Pessoa desativarAtivarPessoa(Pessoa entidade) throws PersistenciaException, NegocioException {
		verificarEntidade(entidade);
		verificarPagamentosPendentes(entidade);

		return desativarAtivarPessoaADM(entidade);
	}
	
	/**
	 * Esse método recebe um aluno como parâmetro e ativa ou desativa sua conta sem verificar pendências
	 * 
	 * @author	ÁTILLA
	 * @date	14/04/2013
	 * @param	Pessoa entidade
	 * @return	Pessoa
	 * @throws	PersistenciaException, NegocioException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Pessoa desativarAtivarPessoaADM(Pessoa entidade) throws PersistenciaException, NegocioException {
		verificarEntidade(entidade);
		
		try {
			if (entidade.isStatus()) {
				entidade.setStatus(Boolean.FALSE);
			} else {
				entidade.setStatus(Boolean.TRUE);
			}
			return salvarOuAtualizar(entidade);
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}

	/**
	 * Este método faz uma validação do objeto de acordo com as regras de negócio
	 * 
	 * @author	ATILLA
	 * @date	24/09/2012
	 * @param	Objeto da Entidade
	 * @throws	NegocioException 
	 */
	@Override
	protected boolean verificarEntidade(Pessoa entidade) throws NegocioException {
		if(entidade == null) {
			throw new NegocioException("negocio.validacao.objetoNulo");
		} else if (entidade.getNome().isEmpty() || entidade.getCpf().isEmpty() || 
				(entidade.getTipoProfessor() == true && entidade.getAssinaturaDigital() == null)){
			throw new NegocioException("negocio.validacao.campoObrigatorio");
		} else {
			return true;
		}
	}
	
	/**
	 * Este método faz uma verificação de pendencias da pessoa
	 * 
	 * @author	ATILLA
	 * @date	14/04/2013
	 * @param	Pessoa usuario
	 * @throws	NegocioException 
	 */
	private void verificarPagamentosPendentes(Pessoa usuario) throws NegocioException {
		pgtoServico.verificarPagamentosPendentes(usuario);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Pessoa buscaPor(String login, String senha) throws PersistenciaException, NegocioException {
		return dao.buscaPor(login, senha);
	}

	/**
	 * Este método faz uma pesquisa dos professores e retorna uma lista de
	 * objetos com os resultados.
	 * 
	 * @author	ATILLA
	 * @date	09/01/2013
	 * @return	List
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Pessoa> listarProfessores(EnumFiltroProfessor filtroProfessorEnum) throws PersistenciaException {
		return dao.listarProfessores(filtroProfessorEnum);
	}
	
	/**
	 * Este método recebe filtros e emite relatório de usuários.
	 * 
	 * @param Boolean tipoAluno
	 * @param Boolean tipoProfessor
	 * @param Boolean tipoAdm
	 * @param Boolean status
	 * @param Date dataInicio
	 * @param Date dataFim
	 * @return byte[]
	 * @throws NegocioException, RelatorioException
	 */
	@SuppressWarnings("rawtypes")
	public byte[] gerarRelatorioUsuarios(Boolean tipoAluno, Boolean tipoProfessor, Boolean tipoAdm, Boolean status, Date dataInicio, Date dataFim) throws NegocioException, RelatorioException {
		try {
			Map<String, Object> parametros = new HashMap<String, Object>();
			List<Map> itens = new ArrayList<Map>();
			
			List<Pessoa> pessoas = dao.listarPessoasPorFiltros(tipoAluno, tipoProfessor, tipoAdm, status, dataInicio, dataFim);

			for (Pessoa usuario : pessoas) {
				try {
					Map<String, Object> item = new HashMap<String, Object>();
					
					item.put("nome",			usuario.getNome());
					item.put("cpf",				usuario.getCpf());
					item.put("tel_fixo",		usuario.getTelefoneFixo());
					item.put("tel_celular", 	usuario.getTelefoneCelular());
					item.put("tel_comercial", 	usuario.getTelefoneComercial());
					item.put("email", 			usuario.getEmail());
					item.put("formacao", 		usuario.getFormacao());
					item.put("atividade", 		usuario.getAtividadeProfissional());
					item.put("tipo_aluno", 		usuario.getTipoAluno());
					item.put("tipo_professor", 	usuario.getTipoProfessor());
					item.put("tipo_adm", 		usuario.getTipoAdm());
					item.put("status", 			usuario.isStatus());
					item.put("data_cadastro", 	usuario.getDataCadastro());
					
					itens.add(item);
				} catch (Exception e) {
					throw new RelatorioException(e);
				}
			}
			return geradorRelatorio.geraRelatorio("RelatorioUsuarios.jasper", itens, parametros);
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}
	
	/**
	 * @return PessoaServico
	 */
	public static PessoaServico getInstancia() {
		return (PessoaServico) SpringContainer.getInstancia().getBean("pessoaServico");
	}
}

