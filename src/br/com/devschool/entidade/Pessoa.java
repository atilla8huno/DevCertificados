package br.com.devschool.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.devschool.util.EntidadeGeral;

/**
 * @author ATILLA
 * @date 10/09/2012
 * 
 */
@Entity
@Table(name = "pessoa")
public class Pessoa extends EntidadeGeral implements Serializable, UserDetails {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pessoa", unique = true, nullable = false)
	private Integer idPessoa;

	@Column(name = "assinatura_digital")
	private byte[] assinaturaDigital;

	@Column(name = "atividade_profissional", length = 45)
	private String atividadeProfissional;

	@Column(nullable = false, length = 14)
	private String cpf;

	@Column(nullable = false, length = 45)
	private String email;

	@Column(length = 45)
	private String formacao;

	@Column(nullable = false, length = 34)
	private String nome;

	@Column(name = "telefone_celular", length = 14)
	private String telefoneCelular;

	@Column(name = "telefone_comercial", length = 14)
	private String telefoneComercial;

	@Column(name = "telefone_fixo", length = 14)
	private String telefoneFixo;

	@Column(length = 32)
	private String senha;

	@Column(name = "tipo_adm")
	private Boolean tipoAdm;

	@Column(name = "tipo_aluno")
	private Boolean tipoAluno;

	@Column(name = "tipo_professor")
	private Boolean tipoProfessor;

	@Column(name = "status", nullable = false)
	private boolean status;
	
	@Column(name = "newsletter", nullable = false)
	private boolean newsletter;
	
	@Column(name = "data_cadastro")
	private Date dataCadastro;

	@Transient
	private String confirmarSenha;

	@OneToMany(mappedBy = "pessoa", fetch=FetchType.EAGER)
	private Set<AlunoTurma> alunoTurmas;

	@OneToMany(mappedBy = "pessoa", fetch=FetchType.EAGER)
	private Set<Turma> turmas;

	public Pessoa() {
		this.tipoAdm 		= Boolean.FALSE;
		this.tipoProfessor 	= Boolean.FALSE;
		this.tipoAluno 		= Boolean.TRUE;
		this.status			= Boolean.TRUE;
		this.newsletter 	= Boolean.TRUE;
		this.dataCadastro	= new Date();
	}

	public Integer getIdPessoa() {
		return this.idPessoa;
	}

	public void setIdPessoa(Integer idPessoa) {
		this.idPessoa = idPessoa;
	}

	public byte[] getAssinaturaDigital() {
		return this.assinaturaDigital;
	}

	public void setAssinaturaDigital(byte[] assinaturaDigital) {
		this.assinaturaDigital = assinaturaDigital;
	}

	public String getAtividadeProfissional() {
		return this.atividadeProfissional;
	}

	public void setAtividadeProfissional(String atividadeProfissional) {
		this.atividadeProfissional = atividadeProfissional;
	}

	public String getCpf() {
		return this.cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFormacao() {
		return this.formacao;
	}

	public void setFormacao(String formacao) {
		this.formacao = formacao;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefoneCelular() {
		return this.telefoneCelular;
	}

	public void setTelefoneCelular(String telefoneCelular) {
		this.telefoneCelular = telefoneCelular;
	}

	public String getTelefoneComercial() {
		return this.telefoneComercial;
	}

	public void setTelefoneComercial(String telefoneComercial) {
		this.telefoneComercial = telefoneComercial;
	}

	public String getTelefoneFixo() {
		return this.telefoneFixo;
	}

	public void setTelefoneFixo(String telefoneFixo) {
		this.telefoneFixo = telefoneFixo;
	}

	public Boolean getTipoAdm() {
		return this.tipoAdm;
	}

	public void setTipoAdm(Boolean tipoAdm) {
		if (tipoAdm == null) {
			tipoAdm = false;
		}
		this.tipoAdm = tipoAdm;
	}

	public Boolean getTipoAluno() {
		return this.tipoAluno;
	}

	public void setTipoAluno(Boolean tipoAluno) {
		if (tipoAluno == null) {
			tipoAluno = false;
		}
		this.tipoAluno = tipoAluno;
	}

	public Boolean getTipoProfessor() {
		return this.tipoProfessor;
	}

	public void setTipoProfessor(Boolean tipoProfessor) {
		if (tipoProfessor == null) {
			tipoProfessor = false;
		}
		this.tipoProfessor = tipoProfessor;
	}

	public Set<AlunoTurma> getAlunoTurmas() {
		return this.alunoTurmas;
	}

	public void setAlunoTurmas(Set<AlunoTurma> alunoTurmas) {
		this.alunoTurmas = alunoTurmas;
	}

	public Set<Turma> getTurmas() {
		return this.turmas;
	}

	public void setTurmas(Set<Turma> turmas) {
		this.turmas = turmas;
	}

	@Override
	public Integer getId() {
		return idPessoa;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}

	public boolean isNewsletter() {
		return newsletter;
	}

	public void setNewsletter(boolean newsletter) {
		this.newsletter = newsletter;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	@Override
	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<Role> list = new ArrayList<Role>();
		
		if(this.tipoAdm){
			Role role = new Role();
			role.setDescricao("ROLE_ADMINISTRADOR");
			list.add(role);
		}
		
		if(this.tipoAluno){
			Role role = new Role();
			role.setDescricao("ROLE_ALUNO");
			list.add(role);
		}
		
		if(this.tipoProfessor){
			Role role = new Role();
			role.setDescricao("ROLE_PROFESSOR");
			list.add(role);
		}
		
		return list;
	}

	@Override
	@Transient
	public String getPassword() {
		return this.senha;
	}

	@Override
	@Transient
	public String getUsername() {
		return this.email;
	}

	@Override
	@Transient
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	@Transient
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	@Transient
	public boolean isEnabled() {
		return false;
	}

}