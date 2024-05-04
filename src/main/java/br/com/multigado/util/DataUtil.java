package br.com.multigado.util;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DataUtil {
	
	public static String pegarDuracao(String data) {
		
		DateTimeFormatter forPattern = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
		
		DateTime dateTime1 = forPattern.parseDateTime(data);
		DateTime dateTime2 = new DateTime();
		
		
		Duration duration = new Duration(dateTime1,dateTime2);
		
		
		Long duracao = (long) (duration.getStandardHours()*60.00);
		
		duracao = duration.getStandardMinutes() - duracao;
		
		Integer valor = Integer.valueOf(String.valueOf(duration.getStandardHours()))/24;
		
		
		Integer horas =  Integer.valueOf(String.valueOf(duration.getStandardHours()))-(24 * valor);
		
		return duration.getStandardDays()+" dias, " + horas.toString() + " horas e "+ duracao+ " minutos.";
	}
	
	
	public static String getDataGravacaoBR(String data) {
		DateTimeFormatter us = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
		
		DateTime dateTime = us.parseDateTime(data);
		
		return dateTime.toString("dd-MM-yyyy HH:mm:ss.SSS");
	}
	
	public static java.util.Date ultimoDiaDoMes(Date data) {
		LocalDateTime localDate = new LocalDateTime(data);
		LocalDateTime fim = localDate.dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(99);
		
		return fim.toDate();
		
	}
}
