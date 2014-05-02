package br.com.devschool.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class FacesUtils {

	public static FacesContext getInstance(){
	    return FacesContext.getCurrentInstance();
	}
	
	public static HttpSession getSessao(){
		FacesContext fc 	= FacesUtils.getInstance();
	    ExternalContext ec 	= fc.getExternalContext();
	    return (HttpSession) ec.getSession(false);
	}
}
