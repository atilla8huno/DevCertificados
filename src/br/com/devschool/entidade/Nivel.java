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
@Table(name="nivel")
public class Nivel extends EntidadeGeral implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_nivel", unique=true, nullable=false)
	private Integer idNivel;

	@Column(nullable=false, length=14)
	private String descricao;

	@Column(nullable=false, length=1)
	private String sigla;

	@OneToMany(mappedBy="nivel", fetch=FetchType.EAGER)
	private Set<MatrizCurricular> matrizCurriculars;

    public Nivel() {
    }

	public Integer getIdNivel() {
		return this.idNivel;
	}

	public void setIdNivel(Integer idNivel) {
		this.idNivel = idNivel;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getSigla() {
		return this.sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Set<MatrizCurricular> getMatrizCurriculars() {
		return this.matrizCurriculars;
	}

	public void setMatrizCurriculars(Set<MatrizCurricular> matrizCurriculars) {
		this.matrizCurriculars = matrizCurriculars;
	}

	@Override
	public Integer getId() {
		return idNivel;
	}
	
}