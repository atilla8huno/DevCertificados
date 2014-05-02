package br.com.devschool.turma;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.entidade.AlunoTurma;
import br.com.devschool.entidade.Pessoa;
import br.com.devschool.entidade.Turma;
import br.com.devschool.matricula.servico.AlunoTurmaServico;
import br.com.devschool.pessoa.servico.PessoaServico;
import br.com.devschool.turma.servico.TurmaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.ICadastroControlador;
import br.com.devschool.util.ServicoGenerico;

@Component("transferenciaTurmaControlador")
@Scope("session")
public class TransferenciaTurmaControlador extends ControladorGenerico<Turma> implements ICadastroControlador {

	@Autowired
	private PessoaServico pessoaServico;
	@Autowired
	private AlunoTurmaServico alunoTurmaServico;
	@Autowired
	private TurmaServico turmaServico;
	
	private List<Pessoa> listaDeAlunos;
	private List<AlunoTurma> listaDeAlunoTurmas;
	private List<Turma> listaDeTurmas;
	private Turma turma;
	private AlunoTurma alunoTurma;
	private Pessoa aluno;
	
	/**
	 * Este método efetua a transferência da turma
	 * 
	 * @author	ATILLA
	 * @date	17/04/2013
	 * @return	String - Página Corrente
	 */
	public String efetuarTransferenciaDeTurma(){
		try {
			alunoTurmaServico.transferirAluno(getTurma(), getAlunoTurma());
			setarMensagemAlteracaoSucesso();
			limpar();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método analisa o parâmetro recebido da página e pesquisa entre a lista de alunos, retornando uma 
	 * List com sugestões de alunos (AutoComplete)
	 * 
	 * @author 	ATILLA
	 * @date	17/04/2013
	 * @param 	String
	 * @return	List<Pessoa> - Sugestões de Alunos
	 */
	public List<Pessoa> completeAlunos(String query) {
        List<Pessoa> sugestoes = new ArrayList<Pessoa>();
        
        for(Pessoa p : getListaDeAlunos()) {
        	if(p.getNome().startsWith(query))
        		sugestoes.add(p);
        }
        
        return sugestoes;
	}
	
	public String selecionar(){
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método retorna o index do Aluno
	 */
	@Override
	public String cancelar() {
		return "/paginas/adm/indexAdm.jsf?faces-redirect=true";
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
	
	@Override
	public String entrar() {
		limpar();
		return "/paginas/adm/transferenciaTurma.jsf?faces-redirect=true";
	}
	
	@Override
	public String limpar() {
		try {
			turma = new Turma();
			aluno = new Pessoa();
			
			listaDeAlunoTurmas = new ArrayList<AlunoTurma>();
			
			listaDeTurmas = turmaServico.listarTurmasEmAberto();
			listaDeAlunos = pessoaServico.listarTodos();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	public List<Pessoa> getListaDeAlunos() {
		if (listaDeAlunos == null) {
			try {
				listaDeAlunos = pessoaServico.listarTodos();
			} catch (Exception e) {
				setarMensagemErro(e.getMessage());
			}
		}
		return listaDeAlunos;
	}

	public void setListaDeAlunos(List<Pessoa> listaDeAlunos) {
		this.listaDeAlunos = listaDeAlunos;
	}

	public Pessoa getAluno() {
		if (aluno == null) {
			aluno = new Pessoa();
		}
		return aluno;
	}

	public void setAluno(Pessoa aluno) {
		this.aluno = aluno;
	}

	@Override
	protected ServicoGenerico<Turma> getService() {
		return turmaServico;
	}

	public List<AlunoTurma> getListaDeAlunoTurmas() {
		if (listaDeAlunoTurmas == null) {
			listaDeAlunoTurmas = new ArrayList<AlunoTurma>();
		}
		return listaDeAlunoTurmas;
	}

	public void setListaDeAlunoTurmas(List<AlunoTurma> listaDeAlunoTurmas) {
		this.listaDeAlunoTurmas = listaDeAlunoTurmas;
	}

	public List<Turma> getListaDeTurmas() {
		if (listaDeTurmas == null) {
			try {
				listaDeTurmas = turmaServico.listarTurmasEmAberto();
			} catch (Exception e) {
				setarMensagemErro(e.getMessage());
			}
		}
		return listaDeTurmas;
	}

	public void setListaDeTurmas(List<Turma> listaDeTurmas) {
		this.listaDeTurmas = listaDeTurmas;
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

	public AlunoTurma getAlunoTurma() {
		if (alunoTurma == null) {
			alunoTurma = new AlunoTurma();
		}
		return alunoTurma;
	}

	public void setAlunoTurma(AlunoTurma alunoTurma) {
		this.alunoTurma = alunoTurma;
	}
}
