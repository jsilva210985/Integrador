package com.integrador.restcontroller.estafeta;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.estafeta.restconnector.consultaenvios.ConsultaEnviosConnector;
import org.estafeta.restconnector.oauth.AuthenticatorClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.estafeta.websrvice.consultaenvios.EstafetaConsultaEnvios;
import com.integrador.models.Usuario;
import com.integrador.repositories.TokenRepository;
import com.integrador.repositories.UsuarioRepository;
import com.integrador.services.AtributoService;
import com.integrador.util.AESAlgorithm;
@RestController
@RequestMapping({"/Estafeta"})
public class ConsultaEnviosController{
	@Autowired TokenRepository tokenRepository;
	@Autowired AtributoService atributoService;
	@Autowired UsuarioRepository usuarioRepository;
	static final Logger log = LoggerFactory.getLogger(ConsultaEnviosController.class);
	/**
	 * Handles the Consulta Envios request for Estafeta.
	 * @param request The HTTP request object.
	 * @param content The request body as a JSON string.
	 * @return ResponseEntity containing a JSON string with either the validation error or the response from the Consulta Envios service.
	 * @throws Exception If an error occurs during the processing of the request.
	 */
	@PostMapping(value = "/ConsultaEnvios")
	public ResponseEntity<String> consultaEnvios(HttpServletRequest request, @RequestBody String content) throws Exception{
		JSONObject response = new JSONObject();
		JSONObject consultaRequest = new JSONObject();
		String error = "";
		log.info("");
		log.info("== Estafeta RestService / Consulta Envios ==>");
		try {
			consultaRequest = new JSONObject(content);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		if(!consultaRequest.has("cliente")) {
			error = "Debe especificar el cliente";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!consultaRequest.has("password")) {
			error = "Debe especificar la contraseña";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!consultaRequest.has("token")) {
			error = "Debe especificar el token";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		AESAlgorithm e = new AESAlgorithm();
		Usuario usuario = usuarioRepository.findByUsuarioAndContrasena(String.valueOf(consultaRequest.get("cliente")), e.encrypt(String.valueOf(consultaRequest.get("password"))));
		if(usuario==null) {
			error = "Usuario o Contraseña invalido";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		com.integrador.models.Token token = tokenRepository.findByIdUsuarioAndToken(usuario.getIdUsuario(), String.valueOf(consultaRequest.get("token")));
		if(token==null) {
			error = "Token invalido";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		consultaRequest.remove("cliente");
		consultaRequest.remove("password");
		consultaRequest.remove("token");
		Map<String,String> params = atributoService.getByTipoInMap("Estafeta_Consulta_Envios_Rest");
		String _url = params.get("url");
		String _idUsuario = params.get("id");
		String _usuario =  params.get("usuario");
		String _contrasena = params.get("contrasena");
		String _apiKey = params.get("api_key");
		String _apiSecret = params.get("api_secret");
		String _urlToken = params.get("url_token");
		AuthenticatorClient auth = new AuthenticatorClient();
		auth.setAuthURL(_urlToken);
		auth.setClientId(_apiKey);
		auth.setClientSecret(_apiSecret);
		org.estafeta.restconnector.models.Token _token = auth.getToken();
		ConsultaEnviosConnector conector = new ConsultaEnviosConnector(_url, _apiKey, _token.getToken());
		conector.setSuscriberId(_idUsuario);
		conector.setLogin(_usuario);
		conector.setPassword(_contrasena);
		String waybillType = consultaRequest.getJSONObject("searchType").getJSONObject("waybillList").getString("waybillType");
		String initialWaybill = consultaRequest.getJSONObject("searchType").getJSONObject("waybillRange").getString("initialWaybill");
		String finalWaybill = consultaRequest.getJSONObject("searchType").getJSONObject("waybillRange").getString("finalWaybill");
		String type = consultaRequest.getJSONObject("searchType").getString("type");
		JSONArray _guias = consultaRequest.getJSONObject("searchType").getJSONObject("waybillList").getJSONObject("waybills").getJSONArray("string");
		String[] guias = new String[_guias.length()];
		for(int i=0;i<_guias.length();i++){
			Object objeto = _guias.get(i);
			if(objeto instanceof String){
				guias[i] = objeto.toString();
			}
		}
		boolean filterInformation = consultaRequest.getJSONObject("searchConfiguration").getJSONObject("filterType").getBoolean("filterInformation");
		String filterType = consultaRequest.getJSONObject("searchConfiguration").getJSONObject("filterType").getString("filterType");
		boolean includeHistory = consultaRequest.getJSONObject("searchConfiguration").getJSONObject("historyConfiguration").getBoolean("includeHistory");
		String historyType = consultaRequest.getJSONObject("searchConfiguration").getJSONObject("historyConfiguration").getString("historyType");
		boolean includeCustomerInfo = consultaRequest.getJSONObject("searchConfiguration").getBoolean("includeCustomerInfo");
		boolean includeDimensions = consultaRequest.getJSONObject("searchConfiguration").getBoolean("includeDimensions");
		boolean includeInternationalData = consultaRequest.getJSONObject("searchConfiguration").getBoolean("includeInternationalData");
		boolean includeMultipleServiceData = consultaRequest.getJSONObject("searchConfiguration").getBoolean("includeMultipleServiceData");
		boolean includeReturnDocumentData = consultaRequest.getJSONObject("searchConfiguration").getBoolean("includeReturnDocumentData");
		boolean includeSignature = consultaRequest.getJSONObject("searchConfiguration").getBoolean("includeSignature");
		boolean includeWaybillReplaceData = consultaRequest.getJSONObject("searchConfiguration").getBoolean("includeWaybillReplaceData");
		conector.setWaybillType(waybillType);
		conector.setInitialWaybill(initialWaybill);
		conector.setFinalWaybill(finalWaybill);
		conector.setWaybills(guias);
		conector.setType(type);
		conector.setFilterInformation(filterInformation);
		conector.setFilterType(filterType);
		conector.setHistoryType(historyType);
		conector.setIncludeHistory(includeHistory);
		conector.setIncludeCustomerInfo(includeCustomerInfo);
		conector.setIncludeDimensions(includeDimensions);
		conector.setIncludeInternationalData(includeInternationalData);
		conector.setIncludeMultipleServiceData(includeMultipleServiceData);
		conector.setIncludeReturnDocumentData(includeReturnDocumentData);
		conector.setIncludeSignature(includeSignature);
		conector.setIncludeWaybillReplaceData(includeWaybillReplaceData);
		conector.execute();
		System.out.println("Request: "+conector.getRequest());
		System.out.println("Response: "+conector.getResponse());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(conector.getResponse().toString(),headers, HttpStatus.OK);
	}
	/**
	 * Handles the Consulta Envios V2 request for Estafeta.
	 * @param request The HTTP request object.
	 * @param content The request body as a JSON string.
	 * @return ResponseEntity containing a JSON string with either the validation error or the response from the Consulta Envios service.
	 * @throws Exception If an error occurs during the processing of the request.
	 */
	@PostMapping(value = "/ConsultaEnviosV2")
	public ResponseEntity<String> consultaEnviosV2(HttpServletRequest request, @RequestBody String content) throws Exception{
		JSONObject response = new JSONObject();
		JSONObject consultaRequest = new JSONObject();
		String error = "";
		log.info("");
		log.info("== Estafeta RestService / Consulta Envios ==>");
		try {
			consultaRequest = new JSONObject(content);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		if(!consultaRequest.has("cliente")) {
			error = "Debe especificar el cliente";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!consultaRequest.has("password")) {
			error = "Debe especificar la contraseña";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!consultaRequest.has("token")) {
			error = "Debe especificar el token";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		AESAlgorithm e = new AESAlgorithm();
		Usuario usuario = usuarioRepository.findByUsuarioAndContrasena(String.valueOf(consultaRequest.get("cliente")), e.encrypt(String.valueOf(consultaRequest.get("password"))));
		if(usuario==null) {
			error = "Usuario o Contraseña invalido";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		com.integrador.models.Token token = tokenRepository.findByIdUsuarioAndToken(usuario.getIdUsuario(), String.valueOf(consultaRequest.get("token")));
		if(token==null) {
			error = "Token invalido";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		consultaRequest.remove("cliente");
		consultaRequest.remove("password");
		consultaRequest.remove("token");

		Map<String,String> values = atributoService.getByTipoInMap("Estafeta_Consulta_Envios");

		EstafetaConsultaEnvios c = new EstafetaConsultaEnvios();

		c.setEndPoint(values.get("url").toString());
		c.setSuscriberId(values.get("suscriber_id").toString());
		c.setLogin(values.get("usuario").toString());
		c.setPassword(values.get("password").toString());

		JSONObject searchType = consultaRequest.getJSONObject("searchType");
		JSONObject waybillList = searchType.getJSONObject("waybillList");
		JSONObject waybills = waybillList.getJSONObject("waybills");
		JSONArray guiasArray = waybills.getJSONArray("trackings");

		List<String> guias = new ArrayList<>();
		for (int i = 0; i < guiasArray.length(); i++) {
			guias.add(guiasArray.getString(i));
		}
		String allGuias = "";
		for(String guia : guias) {
			if(allGuias.trim().equalsIgnoreCase("")) {
				allGuias = guia;
			}else{
				allGuias = allGuias+","+guia;
			}
		}

		c.setWaybills(allGuias.split(","));
		c.setWaybillType(String.valueOf(waybillList.getString("waybillType")));
		c.setType( String.valueOf(searchType.get("type")) );
		JSONObject searchConfiguration = consultaRequest.getJSONObject("searchConfiguration");

		c.setIncludeDimensions( String.valueOf(searchConfiguration.get("includeDimensions")) );
		c.setIncludeWaybillReplaceData( String.valueOf(searchConfiguration.get("includeWaybillReplaceData")) );
		c.setIncludeReturnDocumentData( String.valueOf(searchConfiguration.get("includeReturnDocumentData")));
		c.setIncludeMultipleServiceData( String.valueOf(searchConfiguration.get("includeMultipleServiceData")) );
		c.setIncludeInternationalData( String.valueOf(searchConfiguration.get("includeInternationalData")) );
		c.setIncludeSignature( String.valueOf(searchConfiguration.get("includeSignature")) );
		c.setIncludeCustomerInfo( String.valueOf(searchConfiguration.get("includeCustomerInfo")) );
		JSONObject historyConfiguration = searchConfiguration.getJSONObject("historyConfiguration");
		c.setIncludeHistory( String.valueOf(historyConfiguration.get("includeHistory")) );
		c.setHistoryType( String.valueOf(historyConfiguration.get("historyType")) );
		JSONObject filterType = searchConfiguration.getJSONObject("filterType");
		c.setFilterInformation( String.valueOf(filterType.get("filterInformation")) );
		c.setFilterType( String.valueOf(filterType.get("filterType")) );
		c.execute();
		//System.out.println("JSON: "+c.getResponseJSON());
		//System.out.println("XML: "+c.getResponseXML());
		JSONObject responseService = c.getResponseJSON();
		JSONObject executeQueryResponse = responseService.getJSONObject("ExecuteQueryResponse");
		JSONObject result = executeQueryResponse.getJSONObject("ExecuteQueryResult");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(result.toString(),headers, HttpStatus.OK);
	}
}
