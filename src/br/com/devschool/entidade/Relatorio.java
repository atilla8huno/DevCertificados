package br.com.devschool.entidade;

import java.util.Collection;
import java.util.Map;

public class Relatorio {

	public static final String SESSAO = "relatorioSessao";
	private String arquivoJasper;
	private Map<String, Object> parameters;
	private Collection<?> collection;

	public Relatorio(String arquivoJasper, Map<String, Object> parameters) {
		setArquivoJasper(arquivoJasper);
		setParameters(parameters);
	}

	public Relatorio(String arquivoJasper, Collection<?> collection) {
		setArquivoJasper(arquivoJasper);
		setCollection(collection);
	}

	public Relatorio(String arquivoJasper, Collection<?> collection,
			Map<String, Object> parameters) {
		setArquivoJasper(arquivoJasper);
		setCollection(collection);
		setParameters(parameters);
	}

	public String getArquivoJasper() {
		return arquivoJasper;
	}

	public void setArquivoJasper(String arquivoJasper) {
		this.arquivoJasper = arquivoJasper;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public Collection<?> getCollection() {
		return collection;
	}

	public void setCollection(Collection<?> collection) {
		this.collection = collection;
	}
}
