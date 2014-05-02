/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.devschool.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DataUtil {

	public static final String DATA_HORA_PADRAO = "dd/MM/yyyy HH:mm:ss";
	public static final String DATA_PADRAO = "dd/MM/yyyy";

	public static int getAno(Date data) {
		SimpleDateFormat dt = new SimpleDateFormat("yyyy");
		return Integer.parseInt(dt.format(data));
	}

	public static String formatarData(Date data, String mascara) {

		if (data == null)
			return "";

		SimpleDateFormat dt = new SimpleDateFormat(mascara, new Locale("pt", "BR"));

		return dt.format(data);

	}

	public static String formatarData(Date data) {

		return formatarData(data, DATA_PADRAO);
	}

	public static String formatarDataHora(Date data) {

		return formatarData(data, DATA_HORA_PADRAO);
	}

	public static String formatarDataHoraToDate(Date data) {
		StringBuilder dataFormatada = new StringBuilder();
		dataFormatada.append(" to_date(' ").append(formatarData(data, DATA_HORA_PADRAO)).append("', 'dd/MM/yyyy hh24:mi:ss')");

		return dataFormatada.toString();
	}

	public static String formatarDataToDate(Date data) {
		StringBuilder dataFormatada = new StringBuilder();
		dataFormatada.append(" to_date(' ").append(formatarData(data, DATA_PADRAO)).append("', 'dd/MM/yyyy')");

		return dataFormatada.toString();
	}

	public static Date toDate(String data, String mascara) {
		SimpleDateFormat dt = new SimpleDateFormat(mascara);

		Date dataAux = null;
		try {
			dataAux = dt.parse(data);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}

		return dataAux;

	}

	public static Date toDate(String data) {
		return toDate(data, DATA_PADRAO);
	}

	public static Date toDateHour(String data) {
		return toDate(data, DATA_HORA_PADRAO);
	}

	public static Integer obterDiasEntreDatas(Date dataInicio, Date dataFim) {
		Long dt = (dataFim.getTime() - dataInicio.getTime()) + 3600000;
		Long dias = (dt / 86400000L) + 1;
		return dias.intValue();
	}

	public static Date removerHorario(Date data) {
		Calendar gCal = new GregorianCalendar();
		gCal.setTime(data);

		gCal.set(Calendar.HOUR, 0);
		gCal.set(Calendar.MINUTE, 0);
		gCal.set(Calendar.SECOND, 0);
		gCal.set(Calendar.MILLISECOND, 0);

		return gCal.getTime();
	}

	public static Date adicionarHorario(Date data) {
		Calendar gCal = new GregorianCalendar();
		gCal.setTime(data);
		gCal.set(Calendar.HOUR, 23);
		gCal.set(Calendar.MINUTE, 59);
		gCal.set(Calendar.SECOND, 59);
		gCal.set(Calendar.MILLISECOND, 0);

		return gCal.getTime();
	}

	/**
	 * adicionar ou subtrai um periodo da Data; Use a Interface Calendar para
	 * definir os tipos de Periodo. ex.: Calendar.DAY_OF_MONTH
	 * 
	 * Obs.: para remover basta passar uma quantidade negativa
	 * 
	 * @param data
	 * @param tipoPeriodo
	 * @param quantidade
	 * @return
	 */
	public static Date adicionarPeriodo(Date data, int tipoPeriodo, int quantidade) {
		Calendar gCal = new GregorianCalendar();
		gCal.setTime(data);
		gCal.add(tipoPeriodo, quantidade);

		return gCal.getTime();
	}

	/**
	 * Calcula a diferen�a de duas datas em horas <br>
	 * Importante: Quando realiza a diferen�a em horas entre duas datas, este
	 * m�todo considera os minutos restantes e os converte em fra��o de horas.
	 * 
	 * @param dataInicial
	 * @param dataFinal
	 * @return quantidade de horas existentes entre a dataInicial e dataFinal.
	 */
	public static double diferencaEmHoras(Date dataInicial, Date dataFinal) {
		double result = 0;
		long diferenca = dataFinal.getTime() - dataInicial.getTime();
		long diferencaEmHoras = (diferenca / 1000) / 60 / 60;
		long minutosRestantes = (diferenca / 1000) / 60 % 60;
		double horasRestantes = minutosRestantes / 60d;
		result = diferencaEmHoras + (horasRestantes);

		return result;
	}

	/**
	 * Calcula a diferen�a de duas datas em minutos <br>
	 * Importante: Quando realiza a diferen�a em minutos entre duas datas, este
	 * m�todo considera os segundos restantes e os converte em fra��o de
	 * minutos.
	 * 
	 * @param dataInicial
	 * @param dataFinal
	 * @return quantidade de minutos existentes entre a dataInicial e dataFinal.
	 */
	public static double diferencaEmMinutos(Date dataInicial, Date dataFinal) {
		double result = 0;
		long diferenca = dataFinal.getTime() - dataInicial.getTime();
		double diferencaEmMinutos = (diferenca / 1000) / 60; // resultado �
		// diferen�a
		// entre as
		// datas em
		// minutos
		long segundosRestantes = (diferenca / 1000) % 60; // calcula os segundos
		// restantes
		result = diferencaEmMinutos + (segundosRestantes / 60d); // transforma
		// os
		// segundos
		// restantes
		// em
		// minutos

		return result;
	}

	/**
	 * Calcula a diferen�a de duas datas em dias Importante: Quando realiza a
	 * diferen�a em dias entre duas datas, este m�todo considera as horas
	 * restantes e as converte em fra��o de dias.
	 * 
	 * @param dataInicial
	 * @param dataFinal
	 * @return quantidade de dias existentes entre a dataInicial e dataFinal.
	 */
	public static double diferencaEmDias(Date dataInicial, Date dataFinal) {
		double result = 0;
		long diferenca = dataFinal.getTime() - dataInicial.getTime();
		double diferencaEmDias = (diferenca / 1000) / 60 / 60 / 24; // resultado
		// �
		// diferen�a
		// entre as
		// datas em
		// dias
		long horasRestantes = (diferenca / 1000) / 60 / 60 % 24; // calcula as
		// horas
		// restantes
		result = diferencaEmDias + (horasRestantes / 24d); // transforma as
		// horas restantes
		// em fra��o de dias

		return result;
	}

}
