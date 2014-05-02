package br.com.devschool.entidade;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.devschool.enumeradores.EnumStatusTurma;
import br.com.devschool.util.EntidadeGeral;

/**
 * @author	ATILLA
 * @date	10/09/2012
 * 
 */
@Entity
@Table(name="turma")
public class Turma extends EntidadeGeral implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_turma", unique=true, nullable=false)
	private Integer idTurma;

	@Column(nullable=false, length=14)
	private String nome;
	
	@Temporal(TemporalType.DATE)
    @Column(name = "data_real_fim")
    private Date dataRealFim;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_real_inicio")
    private Date dataRealInicio;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "previsao_data_fim")
    private Date previsaoDataFim;

    @Temporal(TemporalType.DATE)
    @Column(name = "previsao_data_inicio")
    private Date previsaoDataInicio;

	@OneToMany(mappedBy="turma", fetch=FetchType.EAGER)
	private Set<AlunoTurma> alunoTurmas;

	@OneToMany(mappedBy="turma", fetch=FetchType.EAGER)
	private Set<Aula> aulas;

	@ManyToOne
	@JoinColumn(name="id_matriz_curricular", nullable=false)
	private MatrizCurricular matrizCurricular;

	@ManyToOne
	@JoinColumn(name="id_pessoa", nullable=false)
	private Pessoa pessoa;
	
	@Column(name="status", nullable=false)
	@Enumerated(EnumType.STRING)
	private EnumStatusTurma status;
	
	@Transient
	private byte[] certificados;

    public Turma() {
    }

	public Integer getIdTurma() {
		return this.idTurma;
	}

	public void setIdTurma(Integer idTurma) {
		this.idTurma = idTurma;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Date getDataRealFim() {
		return this.dataRealFim;
	}

	public void setDataRealFim(Date dataRealFim) {
		this.dataRealFim = dataRealFim;
	}

	public Date getDataRealInicio() {
		return this.dataRealInicio;
	}

	public void setDataRealInicio(Date dataRealInicio) {
		this.dataRealInicio = dataRealInicio;
	}

	public Date getPrevisaoDataFim() {
		return this.previsaoDataFim;
	}

	public void setPrevisaoDataFim(Date previsaoDataFim) {
		this.previsaoDataFim = previsaoDataFim;
	}

	public Date getPrevisaoDataInicio() {
		return this.previsaoDataInicio;
	}

	public void setPrevisaoDataInicio(Date previsaoDataInicio) {
		this.previsaoDataInicio = previsaoDataInicio;
	}

	public Set<AlunoTurma> getAlunoTurmas() {
		return this.alunoTurmas;
	}

	public void setAlunoTurmas(Set<AlunoTurma> alunoTurmas) {
		this.alunoTurmas = alunoTurmas;
	}
	
	public Set<Aula> getAulas() {
		return this.aulas;
	}

	public void setAulas(Set<Aula> aulas) {
		this.aulas = aulas;
	}
	
	public MatrizCurricular getMatrizCurricular() {
		return this.matrizCurricular;
	}

	public void setMatrizCurricular(MatrizCurricular matrizCurricular) {
		this.matrizCurricular = matrizCurricular;
	}
	
	public Pessoa getPessoa() {
		return this.pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public byte[] getCertificados() {
		return certificados;
	}

	public void setCertificados(byte[] certificados) {
		this.certificados = certificados;
	}

	public EnumStatusTurma getStatus() {
		return status;
	}

	public void setStatus(EnumStatusTurma status) {
		this.status = status;
	}

	@Override
	public Integer getId() {
		return idTurma;
	}
}