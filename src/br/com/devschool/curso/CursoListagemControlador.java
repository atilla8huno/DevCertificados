package br.com.devschool.curso;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.curso.servico.CursoServico;
import br.com.devschool.entidade.Curso;
import br.com.devschool.entidade.MatrizCurricular;
import br.com.devschool.entidade.MatrizCurricularDisciplina;
import br.com.devschool.entidade.Turma;
import br.com.devschool.matriz_curricular.servico.MatrizCurricularDisciplinaServico;
import br.com.devschool.turma.servico.TurmaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.Ferramentas;
import br.com.devschool.util.IListagemControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

/**
 * @author ATILLA
 * @date 10/09/2012
 * 
 */
@Component("cursoListagemControlador")
@Scope("session")
public class CursoListagemControlador extends ControladorGenerico<Curso> implements IListagemControlador {

    private Curso entidade;
    private Turma turma;
    private MatrizCurricular matrizCurricular;
    private List<MatrizCurricularDisciplina> matrizCurricularDisciplina;
    private List<Turma> listaTurma;
    private List<Curso> lista;
    
    @Autowired
    private MatrizCurricularDisciplinaServico matrizCurricularDisciplinaServico;
    @Autowired
    private CursoServico servico;
    @Autowired
    private TurmaServico turmaServico;

    @Override
    public String selecionar() {
    	return PAGINA_CORRENTE;
    }

    @Override
    public String limpar() {
    	return PAGINA_CORRENTE;
    }

    /**
     * Este método limpa o formulário e retorna para página de Cadastro
     * 
     * @author	ATILLA
     * @date	02/01/2013
     * @return	String - Página de Cadastro
     */
    @Override
    public String novo() {
		try {
		    return CursoCadastroControlador.getInstancia().entrar();
		} catch (Exception e) {
		    setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
    }

    /**
     * Este método exclui o curso do BD Obs: Deve-se reavaliar o uso deste
     * método antes de executar
     * 
     * @author ATILLA
     * @date 02/01/2013
     * @return String - Página de Cadastro
     */
    @Override
    public String excluir() {
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
     * Este método pesquisa os registros salvos no banco e retorna para página
     * de Cadastro
     * 
     * @author ATILLA
     * @date 02/01/2013
     * @return String - Página de Cadastro
     */
    @Override
    public String entrar() {
		try {
		    setLista(listarTodos());
		    return "/paginas/adm/cursoListagem.jsf?faces-redirect=true";
		} catch (Exception e) {
		    setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
    }

    /**
     * Metodo utilizado para listar os cursos com imagem
     * 
     * @return
     */
    public String entrarListaCursos() {
    	try {
		    setListaTurma(new ArrayList<Turma>());
		    for (Turma t : turmaServico.listarTodos()) {
		    	t.getMatrizCurricular().getCurso().setMostraImagem(Ferramentas.mostraImagem(t.getMatrizCurricular().getCurso().getImagem(), t.getMatrizCurricular().getCurso()));
		    	getListaTurma().add(t);
		    }
		    
		    return "/paginas/aluno/listaCursos.jsf?faces-redirect=true";
		} catch (Exception e) {
		    setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
    }

    /**
     * Calcula carga horaria total
     * 
     * @return Integer
     */
    public Integer calculaCargaHoraria() {
		Integer total = 0;
		try {
			if (getTurma() != null) {
			    setMatrizCurricularDisciplina(matrizCurricularDisciplinaServico.listarPorMatrizCurricular(getTurma().getMatrizCurricular()));
			    for (MatrizCurricularDisciplina m : getMatrizCurricularDisciplina()) {
			    	total += m.getCargaHoraria();
			    }
			}
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return total;
    }

    public String detalharCurso() {
		try {
			// TODO
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

    public List<Curso> getLista() {
    	if (lista == null) {
    		lista = new ArrayList<Curso>();
    	}
    	return lista;
    }

    public void setLista(List<Curso> lista) {
    	this.lista = lista;
    }

    /**
     * @return CursoListagemControlador
     */
    public static CursoListagemControlador getInstancia() {
    	return (CursoListagemControlador) SpringContainer.getInstancia().getBean("cursoListagemControlador");
    }

    @Override
    public void setEntidade(Curso entidade) {
    	this.entidade = entidade;
    }

    @Override
    public Curso getEntidade() {
    	if (entidade == null) {
    		entidade = new Curso();
    	}
    	return entidade;
    }

    public MatrizCurricular getMatrizCurricular() {
    	if (matrizCurricular == null) {
    		matrizCurricular = new MatrizCurricular();
    	}
    	return matrizCurricular;
    }

    public void setMatrizCurricular(MatrizCurricular matrizCurricular) {
    	this.matrizCurricular = matrizCurricular;
    }

    public Turma getTurma() {
    	if (turma == null) {
    		turma = new Turma();
    	}
    	return turma;
    }

    public void setTurma(Turma turma) {
    	this.turma = turma;
    }

    public List<Turma> getListaTurma() {
    	if (listaTurma == null) {
    		listaTurma = new ArrayList<Turma>();
    	}
    	return listaTurma;
    }

    public void setListaTurma(List<Turma> listaTurma) {
    	this.listaTurma = listaTurma;
    }

    public TurmaServico getTurmaServico() {
    	return turmaServico;
    }

    public void setTurmaServico(TurmaServico turmaServico) {
    	this.turmaServico = turmaServico;
    }

    public List<MatrizCurricularDisciplina> getMatrizCurricularDisciplina() {
    	if (matrizCurricularDisciplina == null) {
    		matrizCurricularDisciplina = new ArrayList<MatrizCurricularDisciplina>();
    	}
        return matrizCurricularDisciplina;
    }

    public void setMatrizCurricularDisciplina(List<MatrizCurricularDisciplina> matrizCurricularDisciplina) {
        this.matrizCurricularDisciplina = matrizCurricularDisciplina;
    }

    public MatrizCurricularDisciplinaServico getMatrizCurricularDisciplinaServico() {
        return matrizCurricularDisciplinaServico;
    }

    public void setMatrizCurricularDisciplinaServico(MatrizCurricularDisciplinaServico matrizCurricularDisciplinaServico) {
        this.matrizCurricularDisciplinaServico = matrizCurricularDisciplinaServico;
    }
}
