package br.com.devschool.disciplina;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.disciplina.servico.DisciplinaServico;
import br.com.devschool.entidade.Disciplina;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.ICadastroControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

/**
 * @author ATILLA
 * @date 10/09/2012
 * 
 */
@Component("disciplinaCadastroControlador")
@Scope("session")
public class DisciplinaCadastroControlador extends ControladorGenerico<Disciplina> implements ICadastroControlador {

	private Disciplina entidade;

	@Autowired
	private DisciplinaServico servico;

	/**
	 * Este método cancela o cadastro ou edição de disciplina,
	 * e prepara tela de listagem de disciplina para voltar a mesma
	 * 
	 * @author 	WENDEL
	 * @date	03/01/2013
	 * @return	String - Página Listagem de Disciplina
	 */
	@Override
	public String cancelar() {
		try {
			return DisciplinaListagemControlador.getInstancia().entrar();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método prepara a tela para realizar um novo cadastro
	 * @author 	WENDEL
	 * @date	03/01/2013
	 * @return	String - Página Cadastro de Disciplina
	 */
	@Override
	public String entrar() {
		try {
			limpar();
			return "/paginas/adm/disciplinaCadastro.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método salva ou atualiza uma disciplina, e prepara a tela de 
	 * listagem de disciplina para voltar a mesma.
	 * 
	 * @author 	WENDEL
	 * @date	03/01/2013
	 * @return	String - Página Listagem de Disciplina
	 */
	@Override
	public String salvar(){
		try {
			setEntidade(getService().salvarOuAtualizar(getEntidade()));
			limpar();
			setarMensagemInclusaoSucesso();
			
			return DisciplinaListagemControlador.getInstancia().entrar();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método limpa tela de cadastro de disciplina para ficar disponível
	 * para realizar um novo cadastro
	 * 
	 * @author 	WENDEL
	 * @date	03/01/2013
	 * @return	String - Página Cadastro de Disciplina
	 */
	@Override
	public String limpar() {
		try {
			setEntidade(new Disciplina());
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * @return DisciplinaServico
	 */
	@Override
	protected ServicoGenerico<Disciplina> getService() {
		return servico;
	}

	@Override
	public Disciplina getEntidade() {
		if (entidade == null) {
			entidade = new Disciplina();
		}
		return entidade;
	}

	@Override
	public void setEntidade(Disciplina entidade) {
		this.entidade = entidade;
	}

	/**
	 * @return DisciplinaCadastroControlador
	 */
	public static DisciplinaCadastroControlador getInstancia() {
		return (DisciplinaCadastroControlador) SpringContainer.getInstancia().getBean("disciplinaCadastroControlador");
	}
}
