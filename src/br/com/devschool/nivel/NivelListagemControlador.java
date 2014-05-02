package br.com.devschool.nivel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.entidade.Nivel;
import br.com.devschool.nivel.servico.NivelServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.IListagemControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

/**
 * @author ATILLA
 * @date 10/09/2012
 */
@Component("nivelListagemControlador")
@Scope("session")
public class NivelListagemControlador extends ControladorGenerico<Nivel> implements IListagemControlador{

	private Nivel entidade;
	private List<Nivel> lista;

	@Autowired
	private NivelServico servico;
	
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
			NivelCadastroControlador.getInstancia().limpar();
			return "/paginas/adm/nivelCadastro.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método exclui o nivel do BD
	 * Obs: Deve-se reavaliar o uso deste método antes de executar
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página de Cadastro
	 */
	@Override
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
	@Override
	public String entrar() {
		try {
			setLista(listarTodos());
			return "/paginas/adm/nivelListagem.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * @return NivelServico
	 */
	@Override
	protected ServicoGenerico<Nivel> getService() {
		return servico;
	}

	public List<Nivel> getLista() {
		if (lista == null) {
			lista = new ArrayList<Nivel>();
		}
		return lista;
	}

	public void setLista(List<Nivel> lista) {
		this.lista = lista;
	}

	/**
	 * @return NivelListagemControlador
	 */
	public static NivelListagemControlador getInstancia() {
		return (NivelListagemControlador) SpringContainer.getInstancia().getBean("nivelListagemControlador");
	}

	@Override
	public void setEntidade(Nivel entidade) {
		this.entidade = entidade;
	}

	@Override
	public Nivel getEntidade() {
		if (entidade == null) {
			entidade = new Nivel();
		}
		return entidade;
	}
}
