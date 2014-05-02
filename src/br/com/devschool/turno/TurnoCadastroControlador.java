package br.com.devschool.turno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import br.com.devschool.entidade.Turno;
import br.com.devschool.turno.servico.TurnoServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.ICadastroControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

/**
 * @author	ATILLA
 * @date	10/09/2012
 * 
 */
@Component("turnoCadastroControlador")
@Scope("session")
public class TurnoCadastroControlador extends ControladorGenerico<Turno> implements ICadastroControlador{

	private Turno entidade;

	@Autowired
	private TurnoServico servico;
	
	/**
	 * Este método prepara a tela para realizar um
	 * novo cadastro do objeto turno 
	 * @author 	WENDEL
	 * @date	03/01/2013
	 * @return	String - Página de Cadastro de Turno
	 */
	@Override
	public String entrar() {
		try {
			limpar();
			return "/paginas/adm/turnoCadastro.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método limpa o formulário instânciando um novo 
	 * objeto para entidade Turno.
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página Atual
	 */
	@Override
	public String limpar() {
		try {
			setEntidade(new Turno());
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método executa o método 'entrar' da listagem, 
	 * redirecionando a página para a listagem de turnos.
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página de Listagem
	 */
	@Override
	public String cancelar() {
		try {
			TurnoListagemControlador.getInstancia().entrar();
			return "/paginas/adm/turnoListagem.jsf?faces-redirect=true";
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
			TurnoListagemControlador.getInstancia().entrar();
			setarMensagemInclusaoSucesso();
			
			return "/paginas/adm/turnoListagem.jsf";
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

	@Override
	public Turno getEntidade() {
		if (entidade == null) {
			entidade = new Turno();
		}
		return entidade;
	}

	@Override
	public void setEntidade(Turno entidade) {
		this.entidade = entidade;
	}

	/**
	 * @return TurnoCadastroControlador
	 */
	public static TurnoCadastroControlador getInstancia() {
		return (TurnoCadastroControlador) SpringContainer.getInstancia().getBean("turnoCadastroControlador");
	}
}
