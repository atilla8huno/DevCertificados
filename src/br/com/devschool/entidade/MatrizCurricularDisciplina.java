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
 * @author	ATILLA
 * @date	10/09/2012
 * 
 */
@Entity
@Table(name="matriz_curricular_disciplina")
public class MatrizCurricularDisciplina extends EntidadeGeral implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_matriz_curricular_disciplina", unique=true, nullable=false)
	private Integer idMatrizCurricularDisciplina;

	@Column(name="carga_horaria", nullable=false)
	private Integer cargaHoraria;

	@ManyToOne
	@JoinColumn(name="id_disciplina", nullable=false)
	private Disciplina disciplina;

	@ManyToOne
	@JoinColumn(name="id_matriz_curricular", nullable=false)
	private MatrizCurricular matrizCurricular;

    public MatrizCurricularDisciplina() {
    }

	public Integer getIdMatrizCurricularDisciplina() {
		return this.idMatrizCurricularDisciplina;
	}

	public void setIdMatrizCurricularDisciplina(Integer idMatrizCurricularDisciplina) {
		this.idMatrizCurricularDisciplina = idMatrizCurricularDisciplina;
	}

	public Integer getCargaHoraria() {
		return this.cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public Disciplina getDisciplina() {
		return this.disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}
	
	public MatrizCurricular getMatrizCurricular() {
		return this.matrizCurricular;
	}

	public void setMatrizCurricular(MatrizCurricular matrizCurricular) {
		this.matrizCurricular = matrizCurricular;
	}

	@Override
	public Integer getId() {
		return idMatrizCurricularDisciplina;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((idMatrizCurricularDisciplina == null) ? 0
						: idMatrizCurricularDisciplina.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this.getId() != null && this.getId().intValue() > 0){
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			MatrizCurricularDisciplina other = (MatrizCurricularDisciplina) obj;
			if (idMatrizCurricularDisciplina == null) {
				if (other.idMatrizCurricularDisciplina != null)
					return false;
			} else if (!idMatrizCurricularDisciplina
					.equals(other.idMatrizCurricularDisciplina))
				return false;
			return true;
		} else {
			if(this.getDisciplina() != null){
				MatrizCurricularDisciplina other = (MatrizCurricularDisciplina) obj;
				if(this.getDisciplina().equals(other.getDisciplina()))
					return true;
				else
					return false;
			} else
				return true;
		}
	}
}