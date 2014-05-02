package br.com.devschool.pagamento.servico;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.devschool.entidade.AlunoTurma;
import br.com.devschool.entidade.Pagamento;
import br.com.devschool.entidade.Pessoa;
import br.com.devschool.enumeradores.EnumFormaDePagamento;
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.NegocioException;
import br.com.devschool.util.PersistenciaException;
import br.com.devschool.util.ServicoGenerico;

/**
 * @author ATILLA
 * @date 13/09/2012
 * 
 */
@Service
public class PagamentoServico extends ServicoGenerico<Pagamento> {

	@Autowired
	private PagamentoDAO dao;

	@Override
	protected DAOGenerico<Pagamento> getDAO() {
		return dao;
	}

	/**
	 * Este método lista os pagamentos referente a um aluno e turma
	 * 
	 * @author WENDEL
	 * @param alunoTurma
	 * @return List<Pagamento> - Lista de pagamentos
	 * @throws PersistenciaException 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<Pagamento> listarPorAlunoTurma(AlunoTurma alunoTurma) throws PersistenciaException {
		return dao.listarPorAlunoTurma(alunoTurma);
	}

	/**
	 * Este método salva um pagamento referente a um aluno
	 * 
	 * @author WENDEL
	 * @param entidade - Pagamento a ser salvo
	 * @return Pagamento - Pagamento salvo no banco
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Pagamento salvarOuAtualizar(Pagamento entidade) throws PersistenciaException, NegocioException {
		verificarEntidade(entidade);
		return super.salvarOuAtualizar(entidade);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void salvarPagamentos(	AlunoTurma alunoTurma, 
									BigDecimal valor, 
									EnumFormaDePagamento formaDePagamento, 
									Integer qtde, 
									String observacao,
									Date dataVencimento) throws PersistenciaException, NegocioException {
		
		for(int i = 0; i<qtde; i++){
			Pagamento pagamento = new Pagamento();
			pagamento.setValor(valor.divide(new BigDecimal(qtde)));
			pagamento.setAlunoTurma(alunoTurma);
			pagamento.setFormaPagamento(formaDePagamento);
			pagamento.setParcela(i+1);
			pagamento.setObservacao(observacao+" ("+(i+1)+"º Parcela)");
			
			if(qtde>1){
				GregorianCalendar data = new GregorianCalendar();
				data.setTime(dataVencimento);
				data.add(GregorianCalendar.MONTH, i);
				pagamento.setDataVencimento(data.getTime());
			} else {
				pagamento.setDataVencimento(new Date());
			}
			salvarOuAtualizar(pagamento);
		}
	}
	
	
	
	/**
	 * Este método faz uma validação do objeto de acordo com as regras de negócio
	 * 
	 * @author 	WENDEL
	 * @date 	24/09/2012
	 * @param	Objeto da Entidade
	 * @return 	boolean
	 */
	@Override
	protected boolean verificarEntidade(Pagamento entidade) throws NegocioException {
		if(entidade == null)
			throw new NegocioException("negocio.validacao.objetoNulo");
		else
			return true;
	}

	/**
	 * 
	 */
	public void verificarPagamentosPendentes(Pessoa usuario) {
		
	}
}
