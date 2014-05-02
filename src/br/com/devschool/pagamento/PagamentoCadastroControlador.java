package br.com.devschool.pagamento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.entidade.AlunoTurma;
import br.com.devschool.entidade.Pagamento;
import br.com.devschool.enumeradores.EnumFormaDePagamento;
import br.com.devschool.pagamento.servico.PagamentoServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.ICadastroControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

@Component("pagamentoCadastroControlador")
@Scope("session")
public class PagamentoCadastroControlador extends ControladorGenerico<Pagamento> implements ICadastroControlador {
	
	@Autowired
	private PagamentoServico servico;
	private AlunoTurma alunoTurma;
	private Pagamento entidadeSelecionada;
	private List<Pagamento> listaPagamentos;
	private EnumFormaDePagamento formaDePagamento;
	private BigDecimal valor;
	private String observacao;
	private Integer qtdePagamento;
	private Date dataVencimento;
	
	/**
	 * Este método salva um pagamento referente ao aluno e turma
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	@Override
	public String salvar(){
		try {
			servico.salvarPagamentos(getAlunoTurma(), getValor(), getFormaDePagamento(), getQtdePagamento(), getObservacao(), getDataVencimento());
			limpar();
			setarMensagemInclusaoSucesso();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método exclui um pagamento referente ao aluno e turma
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	@Override
	public String excluir() {
		try {
			servico.excluir(getEntidadeSelecionada());
			listarPagamentos();
			setarMensagemExclusaoSucesso();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	public void atualizarPagamento(){
		try {
			servico.salvarOuAtualizar(getEntidadeSelecionada());
			listarPagamentos();
			setarMensagemAlteracaoSucesso();
		} catch (Exception e) {
			setarMensagemErro(e);
		}
	}

	/**
	 * Esté metodo lista todos os pagamentos referente ao aluno e turma
	 * 
	 * @author WENDEL
	 */
	public void listarPagamentos() {
		try {
			setListaPagamentos(servico.listarPorAlunoTurma(getAlunoTurma()));
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}

	@Override
	public String cancelar() {
		return PAGINA_CORRENTE;
	}

	@Override
	protected ServicoGenerico<Pagamento> getService() {
		return servico;
	}

	/**
	 * Este método prepara a tela de pagamento para entrar na mesma
	 * 
	 * @author WENDEL
	 * @return String - Página de cadastro de pagamneto
	 */
	@Override
	public String entrar() {
		try{
			limpar();
		} catch (Exception e){
			setarMensagemErro(e.getMessage());
		}
		return "/paginas/adm/pagamentoCadastro.jsf?faces-redirect=true";
	}

	/**
	 * Este método limpa a tela de pagamento
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	@Override
	public String limpar() {
		try{
			setFormaDePagamento(EnumFormaDePagamento.AVISTA);
			setValor(new BigDecimal("0.0"));
			setObservacao("");
			setQtdePagamento(0);
			setDataVencimento(new Date());
			setEntidade(new Pagamento());
			listarPagamentos();
		} catch (Exception e){
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	public List<Pagamento> getListaPagamentos() {
		if(listaPagamentos == null){
			listaPagamentos = new ArrayList<Pagamento>();
		}
		return listaPagamentos;
	}

	public void setListaPagamentos(List<Pagamento> listaPagamentos) {
		this.listaPagamentos = listaPagamentos;
	}

	public AlunoTurma getAlunoTurma() {
		if (alunoTurma == null) {
			alunoTurma = new AlunoTurma();
		}
		return alunoTurma;
	}

	public void setAlunoTurma(AlunoTurma alunoTurma) {
		this.alunoTurma = alunoTurma;
	}
	
	public Pagamento getEntidadeSelecionada() {
		if (entidadeSelecionada == null) {
			entidadeSelecionada = new Pagamento();
		}
		return entidadeSelecionada;
	}

	public void setEntidadeSelecionada(Pagamento entidadeSelecionada) {
		this.entidadeSelecionada = entidadeSelecionada;
	}

	public EnumFormaDePagamento getFormaDePagamento() {
		if(this.formaDePagamento == null){
			this.formaDePagamento = EnumFormaDePagamento.AVISTA;
		}
		return formaDePagamento;
	}

	public void setFormaDePagamento(EnumFormaDePagamento formaDePagamento) {
		this.formaDePagamento = formaDePagamento;
	}

	public Integer getQtdePagamento() {
		if(this.qtdePagamento == null){
			this.qtdePagamento = new Integer(1);
		}
		return qtdePagamento;
	}

	public void setQtdePagamento(Integer qtdePagamento) {
		this.qtdePagamento = qtdePagamento;
	}

	public Date getDataVencimento() {
		if(this.dataVencimento == null){
			this.dataVencimento = Calendar.getInstance().getTime();
		}
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	
	public List<EnumFormaDePagamento> getFormasDePagamentos(){
		return Arrays.asList(EnumFormaDePagamento.values());
	}
	
	public List<Integer> getParcelas(){
		List<Integer> lista = new ArrayList<Integer>();
		for(int i = 0; i<getFormaDePagamento().getQtdeParcelas(); i++){
			lista.add(i+1);
		}
		return lista;
	}

	public BigDecimal getValor() {
		if(this.valor == null){
			this.valor = new BigDecimal(0);
		}
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getObservacao() {
		if(this.observacao == null){
			this.observacao = new String("");
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	/**
	 * @return PagamentoCadastroControlador
	 */
	public static PagamentoCadastroControlador getInstancia() {
		return (PagamentoCadastroControlador) SpringContainer.getInstancia().getBean("pagamentoCadastroControlador");
	}

	@Override
	public Pagamento getEntidade() {
		return null;
	}

	@Override
	public void setEntidade(Pagamento entidade) {
	}
}
