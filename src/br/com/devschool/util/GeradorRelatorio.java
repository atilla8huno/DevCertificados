package br.com.devschool.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

import org.springframework.stereotype.Service;

import br.com.devschool.entidade.Relatorio;

/**
 * @author ATILLA
 */
@Service("geradorRelatorio")
public class GeradorRelatorio {

	public static final String RELATORIO_PREFIX = "jasper";
	public static final String CONTEXT_ROOT = "CONTEXT_ROOT";
	public static final String PAGE_INDEX = "PAGE_INDEX";

	public byte[] geraRelatorio(String arquivoJasper, Map<String, Object> parameters) throws RelatorioException {
		try {
			// gera o pdf
			return geraPDF(preencheRelatorio(arquivoJasper, null, parameters, null));
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}

	public byte[] geraRelatorio(String arquivoJasper, Collection<?> collection) throws RelatorioException {
		try {
			// gera o pdf
			return geraPDF(preencheRelatorio(arquivoJasper, collection, null, null));
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}

	public byte[] geraBoletimGeral(String arquivoJasper, Collection<?> collection, Map<String, Object> parameters) throws RelatorioException {
		try {
			// gera o docx
			return geraDocx(preencheRelatorio(arquivoJasper, collection, parameters, null));
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}

	public byte[] geraRelatorio(String arquivoJasper, Collection<?> collection, Map<String, Object> parameters) throws RelatorioException {
		try {
			// gera o pdf
			return geraPDF(preencheRelatorio(arquivoJasper, collection, parameters, null));
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}

	public byte[] geraRelatorio(Relatorio relatorio) throws RelatorioException {
		try {
			// gera o pdf
			return geraPDF(preencheRelatorio(relatorio.getArquivoJasper(), relatorio.getCollection(), relatorio.getParameters(), null));
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}

	public byte[] geraRelatorios(List<Relatorio> relatorios) throws RelatorioException {
		try {
			// gera a lista de JasperPrint a partir da lista de relatorios
			List<JasperPrint> listaJasperPrint = new ArrayList<JasperPrint>();
			Integer pageIndex = 0;
			for (Relatorio relatorio : relatorios) {
				if (listaJasperPrint.size() > 0) {
					pageIndex += listaJasperPrint.get(listaJasperPrint.size() - 1).getPages().size();
				}
				listaJasperPrint.add(preencheRelatorio(relatorio.getArquivoJasper(), relatorio.getCollection(), 
						relatorio.getParameters(), pageIndex));
			}
			// gera o pdf
			return geraPDF(listaJasperPrint);
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}

	private byte[] geraPDF(JasperPrint jasperPrint) throws RelatorioException {
		try {
			// transforma o JasperPrint em pdf e retorna seus bytes[]
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}

	private byte[] geraDocx(JasperPrint jasperPrint) throws RelatorioException {
		try {
			// transforma o JasperPrint em Docx e retorna seus bytes[]
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			JRDocxExporter ex = new JRDocxExporter();
			ex.setParameter(JRExporterParameter.OUTPUT_STREAM, arrayOutputStream);
			ex.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			ex.exportReport();
	
			arrayOutputStream.flush();
			arrayOutputStream.close();
	
			return arrayOutputStream.toByteArray();
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}

	private byte[] geraPDF(List<JasperPrint> listaJasperPrint) throws RelatorioException {
		try {
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			JRPdfExporter ex = new JRPdfExporter();
	
			// transforma a lista de JasperPrint em pdf e preenche o arrayOutputStream
			ex.setParameter(JRExporterParameter.OUTPUT_STREAM, arrayOutputStream);
			ex.setParameter(JRExporterParameter.JASPER_PRINT_LIST, listaJasperPrint);
			ex.exportReport();
	
			arrayOutputStream.flush();
			arrayOutputStream.close();
	
			return arrayOutputStream.toByteArray();
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}

	private JasperPrint preencheRelatorio(String arquivoJasper, Collection<?> collection, Map<String, Object> parameters, Integer pageIndex) throws RelatorioException {
		try {
			// obtem caminho real do arquivo no servidor
			ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String realPath = context.getRealPath("") + File.separator;
			String reportPath = realPath + GeradorRelatorio.RELATORIO_PREFIX + File.separator;
	
			// adiciona o parametro REPORT_DIR usado para imagens e sub_relatorios
			if (parameters == null) {
				parameters = new HashMap<String, Object>();
			}
			parameters.put(GeradorRelatorio.CONTEXT_ROOT, realPath);
			if (pageIndex != null && pageIndex > 0) {
				parameters.put(GeradorRelatorio.PAGE_INDEX, pageIndex);
			}
	
			JRDataSource dataSource = null;
	
			// cria o DataSource caso exista uma collection
			if (collection != null && collection.size() > 0) {
				dataSource = new JRBeanCollectionDataSource(collection);
			}
	
			// gera o jasperPrint de acordo com os dados recebidos
			JasperPrint jasperPrint;
			if (dataSource != null) {
				jasperPrint = JasperFillManager.fillReport(reportPath + arquivoJasper, new HashMap<String, Object>(), dataSource);
			} else {
				jasperPrint = JasperFillManager.fillReport(reportPath + arquivoJasper, parameters);
			}
	
			return jasperPrint;
		} catch (Exception e) {
			throw new RelatorioException(e);
		}
	}
}
