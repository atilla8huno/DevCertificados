package br.com.devschool.util;

import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;

import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import br.com.devschool.auditoria.servico.AuditoriaServico;
import br.com.devschool.entidade.Auditoria;
import br.com.devschool.entidade.Mensagem;

@SuppressWarnings("serial")
public class PersistenciaException extends Exception {

	@Autowired
	private AuditoriaServico auditoriaServico;
	
	private String message;
	private Logger log = Logger.getGlobal();

	public PersistenciaException(Throwable t) {
		try {
			if(t instanceof DataAccessException || t instanceof JDBCException) {
				if(t.getCause() instanceof JDBCException){
					String codigoErro = String.valueOf( ((JDBCException)t.getCause()).getErrorCode() );
					this.setMessage( Mensagem.get(codigoErro) );
					auditoriaServico.audit(codigoErro, this.getMessage(), Auditoria.MENSAGEM_PERSISTENCIA, StackTraceUtil.getStackTrace(t));
				} else if (t.getCause() instanceof SQLException){
					String codigoErro = String.valueOf( ((SQLException)t.getCause()).getErrorCode() );
					this.setMessage( Mensagem.get(codigoErro) );
					auditoriaServico.audit(codigoErro, this.getMessage(), Auditoria.MENSAGEM_PERSISTENCIA, StackTraceUtil.getStackTrace(t));
				} else {
					String codigo = String.valueOf(new Date().getTime());
					this.setMessage( Mensagem.get("erro.persistencia", codigo ) );
					auditoriaServico.audit(codigo, t.getLocalizedMessage() != null ? t.getLocalizedMessage() : "erro.persistencia" , Auditoria.MENSAGEM_PERSISTENCIA, StackTraceUtil.getStackTrace(t));
				}
			} else {
				String codigo = String.valueOf(new Date().getTime());
				this.setMessage( Mensagem.get("erro.persistencia", codigo ) );
				auditoriaServico.audit(codigo,  t.getLocalizedMessage() != null ? t.getLocalizedMessage() : "erro.persistencia", Auditoria.MENSAGEM_PERSISTENCIA, StackTraceUtil.getStackTrace(t));
			}
			
			log.warning(this.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PersistenciaException(String mensagem) {
		this.setMessage(mensagem);
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String mensagem) {
		this.message = mensagem;
	}
}
