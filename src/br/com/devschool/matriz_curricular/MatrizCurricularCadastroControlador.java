package br.com.devschool.matriz_curricular;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.curso.servico.CursoServico;
import br.com.devschool.disciplina.servico.DisciplinaServico;
import br.com.devschool.entidade.Curso;
import br.com.devschool.entidade.Disciplina;
import br.com.devschool.entidade.MatrizCurricular;
import br.com.devschool.entidade.MatrizCurricularDisciplina;
import br.com.devschool.entidade.Nivel;
import br.com.devschool.entidade.Turno;
import br.com.devschool.matriz_curricular.servico.MatrizCurricularServico;
import br.com.devschool.nivel.servico.NivelServico;
import br.com.devschool.turno.servico.TurnoServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.ICadastroControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

/**
 * @author	ATILLA
 * @date	10/09/2012
 * 
 */
@Component("matrizCurricularCadastroControlador")
@Scope("session")
public class MatrizCurricularCadastroControlador extends ControladorGenerico<MatrizCurricular> implements ICadastroControlador{

	private MatrizCurricular entidade;
	private List<Disciplina> disciplinasAtivas;
	private List<MatrizCurricularDisciplina> disciplinasSelecionadas;
	private Disciplina disciplina;
	private MatrizCurricularDisciplina matrizDisciplina;

	@Autowired
	private MatrizCurricularServico servico;
	@Autowired
	private CursoServico cursoServico;
	@Autowired
	private NivelServico nivelServico;
	@Autowired 
	private TurnoServico turnoServico;
	@Autowired
	private DisciplinaServico disciplinaServico;

	/**
	 * Este método obtem a disciplina selecionada na tela e adiciona
	 * da List de disciplinas selecionadas e remove da tabela
	 * 
	 * @author 	ATILLA
	 * @date	10/01/2013
	 */
	public void selecionarDisciplina(){
		try {
			MatrizCurricularDisciplina matrizTemp = new MatrizCurricularDisciplina();
			matrizTemp.setDisciplina(matrizDisciplina.getDisciplina());
			matrizTemp.setCargaHoraria(matrizDisciplina.getCargaHoraria());
			
			disciplinasSelecionadas.add(matrizTemp);
			
			setMatrizDisciplina(new MatrizCurricularDisciplina());
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}
	
	/**
	 * Este método obtem a disciplina removida na tela e adiciona da List
	 * de disciplinas na tabela e remove das disciplinas selecionadas
	 * 
	 * @author 	ATILLA
	 * @date	10/01/2013
	 */
	public void removerDisciplina(){
		try {
			disciplinasAtivas.add(matrizDisciplina.getDisciplina());
			disciplinasSelecionadas.remove(matrizDisciplina);
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
	}
	
	/**
	 * Este método analisa o parâmetro recebido da página
	 * e pesquisa entre a lista de disciplinas ativas
	 * retornando uma List com sugestões de disciplinas (AutoComplete)
	 * 
	 * @author 	ATILLA
	 * @date	10/01/2013
	 * @param 	String
	 * @return	List<Disciplina> - Sugestões de Disciplinas
	 */
	public List<Disciplina> completeDisciplinas(String query) {
        List<Disciplina> sugestoes = new ArrayList<Disciplina>();
        
        for(Disciplina d : disciplinasAtivas) {
        	if(d.getTitulo().startsWith(query))
        		sugestoes.add(d);
        }
        
        return sugestoes;
	}
	
	/**
	 * Este método faz uma busca de todos os cursos no banco
	 * e retorna uma Lista para popular um Select
	 * 
	 * @author 	ATILLA
	 * @date	07/01/2013
	 * @return	List<Curso> - Todos cursos
	 */
	public List<Curso> getCursos(){
		try {
			return cursoServico.listarTodos();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return new ArrayList<Curso>();
	}
	
	/**
	 * Este método faz uma busca de todos os niveis no banco
	 * e retorna uma Lista para popular um Select
	 * 
	 * @author 	ATILLA
	 * @date	07/01/2013
	 * @return	List<Nivel> - Todos niveis
	 */
	public List<Nivel> getNiveis(){
		try {
			return nivelServico.listarTodos();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return new ArrayList<Nivel>();
	}
	
	/**
	 * Este método faz uma busca de todos os turnos no banco
	 * e retorna uma Lista para popular um Select
	 * 
	 * @author 	ATILLA
	 * @date	07/01/2013
	 * @return	List<Turno> - Todos turnos
	 */
	public List<Turno> getTurnos(){
		try {
			return turnoServico.listarTodos();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return new ArrayList<Turno>();
	}
	
	/**
	 * Este método limpa o formulário, preparando para 
	 * realizar um novo cadastro do objeto matrizCurricular
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página Atual
	 */
	@Override
	public String entrar() {
		try {
			limpar();
			return "/paginas/adm/matrizCurricularCadastro.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * Este método preenche o formulário com uma Matriz Curricular, preparando para 
	 * realizar uma alteração
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página Atual
	 */
	public String entrarAlterarMatriz() {
		try {
			disciplinasAtivas 		= disciplinaServico.listarDisciplinasAtivas();
			disciplinasSelecionadas = new ArrayList<MatrizCurricularDisciplina>();
			entidade = servico.procurarPorCodigo(entidade.getId());
			disciplinasSelecionadas.addAll(entidade.getMatrizCurricularDisciplinas());
			
			return "/paginas/adm/matrizCurricularCadastro.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método limpa o formulário instânciando um novo 
	 * objeto para entidade MatrizCurricular.
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página Atual
	 */
	@Override
	public String limpar() {
		try {
			setEntidade(new MatrizCurricular());
			disciplinasAtivas 		= disciplinaServico.listarDisciplinasAtivas();
			disciplinasSelecionadas = new ArrayList<MatrizCurricularDisciplina>();
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método executa o método 'entrar' da listagem, 
	 * redirecionando a página para a listagem de matrizCurriculars.
	 * 
	 * @author 	ATILLA
	 * @date	02/01/2013
	 * @return	String - Página de Listagem
	 */
	@Override
	public String cancelar() {
		try {
			MatrizCurricularListagemControlador.getInstancia().entrar();
			
			return "/paginas/adm/matrizCurricularListagem.jsf?faces-redirect=true";
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
			Set<MatrizCurricularDisciplina> disciplinas = new HashSet<MatrizCurricularDisciplina>();
			disciplinas.addAll(disciplinasSelecionadas);
			entidade.setMatrizCurricularDisciplinas(disciplinas);
			entidade = servico.salvarOuAtualizar(entidade);
			setarMensagemInclusaoSucesso();
			
			MatrizCurricularListagemControlador.getInstancia().entrar();
			return "/paginas/adm/matrizCurricularListagem.jsf?faces-redirect=true";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * @return MatrizCurricularServico
	 */
	@Override
	protected ServicoGenerico<MatrizCurricular> getService() {
		return servico;
	}

	public Disciplina getDisciplina() {
		if (disciplina == null) {
			disciplina = new Disciplina();
		}
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	@Override
	public MatrizCurricular getEntidade() {
		if (entidade == null) {
			entidade = new MatrizCurricular();
		}
		return entidade;
	}

	@Override
	public void setEntidade(MatrizCurricular entidade) {
		this.entidade = entidade;
	}

	public List<Disciplina> getDisciplinasAtivas() {
		if (disciplinasAtivas == null) {
			disciplinasAtivas = new ArrayList<Disciplina>();
		}
		return disciplinasAtivas;
	}

	public List<MatrizCurricularDisciplina> getDisciplinasSelecionadas() {
		if (disciplinasSelecionadas == null) {
			disciplinasSelecionadas = new ArrayList<MatrizCurricularDisciplina>();
		}
		return disciplinasSelecionadas;
	}

	public MatrizCurricularDisciplina getMatrizDisciplina() {
		if (matrizDisciplina == null) {
			matrizDisciplina = new MatrizCurricularDisciplina();
		}
		return matrizDisciplina;
	}

	public void setMatrizDisciplina(MatrizCurricularDisciplina matrizDisciplina) {
		this.matrizDisciplina = matrizDisciplina;
	}

	/**
	 * @return MatrizCurricularCadastroControlador
	 */
	public static MatrizCurricularCadastroControlador getInstancia() {
		return (MatrizCurricularCadastroControlador) SpringContainer.getInstancia().getBean("matrizCurricularCadastroControlador");
	}
}
