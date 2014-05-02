package br.com.devschool.enumeradores;

public enum EnumStatusTurma {
	
	EM_FORMACAO("Em Formação"),
	CONFIRMADA("Confirmada"),
	EM_ANDAMENTO("Em Andamento"),
	CONCLUIDA("Concluida"),
	CANCELADA("Cancelada")
	;
	
	private String descricao;
	
	private EnumStatusTurma(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
