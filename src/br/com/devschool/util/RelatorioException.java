package br.com.devschool.util;

import java.util.Date;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.devschool.auditoria.servico.AuditoriaServico;
import br.com.devschool.entidade.Auditoria;
import br.com.devschool.entidade.Mensagem;

public class RelatorioException extends Exception {

	private static final long serialVersionUID = -3608073370332096493L;
	
	@Autowired
	private AuditoriaServico auditoriaServico;
	
	private Logger log = Logger.getGlobal();
	
	private static String codigo = String.valueOf(new Date().getTime());

	public RelatorioException(Throwable e) {
		super(Mensagem.get("erro.relatorio", codigo), e);
		try {
			auditoriaServico.audit(codigo, e.getLocalizedMessage() != null ? e.getLocalizedMessage() : "erro.relatorio", Auditoria.MENSAGEM_RELATORIO, StackTraceUtil.getStackTrace(e));
			log.warning(this.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
