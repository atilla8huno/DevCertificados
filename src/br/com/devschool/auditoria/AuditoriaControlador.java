package br.com.devschool.auditoria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.auditoria.servico.AuditoriaServico;
import br.com.devschool.entidade.Auditoria;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.ServicoGenerico;

@Scope("request")
@Component("auditoriaControlador")
public class AuditoriaControlador extends ControladorGenerico<Auditoria> {

	@Autowired
	private AuditoriaServico auditoriaServico;
	
	private Date data;
	private List<Auditoria> listaAuditorias;
	private Auditoria auditoria;
	
	public TimeZone getTimeZone(){
		return TimeZone.getDefault();
	}

	public void pesquisar() {
		try {
			listaAuditorias = auditoriaServico.pesquisar(data);
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}
	
	public Date getData() {
		if(data == null){
			data = new Date();
		}
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	@Override
	public String limpar(){
		try {
			listaAuditorias = null;
			data = null;
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	public List<Auditoria> getListaAuditorias() {
		if(listaAuditorias == null){
			listaAuditorias = new ArrayList<Auditoria>();
		}
		return listaAuditorias;
	}

	public void setListaAuditorias(List<Auditoria> listaAuditorias) {
		this.listaAuditorias = listaAuditorias;
	}

	public AuditoriaServico getAuditoriaServico() {
		if (auditoriaServico == null) {
			auditoriaServico = AuditoriaServico.getInstance();
		}
		return auditoriaServico;
	}

	public void setAuditoriaServico(AuditoriaServico auditoriaServico) {
		this.auditoriaServico = auditoriaServico;
	}

	public Auditoria getAuditoria() {
		if (auditoria == null) {
			auditoria = new Auditoria();
		}
		return auditoria;
	}

	public void setAuditoria(Auditoria auditoria) {
		this.auditoria = auditoria;
	}

	@Override
	protected ServicoGenerico<Auditoria> getService() {
		return getAuditoriaServico();
	}

	@Override
	public Auditoria getEntidade() {
		return getAuditoria();
	}

	@Override
	public void setEntidade(Auditoria entidade) {
		this.auditoria = entidade;
	}

	@Override
	public String entrar() {
		return "/paginas/auditoria.jsf?faces-redirect=true";
	}
}
