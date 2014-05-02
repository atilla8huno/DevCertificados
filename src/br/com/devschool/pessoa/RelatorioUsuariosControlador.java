package br.com.devschool.pessoa;

import java.io.IOException;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.entidade.Relatorio;
import br.com.devschool.pessoa.servico.PessoaServico;
import br.com.devschool.util.GeradorRelatorio;
import br.com.devschool.util.IRelatorioControlador;
import br.com.devschool.util.SpringContainer;

/**
 * @author	ATILLA
 * @date	10/09/2012
 */
@Component("relatorioUsuariosControlador")
@Scope("request")
public class RelatorioUsuariosControlador implements IRelatorioControlador {

	private static String PAGINA_CORRENTE = "/paginas/adm/relatorioUsuarios.jsf?faces-redirect=true";
	
	private Boolean tipoAluno, tipoProfessor, tipoAdm, status;
	private Date dataInicio, dataFim;

	@Autowired
	private PessoaServico servico;
	@Autowired
	private GeradorRelatorio geradorRelatorio;

	/**
	 * Este método prepara os filtros e parâmetros e emite o relatório de usuários.
	 * 
	 * @return String
	 */
	@Override
	public String emitirRelatorio() {
		try {
			exibirRelatorioSessao(servico.gerarRelatorioUsuarios(tipoAluno, tipoProfessor, tipoAdm, status, dataInicio, dataFim));
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
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
			tipoAluno		= null;
			tipoProfessor	= null;
			tipoAdm			= null;
			status			= null;
			dataInicio		= null;
			dataFim			= null;
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
	public static RelatorioUsuariosControlador getInstancia() {
		return (RelatorioUsuariosControlador) SpringContainer.getInstancia().getBean("relatorioUsuariosControlador");
	}

	public boolean isTipoAluno() {
		if (tipoAluno == null) {
			tipoAluno = true;
		}
		return tipoAluno;
	}

	public void setTipoAluno(boolean tipoAluno) {
		this.tipoAluno = tipoAluno;
	}

	public boolean isTipoProfessor() {
		if (tipoProfessor == null) {
			tipoProfessor = true;
		}
		return tipoProfessor;
	}

	public void setTipoProfessor(boolean tipoProfessor) {
		this.tipoProfessor = tipoProfessor;
	}

	public boolean isTipoAdm() {
		if (tipoAdm == null) {
			tipoAdm = true;
		}
		return tipoAdm;
	}

	public void setTipoAdm(boolean tipoAdm) {
		this.tipoAdm = tipoAdm;
	}

	public boolean isStatus() {
		if (status == null) {
			status = true;
		}
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
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
