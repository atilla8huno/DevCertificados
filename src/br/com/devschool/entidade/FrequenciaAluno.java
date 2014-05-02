package br.com.devschool.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.devschool.util.EntidadeGeral;


/**
 * The persistent class for the frequencia_aluno database table.
 * 
 */
@Entity
@Table(name="frequencia_aluno")
public class FrequenciaAluno extends EntidadeGeral implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_frequencia_aluno")
	private Integer idFrequenciaAluno;

	private Boolean falta;

    @ManyToOne
	@JoinColumn(name="id_aluno_turma")
	private AlunoTurma alunoTurma;

    @ManyToOne
	@JoinColumn(name="id_aula")
	private Aula aula;

    public FrequenciaAluno() {
    }

	public Integer getIdFrequenciaAluno() {
		return this.idFrequenciaAluno;
	}

	public void setIdFrequenciaAluno(Integer idFrequenciaAluno) {
		this.idFrequenciaAluno = idFrequenciaAluno;
	}

	public Boolean getFalta() {
		return this.falta;
	}

	public void setFalta(Boolean falta) {
		this.falta = falta;
	}

	public AlunoTurma getAlunoTurma() {
		return this.alunoTurma;
	}

	public void setAlunoTurma(AlunoTurma alunoTurma) {
		this.alunoTurma = alunoTurma;
	}
	
	public Aula getAula() {
		return this.aula;
	}

	public void setAula(Aula aula) {
		this.aula = aula;
	}

	@Override
	public Integer getId() {
		return idFrequenciaAluno;
	}
	
}