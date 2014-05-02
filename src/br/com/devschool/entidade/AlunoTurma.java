package br.com.devschool.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.devschool.enumeradores.EnumStatusAlunoTurma;
import br.com.devschool.util.EntidadeGeral;

@Entity
@Table(name = "aluno_turma")
public class AlunoTurma extends EntidadeGeral implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_aluno_turma", unique = true, nullable = false)
	private Integer idAlunoTurma;

	@Column(name = "porcentagem_aproveitamento")
	private BigDecimal porcentagemAproveitamento;

	@Column(name = "porcentagem_frequencia")
	private BigDecimal porcentagemFrequencia;

	@ManyToOne
	@JoinColumn(name = "id_pessoa")
	private Pessoa pessoa;

	@ManyToOne
	@JoinColumn(name = "id_turma")
	private Turma turma;
	
	@Column
	@Enumerated(EnumType.STRING)
	private EnumStatusAlunoTurma status;
	
	@OneToMany(mappedBy = "alunoTurma", fetch=FetchType.EAGER)
	private Set<FrequenciaAluno> frequenciaAlunos;

	@OneToMany(mappedBy = "alunoTurma", fetch=FetchType.EAGER)
	private Set<Pagamento> pagamentos;

	@Transient
	private byte[] certificado;
	
	@Column(name="ativo")
	private Boolean ativo;

	public AlunoTurma() {
	}

	public Integer getIdAlunoTurma() {
		return this.idAlunoTurma;
	}

	public void setIdAlunoTurma(Integer idAlunoTurma) {
		this.idAlunoTurma = idAlunoTurma;
	}

	public BigDecimal getPorcentagemAproveitamento() {
		return porcentagemAproveitamento;
	}

	public void setPorcentagemAproveitamento(BigDecimal porcentagemAproveitamento) {
		this.porcentagemAproveitamento = porcentagemAproveitamento;
	}

	public BigDecimal getPorcentagemFrequencia() {
		return porcentagemFrequencia;
	}

	public void setPorcentagemFrequencia(BigDecimal porcentagemFrequencia) {
		this.porcentagemFrequencia = porcentagemFrequencia;
	}

	public Pessoa getPessoa() {
		return this.pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Turma getTurma() {
		return this.turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}
	
	public EnumStatusAlunoTurma getStatus() {
		return status;
	}

	public void setStatus(EnumStatusAlunoTurma status) {
		this.status = status;
	}

	public Set<FrequenciaAluno> getFrequenciaAlunos() {
		return this.frequenciaAlunos;
	}

	public void setFrequenciaAlunos(Set<FrequenciaAluno> frequenciaAlunos) {
		this.frequenciaAlunos = frequenciaAlunos;
	}

	public Set<Pagamento> getPagamentos() {
		return this.pagamentos;
	}

	public void setPagamentos(Set<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}

	@Override
	public Integer getId() {
		return idAlunoTurma;
	}

	public byte[] getCertificado() {
		return certificado;
	}

	public void setCertificado(byte[] certificado) {
		this.certificado = certificado;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}