package br.com.devschool.curso;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.controle.LoginControlador;
import br.com.devschool.entidade.AlunoTurma;
import br.com.devschool.entidade.MatrizCurricular;
import br.com.devschool.matriz_curricular.servico.MatrizCurricularServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.Ferramentas;
import br.com.devschool.util.ServicoGenerico;

@Component("meusCursosControlador")
@Scope("session")
public class MeusCursosControlador extends
		ControladorGenerico<MatrizCurricular> {

	@Autowired
	private MatrizCurricularServico servico;
	private MatrizCurricular entidade;
	private List<MatrizCurricular> listaMatrizes;

	@Override
	protected ServicoGenerico<MatrizCurricular> getService() {
		return servico;
	}

	@Override
	public MatrizCurricular getEntidade() {
		if (entidade == null) {
			entidade = new MatrizCurricular();
		}
		return this.entidade;
	}

	@Override
	public String entrar() {
		try {
			getListaMatrizes().clear();

			for (AlunoTurma alunoTurma : LoginControlador.getInstancia()
					.getUsuarioSessao().getAlunoTurmas()) {

				if (!getListaMatrizes().contains(
						alunoTurma.getTurma().getMatrizCurricular())) {
					MatrizCurricular matrizCurricular = alunoTurma.getTurma()
							.getMatrizCurricular();

					matrizCurricular.getCurso().setMostraImagem(
							Ferramentas.mostraImagem(matrizCurricular
									.getCurso().getImagem(), matrizCurricular
									.getCurso()));

					getListaMatrizes().add(matrizCurricular);
				}
			}

			return "/paginas/aluno/meusCursos.jsf";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	@Override
	public String limpar() {
		return PAGINA_CORRENTE;
	}

	@Override
	public void setEntidade(MatrizCurricular entidade) {
		this.entidade = entidade;
	}

	public List<MatrizCurricular> getListaMatrizes() {
		if (listaMatrizes == null) {
			this.listaMatrizes = new ArrayList<MatrizCurricular>();
		}
		return listaMatrizes;
	}

	public void setListaMatrizes(List<MatrizCurricular> listaMatrizes) {
		this.listaMatrizes = listaMatrizes;
	}
}
