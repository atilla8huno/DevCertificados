package br.com.devschool.entidade;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.devschool.util.EntidadeGeral;

/**
 * @author	ATILLA
 * @date	10/09/2012
 * 
 */
@Entity
@Table(name="disciplina")
public class Disciplina extends EntidadeGeral implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_disciplina", unique=true, nullable=false)
	private Integer idDisciplina;

	@Column(nullable=false, length=34)
	private String titulo;

	@OneToMany(mappedBy="disciplina", fetch=FetchType.EAGER)
	private Set<MatrizCurricularDisciplina> matrizCurricularDisciplinas;

	@Column(name="status", nullable=false)
	private boolean status;
	
    public Disciplina() {
    	this.status = true;
    }

	public Integer getIdDisciplina() {
		return this.idDisciplina;
	}

	public void setIdDisciplina(Integer idDisciplina) {
		this.idDisciplina = idDisciplina;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Set<MatrizCurricularDisciplina> getMatrizCurricularDisciplinas() {
		return this.matrizCurricularDisciplinas;
	}

	public void setMatrizCurricularDisciplinas(Set<MatrizCurricularDisciplina> matrizCurricularDisciplinas) {
		this.matrizCurricularDisciplinas = matrizCurricularDisciplinas;
	}
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public Integer getId() {
		return idDisciplina;
	}	
}