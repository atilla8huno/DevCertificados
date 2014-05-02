package br.com.devschool.entidade;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.devschool.util.EntidadeGeral;

/**
 * @author	ATILLA
 * @date	10/09/2012
 * 
 */
@Entity
@Table(name="matriz_curricular")
public class MatrizCurricular extends EntidadeGeral implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_matriz_curricular", unique=true, nullable=false)
	private Integer idMatrizCurricular;

	@ManyToOne
	@JoinColumn(name="id_curso", nullable=false)
	private Curso curso;

	@ManyToOne
	@JoinColumn(name="id_nivel", nullable=false)
	private Nivel nivel;

	@ManyToOne
	@JoinColumn(name="id_turno", nullable=false)
	private Turno turno;

	@OneToMany(mappedBy="matrizCurricular", fetch=FetchType.EAGER)
	private Set<MatrizCurricularDisciplina> matrizCurricularDisciplinas;

	@OneToMany(mappedBy="matrizCurricular", fetch=FetchType.EAGER)
	private Set<Turma> turmas;

    public MatrizCurricular() {
    }

	public Integer getIdMatrizCurricular() {
		return this.idMatrizCurricular;
	}

	public void setIdMatrizCurricular(Integer idMatrizCurricular) {
		this.idMatrizCurricular = idMatrizCurricular;
	}

	public Curso getCurso() {
		return this.curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	
	public Nivel getNivel() {
		return this.nivel;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}
	
	public Turno getTurno() {
		return this.turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}
	
	public Set<MatrizCurricularDisciplina> getMatrizCurricularDisciplinas() {
		return this.matrizCurricularDisciplinas;
	}

	public void setMatrizCurricularDisciplinas(Set<MatrizCurricularDisciplina> matrizCurricularDisciplinas) {
		this.matrizCurricularDisciplinas = matrizCurricularDisciplinas;
	}
	
	public Set<Turma> getTurmas() {
		return this.turmas;
	}

	public void setTurmas(Set<Turma> turmas) {
		this.turmas = turmas;
	}

	@Override
	public Integer getId() {
		return idMatrizCurricular;
	}
	
}