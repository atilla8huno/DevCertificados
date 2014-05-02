package br.com.devschool.entidade;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Properties;

public class Mensagem {
	private static Properties properties = null;
	
	public static String get(String alias, Object... args) {
		if	(alias == null) {
			return Mensagem.get("erro.geral", String.valueOf(new Date().getTime()));
		}

		if	(properties == null) {
			properties = new Properties();

			try {
				properties.load(Mensagem.class.getClassLoader().getResourceAsStream("messages.properties"));
			} catch (IOException e) {
				throw new RuntimeException("Erro na carga do arquivo de mensagens",e);
			}
		}
		
		try {
			String mensagem = properties.getProperty(alias);

			if(mensagem == null) {
					mensagem = Mensagem.get("erro.geral", alias);
			} else {
				if	(args.length > 0) {
					mensagem = MessageFormat.format(mensagem,(Object[]) args);
				}
			}
			return mensagem;
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Erro na formata��o da mensagem",e);			
		}
	}
}
