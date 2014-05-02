package br.com.devschool.matricula;

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

import br.com.devschool.entidade.AlunoTurma;
import br.com.devschool.entidade.MatrizCurricular;
import br.com.devschool.entidade.Pessoa;
import br.com.devschool.entidade.Relatorio;
import br.com.devschool.entidade.Turma;
import br.com.devschool.matricula.servico.AlunoTurmaServico;
import br.com.devschool.matriz_curricular.servico.MatrizCurricularServico;
import br.com.devschool.pessoa.servico.PessoaServico;
import br.com.devschool.util.GeradorRelatorio;
import br.com.devschool.util.IRelatorioControlador;
import br.com.devschool.util.SpringContainer;

/**
 * @author	ATILLA
 * @date	20/06/2013
 */
@Component("relatorioNotasControlador")
@Scope("request")
public class RelatorioNotasControlador implements IRelatorioControlador {

	private static String PAGINA_CORRENTE = "/paginas/adm/relatorioNotas.jsf?faces-redirect=true";
	
	private Pessoa aluno;
	private MatrizCurricular matrizCurricular;
	private Turma turma;
	private List<MatrizCurricular> cursos;
	private List<Turma> turmas;
	private List<Pessoa> alunos;
	
	@Autowired
	private AlunoTurmaServico servico;
	@Autowired
	private PessoaServico pessoaServico;
	@Autowired
	private GeradorRelatorio geradorRelatorio;
	@Autowired
	private MatrizCurricularServico matrizCurricularServico;

	/**
	 * Este método prepara os filtros e parâmetros e emite o relatório de notas.
	 * 
	 * @return String
	 */
	@Override
	public String emitirRelatorio() {
		try {
			exibirRelatorioSessao(servico.gerarRelatorioNotas(aluno, turma));
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
			aluno			= null;
			matrizCurricular= null;
			turma			= null;
			turmas			= null;
			cursos			= null;
			alunos 			= null;
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método atualiza a lista de turmas (AlunoTurma) de acordo com o aluno selecionado.
	 */
	public void atualizaComboTurmas(){
		try {
			getTurmas().clear();
			if(aluno != null && !aluno.isTransient()) {
				aluno = pessoaServico.procurarPorCodigo(aluno.getId());
				
				for (AlunoTurma alunoTurma : aluno.getAlunoTurmas()) {
					turmas.add(alunoTurma.getTurma());
				}
			} else if(matrizCurricular != null && !matrizCurricular.isTransient()) {
				matrizCurricular = matrizCurricularServico.procurarPorCodigo(matrizCurricular.getId());
				turmas.addAll(matrizCurricular.getTurmas());
			}
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}
	
	/**
	 * Este método analisa o parâmetro recebido da página e pesquisa entre a lista de alunos, retornando uma 
	 * List com sugestões de alunos (AutoComplete)
	 * 
	 * @param 	String
	 * @return	List<Pessoa> - Sugestões de Alunos
	 */
	public List<Pessoa> completeAlunos(String query) {
        List<Pessoa> sugestoes = new ArrayList<Pessoa>();
        
        for(Pessoa p : getAlunos()) {
        	if(p.getNome().startsWith(query))
        		sugestoes.add(p);
        }
        return sugestoes;
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
	 * @return RelatorioNotasControlador
	 */
	public static RelatorioNotasControlador getInstancia() {
		return (RelatorioNotasControlador) SpringContainer.getInstancia().getBean("relatorioNotasControlador");
	}

	public Pessoa getAluno() {
		return aluno;
	}

	public void setAluno(Pessoa aluno) {
		this.aluno = aluno;
	}

	public MatrizCurricular getMatrizCurricular() {
		return matrizCurricular;
	}

	public void setMatrizCurricular(MatrizCurricular matrizCurricular) {
		this.matrizCurricular = matrizCurricular;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}
	
	public List<Turma> getTurmas() {
		if (turmas == null) {
			turmas = new ArrayList<Turma>();
		}
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}
	
	public List<MatrizCurricular> getCursos() {
		if (cursos == null || cursos.isEmpty()) {
			try {
				cursos = matrizCurricularServico.listarTodos();
			} catch (Exception e) {
				setarMensagemErro(e.getMessage());
			}
		}
		return cursos;
	}

	public void setCursos(List<MatrizCurricular> cursos) {
		this.cursos = cursos;
	}

	public List<Pessoa> getAlunos() {
		if (alunos == null || alunos.isEmpty()) {
			try {
				alunos = pessoaServico.listarTodos();
			} catch (Exception e) {
				setarMensagemErro(e.getMessage());
			}
		}
		return alunos;
	}

	public void setAlunos(List<Pessoa> alunos) {
		this.alunos = alunos;
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
