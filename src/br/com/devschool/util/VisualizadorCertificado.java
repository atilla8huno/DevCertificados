package br.com.devschool.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.devschool.entidade.Relatorio;

/**
 * Servlet responsável por disponibilizar os certificados para Download
 * 
 * @author ATILLA
 */
@WebServlet(name="VisualizadorCertificado", urlPatterns={"/paginas/aluno/VisualizaCertificado", "/paginas/professor/VisualizaCertificado", "/paginas/adm/VisualizaCertificado"})
public class VisualizadorCertificado extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void service(ServletRequest arg1, ServletResponse arg2) throws ServletException, IOException {
		
		HttpServletRequest request = (HttpServletRequest) arg1;
		byte[] bytes = (byte[]) request.getSession().getAttribute(Relatorio.SESSAO);
		
		if (bytes != null) {
			HttpServletResponse response = (HttpServletResponse) arg2;
			response.setContentType("application/pdf");
			response.setContentLength(bytes.length);
			response.setHeader("Content-Disposition", "inline;filename=\"relatorio.pdf\"");
			response.getOutputStream().write(bytes, 0, bytes.length);
			response.getOutputStream().flush();
			response.getOutputStream().close();
			
			request.getSession().removeAttribute(Relatorio.SESSAO);
		}
	}
}
