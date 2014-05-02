package br.com.devschool.enumeradores;

public enum EnumStatusAlunoTurma {

	EM_ANDAMENTO("EM ANDAMENTO"), APROVADO("APROVADO"), REPROVADO("REPROVADO");
	
	private String value;

	public String getValue() {
		return value;
	}

	private EnumStatusAlunoTurma(String value) {
		this.value = value;
	}
}
