package br.com.devschool.curso;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.curso.servico.CursoServico;
import br.com.devschool.entidade.Curso;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.ICadastroControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

/**
 * @author	ATILLA
 * @date	10/09/2012
 */
@Component("cursoCadastroControlador")
@Scope("session")
public class CursoCadastroControlador extends ControladorGenerico<Curso> implements ICadastroControlador{

	private Curso entidade;

	@Autowired
	private CursoServico servico;

	/**
	 * Este método limpa o formulário, preparando para 
	 * realizar um novo cadastro do objeto curso
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página Atual
	 */
	@Override
	public String entrar() {
		try {
			limpar();
			return "/paginas/adm/cursoCadastro.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
     * Método utilizado para fazer upload de uma imagem do curso
     * 
     * @author 	ATILLA
     * @date 	13/01/2013
     * @param	FileUploadEvent - Evento
     */
    public void fileUpload(FileUploadEvent event) {
		try {
		    String nomeArquivo = event.getFile().getFileName();
		    byte[] conteudo = event.getFile().getContents();
		    getEntidade().setImagem(conteudo);
		    setarMensagemInfo("Imagem " + nomeArquivo + " carregada com sucesso!");
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
    }
	
	/**
	 * Este método limpa o formulário instânciando um novo 
	 * objeto para entidade Curso.
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página Atual
	 */
	@Override
	public String limpar() {
		try {
			setEntidade(new Curso());
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método executa o método 'entrar' da listagem, 
	 * redirecionando a página para a listagem de cursos.
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página de Listagem
	 */
	@Override
	public String cancelar() {
		try {
			CursoListagemControlador.getInstancia().entrar();
			return "/paginas/adm/cursoListagem.jsf?faces-redirect=true";
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
			CursoListagemControlador.getInstancia().entrar();
			setarMensagemInclusaoSucesso();
			
			return "/paginas/adm/cursoListagem.jsf";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * @return CursoServico
	 */
	@Override
	protected ServicoGenerico<Curso> getService() {
		return servico;
	}

	@Override
	public Curso getEntidade() {
		if (entidade == null) {
			entidade = new Curso();
		}
		return entidade;
	}

	@Override
	public void setEntidade(Curso entidade) {
		this.entidade = entidade;
	}

	/**
	 * @return CursoCadastroControlador
	 */
	public static CursoCadastroControlador getInstancia() {
		return (CursoCadastroControlador) SpringContainer.getInstancia().getBean("cursoCadastroControlador");
	}
}
