package br.com.devschool.util;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.devschool.entidade.Mensagem;
import br.com.devschool.entidade.Relatorio;

/**
 * @author ATILLA
 * @date 13/09/2012
 * 
 */
public abstract class ControladorGenerico<T extends EntidadeGeral> {

	public static final String INFO = "INFO";
	public static final String ERROR = "ERROR";
	public static final String WARN = "WARN";
	public static final String FATAL = "FATAL";

	public String AVISO_INCLUSAO;
	public String AVISO_EXCLUSAO;
	public String AVISO_ALTERACAO;
	
	protected abstract ServicoGenerico<T> getService();

	public abstract T getEntidade();
	public abstract void setEntidade(T entidade);

	/**
	 * Este método prepara para entrar na tela do controlador.
	 * 
	 * @author WENDEL
	 * @date 02/01/2013
	 * @return String
	 */
	public abstract String entrar();

	/**
	 * Este método limpa a tela.
	 * 
	 * @author WENDEL
	 * @date 02/01/2013
	 * @return String
	 */
	public abstract String limpar();

	public String PAGINA_CORRENTE = "";

	@SuppressWarnings("unchecked")
	public ControladorGenerico() {
		try {
			setEntidade(((Class<T>) getClasseGenerica(this.getClass())).newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			setarMensagemErro(e.getMessage());
		}
	}

	public static Class<?> getClasseGenerica(Class<?> classe) {
		if (classe == null)
			return null;

		if (classe.getGenericSuperclass() instanceof ParameterizedType)
			return (Class<?>) ((ParameterizedType) classe
					.getGenericSuperclass()).getActualTypeArguments()[0];

		return null;
	}

	/**
	 * Este método salva ou atualiza o objeto na base de dados
	 * 
	 * @author	ATILLA
	 * @date	24/09/2012
	 * @return	Objeto da Entidade
	 */
	public String salvar() {
		try {
			T entidade = getService().salvarOuAtualizar(getEntidade());
			setEntidade(entidade);
			setarMensagemInclusaoSucesso();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método exclui o objeto na base de dados <b>Obs: Cuidado ao usar este método!!</b>
	 * 
	 * @author	ATILLA
	 * @date	24/09/2012
	 * @param	Objeto da Entidade
	 */
	public String excluir() {
		try {
			getService().excluir(getEntidade());
			setarMensagemExclusaoSucesso();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método faz uma pesquisa na base de dados e retorna uma lista de
	 * objetos com os resultados. <b>Obs: Cuidado ao usar este método!!</b>
	 * 
	 * @author	ATILLA
	 * @date	24/09/2012
	 * @return	List
	 */
	public List<T> listarTodos() {
		try {
			return getService().listarTodos();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return new ArrayList<T>();
	}

	/**
	 * Este método procura um registro na base de dados de acordo com o código
	 * passado como argumento.
	 * 
	 * @author	ATILLA
	 * @date	24/09/2012
	 * @return	Objeto da Entidade
	 * @param	Integer - Código a ser pesquisado
	 */
	public T procurarPorCodigo(Integer codigo) {
		try {
			T entidade = getService().procurarPorCodigo(codigo);
			setEntidade(entidade);
			return entidade;
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return null;
	}
	
	/**
	 * Este método recebe uma exceção como parâmentro, exibe sua mensagem na tela e o stacktrace no console.
	 * 
	 * @param Exception e
	 */
	protected void setarMensagemErro(Exception e) {
		e.printStackTrace();
		setarMensagemErro(e.getMessage());
	}
	
	private void addMensagem(Severity severity, String titulo, String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		
		context.getExternalContext().getFlash().setKeepMessages(true);
		
		context.addMessage(null, new FacesMessage(severity, titulo, msg));
	}

	protected void setarMensagemErro(String message) {
		addMensagem(FacesMessage.SEVERITY_ERROR, ERROR, message);
	}
	
	protected void setarMensagemInfo(String message) {
		addMensagem(FacesMessage.SEVERITY_INFO, INFO, message);
	}

	protected void setarMensagemAviso(String message) {
		addMensagem(FacesMessage.SEVERITY_WARN, WARN, message);
	}

	protected void setarMensagemFatal(String message) {
		addMensagem(FacesMessage.SEVERITY_FATAL, FATAL, message);
	}

	protected void setarMensagemErroMapeada(String chave, Object... parametros) {
		setarMensagemErro(Mensagem.get(chave, parametros));
	}

	protected void setarMensagemInfoMapeada(String chave, Object... parametros) {
		setarMensagemInfo(Mensagem.get(chave, parametros));
	}

	protected void setarMensagemAvisoMapeada(String chave, Object... parametros) {
		setarMensagemAviso(Mensagem.get(chave, parametros));
	}

	protected void setarMensagemFatalMapeada(String chave, Object... parametros) {
		setarMensagemFatal(Mensagem.get(chave, parametros));
	}

	protected void setarMensagemErroRelatorio() {
		addMensagem(FacesMessage.SEVERITY_ERROR, ERROR, Mensagem.get("erro.relatorio", new Date().getTime()));
	}

	/**************************************/
	/** Mensagens de Sucesso Padronizadas */
	/**************************************/

	protected void setarMensagemInclusaoSucesso() {
		setarMensagemInfo(Mensagem.get("controle.inclusao.sucesso"));
	}

	protected void setarMensagemAlteracaoSucesso() {
		setarMensagemInfo(Mensagem.get("controle.alteracao.sucesso"));
	}

	protected void setarMensagemExclusaoSucesso() {
		setarMensagemInfo(Mensagem.get("controle.exclusao.sucesso"));
	}

	public void exibirRelatorioSessao(byte[] documento){
		 HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		 request.getSession().setAttribute(Relatorio.SESSAO, documento);
	}

	public void exibirRelatorio(byte[] documento) {
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
