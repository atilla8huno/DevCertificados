package br.com.devschool.turma;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.entidade.Pessoa;
import br.com.devschool.entidade.Turma;
import br.com.devschool.enumeradores.EnumFiltroProfessor;
import br.com.devschool.pessoa.servico.PessoaServico;
import br.com.devschool.turma.servico.TurmaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.IListagemControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

/**
 * @author ATILLA
 */
@Component("certificadosProfessorControlador")
@Scope("session")
public class CertificadosProfessorControlador extends ControladorGenerico<Turma> implements IListagemControlador {

	@Autowired
	private TurmaServico turmaServico;
	@Autowired
	private PessoaServico pessoaServico;
	
	private Turma turma;
	private Pessoa professor;
	private Integer codigo;
	private List<Pessoa> listaDeProfessores;
	private List<Turma> listaTurmas;
	
	@Override
	public String novo() {
		return PAGINA_CORRENTE;
	}

	@Override
	public String selecionar() {
		return PAGINA_CORRENTE;
	}

	@Override
	public String entrar() {
		try {
			limpar();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return "/paginas/professor/certificadosProfessor.jsf?faces-redirect=true";
	}
	
	public String entrarComFiltroDeProfessor() {
		limpar();
		return "/paginas/adm/certificadosProfessor.jsf?faces-redirect=true";
	}

	@Override
	public String limpar() {
		try {
			turma		= new Turma();
			professor	= new Pessoa();
			codigo		= null;
		
			listaDeProfessores = pessoaServico.listarProfessores(EnumFiltroProfessor.TODOS);
			listaTurmas = new ArrayList<Turma>();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método gera o certificado do professor e disponibiliza para exibição/download.
	 * 
	 * @author ATILLA
	 */
	public void emitirCertificado(){
		try {
			turma = turmaServico.procurarPorCodigo(codigo);
			
			if(turma != null && !turma.isTransient()) {
				exibirRelatorioSessao(turmaServico.gerarCertificadoDoProfessor(turma));
				codigo = 0;
			} else {
				setarMensagemInfo("Ops! Certificado não encontrado.");
			}
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}
	
	/**
	 * Este método gera o certificado do professor e disponibiliza para exibição/download.
	 * 
	 * @author ATILLA
	 */
	public void emitirCertificadoProfessor(){
		try {
			if(turma != null && !turma.isTransient()) {
				turma = turmaServico.procurarPorCodigo(turma.getId());
				exibirRelatorioSessao(turmaServico.gerarCertificadoDoProfessor(turma));
				codigo = 0;
			} else {
				setarMensagemInfo("Ops! Certificado não encontrado.");
			}
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}
	
	/**
	 * Este método analisa o parâmetro recebido da página
	 * e pesquisa entre a lista de professores, retornando uma 
	 * List com sugestões de alunprofessoresos (AutoComplete)
	 * 
	 * @author 	ATILLA
	 * @date	12/01/2013
	 * @param 	String
	 * @return	List<Pessoa> - Sugestões de Professores
	 */
	public List<Pessoa> completeProfessores(String query) {
        List<Pessoa> sugestoes = new ArrayList<Pessoa>();
        
        for(Pessoa p : listaDeProfessores) {
        	if(p.getNome().startsWith(query))
        		sugestoes.add(p);
        }
        
        return sugestoes;
	}
	
	/**
	 * Este método atualiza a lista de turmas de acordo com o professor selecionado.
	 * 
	 * @author	ATILLA
	 * @date	27/03/2013
	 */
	public void atualizaComboTurmas(){
		try {
			listaTurmas.clear();
			if(professor != null && !professor.isTransient()) {
				professor = pessoaServico.procurarPorCodigo(professor.getId());
				listaTurmas.addAll(professor.getTurmas());
			}
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}
	
	@Override
	protected ServicoGenerico<Turma> getService() {
		return turmaServico;
	}

	/**
	 * @return CertificadosDaTurmaControlador
	 */
	public static CertificadosProfessorControlador getInstancia() {
		return (CertificadosProfessorControlador) SpringContainer.getInstancia().getBean("certificadosProfessorControlador");
	}
	
	@Override
	public Turma getEntidade() {
		if (turma == null) {
			turma = new Turma();
		}
		return turma;
	}

	@Override
	public void setEntidade(Turma entidade) {
		this.turma = entidade;
	}

	public Turma getTurma() {
		if (turma == null) {
			turma = new Turma();
		}
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Pessoa getProfessor() {
		if (professor == null) {
			professor = new Pessoa();
		}
		return professor;
	}

	public void setProfessor(Pessoa professor) {
		this.professor = professor;
	}

	public List<Pessoa> getListaDeProfessores() {
		if (listaDeProfessores == null) {
			listaDeProfessores = new ArrayList<Pessoa>();
		}
		return listaDeProfessores;
	}

	public void setListaDeProfessores(List<Pessoa> listaDeProfessores) {
		this.listaDeProfessores= listaDeProfessores;
	}

	public List<Turma> getListaTurmas() {
		if (listaTurmas == null) {
			listaTurmas = new ArrayList<Turma>();
		}
		return listaTurmas;
	}

	public void setListaTurmas(List<Turma> listaTurmas) {
		this.listaTurmas = listaTurmas;
	}
}
