package com.integrador.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
public class Util {

	public static final String LOG_START_PROCESO = "=========================";
	public static final String LOG_END_PROCESO = "===========FIN==========";
	public static final String LN = System.getProperty("line.separator");
	public static final String EMPTY_STRING = "";
	public static final String SPACE = " ";
	public static final String COMMA = ",";
	public static final String SUCCESSFUL = "successful";
	public static final String ERROR = "error";
	public static final String ERRORMSG = "errorMessage";
	public static final String MSG = "message";
	public static final String DEFAULT_FORMAT_DATE = "yyyy-MM-dd";
	public static final String MESSAGE = "message";
	public static final String CODE = "code";
	public static final String TAKE = "take";
	public static final String SKIP	= "skip";
	public static final String MODELS = "models";
	public static final String RESULT = "result";
	public static final String DT_DRAW= "draw";
	public static final String DT_RECORDS_TOTAL = "recordsTotal";
	public static final String DT_RECORDS_FILTERED = "recordsFiltered";
	public static final String DT_ROW_ID = "DT_RowId";
	public static final String DT_DATA = "data";
	public static final String OPTION = "option";

	public static final String PAQUETEXPRESS_FILENAME = "paqueteExpress.pdf";
	public static boolean isIntegerValue(BigDecimal bd) {
		return bd.signum() == 0 || bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0;
	}
	public static String getDiasEntrega(String lunes,String martes, String miercoles, String jueves, String viernes, String sabado, String domingo) {
		StringBuilder sb = new StringBuilder();
		if(lunes!=null && !lunes.trim().equalsIgnoreCase(Util.EMPTY_STRING)){
			sb.append( sb.toString().trim().equalsIgnoreCase(Util.EMPTY_STRING) ? "Lunes" : ", Lunes");
		}
		if(martes!=null && !martes.trim().equalsIgnoreCase(Util.EMPTY_STRING)){
			sb.append( sb.toString().trim().equalsIgnoreCase(Util.EMPTY_STRING) ? "Martes" : ", Martes");
		}
		if(miercoles!=null && !miercoles.trim().equalsIgnoreCase(Util.EMPTY_STRING)){
			sb.append( sb.toString().trim().equalsIgnoreCase(Util.EMPTY_STRING) ? "Miercoles" : ", Miercoles");
		}
		if(jueves!=null && !jueves.trim().equalsIgnoreCase(Util.EMPTY_STRING)){
			sb.append( sb.toString().trim().equalsIgnoreCase(Util.EMPTY_STRING) ? "Jueves" : ", Jueves");
		}
		if(viernes!=null && !viernes.trim().equalsIgnoreCase(Util.EMPTY_STRING)){
			sb.append( sb.toString().trim().equalsIgnoreCase(Util.EMPTY_STRING) ? "Viernes" : ", Viernes");
		}
		if(sabado!=null && !sabado.trim().equalsIgnoreCase(Util.EMPTY_STRING)){
			sb.append( sb.toString().trim().equalsIgnoreCase(Util.EMPTY_STRING) ? "Sabado" : ", Sabado");
		}
		if(domingo!=null && !domingo.trim().equalsIgnoreCase(Util.EMPTY_STRING)){
			sb.append( sb.toString().trim().equalsIgnoreCase(Util.EMPTY_STRING) ? "Domingo" : ", Domingo");
		}
		return sb.toString();
	}
	public static String getCurrentDataTime(){
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(dt);
		return currentTime;
	}
	public static String getCurrentDataTime2(){
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
		String currentTime = sdf.format(dt);
		return currentTime;
	}
	public static String convertDatetoDateTime(String currentDate) throws ParseException{
		SimpleDateFormat parseador = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = parseador.parse(currentDate);
		return formateador.format(date);
	}
	public static String convertDatetoDate(String currentDate) throws ParseException{
		SimpleDateFormat parseador = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
		Date date = parseador.parse(currentDate);
		return formateador.format(date);
	}
	public static String getDay(){
		Calendar today = Calendar.getInstance();
		String[] days = new String[]{"Domingo","Lunes","Martes","Miercoles","Jueves","Viernes","Sabado"};
		return days[today.get(Calendar.DAY_OF_WEEK) - 1];
	}
	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			Integer.parseInt(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	public static JSONObject getMessages(String startWithList) throws FileNotFoundException, IOException {
		JSONObject obj = new JSONObject();
		Properties props = new Properties();
		InputStream input = Util.class.getClassLoader().getResourceAsStream("/messages/messages.properties");
		props.load(input);
		String[] list = startWithList.split(Util.COMMA);
		for(String startWith: list) {
			for (Enumeration<?> e = props.propertyNames(); e.hasMoreElements(); ) {
				String name = (String)e.nextElement();
				String value = props.getProperty(name);
				if (name.startsWith(startWith)) {
					obj.put(name, value);
				}
			}
		}
		return obj;
	}
	public static String getVendedor(String fileName){
		String word = Util.EMPTY_STRING;
		for(int i = 0; i<fileName.length();i++){
			String letter = String.valueOf(fileName.charAt(i));
			if(!Util.isNumeric(letter)){
				word += letter;
			}else {
				break;
			}
		}
		return word;
	}
	public static String getValue(HttpServletRequest request, String param) {
		String value = request.getParameter(param)==null ? Util.EMPTY_STRING : request.getParameter(param).toString().trim();
		return value;
	}
	public static String convertDatetoFormat(String currentDate) throws ParseException{
		SimpleDateFormat parseador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = parseador.parse(currentDate);
		return formateador.format(date);
	}
	public static String getOnlyDigits(String s) {
		Pattern pattern = Pattern.compile("[^0-9]");
		Matcher matcher = pattern.matcher(s);
		String number = matcher.replaceAll("");
		return number;
	}
	public static String getOnlyStrings(String s) {
		Pattern pattern = Pattern.compile("[^a-z A-Z]");
		Matcher matcher = pattern.matcher(s);
		String number = matcher.replaceAll("");
		return number;
	}
	public static String removeAccents(String s) {
		s = Normalizer.normalize(s, Normalizer.Form.NFD);
		s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		s = s.replaceAll("[^a-z,^A-Z,^0-9,^#,^/,^(,^),^.,^;,^:,^*,^\\-,^_,^\\,\\s]", "");
		return s;
	}
	public static com.integrador.xml.services.EstafetaLabelRequest cleanRequest(com.integrador.xml.services.EstafetaLabelRequest xmlRequest) {
		xmlRequest.setOrigenCorporateName(Util.removeAccents(xmlRequest.getOrigenCorporateName()));
		xmlRequest.setOrigenCorporateName(Util.removeAccents(xmlRequest.getOrigenCorporateName()));
		xmlRequest.setOrigenAddress1(Util.removeAccents(xmlRequest.getOrigenAddress1()));
		xmlRequest.setOrigenNeighborhood(Util.removeAccents(xmlRequest.getOrigenNeighborhood()));
		xmlRequest.setOrigenContactName(Util.removeAccents(xmlRequest.getOrigenContactName()));
		xmlRequest.setOrigenState(Util.removeAccents(xmlRequest.getOrigenState()));
		xmlRequest.setOrigenCity(Util.removeAccents(xmlRequest.getOrigenCity()));
		xmlRequest.setOrigenExtNum(Util.removeAccents(xmlRequest.getOrigenExtNum()));
		xmlRequest.setOrigenIntNum(Util.removeAccents(xmlRequest.getOrigenIntNum()));
		xmlRequest.setOrigenAddress2(Util.removeAccents(xmlRequest.getOrigenAddress2()));
		xmlRequest.setOrigenReference(Util.removeAccents(xmlRequest.getOrigenReference()));
		xmlRequest.setCorporateName(Util.removeAccents(xmlRequest.getCorporateName()));
		xmlRequest.setAddress1(Util.removeAccents(xmlRequest.getAddress1()));
		xmlRequest.setNeighborhood(Util.removeAccents(xmlRequest.getNeighborhood()));
		xmlRequest.setContactName(Util.removeAccents(xmlRequest.getContactName()));
		xmlRequest.setState(Util.removeAccents(xmlRequest.getState()));
		xmlRequest.setCity(Util.removeAccents(xmlRequest.getCity()));
		xmlRequest.setExtNum(Util.removeAccents(xmlRequest.getExtNum()));
		xmlRequest.setIntNum(Util.removeAccents(xmlRequest.getIntNum()));
		xmlRequest.setAddress2(Util.removeAccents(xmlRequest.getAddress2()));
		xmlRequest.setReference(Util.removeAccents(xmlRequest.getReference()));
		xmlRequest.setAditionalInfo(Util.removeAccents(xmlRequest.getAditionalInfo()));
		xmlRequest.setContent(Util.removeAccents(xmlRequest.getContent()));
		xmlRequest.setContentDescription(Util.removeAccents(xmlRequest.getContentDescription()));
		return xmlRequest;
	}
	public static com.integrador.xml.fedex.services.FedexLabelRequest cleanRequest(com.integrador.xml.fedex.services.FedexLabelRequest xmlRequest) {
		xmlRequest.setOrigenCorporateName(Util.removeAccents(xmlRequest.getOrigenCorporateName()));
		xmlRequest.setOrigenCorporateName(Util.removeAccents(xmlRequest.getOrigenCorporateName()));
		xmlRequest.setOrigenAddress1(Util.removeAccents(xmlRequest.getOrigenAddress1()));
		xmlRequest.setOrigenNeighborhood(Util.removeAccents(xmlRequest.getOrigenNeighborhood()));
		xmlRequest.setOrigenContactName(Util.removeAccents(xmlRequest.getOrigenContactName()));
		xmlRequest.setOrigenState(Util.removeAccents(xmlRequest.getOrigenState()));
		xmlRequest.setOrigenCity(Util.removeAccents(xmlRequest.getOrigenCity()));
		xmlRequest.setOrigenExtNum(Util.removeAccents(xmlRequest.getOrigenExtNum()));
		xmlRequest.setOrigenIntNum(Util.removeAccents(xmlRequest.getOrigenIntNum()));
		xmlRequest.setOrigenAddress2(Util.removeAccents(xmlRequest.getOrigenAddress2()));
		xmlRequest.setOrigenReference(Util.removeAccents(xmlRequest.getOrigenReference()));
		xmlRequest.setCorporateName(Util.removeAccents(xmlRequest.getCorporateName()));
		xmlRequest.setAddress1(Util.removeAccents(xmlRequest.getAddress1()));
		xmlRequest.setNeighborhood(Util.removeAccents(xmlRequest.getNeighborhood()));
		xmlRequest.setContactName(Util.removeAccents(xmlRequest.getContactName()));
		xmlRequest.setState(Util.removeAccents(xmlRequest.getState()));
		xmlRequest.setCity(Util.removeAccents(xmlRequest.getCity()));
		xmlRequest.setExtNum(Util.removeAccents(xmlRequest.getExtNum()));
		xmlRequest.setIntNum(Util.removeAccents(xmlRequest.getIntNum()));
		xmlRequest.setAddress2(Util.removeAccents(xmlRequest.getAddress2()));
		xmlRequest.setReference(Util.removeAccents(xmlRequest.getReference()));
		xmlRequest.setAditionalInfo(Util.removeAccents(xmlRequest.getAditionalInfo()));
		xmlRequest.setContent(Util.removeAccents(xmlRequest.getContent()));
		xmlRequest.setContentDescription(Util.removeAccents(xmlRequest.getContentDescription()));
		return xmlRequest;
	}
	public static JSONObject cleanRequest(JSONObject request) {
		request.put("OrigenCorporateName", request.has("OrigenCorporateName") ? String.valueOf(request.get("OrigenCorporateName")) : "" );
		request.put("OrigenAddress1", request.has("OrigenAddress1") ? Util.removeAccents(String.valueOf(request.get("OrigenAddress1"))) : "" );
		request.put("OrigenNeighborhood", request.has("OrigenNeighborhood") ? Util.removeAccents(String.valueOf(request.get("OrigenNeighborhood"))) : "" );
		request.put("OrigenContactName", request.has("OrigenContactName") ? Util.removeAccents(String.valueOf(request.get("OrigenContactName"))) : "" );
		request.put("OrigenState", request.has("OrigenState") ? Util.removeAccents(String.valueOf(request.get("OrigenState"))) : "" );
		request.put("OrigenCity", request.has("OrigenCity") ? Util.removeAccents(String.valueOf(request.get("OrigenCity"))) : "" );
		request.put("OrigenExtNum", request.has("OrigenExtNum") ? Util.removeAccents(String.valueOf(request.get("OrigenExtNum"))) : "" );
		request.put("OrigenIntNum", request.has("OrigenIntNum") ? Util.removeAccents(String.valueOf(request.get("OrigenIntNum"))) : "" );
		request.put("OrigenAddress2", request.has("OrigenAddress2") ? Util.removeAccents(String.valueOf(request.get("OrigenAddress2"))) : "" );
		request.put("OrigenReference", request.has("OrigenReference") ? Util.removeAccents(String.valueOf(request.get("OrigenReference"))) : "" );
		request.put("CorporateName", request.has("CorporateName") ? Util.removeAccents(String.valueOf(request.get("CorporateName"))) : "" );
		request.put("Address1", request.has("Address1") ? Util.removeAccents(String.valueOf(request.get("Address1"))) : "" );
		request.put("Neighborhood", request.has("Neighborhood") ? Util.removeAccents(String.valueOf(request.get("Neighborhood"))) : "" );
		request.put("ContactName", request.has("ContactName") ? Util.removeAccents(String.valueOf(request.get("ContactName"))) : "" );
		request.put("State", request.has("State") ? Util.removeAccents(String.valueOf(request.get("State"))) : "" );
		request.put("City", request.has("City") ? Util.removeAccents(String.valueOf(request.get("City"))) : "" );
		request.put("ExtNum", request.has("ExtNum") ? Util.removeAccents(String.valueOf(request.get("ExtNum"))) : "" );
		request.put("IntNum", request.has("IntNum") ? Util.removeAccents(String.valueOf(request.get("IntNum"))) : "" );
		request.put("Address2", request.has("Address2") ? Util.removeAccents(String.valueOf(request.get("Address2"))) : "" );
		request.put("Reference", request.has("Reference") ? Util.removeAccents(String.valueOf(request.get("Reference"))) : "" );
		request.put("AditionalInfo", request.has("AditionalInfo") ? Util.removeAccents(String.valueOf(request.get("AditionalInfo"))) : "" );
		request.put("Content", request.has("Content") ? Util.removeAccents(String.valueOf(request.get("Content"))) : "" );
		request.put("ContentDescription", request.has("ContentDescription") ? Util.removeAccents(String.valueOf(request.get("ContentDescription"))) : "" );
		return request;
	}
	public static boolean isStringNumeric(String cadena) {
		try {
			new BigDecimal(cadena);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	public static boolean isBoolean(String value) {
		return value.matches("(?i)^(true|false)$");
	}
	public static String getCurrentDataTimeMexico() {
		ZoneOffset offset = ZoneOffset.ofHours(-6); // UTC-6 sin DST
		LocalDateTime now = LocalDateTime.now(Clock.system(ZoneOffset.UTC).withZone(offset));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return now.format(formatter);
	}
	public static String getCurrentDataTime(String format){
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
		String currentTime = sdf.format(dt);
		return currentTime;
	}
}

