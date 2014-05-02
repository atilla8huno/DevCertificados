package br.com.devschool.pessoa;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.entidade.Pessoa;
import br.com.devschool.pessoa.servico.PessoaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.Ferramentas;
import br.com.devschool.util.ICadastroControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

/**
 * @author JOSEMAR
 * @date 03/01/2012
 */
@Component("professorCadastroControlador")
@Scope("session")
public class ProfessorCadastroControlador extends ControladorGenerico<Pessoa> implements ICadastroControlador {

	private Pessoa entidade;

	@Autowired
	private PessoaServico servico;

	/**
	 * Este método cancela o cadastro ou edição de professor, e prepara tela de
	 * listagem de professor para voltar a mesma
	 * 
	 * @author JOSEMAR
	 * @date 04/01/2013
	 * @return String - Página Listagem de Professor
	 */
	@Override
	public String cancelar() {
		try {
			return ProfessorListagemControlador.getInstancia().entrar();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método prepara a tela para realizar um novo cadastro
	 * 
	 * @author JOSEMAR
	 * @date 04/01/2013
	 * @return String - Página Cadastro de Professor
	 */
	@Override
	public String entrar() {
		try {
			limpar();
			getEntidade().setTipoProfessor(true);
			getEntidade().setTipoAluno(true);
			
			return "/paginas/adm/professorCadastro.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método limpa tela de cadastro de professor para ficar disponível
	 * para realizar um novo cadastro
	 * 
	 * @author JOSEMAR
	 * @date 04/01/2013
	 * @return String - Página Cadastro de Disciplina
	 */
	@Override
	public String limpar() {
		try {
			setEntidade(new Pessoa());
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método salva ou atualiza uma professor, e prepara a tela de listagem
	 * de professor para voltar a mesma.
	 * 
	 * @author JOSEMAR
	 * @date 05/01/2013
	 * @return String - Página Listagem de Professor
	 */
	@Override
	public String salvar() {
		try {
			if (getEntidade().isTransient() && !getEntidade().getSenha().isEmpty()) {
				getEntidade().setSenha(Ferramentas.md5Critografia(getEntidade().getSenha()));
			}

			setEntidade(getService().salvarOuAtualizar(getEntidade()));
			limpar();
			setarMensagemInclusaoSucesso();
			
			return ProfessorListagemControlador.getInstancia().entrar();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Método utilizado para fazer upload de uma imagem da assinatura digital do professor
	 * 
	 * @author JOSEMAR
	 * @date 13/01/2013
	 */
	public void fileUpload(FileUploadEvent event) {
		try {
			String nomeArquivo = event.getFile().getFileName();
			byte[] conteudo = event.getFile().getContents();
			getEntidade().setAssinaturaDigital(conteudo);
			setarMensagemInfo("Imagem " + nomeArquivo + " foi carregada com sucesso!");
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}

	@Override
	protected ServicoGenerico<Pessoa> getService() {
		return servico;
	}

	@Override
	public Pessoa getEntidade() {
		if (entidade == null) {
			entidade = new Pessoa();
		}
		return entidade;
	}

	@Override
	public void setEntidade(Pessoa entidade) {
		this.entidade = entidade;
	}

	public static ProfessorCadastroControlador getInstancia() {
		return (ProfessorCadastroControlador) SpringContainer.getInstancia().getBean("professorCadastroControlador");
	}
}
