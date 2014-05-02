package br.com.devschool.controle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.devschool.entidade.Pessoa;
import br.com.devschool.util.FacesUtils;
import br.com.devschool.util.Ferramentas;
import br.com.devschool.util.IAutenticador;
import br.com.devschool.util.SpringContainer;

/**
 * @author JOSEMAR
 * @date 26/12/2012
 * 
 */
@Component("loginControlador")
@Scope("session")
public class LoginControlador implements AuthenticationProvider {

	private Pessoa usuario;
	public String PAGINA_CORRENTE = "";
	public String INDEX = "";
	
	@Autowired
	private IAutenticador autenticador;

	/**
	 * Este método verifica se a pessoa já esta matrículado em uma turma
	 * 
	 * @author JOSEMAR
	 * @date 13/01/2013
	 * @return pagina do modelo de acordo com login
	 * 
	 */
	public String logar() {
		try {
			getUsuario().setSenha(Ferramentas.md5Critografia(getUsuario().getSenha()));
			Pessoa usuario = autenticador.autentica(getUsuario().getEmail(), getUsuario().getSenha());
			if (usuario != null && usuario.isStatus()) {
				logarSessao(usuario);
				
				//JOGA USUARIO NO CONTEXTO DO SPRING SECURITY
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(usuario.getEmail(), null, usuario.getAuthorities());
				token.setDetails(usuario);
				SecurityContextHolder.createEmptyContext();
				SecurityContextHolder.getContext().setAuthentication(token);
				
				if (usuario.getTipoAdm() != null && usuario.getTipoAdm()) {
					this.INDEX = "/paginas/adm/indexAdm.jsf";
				} else if (usuario.getTipoProfessor() != null && usuario.getTipoProfessor()) {
					this.INDEX = "/paginas/professor/indexProfessor.jsf";
				} else {
					this.INDEX = "/paginas/aluno/indexAluno.jsf";
				}
	
				return INDEX + "?faces-redirect=true";
			} else if (usuario != null && !usuario.isStatus()) {
				logarSessao(usuario);
				
				//JOGA USUARIO NO CONTEXTO DO SPRING SECURITY
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(usuario.getEmail(), null, usuario.getAuthorities());
				token.setDetails(usuario);
				SecurityContextHolder.createEmptyContext();
				SecurityContextHolder.getContext().setAuthentication(token);
				
				return "/paginas/aluno/reativarConta.jsf?faces-redirect=true";
			}
			setarMensagemInfo("Login ou senha inválidos.");
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return null;
	}

	public String sair() {
		try {
			logoutSessao();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return "/paginas/login.jsf?faces-redirect=true";
	}

	/**
	 * Este método limpa tela de cadastro de professor para ficar disponível
	 * para realizar um novo cadastro
	 * 
	 * @author JOSEMAR
	 * @date 04/01/2013
	 * @return String - Página Cadastro de Disciplina
	 */
	public String limpar() {
		try {
			setUsuario(new Pessoa());
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * @return the usuario
	 */
	public Pessoa getUsuario() {
		if (usuario == null) {
			usuario = new Pessoa();
		}
		return usuario;
	}

	public void setUsuario(Pessoa usuario) {
		this.usuario = usuario;
	}

	private void logarSessao(Pessoa usuario) {
		try {
			FacesUtils.getSessao().setAttribute("usuario", usuario);
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}

	private String logoutSessao() {
		try {
			FacesUtils.getSessao().removeAttribute("usuario");
			FacesUtils.getSessao().invalidate();
			SecurityContextHolder.clearContext();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return "/paginas/login.jsf?faces-redirect=true";
	}

	public Pessoa getUsuarioSessao() {
		return (Pessoa) FacesUtils.getSessao().getAttribute("usuario");
	}

	public boolean isLogado() {
		Pessoa usuario = null;
		try {
			usuario = getUsuarioSessao();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return usuario != null;
	}
	
	private void setarMensagemErro(String message) {
		FacesContext.getCurrentInstance().addMessage("ERROR", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""));
	}
	
	protected void setarMensagemInfo(String message) {
		FacesContext.getCurrentInstance().addMessage("INFO", new FacesMessage(FacesMessage.SEVERITY_INFO, message, ""));
	}
	
	public static LoginControlador getInstancia() {
		return ((LoginControlador) SpringContainer.getInstancia().getBean("loginControlador"));
	}

	@Override
	public Authentication authenticate(Authentication arg0) throws AuthenticationException {
		return null;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return false;
	}
}
