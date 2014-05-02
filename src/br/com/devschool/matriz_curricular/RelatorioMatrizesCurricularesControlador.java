package br.com.devschool.matriz_curricular;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.curso.servico.CursoServico;
import br.com.devschool.entidade.Curso;
import br.com.devschool.entidade.Nivel;
import br.com.devschool.entidade.Relatorio;
import br.com.devschool.entidade.Turno;
import br.com.devschool.matriz_curricular.servico.MatrizCurricularServico;
import br.com.devschool.nivel.servico.NivelServico;
import br.com.devschool.turno.servico.TurnoServico;
import br.com.devschool.util.GeradorRelatorio;
import br.com.devschool.util.IRelatorioControlador;
import br.com.devschool.util.SpringContainer;

/**
 * @author	ATILLA
 * @date	10/09/2012
 */
@Component("relatorioMatrizesCurricularesControlador")
@Scope("request")
public class RelatorioMatrizesCurricularesControlador implements IRelatorioControlador {

	private static String PAGINA_CORRENTE = "/paginas/adm/relatorioMatrizesCurriculares.jsf?faces-redirect=true";
	
	private Curso curso;
	private Nivel nivel;
	private Turno turno;
	
	@Autowired
	private MatrizCurricularServico servico;
	@Autowired
	private CursoServico cursoServico;
	@Autowired
	private NivelServico nivelServico;
	@Autowired
	private TurnoServico turnoServico;
	@Autowired
	private GeradorRelatorio geradorRelatorio;

	/**
	 * Este método prepara os filtros e parâmetros e emite o relatório de matrizes curriculares.
	 * 
	 * @return String
	 */
	@Override
	public String emitirRelatorio() {
		try {
			exibirRelatorioSessao(servico.gerarRelatorioMatrizesCurriculares(curso, nivel, turno));
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método faz uma busca de todos os cursos no banco e retorna uma Lista para popular um Select
	 * 
	 * @return	List<Curso> - Todos cursos
	 */
	public List<Curso> getCursos(){
		try {
			return cursoServico.listarTodos();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return new ArrayList<Curso>();
	}
	
	/**
	 * Este método faz uma busca de todos os niveis no banco e retorna uma Lista para popular um Select
	 * 
	 * @return	List<Nivel> - Todos niveis
	 */
	public List<Nivel> getNiveis(){
		try {
			return nivelServico.listarTodos();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return new ArrayList<Nivel>();
	}
	
	/**
	 * Este método faz uma busca de todos os turnos no banco e retorna uma Lista para popular um Select
	 * 
	 * @return	List<Turno> - Todos turnos
	 */
	public List<Turno> getTurnos(){
		try {
			return turnoServico.listarTodos();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return new ArrayList<Turno>();
	}
	
	/**
	 * Este método redireciona o sistema para o formulário de emissão de relatório.
	 * 
	 * @return String
	 */
	public String entrar() {
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método limpa os campos do formulário.
	 * 
	 * @return String - Página Atual
	 */
	public String limpar() {
		try {
			curso = null;
			nivel = null;
			turno = null;
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método redireciona o sistema para o index do Administrador.
	 * 
	 * @return String - Index Adm
	 */
	public String cancelar() {
		return "/paginas/adm/indexAdm.jsf?faces-redirect=true";
	}

	/**
	 * @return RelatorioUsuariosControlador
	 */
	public static RelatorioMatrizesCurricularesControlador getInstancia() {
		return (RelatorioMatrizesCurricularesControlador) SpringContainer.getInstancia().getBean("relatorioMatrizesCurricularesControlador");
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Nivel getNivel() {
		return nivel;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}

	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

	/**
	 * Este método recebe uma exceção como parâmentro, exibe sua mensagem na tela e o stacktrace no console.
	 * 
	 * @param Exception e
	 */
	@SuppressWarnings("unused")
	private void setarMensagemErro(Exception e) {
		e.printStackTrace();
		setarMensagemErro(e.getMessage());
	}

	private void setarMensagemErro(String message) {
		FacesContext.getCurrentInstance().addMessage("ERROR", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""));
	}
	
	@SuppressWarnings("unused")
	private void setarMensagemInfo(String message) {
		FacesContext.getCurrentInstance().addMessage("INFO", new FacesMessage(FacesMessage.SEVERITY_INFO, message, ""));
	}

	@SuppressWarnings("unused")
	private void setarMensagemAviso(String message) {
		FacesContext.getCurrentInstance().addMessage("WARN", new FacesMessage(FacesMessage.SEVERITY_WARN, message, ""));
	}
	
	private void exibirRelatorioSessao(byte[] documento){
		 HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		 request.getSession().setAttribute(Relatorio.SESSAO, documento);
	}

	@SuppressWarnings("unused")
	private void exibirRelatorio(byte[] documento) {
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		try {
			response.getOutputStream().write(documento);

			String nomePagina = FacesContext.getCurrentInstance().getViewRoot().getViewId();

			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename=\"" + nomePagina + ".pdf\"");

			response.getOutputStream().flush();
			response.getOutputStream().close();

			FacesContext.getCurrentInstance().responseComplete();
		} catch (IOException e) {
			setarMensagemErro(e.getMessage());
		}
	}
}
