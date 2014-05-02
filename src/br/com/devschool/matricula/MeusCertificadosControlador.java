package br.com.devschool.matricula;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.entidade.AlunoTurma;
import br.com.devschool.entidade.Mensagem;
import br.com.devschool.entidade.Pessoa;
import br.com.devschool.matricula.servico.AlunoTurmaServico;
import br.com.devschool.pessoa.servico.PessoaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.IListagemControlador;
import br.com.devschool.util.ServicoGenerico;

/**
 * @author ATILLA
 */
@Component("meusCertificadosControlador")
@Scope("session")
public class MeusCertificadosControlador extends ControladorGenerico<AlunoTurma> implements IListagemControlador {

	@Autowired
	private AlunoTurmaServico alunoTurmaServico;
	@Autowired
	private PessoaServico pessoaServico;
	
	private AlunoTurma	alunoTurma;
	private Pessoa		aluno;
	private Integer		codigo;
	private List<Pessoa> 	 listaDeAlunos;
	private List<AlunoTurma> listaAlunoTurmas;
	
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
		return "/paginas/aluno/certificados.jsf?faces-redirect=true";
	}
	
	public String entrarComFiltroDeAluno() {
		try {
			limpar();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return "/paginas/adm/certificados.jsf?faces-redirect=true";
	}

	@Override
	public String limpar() {
		try {
			alunoTurma	= new AlunoTurma();
			aluno		= new Pessoa();
			codigo		= null;
			listaAlunoTurmas = new ArrayList<AlunoTurma>();
			
			listaDeAlunos = pessoaServico.listarTodos();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método gera o certificado do aluno e disponibiliza para exibição/download.
	 * 
	 * @author ATILLA
	 */
	public void emitirCertificado(){
		try {
			alunoTurma = alunoTurmaServico.procurarPorCodigo(codigo);
			
			if(alunoTurma != null && !alunoTurma.isTransient()) {
				exibirRelatorioSessao(alunoTurmaServico.gerarCertificadoDoAluno(alunoTurma));
				codigo = 0;
			} else {
				setarMensagemInfo(Mensagem.get("certificadoNaoEncontrado", ""));
			}
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}
	
	/**
	 * Este método gera o certificado do aluno e disponibiliza para exibição/download.
	 * 
	 * @author ATILLA
	 */
	public void emitirCertificadoDoAluno(){
		try {
			alunoTurma = alunoTurmaServico.procurarPorAlunoETurma(aluno, alunoTurma.getTurma());
			
			if(alunoTurma != null && !alunoTurma.isTransient()) {
				exibirRelatorioSessao(alunoTurmaServico.gerarCertificadoDoAluno(alunoTurma));
				codigo = 0;
			} else {
				setarMensagemInfo(Mensagem.get("certificadoNaoEncontrado"));
			}
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}
	
	/**
	 * Este método analisa o parâmetro recebido da página
	 * e pesquisa entre a lista de alunos, retornando uma 
	 * List com sugestões de alunos (AutoComplete)
	 * 
	 * @author 	ATILLA
	 * @date	12/01/2013
	 * @param 	String
	 * @return	List<Pessoa> - Sugestões de Alunos
	 */
	public List<Pessoa> completeAlunos(String query) {
        List<Pessoa> sugestoes = new ArrayList<Pessoa>();
        
        for(Pessoa p : listaDeAlunos) {
        	if(p.getNome().startsWith(query))
        		sugestoes.add(p);
        }
        
        return sugestoes;
	}
	
	/**
	 * Este método atualiza a lista de turmas (AlunoTurma) de acordo com o aluno selecionado.
	 * 
	 * @author	ATILLA
	 * @date	27/03/2013
	 */
	public void atualizaComboTurmas(){
		try {
			listaAlunoTurmas.clear();
			if(aluno != null && !aluno.isTransient()) {
				aluno = pessoaServico.procurarPorCodigo(aluno.getId());
				listaAlunoTurmas.addAll(aluno.getAlunoTurmas());
			}
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}
	
	@Override
	protected ServicoGenerico<AlunoTurma> getService() {
		return alunoTurmaServico;
	}

	@Override
	public AlunoTurma getEntidade() {
		if (alunoTurma == null) {
			alunoTurma = new AlunoTurma();
		}
		return alunoTurma;
	}

	@Override
	public void setEntidade(AlunoTurma entidade) {
		this.alunoTurma = entidade;
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

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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

	public List<Pessoa> getListaDeAlunos() {
		if (listaDeAlunos == null) {
			listaDeAlunos = new ArrayList<Pessoa>();
		}
		return listaDeAlunos;
	}

	public void setListaDeAlunos(List<Pessoa> listaDeAlunos) {
		this.listaDeAlunos = listaDeAlunos;
	}

	public List<AlunoTurma> getListaAlunoTurmas() {
		if (listaAlunoTurmas == null) {
			listaAlunoTurmas = new ArrayList<AlunoTurma>();
		}
		return listaAlunoTurmas;
	}

	public void setListaAlunoTurmas(List<AlunoTurma> listaAlunoTurmas) {
		this.listaAlunoTurmas = listaAlunoTurmas;
	}
}
