package br.com.devschool.auditoria.servico;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.devschool.controle.LoginControlador;
import br.com.devschool.entidade.Auditoria;
import br.com.devschool.entidade.Pessoa;
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.NegocioException;
import br.com.devschool.util.PersistenciaException;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

@Service
public class AuditoriaServico extends ServicoGenerico<Auditoria> {

	private static Logger log = Logger.getGlobal();

	@Autowired
	private AuditoriaDAO auditoriaDAO;

	/**
	 * Este método pesquisa registros na base de dados inseridos na data recebida por parâmetro.
	 * 
	 * @author 	ATILLA
	 * @param 	Date data
	 * @return 	List<Auditoria>
	 * @throws 	PersistenciaException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<Auditoria> pesquisar(Date data) throws PersistenciaException {
		return auditoriaDAO.pesquisar(data);
	}

	/**
	 * Este método faz uma auditoria persistindo no banco os dados do erro recebidos como parâmetro.
	 * 
	 * @author 	ATILLA
	 * @param 	Date data
	 * @return 	List<Auditoria>
	 * @throws 	PersistenciaException
	 * @throws NegocioException 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void audit(String codigo, String message, String tipoMensagem, String excecao) throws PersistenciaException, NegocioException {
		Pessoa usuario = LoginControlador.getInstancia().getUsuarioSessao();
		String url = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		
		Auditoria auditoria = new Auditoria();
		auditoria.setIdMensagem(codigo);
		auditoria.setData(new Date());
		auditoria.setMensagem(message);
		auditoria.setPessoa(usuario);
		auditoria.setTipoMensagem(tipoMensagem);
		auditoria.setUrlFuncionalidade(url);
		auditoria.setExcecao(excecao);

		StringBuilder auditMessage = new StringBuilder();

		auditMessage.append(usuario.getNome()).append(Auditoria.AUDIT_SEPARATOR);
		auditMessage.append(url).append(Auditoria.AUDIT_SEPARATOR);
		auditMessage.append(codigo).append(Auditoria.AUDIT_SEPARATOR);
		auditMessage.append(message);

		super.salvarOuAtualizar(auditoria);
		log.log(AuditLevel.AUDIT, auditMessage.toString());
	}

	public static AuditoriaServico getInstance() {
		return (AuditoriaServico) SpringContainer.getInstancia().getBean("auditoriaServico");
	}

	@Override
	protected DAOGenerico<Auditoria> getDAO() {
		return auditoriaDAO;
	}

	/**
	 * Este método faz uma validação na entidade de acordo com a regra de negócio.
	 */
	@Override
	protected boolean verificarEntidade(Auditoria entidade) throws NegocioException {
		if(entidade == null)
			throw new NegocioException("negocio.validacao.objetoNulo");
		else
			return true;
	}
}
