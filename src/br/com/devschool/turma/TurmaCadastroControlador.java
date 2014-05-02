package br.com.devschool.turma;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.entidade.MatrizCurricular;
import br.com.devschool.entidade.Pessoa;
import br.com.devschool.entidade.Turma;
import br.com.devschool.enumeradores.EnumFiltroProfessor;
import br.com.devschool.enumeradores.EnumStatusTurma;
import br.com.devschool.matriz_curricular.servico.MatrizCurricularServico;
import br.com.devschool.pessoa.servico.PessoaServico;
import br.com.devschool.turma.servico.TurmaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.ICadastroControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

/**
 * @author	ATILLA
 * @date	10/09/2012
 * 
 */
@Component("turmaCadastroControlador")
@Scope("session")
public class TurmaCadastroControlador extends ControladorGenerico<Turma> implements ICadastroControlador{

	private Turma entidade;
	private List<Pessoa> listaDeProfessores;

	@Autowired
	private TurmaServico servico;
	@Autowired
	private MatrizCurricularServico matrizCurricularServico;
	@Autowired
	private PessoaServico pessoaServico;
	private List<EnumStatusTurma> listaStatus;
	
	/**
	 * Este método faz uma busca de todos os cursos no banco
	 * e retorna uma Lista para popular um Select
	 * 
	 * @author 	ATILLA
	 * @date	07/01/2013
	 * @return	List<MatrizCurricular> - Todos cursos
	 */
	public List<MatrizCurricular> getMatrizesCurriculares(){
		try {
			return matrizCurricularServico.listarTodos();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return null;
	}

	/**
	 * Este método analisa o parâmetro recebido da página
	 * e pesquisa entre a lista de professores ativos
	 * retornando uma List com sugestões de professores (AutoComplete)
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
	 * Este método limpa o formulário, preparando para 
	 * realizar um novo cadastro do objeto turma
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página Atual
	 */
	@Override
	public String entrar() {
		try {
			limpar();
			return "/paginas/adm/turmaCadastro.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método limpa o formulário instânciando um novo 
	 * objeto para entidade Turma.
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página Atual
	 */
	@Override
	public String limpar() {
		try {
			setEntidade(new Turma());
			setListaDeProfessores(pessoaServico.listarProfessores(EnumFiltroProfessor.ATIVO));
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método executa o método 'entrar' da listagem, 
	 * redirecionando a página para a listagem de turmas.
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página de Listagem
	 */
	@Override
	public String cancelar() {
		try {
			TurmaListagemControlador.getInstancia().entrar();
			return "/paginas/adm/turmaListagem.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método salva o formulário no BD retornando
	 * para página de listagem
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página de Listagem
	 */
	@Override
	public String salvar() {
		try {
			setEntidade(servico.salvarOuAtualizar(getEntidade()));
			TurmaListagemControlador.getInstancia().entrar();
			setarMensagemInclusaoSucesso();
			
			return "/paginas/adm/turmaListagem.jsf";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * @return TurmaServico
	 */
	@Override
	protected ServicoGenerico<Turma> getService() {
		return servico;
	}

	@Override
	public Turma getEntidade() {
		if (entidade == null) {
			entidade = new Turma();
		}
		return entidade;
	}

	@Override
	public void setEntidade(Turma entidade) {
		this.entidade = entidade;
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

	/**
	 * @return TurmaCadastroControlador
	 */
	public static TurmaCadastroControlador getInstancia() {
		return (TurmaCadastroControlador) SpringContainer.getInstancia().getBean("turmaCadastroControlador");
	}

	public List<Pessoa> getListaDeProfessores() {
		if (listaDeProfessores == null) {
			listaDeProfessores = new ArrayList<Pessoa>();
		}
		return listaDeProfessores;
	}

	public void setListaDeProfessores(List<Pessoa> listaDeProfessores) {
		this.listaDeProfessores = listaDeProfessores;
	}
}
