package br.com.devschool.aula;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.aula.servico.AulaServico;
import br.com.devschool.entidade.Aula;
import br.com.devschool.entidade.Turma;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.ICadastroControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

@Component("aulaCadastroControlador")
@Scope("session")
public class AulaCadastroControlador extends ControladorGenerico<Aula> implements ICadastroControlador {

	@Autowired
	private AulaServico servico;

	private Turma turma;
	private Aula entidade;
	private Aula aulaSelecionada;
	private List<Aula> listaAulas;

	@Override
	public String cancelar() {
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método salva uma aula pela turma
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	@Override
	public String salvar() {
		try {
			getEntidade().setTurma(getTurma());
			servico.salvarOuAtualizar(getEntidade());
			limpar();
			setarMensagemInclusaoSucesso();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método retorna uma lista de aulas referente uma turma
	 * 
	 * @author WENDEL
	 * @since 26/01/2013
	 * @throws Exception
	 */
	private void listarAulaPorTurma() {
		try {
			if (!getTurma().isTransient()) {
				setListaAulas(servico.listarAulaPorTurma(getTurma()));
			}
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}

	/**
	 * Este método exclui uma aula na página de cadastro de aula
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	@Override
	public String excluir() {
		try {
			servico.excluir(getAulaSelecionada());
			listarAulaPorTurma();
			setarMensagemExclusaoSucesso();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * @return ServicoGenerico<Aula>
	 */
	@Override
	protected ServicoGenerico<Aula> getService() {
		return servico;
	}

	@Override
	public Aula getEntidade() {
		if (entidade == null) {
			entidade = new Aula();
		}
		return entidade;
	}

	@Override
	public void setEntidade(Aula entidade) {
		this.entidade = entidade;
	}

	/**
	 * Este método entra na página de cadastro de aula
	 * 
	 * @author WENDEL
	 * @return String - Página de cadastro de aula
	 */
	@Override
	public String entrar() {
		try {
			limpar();
			return "/paginas/adm/aulaCadastro.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método limpa a página de cadastro de aula
	 * 
	 * @author WENDEL
	 * @return String - Página corrente
	 */
	@Override
	public String limpar() {
		try {
			setEntidade(new Aula());
			listarAulaPorTurma();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * @return AulaCadastroControlador
	 */
	public static AulaCadastroControlador getInstancia() {
		return (AulaCadastroControlador) SpringContainer.getInstancia().getBean("aulaCadastroControlador");
	}

	public Turma getTurma() {
		if (turma == null){
			turma = new Turma();
		}
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public List<Aula> getListaAulas() {
		if (listaAulas == null) {
			new ArrayList<Aula>();
		}
		return listaAulas;
	}

	public void setListaAulas(List<Aula> listaAulas) {
		this.listaAulas = listaAulas;
	}

	public Aula getAulaSelecionada() {
		if (aulaSelecionada == null) {
			aulaSelecionada = new Aula();
		}
		return aulaSelecionada;
	}

	public void setAulaSelecionada(Aula aulaSelecionada) {
		this.aulaSelecionada = aulaSelecionada;
	}
}
