package br.com.devschool.controle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.util.Ferramentas;
import br.com.devschool.util.SpringContainer;

/**
 * @author ATILLA
 * @date 13/01/2013
 */
@Component("contatoControlador")
@Scope("session")
public class ContatoControlador {

	private static final String EMAIL_REMETENTE = "";
	private static final String SENHA_REMETENTE = "";
	private static final String PAGINA_CORRENTE = "";
	
	private String nome;
	private String email;
	private String telefone;
	private String mensagem;
	
	public String entrarEmContato(){
		try {
			if(!EMAIL_REMETENTE.equals("") && !SENHA_REMETENTE.equals("")){
				if(Ferramentas.entrarEmContato(nome, email, telefone, "Contato Cursos", mensagem, EMAIL_REMETENTE, SENHA_REMETENTE)){
					setarMensagemInfo("Mensagem enviada com sucesso!");
					return entrar();
				} else {
					setarMensagemErro("Erro ao entrar em contato");
				}
			} else {
				setarMensagemErro("Erro interno! É necessário autenticar e-mail Gmail");
			}
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método limpa o formulário 'Entrar em Contato' para ficar
	 * disponível para realizar um novo contato
	 * 
	 * @author	ATILLA
	 * @date 	13/01/2013
	 * @return 	String
	 */
	public String limpar() {
		try {
			nome	= "";
			email 	= "";
			telefone= "";
			mensagem= "";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método prepara para entrar entrar em contato
	 * 
	 * @author ATILLA
	 * @date 13/01/2013
	 * @return String
	 */
	public String entrar() {
		try {
			limpar();
			return "/paginas/adm/entrarEmContato.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	private void setarMensagemErro(String message) {
		FacesContext.getCurrentInstance().addMessage("ERROR", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""));
	}
	
	protected void setarMensagemInfo(String message) {
		FacesContext.getCurrentInstance().addMessage("INFO", new FacesMessage(FacesMessage.SEVERITY_INFO, message, ""));
	}
	
	public static ContatoControlador getInstancia() {
		return (ContatoControlador) SpringContainer.getInstancia().getBean("contatoControlador");
	}

	public String getMensagem() {
		if (mensagem == null) {
			mensagem = ""; 
		}
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		if (telefone == null) {
			telefone = "";
		}
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
}
