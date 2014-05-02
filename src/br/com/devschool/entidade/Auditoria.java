package br.com.devschool.entidade;

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

@Entity
@Table(name = "auditoria")
public class Auditoria extends EntidadeGeral {
	
	public static String AUDIT_SEPARATOR = ";";
	
	public static String MENSAGEM_PERSISTENCIA = "Persistência";
	public static String MENSAGEM_NEGOCIO = "Negócio";
	public static String MENSAGEM_RELATORIO = "Relatório";
	public static String MENSAGEM_GERAL = "Geral";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_auditoria", unique = true, nullable = false)
	private Integer idAuditoria;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	@ManyToOne
	@JoinColumn(name = "id_pessoa")
	private Pessoa pessoa;
	
	@Column(name = "url_funcionalidade")
	private String urlFuncionalidade;
	
	@Column(name = "id_mensagem")
	private String idMensagem;
	
	@Column(name = "tipo_mensagem")
	private String tipoMensagem;
	
	@Column
	private String mensagem;
	
	@Column
	private String excecao;
	
	@Override
	public Integer getId() {
		return idAuditoria;
	}

	public Integer getIdAuditoria() {
		return idAuditoria;
	}

	public void setIdAuditoria(Integer idAuditoria) {
		this.idAuditoria = idAuditoria;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public String getUrlFuncionalidade() {
		return urlFuncionalidade;
	}

	public void setUrlFuncionalidade(String urlFuncionalidade) {
		this.urlFuncionalidade = urlFuncionalidade;
	}

	public String getTipoMensagem() {
		return tipoMensagem;
	}

	public void setTipoMensagem(String tipoMensagem) {
		this.tipoMensagem = tipoMensagem;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getExcecao() {
		return excecao;
	}

	public void setExcecao(String excecao) {
		this.excecao = excecao;
	}

	public String getIdMensagem() {
		return idMensagem;
	}

	public void setIdMensagem(String idMensagem) {
		this.idMensagem = idMensagem;
	}
}
