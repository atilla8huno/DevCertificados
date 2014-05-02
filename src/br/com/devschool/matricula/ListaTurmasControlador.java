package br.com.devschool.matricula;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.controle.LoginControlador;
import br.com.devschool.entidade.Turma;
import br.com.devschool.matricula.servico.AlunoTurmaServico;
import br.com.devschool.turma.servico.TurmaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.ServicoGenerico;

@Component("listaTurmaTeste")
@Scope("session")
public class ListaTurmasControlador extends ControladorGenerico<Turma> {

	@Autowired
	private TurmaServico servico;
	@Autowired
	private AlunoTurmaServico matriculaServico;

	private Turma entidade;
	private List<Turma> lista;

	@Override
	protected ServicoGenerico<Turma> getService() {
		return servico;
	}

	@Override
	public String entrar() {
		try {
			setEntidade(new Turma());
			setLista(getService().listarTodos());
			
			return "/paginas/aluno/listatTurmasTeste.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	public String selecionar() {
		try {
			MatriculaCadastroControlador.getInstancia().getEntidade().setTurma(getEntidade());
			
			return MatriculaCadastroControlador.getInstancia().entrar();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	@Override
	public Turma getEntidade() {
		if (entidade == null) {
			entidade = new Turma();
		}
		return entidade;
	}

	@Override
	public void setEntidade(Turma entidade) {
		this.entidade = entidade;
	}

	public boolean verificaMatricula(Turma t) {
		try {
			return matriculaServico.verificaMatricula(LoginControlador.getInstancia().getUsuarioSessao(), t);
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return false;
	}

	@Override
	public String limpar() {
		return PAGINA_CORRENTE;
	}

	public TurmaServico getServico() {
		return servico;
	}

	public void setServico(TurmaServico servico) {
		this.servico = servico;
	}

	public List<Turma> getLista() {
		if (lista == null) {
			lista = new ArrayList<Turma>();
		}
		return lista;
	}

	public void setLista(List<Turma> lista) {
		this.lista = lista;
	}
}
