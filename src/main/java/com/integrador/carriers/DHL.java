package com.integrador.carriers;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.integrador.models.GuiaIntegrador;
import com.integrador.repositories.GuiaIntegradorRepository;
import com.integrador.services.AtributoService;
import com.integrador.services.UsuariosService;

public class DHL implements Carrier{

	private Map<String,String> account;
	private com.integrador.xml.services.EstafetaLabelRequest xmlRequest;
	private com.integrador.xml.services.EstafetaLabelResponse xmlResponse;
	private GuiaIntegradorRepository guiaIntegradorRepository;
	private GuiaIntegrador guiaIntegrador;
	private AtributoService atributoService;
	private UsuariosService usuariosService;
	private String businessRuleBeforeSend;
	static final Logger log = LoggerFactory.getLogger(DHL.class);

	public Object performRate(JSONObject request) throws Exception {
		Map<String, String> params = this.getAccount();
		JSONObject response = new JSONObject();
		String dhlUrl = params.get("url");
		String dhlKey = params.get("key");
		String dhlPassword = params.get("password");
		String dhlAccountNumber = params.get("account_number");
		log.info("== DHL.performRate() ==");
		log.info("\tURL: " + dhlUrl);
		log.info("\tAccountNumber: " + dhlAccountNumber);
		try{
			if (request.has("accounts")) {
				for (int i = 0; i < request.getJSONArray("accounts").length(); i++) {
					JSONObject accountObj = request.getJSONArray("accounts").getJSONObject(i);
					if (accountObj.has("typeCode") && "shipper".equalsIgnoreCase(accountObj.getString("typeCode"))) {
						String prevNumber = accountObj.optString("number", "");
						accountObj.put("number", dhlAccountNumber);
						log.info("\tActualizado accountNumber de " + prevNumber + " a " + dhlAccountNumber);
					}
				}
			} else {
				log.warn("\tEl request no contiene el nodo 'accounts'.");
			}
			java.net.URL url = new java.net.URL(dhlUrl);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			String auth = dhlKey + ":" + dhlPassword;
			String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
			conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
			conn.setDoOutput(true);
			String jsonRequest = request.toString();
			try(java.io.OutputStream os = conn.getOutputStream()) {
				byte[] input = jsonRequest.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			int statusCode = conn.getResponseCode();
			log.info("\tHTTP Status: " + statusCode);
			java.io.InputStream is = (statusCode >= 200 && statusCode < 300) ? conn.getInputStream() : conn.getErrorStream();
			StringBuilder responseStrBuilder = new StringBuilder();
			try(java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(is, "utf-8"))) {
				String line;
				while ((line = br.readLine()) != null) {
					responseStrBuilder.append(line.trim());
				}
			}
			String rawResponse = responseStrBuilder.toString();
			try{
				response = new JSONObject(rawResponse);
			}catch (Exception parseEx) {
				log.error("\tError parseando respuesta DHL: " + parseEx.getMessage());
				response.put("rawResponse", rawResponse);
			}
			conn.disconnect();
		}catch(Exception ex) {
			log.error("\tError en performRate: " + ex.getMessage(), ex);
			response.put("error", ex.getMessage());
		}
		return response;
	}
	public Object performShipment(JSONObject request) throws Exception {
		Map<String, String> params = this.getAccount();
		JSONObject response = new JSONObject();
		String dhlUrl = params.get("url");
		String dhlKey = params.get("key");
		String dhlPassword = params.get("password");
		String dhlAccountNumber = params.get("account_number");
		log.info("== DHL.performShipment() ==");
		log.info("\tURL: " + dhlUrl);
		log.info("\tAccountNumber: " + dhlAccountNumber);
		try{
			if (request.has("accounts")) {
				for (int i = 0; i < request.getJSONArray("accounts").length(); i++) {
					JSONObject accountObj = request.getJSONArray("accounts").getJSONObject(i);
					if (accountObj.has("typeCode") && "shipper".equalsIgnoreCase(accountObj.getString("typeCode"))) {
						String prevNumber = accountObj.optString("number", "");
						accountObj.put("number", dhlAccountNumber);
						log.info("\tActualizado accountNumber de " + prevNumber + " a " + dhlAccountNumber);
					}
				}
			} else {
				log.warn("\tEl request no contiene el nodo 'accounts'.");
			}
			java.net.URL url = new java.net.URL(dhlUrl);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			String auth = dhlKey + ":" + dhlPassword;
			String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
			conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
			conn.setDoOutput(true);
			String jsonRequest = request.toString();
			try(java.io.OutputStream os = conn.getOutputStream()) {
				byte[] input = jsonRequest.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			int statusCode = conn.getResponseCode();
			log.info("\tHTTP Status: " + statusCode);
			java.io.InputStream is = (statusCode >= 200 && statusCode < 300) ? conn.getInputStream() : conn.getErrorStream();
			StringBuilder responseStrBuilder = new StringBuilder();
			try(java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(is, "utf-8"))) {
				String line;
				while ((line = br.readLine()) != null) {
					responseStrBuilder.append(line.trim());
				}
			}
			String rawResponse = responseStrBuilder.toString();
			try{
				response = new JSONObject(rawResponse);
			}catch (Exception parseEx) {
				log.error("\tError parseando respuesta DHL: " + parseEx.getMessage());
				response.put("rawResponse", rawResponse);
			}
			conn.disconnect();
		}catch(Exception ex) {
			log.error("\tError en performShipment: " + ex.getMessage(), ex);
			response.put("error", ex.getMessage());
		}
		return response;
	}

	@Override
	public Object executeWebService() {
		return null;
	}

	@Override
	public Object executeRestService() throws Exception {
		return null;
	}

	public Map<String, Object> getContext(){
		Map<String,String> values = this.getAccount();
		GuiaIntegrador guiaIntegrador = this.getGuiaIntegrador();
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = this.getXmlRequest();
		com.integrador.xml.services.EstafetaLabelResponse xmlResponse = this.getXmlResponse();
		GuiaIntegradorRepository guiaIntegradorRepository = this.getGuiaIntegradorRepository();
		AtributoService atributoService = this.getAtributoService();
		UsuariosService usuariosService = this.getUsuariosService();
		Map<String,Object> context = new HashMap<String,Object>();
		context.put("account", values);
		context.put("guiaIntegrador", guiaIntegrador);
		context.put("xmlRequest", xmlRequest);
		context.put("xmlResponse", xmlResponse);
		context.put("guiaIntegradorRepository", guiaIntegradorRepository);
		context.put("atributoService", atributoService);
		context.put("usuariosService", usuariosService);
		return context;
	}

	@SuppressWarnings("unchecked")
	public void setContext(Map<String, Object> context){
		Map<String,String> values = (Map<String,String>) context.get("account");
		this.setAccount(values);
		GuiaIntegrador guiaIntegrador = (GuiaIntegrador) context.get("guiaIntegrador");
		this.setGuiaIntegrador(guiaIntegrador);
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = (com.integrador.xml.services.EstafetaLabelRequest) context.get("xmlRequest");
		this.setXmlRequest(xmlRequest);
		com.integrador.xml.services.EstafetaLabelResponse xmlResponse = (com.integrador.xml.services.EstafetaLabelResponse) context.get("xmlResponse");
		this.setXmlResponse(xmlResponse);
		GuiaIntegradorRepository guiaIntegradorRepository = (GuiaIntegradorRepository) context.get("guiaIntegradorRepository");
		this.setGuiaIntegradorRepository(guiaIntegradorRepository);
		AtributoService atributoService = (AtributoService) context.get("atributoService");
		this.setAtributoService(atributoService);
		UsuariosService usuariosService = (UsuariosService) context.get("usuariosService");
		this.setUsuariosService(usuariosService);
	}

	public Map<String, String> getAccount() {
		return account;
	}

	public void setAccount(Map<String, String> account) {
		this.account = account;
	}

	public com.integrador.xml.services.EstafetaLabelRequest getXmlRequest() {
		return xmlRequest;
	}

	public void setXmlRequest(com.integrador.xml.services.EstafetaLabelRequest xmlRequest) {
		this.xmlRequest = xmlRequest;
	}

	public com.integrador.xml.services.EstafetaLabelResponse getXmlResponse() {
		return xmlResponse;
	}

	public void setXmlResponse(com.integrador.xml.services.EstafetaLabelResponse xmlResponse) {
		this.xmlResponse = xmlResponse;
	}

	public GuiaIntegradorRepository getGuiaIntegradorRepository() {
		return guiaIntegradorRepository;
	}

	public void setGuiaIntegradorRepository(GuiaIntegradorRepository guiaIntegradorRepository) {
		this.guiaIntegradorRepository = guiaIntegradorRepository;
	}

	public GuiaIntegrador getGuiaIntegrador() {
		return guiaIntegrador;
	}

	public void setGuiaIntegrador(GuiaIntegrador guiaIntegrador) {
		this.guiaIntegrador = guiaIntegrador;
	}

	public String getBusinessRuleBeforeSend() {
		return businessRuleBeforeSend;
	}

	public void setBusinessRuleBeforeSend(String businessRuleBeforeSend) {
		this.businessRuleBeforeSend = businessRuleBeforeSend;
	}

	public AtributoService getAtributoService() {
		return atributoService;
	}

	public void setAtributoService(AtributoService atributoService) {
		this.atributoService = atributoService;
	}

	public UsuariosService getUsuariosService() {
		return usuariosService;
	}

	public void setUsuariosService(UsuariosService usuarioService) {
		this.usuariosService = usuarioService;
	}
}