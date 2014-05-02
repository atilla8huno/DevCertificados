package br.com.devschool.enumeradores;

public enum EnumStatusAtivoInativo {

	ATIVO("ATIVO"), INATIVO("INATIVO");
	
	private String value;

	public String getValue() {
		return value;
	}

	private EnumStatusAtivoInativo(String value) {
		this.value = value;
	}
}
