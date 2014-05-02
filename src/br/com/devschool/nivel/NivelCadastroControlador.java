package br.com.devschool.nivel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import br.com.devschool.entidade.Nivel;
import br.com.devschool.nivel.servico.NivelServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.ICadastroControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

/**
 * @author	ATILLA
 * @date	10/09/2012
 */
@Component("nivelCadastroControlador")
@Scope("session")
public class NivelCadastroControlador extends ControladorGenerico<Nivel> implements ICadastroControlador{

	private Nivel entidade;

	@Autowired
	private NivelServico servico;

	/**
	 * Este método prepara a tela para realizar um novo cadastro
	 * @author 	WENDEL
	 * @date	03/01/2013
	 * @return	String - Página Cadastro de Nivel
	 */
	@Override
	public String entrar() {
		try {
			limpar();
			return "/paginas/adm/nivelCadastro.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método limpa o formulário instânciando um novo 
	 * objeto para entidade Nivel.
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página Atual
	 */
	@Override
	public String limpar() {
		try {
			setEntidade(new Nivel());
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método executa o método 'entrar' da listagem, 
	 * redirecionando a página para a listagem de nivels.
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página de Listagem
	 */
	@Override
	public String cancelar() {
		try {
			NivelListagemControlador.getInstancia().entrar();
			return "/paginas/adm/nivelListagem.jsf?faces-redirect=true";
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
			setEntidade(getService().salvarOuAtualizar(getEntidade()));
			NivelListagemControlador.getInstancia().entrar();
			setarMensagemInclusaoSucesso();
			
			return "/paginas/adm/nivelListagem.jsf";
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

	@Override
	public Nivel getEntidade() {
		if (entidade == null) {
			entidade = new Nivel();
		}
		return entidade;
	}

	@Override
	public void setEntidade(Nivel entidade) {
		this.entidade = entidade;
	}

	/**
	 * @return NivelCadastroControlador
	 */
	public static NivelCadastroControlador getInstancia() {
		return (NivelCadastroControlador) SpringContainer.getInstancia().getBean("nivelCadastroControlador");
	}
}
