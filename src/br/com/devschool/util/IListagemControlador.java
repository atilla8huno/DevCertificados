package br.com.devschool.util;

public interface IListagemControlador {
	
	/**
	 * Este método leva para tela de cadastro para incluir um objeto.
	 * 
	 * @author WENDEL
	 * @date 02/01/2013
	 * @return String
	 */
	public String novo();

	/**
	 * Este método exclui ou desativa/ativa um objeto.
	 * 
	 * @author WENDEL
	 * @date 02/01/2013
	 * @return String
	 */
	public String excluir();
	
	/**
	 * Este método seleciona um objeto da listagem.
	 * 
	 * @author WENDEL
	 * @date 02/01/2013
	 * @return String
	 */
	public String selecionar();
	
}
