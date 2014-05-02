package br.com.devschool.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.primefaces.model.StreamedContent;

import br.com.devschool.util.EntidadeGeral;
import br.com.devschool.util.Ferramentas;

/**
 * @author ATILLA
 * @date 10/09/2012
 * 
 */
@Entity
@Table(name = "curso")
public class Curso extends EntidadeGeral implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_curso", unique = true, nullable = false)
	private Integer idCurso;

	@Column(name = "aluno_apos_curso", nullable = false, length = 2147483647)
	private String alunoAposCurso;

	@Column(nullable = false, length = 2147483647)
	private String descricao;

	@Column(precision = 131089)
	private BigDecimal investimento;

	@Column(name = "pre_requisito", nullable = false, length = 2147483647)
	private String preRequisito;

	@Column(name = "publico_alvo", nullable = false, length = 2147483647)
	private String publicoAlvo;

	@Column(nullable = false, length = 45)
	private String titulo;

	@Column(name = "imagem")
	private byte[] imagem;
	
	@Column(name="status", nullable=false)
	private boolean status;

	@OneToMany(mappedBy = "curso", fetch=FetchType.EAGER)
	private Set<MatrizCurricular> matrizCurriculars;

	@Transient
	private StreamedContent mostraImagem;

	public Curso() {

	}

	public Integer getIdCurso() {
		return this.idCurso;
	}

	public void setIdCurso(Integer idCurso) {
		this.idCurso = idCurso;
	}

	public String getAlunoAposCurso() {
		return this.alunoAposCurso;
	}

	public void setAlunoAposCurso(String alunoAposCurso) {
		this.alunoAposCurso = alunoAposCurso;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getInvestimento() {
		return this.investimento;
	}

	public void setInvestimento(BigDecimal investimento) {
		this.investimento = investimento;
	}

	public String getPreRequisito() {
		return this.preRequisito;
	}

	public void setPreRequisito(String preRequisito) {
		this.preRequisito = preRequisito;
	}

	public String getPublicoAlvo() {
		return this.publicoAlvo;
	}

	public void setPublicoAlvo(String publicoAlvo) {
		this.publicoAlvo = publicoAlvo;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Set<MatrizCurricular> getMatrizCurriculars() {
		return this.matrizCurriculars;
	}

	public void setMatrizCurriculars(Set<MatrizCurricular> matrizCurriculars) {
		this.matrizCurriculars = matrizCurriculars;
	}

	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

	/**
	 * @return the mostraImagem
	 */
	public StreamedContent getMostraImagem() {
		return mostraImagem = Ferramentas.mostraImagem(getImagem(), null);
	}

	/**
	 * @param mostraImagem
	 *            the mostraImagem to set
	 */
	public void setMostraImagem(StreamedContent mostraImagem) {
		this.mostraImagem = mostraImagem;
	}

	@Override
	public Integer getId() {
		return idCurso;
	}

}