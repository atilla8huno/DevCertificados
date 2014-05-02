package br.com.devschool.turno;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.entidade.Turno;
import br.com.devschool.turno.servico.TurnoServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.IListagemControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

/**
 * @author ATILLA
 * @date 10/09/2012
 * 
 */
@Component("turnoListagemControlador")
@Scope("session")
public class TurnoListagemControlador extends ControladorGenerico<Turno> implements IListagemControlador{

	private Turno entidade;
	private List<Turno> lista;

	@Autowired
	private TurnoServico servico;
	
	@Override
	public String selecionar() {
		return PAGINA_CORRENTE;
	}

	@Override
	public String limpar() {
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método limpa o formulário e retorna
	 * para página de Cadastro
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página de Cadastro
	 */
	@Override
	public String novo() {
		try {
			TurnoCadastroControlador.getInstancia().entrar();
			return "/paginas/adm/turnoCadastro.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método exclui o turno do BD
	 * Obs: Deve-se reavaliar o uso deste método antes de executar
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página de Cadastro
	 */
	public String excluir(){
		try {
			getService().excluir(getEntidade());
			entrar();
			setarMensagemExclusaoSucesso();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método pesquisa os registros salvos no banco e
	 * retorna para página de Cadastro
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página de Cadastro
	 */
	public String entrar() {
		try {
			setLista(listarTodos());
			return "/paginas/adm/turnoListagem.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * @return TurnoServico
	 */
	@Override
	protected ServicoGenerico<Turno> getService() {
		return servico;
	}

	public List<Turno> getLista() {
		if (lista == null) {
			lista = new ArrayList<Turno>();
		}
		return lista;
	}

	public void setLista(List<Turno> lista) {
		this.lista = lista;
	}

	/**
	 * @return TurnoListagemControlador
	 */
	public static TurnoListagemControlador getInstancia() {
		return (TurnoListagemControlador) SpringContainer.getInstancia().getBean("turnoListagemControlador");
	}

	@Override
	public void setEntidade(Turno entidade) {
		this.entidade = entidade;
	}

	@Override
	public Turno getEntidade() {
		if (entidade == null) {
			entidade = new Turno();
		}
		return entidade;
	}
}
