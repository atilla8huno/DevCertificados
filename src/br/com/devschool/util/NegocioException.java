package br.com.devschool.util;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.devschool.auditoria.servico.AuditoriaServico;
import br.com.devschool.entidade.Auditoria;
import br.com.devschool.entidade.Mensagem;

@SuppressWarnings("serial")
public class NegocioException extends Exception {
	
	@Autowired
	private AuditoriaServico auditoriaServico;
	
	private String message;
	private Logger log = Logger.getGlobal();

	public NegocioException(String idMensagem) {
		setMessage( Mensagem.get(idMensagem) );
		writeLog(idMensagem);
	}
	
	public NegocioException(String idMensagem, Object... parametros) {
		setMessage( Mensagem.get(idMensagem, parametros) );
		writeLog(idMensagem);
	}
	
	private void writeLog(String idMensagem) {
		try {
			auditoriaServico.audit(idMensagem, this.getMessage(), Auditoria.MENSAGEM_NEGOCIO, null);
			log.warning(this.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String mensagem) {
		this.message = mensagem;
	}
}
