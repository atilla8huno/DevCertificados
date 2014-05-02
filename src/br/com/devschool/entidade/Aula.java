package br.com.devschool.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.devschool.util.EntidadeGeral;

/**
 * @author	ATILLA
 * @date	10/09/2012
 * 
 */
@Entity
@Table(name="aula")
public class Aula extends EntidadeGeral implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_aula", unique=true, nullable=false)
	private Integer idAula;

    @Temporal( TemporalType.DATE)
	@Column(name="data_aula", nullable=false)
	private Date dataAula;

	@ManyToOne
	@JoinColumn(name="id_turma", nullable=false)
	private Turma turma;
	
	@Column(name="plano_aula")
	private String planoDeAula;

    public Aula() {
    }

	public Integer getIdAula() {
		return this.idAula;
	}

	public void setIdAula(Integer idAula) {
		this.idAula = idAula;
	}

	public Date getDataAula() {
		return this.dataAula;
	}

	public void setDataAula(Date dataAula) {
		this.dataAula = dataAula;
	}

	public Turma getTurma() {
		return this.turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}
	
	public String getPlanoDeAula() {
		return planoDeAula;
	}

	public void setPlanoDeAula(String planoDeAula) {
		this.planoDeAula = planoDeAula;
	}

	@Override
	public Integer getId() {
		return idAula;
	}
}