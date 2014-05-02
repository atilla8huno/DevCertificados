package br.com.devschool.controle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.entidade.Pessoa;

@Component("menuBarControlador")
@Scope("session")
public class MenuBarControlador {

	private static String MENU_PROFESSOR = "/menus/menuBarProfessor.xhtml";
	private static String MENU_ADMIN = "/menus/menuBarAdm.xhtml";
	private static String MENU_ALUNO = "/menus/menuBarAluno.xhtml";
	private static String HOME = "";
	private String menuAtivo;

	public String ativaMenuAdm() {
		try {
			setMenuAtivo(MENU_ADMIN);
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return "/paginas/adm/indexAdm.jsf?faces-redirect=true";
	}

	public String ativaMenuProfessor() {
		try {
			setMenuAtivo(MENU_PROFESSOR);
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return "/paginas/professor/indexProfessor.jsf?faces-redirect=true";
	}

	public String ativaMenuAluno() {
		try {
			setMenuAtivo(MENU_ALUNO);
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return "/paginas/aluno/indexAluno.jsf?faces-redirect=true";
	}

	public String getMenuAtivo() {
		if (menuAtivo == null) {
			menuAtivo = "";
		}
		return menuAtivo;
	}

	public void setMenuAtivo(String menuAtivo) {
		this.menuAtivo = menuAtivo;
	}

	public String controleHomepage() {
		try {
			Pessoa usuarioLogado = LoginControlador.getInstancia().getUsuarioSessao();
			HOME = "";
			if (usuarioLogado.getTipoAdm()) {
				HOME = "/paginas/adm/indexAdm.jsf?faces-redirect=true";
			} else if (usuarioLogado.getTipoProfessor()) {
				HOME = "/paginas/professor/indexProfessor.jsf?faces-redirect=true";
			} else {
				HOME = "/paginas/aluno/indexAluno.jsf?faces-redirect=true";
			}
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return HOME;
	}
	
	private void setarMensagemErro(String message) {
		FacesContext.getCurrentInstance().addMessage("ERROR", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""));
	}
}
