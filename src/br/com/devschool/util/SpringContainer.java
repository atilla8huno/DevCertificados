package br.com.devschool.util;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

public class SpringContainer {
	private ApplicationContext contextoSpring;

	private ApplicationContext getContextoSpring() {
		if (contextoSpring == null) {
			contextoSpring = FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance());
		}
		return contextoSpring;
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(String nome) {
		ApplicationContext contexto = getContextoSpring();
		if (contexto != null) {
			try {
				return (T) contexto.getBean(nome);
			} catch (NoSuchBeanDefinitionException ex) {
				return null;
			}
		}
		return null;
	}

	public static synchronized SpringContainer getInstancia() {
		return _instancia;
	}

	private static SpringContainer _instancia = new SpringContainer();

	private SpringContainer() {
	}
}
