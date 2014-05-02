package br.com.devschool.matricula;

import static br.com.devschool.enumeradores.EnumFormaDePagamento.AVISTA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.devschool.controle.LoginControlador;
import br.com.devschool.entidade.AlunoTurma;
import br.com.devschool.entidade.MatrizCurricularDisciplina;
import br.com.devschool.enumeradores.EnumFormaDePagamento;
import br.com.devschool.matricula.servico.AlunoTurmaServico;
import br.com.devschool.matriz_curricular.servico.MatrizCurricularDisciplinaServico;
import br.com.devschool.util.ControladorGenerico;
import br.com.devschool.util.ICadastroControlador;
import br.com.devschool.util.ServicoGenerico;
import br.com.devschool.util.SpringContainer;

@Component("matriculaCadastroControlador")
@Scope("session")
public class MatriculaCadastroControlador extends ControladorGenerico<AlunoTurma> implements ICadastroControlador {

	@Autowired
	private AlunoTurmaServico servico;
	@Autowired
	private MatrizCurricularDisciplinaServico matrizCurricularDisciplinaServico;
	private EnumFormaDePagamento formaDePagamento;
	private Integer qtdePagamento;
	private AlunoTurma entidade;
	private Date dataVencimento;
	
	/**
	 * Este método cancela a efetivação de matrícula
	 * 
	 * @author 	WENDEL NUNES DONIZETE
	 * @date	15/01/2013
	 */
	@Override
	public String cancelar() {
		try{
			entidade = new AlunoTurma();			
			return "/paginas/aluno/detalhesCurso.jsf?faces-redirect=true";
		} catch (Exception e){
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}
	
	/**
	 * @return AlunoTurmaServico
	 */
	@Override
	protected ServicoGenerico<AlunoTurma> getService() {
		return servico;
	}

	@Override
	public AlunoTurma getEntidade() {
		try {
			if (entidade == null) {
				entidade = new AlunoTurma();
			}
			entidade.setPessoa(LoginControlador.getInstancia().getUsuarioSessao());
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return entidade;
	}

	@Override
	public void setEntidade(AlunoTurma entidade) {
		this.entidade = entidade;
	}
	
	/**
	 * Este método efetiva a matrícula do aluno no curso.
	 * 
	 * @author WENDEL
	 * @return String - Listagem de cursos
	 */
	@Override
	public String salvar() {
		try{
			servico.salvar(getEntidade(),getFormaDePagamento(),getQtdePagamento(),getDataVencimento());
			setarMensagemInclusaoSucesso();
			limpar();
			return "/paginas/aluno/listatTurmasTeste.jsf";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	/**
	 * Este método prepara a tela de matrícula, e faz redirecionamento para a mesma
	 * 
	 * @author WENDEL
	 * @return String - Cadastro de Matrícula
	 */
	@Override
	public String entrar() {
		try {
			return "/paginas/aluno/matriculaCadastro.jsf";
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return PAGINA_CORRENTE;
	}

	@Override
	public String limpar() {
		setFormaDePagamento(AVISTA);
		setQtdePagamento(1);
		setEntidade(new AlunoTurma());
		setDataVencimento(new Date());
		return PAGINA_CORRENTE;
	}
	
	/**
	 * @return MatriculaCadastroControlador
	 */
	public static MatriculaCadastroControlador getInstancia() {
		return (MatriculaCadastroControlador) SpringContainer.getInstancia().getBean("matriculaCadastroControlador");
	}

	public AlunoTurmaServico getServico() {
		return servico;
	}

	public void setServico(AlunoTurmaServico servico) {
		this.servico = servico;
	}

	/**
	 * Este método retorna a quantidade de horas do curso
	 * 
	 * @author WENDEL
	 * @return String - Horas do Curso
	 */
	public String totalCargaHoraria(){
		Integer cargaHoraria = 0;
		try {
			for(MatrizCurricularDisciplina m : entidade.getTurma().getMatrizCurricular().getMatrizCurricularDisciplinas()){
				cargaHoraria += m.getCargaHoraria();
			}
		} catch (Exception e) {
			setarMensagemErro(e.getMessage());
		}
		return cargaHoraria.toString()+" hrs.";
	}

	public EnumFormaDePagamento getFormaDePagamento() {
		if(formaDePagamento == null){
			formaDePagamento = EnumFormaDePagamento.AVISTA;
		}
		return formaDePagamento;
	}

	public void setFormaDePagamento(EnumFormaDePagamento formaDePagamento) {
		this.formaDePagamento = formaDePagamento;
	}

	public Integer getQtdePagamento() {
		if(qtdePagamento == null){
			qtdePagamento = new Integer(1);
		}
		return qtdePagamento;
	}

	public void setQtdePagamento(Integer qtdePagamento) {
		this.qtdePagamento = qtdePagamento;
	}
	
	public List<EnumFormaDePagamento> getFormasDePagamentos(){
		return Arrays.asList(EnumFormaDePagamento.values());
	}
	
	public List<Integer> getParcelas(){
		List<Integer> lista = new ArrayList<Integer>();
		for(int i = 0; i<getFormaDePagamento().getQtdeParcelas(); i++){
			lista.add(i+1);
		}
		return lista;
	}

	public Date getDataVencimento() {
		if(dataVencimento == null){
			dataVencimento = new Date();
		}
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
}
