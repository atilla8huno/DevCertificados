package br.com.devschool.turma;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.pessoa.servico.PessoaServico;
import br.com.devschool.turma.servico.TurmaServico;
import br.com.devschool.entidade.Pessoa;
import br.com.devschool.entidade.Relatorio;
import br.com.devschool.enumeradores.EnumFiltroProfessor;
import br.com.devschool.enumeradores.EnumStatusTurma;
import br.com.devschool.util.GeradorRelatorio;
import br.com.devschool.util.IRelatorioControlador;
import br.com.devschool.util.SpringContainer;

/**
 * @author	ATILLA
 * @date	13/09/2012
 */
@Component("relatorioTurmasControlador")
@Scope("request")
public class RelatorioTurmasControlador implements IRelatorioControlador {

	private static String PAGINA_CORRENTE = "/paginas/adm/relatorioTurmas.jsf?faces-redirect=true";
	
	private Pessoa professor;
	private EnumStatusTurma status;
	private Date dataRealInicio, dataRealFim;
	private List<Pessoa> listaDeProfessores;
	private List<EnumStatusTurma> listaStatus;

	@Autowired
	private TurmaServico servico;
	@Autowired
	private PessoaServico pessoaServico;
	@Autowired
	private GeradorRelatorio geradorRelatorio;

	/**
	 * Este método prepara os filtros e parâmetros e emite o relatório de turmas.
	 * 
	 * @return String
	 */
	@Override
	public String emitirRelatorio() {
		try {
			exibirRelatorioSessao(servico.gerarRelatorioTurmas(dataRealInicio, dataRealFim, professor, status));
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método analisa o parâmetro recebido da página e pesquisa entre a lista de professores ativos
	 * retornando uma List com sugestões de professores (AutoComplete)
	 * 
	 * @param 	String query
	 * @return	List<Pessoa> - Sugestões de Professores
	 */
	public List<Pessoa> completeProfessores(String query) {
        List<Pessoa> sugestoes = new ArrayList<Pessoa>();
        
        for(Pessoa p : getListaDeProfessores()) {
        	if(p.getNome().startsWith(query))
        		sugestoes.add(p);
        }
        
        return sugestoes;
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
			status			= null;
			professor 		= null;
			dataRealFim 	= null;
			dataRealInicio 	= null;
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
	public static RelatorioTurmasControlador getInstancia() {
		return (RelatorioTurmasControlador) SpringContainer.getInstancia().getBean("relatorioTurmasControlador");
	}

	public Pessoa getProfessor() {
		return professor;
	}

	public void setProfessor(Pessoa professor) {
		this.professor = professor;
	}

	public EnumStatusTurma getStatus() {
		return status;
	}

	public void setStatus(EnumStatusTurma status) {
		this.status = status;
	}

	public Date getDataRealInicio() {
		return dataRealInicio;
	}

	public void setDataRealInicio(Date dataRealInicio) {
		this.dataRealInicio = dataRealInicio;
	}

	public Date getDataRealFim() {
		return dataRealFim;
	}

	public void setDataRealFim(Date dataRealFim) {
		this.dataRealFim = dataRealFim;
	}
	
	public List<EnumStatusTurma> getListaStatus() {
		if(listaStatus == null){
			listaStatus = new ArrayList<EnumStatusTurma>();
			for(EnumStatusTurma status : EnumStatusTurma.values()){
				listaStatus.add(status);
			}
		}
		return listaStatus;
	}

	public void setListaStatus(List<EnumStatusTurma> listaStatus) {
		this.listaStatus = listaStatus;
	}
	
	public List<Pessoa> getListaDeProfessores() {
		if (listaDeProfessores == null || listaDeProfessores.isEmpty()) {
			try {
				setListaDeProfessores(pessoaServico.listarProfessores(EnumFiltroProfessor.ATIVO));
			} catch (Exception e) {
				setarMensagemErro(e.getMessage());
			}
		}
		return listaDeProfessores;
	}

	public void setListaDeProfessores(List<Pessoa> listaDeProfessores) {
		this.listaDeProfessores = listaDeProfessores;
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
