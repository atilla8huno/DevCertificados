package br.com.devschool.matriz_curricular;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import br.com.devschool.entidade.MatrizCurricular;
import br.com.devschool.matriz_curricular.servico.MatrizCurricularServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.IListagemControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

/**
 * @author ATILLA
 * @date 10/09/2012
 * 
 */
@Component("matrizCurricularListagemControlador")
@Scope("session")
public class MatrizCurricularListagemControlador extends ControladorGenerico<MatrizCurricular> implements IListagemControlador{

	private MatrizCurricular entidade;
	private List<MatrizCurricular> lista;

	@Autowired
	private MatrizCurricularServico servico;

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
			return MatrizCurricularCadastroControlador.getInstancia().entrar();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método redireciona para página de Cadastro de Matriz
	 * 
	 * @author 	ATILLA
	 * @date	03/04/2013
	 * @return	String - Página de Cadastro
	 */
	public String alterarMatriz() {
		try {
			return MatrizCurricularCadastroControlador.getInstancia().entrarAlterarMatriz();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método exclui o matrizCurricular do BD
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
			return "/paginas/adm/matrizCurricularListagem.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * @return MatrizCurricularServico
	 */
	@Override
	protected ServicoGenerico<MatrizCurricular> getService() {
		return servico;
	}

	public List<MatrizCurricular> getLista() {
		if (lista == null) {
			lista = new ArrayList<MatrizCurricular>();
		}
		return lista;
	}

	public void setLista(List<MatrizCurricular> lista) {
		this.lista = lista;
	}

	/**
	 * @return MatrizCurricularListagemControlador
	 */
	public static MatrizCurricularListagemControlador getInstancia() {
		return (MatrizCurricularListagemControlador) SpringContainer.getInstancia().getBean("matrizCurricularListagemControlador");
	}

	@Override
	public void setEntidade(MatrizCurricular entidade) {
		this.entidade = entidade;
	}

	@Override
	public MatrizCurricular getEntidade() {
		if (entidade == null) {
			entidade = new MatrizCurricular();
		}
		return entidade;
	}
}
