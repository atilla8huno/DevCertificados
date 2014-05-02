package br.com.devschool.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.devschool.enumeradores.EnumFormaDePagamento;
import br.com.devschool.util.EntidadeGeral;

/**
 * @author ATILLA
 * @date 10/09/2012
 * 
 */
@Entity
@Table(name = "pagamento")
public class Pagamento extends EntidadeGeral implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pagamento", unique = true, nullable = false)
	private Integer idPagamento;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_pagamento")
	private Date dataPagamento;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data_vencimento")
	private Date dataVencimento;
	
	@Column(length = 2147483647)
	private String observacao;

	@Column(nullable = false, precision = 6, scale = 2)
	private BigDecimal valor;

	@ManyToOne
	@JoinColumn(name = "id_aluno_turma", nullable = false)
	private AlunoTurma alunoTurma;

	@Column(name = "forma_pagamento", nullable = false)
	@Enumerated(EnumType.STRING)
	private EnumFormaDePagamento formaPagamento;

	@Column(name = "parcela", nullable=false)
	private Integer parcela;

	public Pagamento() {
	}

	public Integer getIdPagamento() {
		return this.idPagamento;
	}

	public void setIdPagamento(Integer idPagamento) {
		this.idPagamento = idPagamento;
	}

	public Date getDataPagamento() {
		return this.dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getObservacao() {
		return this.observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public BigDecimal getValor() {
		return this.valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public AlunoTurma getAlunoTurma() {
		return this.alunoTurma;
	}

	public void setAlunoTurma(AlunoTurma alunoTurma) {
		this.alunoTurma = alunoTurma;
	}

	public EnumFormaDePagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(EnumFormaDePagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public Integer getParcela() {
		return parcela;
	}

	public void setParcela(Integer parcela) {
		this.parcela = parcela;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}
	
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	
	@Override
	public Integer getId() {
		return idPagamento;
	}

}