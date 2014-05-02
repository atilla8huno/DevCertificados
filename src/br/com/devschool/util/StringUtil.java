package br.com.devschool.util;

import java.text.Normalizer;

public class StringUtil {
	
	public static String semAcento(String nome) {
		String descricao = "";
		descricao = Normalizer.normalize(nome, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toUpperCase();

		return descricao;
	}

	public static String entreAspasDuplas(String nome) {
		nome = nome != null ? nome : "";
		return "\"" + nome + "\"";
	}

	public static String corrigirEspacamentoVelocity(String string) {
		
		string = string
			.replaceAll("\n", "")
			.replaceAll("\r", "")
			.replaceAll("\t", "")
			.replaceAll("  ", " ")
			.replaceAll(" ,", ",")
			.replace("\\t", "\t")
			.replace("\\n", "\n");
		
		return string;
	}
	
	public static String formatarCPF(String cpf) {
		
		StringBuilder cpfFormatado = new StringBuilder(cpf);
		cpfFormatado.insert(3, ".");
		cpfFormatado.insert(7, ".");
		cpfFormatado.insert(11, "-");
		
		return cpfFormatado.toString();
		
	}
}
