package br.com.devschool.enumeradores;

public enum EnumFormaDePagamento {
	
	AVISTA("À Vista",0),
	CHEQUE("Cheque",2),
	CARTAOCREDITO("Cartão de Crédito",3),
	CARTAODEBITO("Cartão de Débito",0),
	BOLETO("Boleto",3);
	
	private String descricao;
	private int qtdeParcelas;
	
	private EnumFormaDePagamento(String descricao, int qtdeParcelas) {
		this.descricao = descricao;
		this.qtdeParcelas = qtdeParcelas;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public int getQtdeParcelas() {
		return qtdeParcelas;
	}
	
	public void setQtdeParcelas(int qtdeParcelas) {
		this.qtdeParcelas = qtdeParcelas;
	}
}
