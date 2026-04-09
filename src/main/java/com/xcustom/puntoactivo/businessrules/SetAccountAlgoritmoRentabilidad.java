package com.xcustom.puntoactivo.businessrules;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import com.integrador.services.AtributoService;
/**
 * <b>Punto Activo Business Rule SetAccount</b><br/>
 * Depending of account received is the account that the integrator will use.<br/><br/>
 */
@SuppressWarnings("all")
public class SetAccountAlgoritmoRentabilidad {
	/**
	 * The format in attributes type is like this: Fedex3_Ship where the number is the id of trhe carrier.
	 * @param context
	 * @return
	 */
	public Map<String,Object> run(Map<String,Object> context) {
		JSONObject request = (JSONObject) context.get("request");
		AtributoService atributoService = (AtributoService) context.get("atributoService");
		Map<String,String> values = (Map<String,String>) context.get("account");
		String env = (String) context.get("env");
		String app = "Generador";
		if(env.equalsIgnoreCase("QA"))
			app = "GeneradorQA";
		String _url = "http://localhost:8080/"+app+"/Fedex/AlgoritmoRentabilidad?cp_origen="+String.valueOf(request.get("OrigenZipCode"))+"&cp_destino="+String.valueOf(request.get("ZipCode"))+"&peso_total="+String.valueOf(request.get("Weight"))+"&";
		try {
			URL url = new URL(_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200){
				throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
			}
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);
			String output;
			StringBuffer sb = new StringBuffer();
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			conn.disconnect();
			JSONObject conf = new JSONObject(sb.toString());
			context.put("fedex", conf);
		} catch (Exception e) {
			System.out.println("Exception in NetClientGet:- " + e);
		}
		return context;
	}
}
