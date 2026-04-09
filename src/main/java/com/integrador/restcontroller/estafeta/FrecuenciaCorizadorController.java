package com.integrador.restcontroller.estafeta;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.estafeta.frecuenciacotizador.FrecuenciaCotizadorRestConnector;
import org.estafeta.restconnector.oauth.AuthenticatorClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
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
import com.estafeta.webservice.frecuencia.EstafetaFrecuencia;
import com.estafeta.webservice.frecuencia.EstafetaFrecuenciaCliente;
import com.integrador.models.Usuario;
import com.integrador.repositories.TokenRepository;
import com.integrador.repositories.UsuarioRepository;
import com.integrador.services.AtributoService;
import com.integrador.util.AESAlgorithm;
@RestController
@RequestMapping({"/Estafeta"})
public class FrecuenciaCorizadorController {
	@Autowired TokenRepository tokenRepository;
	@Autowired AtributoService atributoService;
	@Autowired UsuarioRepository usuarioRepository;
	static final Logger log = LoggerFactory.getLogger(FrecuenciaCorizadorController.class);
	/**
	 * Handles the frequency estimator request for Estafeta.
	 * @param request The HTTP request object.
	 * @param content The request body as a JSON string.
	 * @return ResponseEntity containing a JSON string with either the validation error or the response from the frequency estimator service.
	 * @throws Exception If an error occurs during the processing of the request.
	 * 
	 * NOTE: THE EXTERNAL LAYER IS REST SERVICE FORMAT.
	 * THIS VERSION CONSUME THE RESTSERVICE IN ESTAFETA.
	 */
	@PostMapping(value = "/FrecuenciaCotizador")
	public ResponseEntity<String> frecuenciaCotizador(HttpServletRequest request, @RequestBody String content) throws Exception{
		JSONObject response = new JSONObject();
		JSONObject frecuenciaRequest = new JSONObject();
		String error = "";
		log.info("");
		log.info("== Estafeta RestService / FrecuenciaCotizador ==>");
		try {
			frecuenciaRequest = new JSONObject(content);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		if(!frecuenciaRequest.has("cliente")) {
			error = "Debe especificar el cliente";
			log.info("\t"+error);
			response.put("validacion", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!frecuenciaRequest.has("password")) {
			error = "Debe especificar la contraseña";
			log.info("\t"+error);
			response.put("validacion", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!frecuenciaRequest.has("token")) {
			error = "Debe especificar el token";
			log.info("\t"+error);
			response.put("validacion", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		AESAlgorithm e = new AESAlgorithm();
		Usuario usuario = usuarioRepository.findByUsuarioAndContrasena(String.valueOf(frecuenciaRequest.get("cliente")), e.encrypt(String.valueOf(frecuenciaRequest.get("password"))));
		if(usuario==null) {
			error = "Usuario o Contraseña invalido";
			log.info("\t"+error);
			response.put("validacion", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		com.integrador.models.Token token = tokenRepository.findByIdUsuarioAndToken(usuario.getIdUsuario(), String.valueOf(frecuenciaRequest.get("token")));
		if(token==null) {
			error = "Token invalido";
			log.info("\t"+error);
			response.put("validacion", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		frecuenciaRequest.remove("cliente");
		frecuenciaRequest.remove("password");
		frecuenciaRequest.remove("token");

		Map<String,String> params = atributoService.getByTipoInMap("Estafeta_Frecuencia_Cotizador_Rest");
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
		FrecuenciaCotizadorRestConnector conector = new  FrecuenciaCotizadorRestConnector(_url,_apiKey,_token.getToken());
		conector.setIdUsuario(_idUsuario);
		conector.setUsuario(_usuario);
		conector.setContra(_contrasena);
		conector.setEsFrecuencia(frecuenciaRequest.getString("esFrecuencia"));
		conector.setEsLista(frecuenciaRequest.getString("esLista"));
		JSONObject tipoEnvio = frecuenciaRequest.getJSONObject("tipoEnvio");
		conector.setAlto(tipoEnvio.getString("Alto"));
		conector.setAncho(tipoEnvio.getString("Ancho"));
		conector.setEsPaquete(tipoEnvio.getString("EsPaquete"));
		conector.setLargo(tipoEnvio.getString("Largo"));
		conector.setPeso(tipoEnvio.getString("Peso"));
		conector.setCpOrigen(frecuenciaRequest.getJSONObject("datosOrigen").getJSONArray("string").getString(0) );
		conector.setCpDestino(frecuenciaRequest.getJSONObject("datosDestino").getJSONArray("string").getString(0));
		conector.execute();
		log.info("\tRequest: "+conector.getRequest());
		log.info("\tResponse: "+conector.getResponse());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(conector.toString(),headers, HttpStatus.OK);
	}
	/**
	 * Handles the frequency estimator request for Estafeta.
	 * @param request The HTTP request object.
	 * @param content The request body as a JSON string.
	 * @return ResponseEntity containing a JSON string with either the validation error or the response from the frequency estimator service.
	 * @throws Exception If an error occurs during the processing of the request.
	 *
	 * NOTE: THE EXTERNAL LAYER IS REST SERVICE FORMAT.
	 * THIS VERSION CONSUME THE WEBSERVICE IN ESTAFETA INTERNALLY.
	 *
	 */
	@PostMapping(value = "/FrecuenciaCotizadorV2")
	public ResponseEntity<String> frecuenciaCotizadorV2(HttpServletRequest request, @RequestBody String content) throws Exception{
		JSONObject response = new JSONObject();
		JSONObject frecuenciaRequest = new JSONObject();
		String error = "";
		log.info("");
		log.info("== Estafeta RestService / FrecuenciaCotizadorV2 ==>");
		try {
			frecuenciaRequest = new JSONObject(content);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		if(!frecuenciaRequest.has("cliente")) {
			error = "Debe especificar el cliente";
			log.info("\t"+error);
			response.put("validacion", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!frecuenciaRequest.has("password")) {
			error = "Debe especificar la contraseña";
			log.info("\t"+error);
			response.put("validacion", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!frecuenciaRequest.has("token")) {
			error = "Debe especificar el token";
			log.info("\t"+error);
			response.put("validacion", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		AESAlgorithm e = new AESAlgorithm();
		Usuario usuario = usuarioRepository.findByUsuarioAndContrasena(String.valueOf(frecuenciaRequest.get("cliente")), e.encrypt(String.valueOf(frecuenciaRequest.get("password"))));
		if(usuario==null) {
			error = "Usuario o Contraseña invalido";
			log.info("\t"+error);
			response.put("validacion", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		com.integrador.models.Token token = tokenRepository.findByIdUsuarioAndToken(usuario.getIdUsuario(), String.valueOf(frecuenciaRequest.get("token")));
		if(token==null) {
			error = "Token invalido";
			log.info("\t"+error);
			response.put("validacion", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		frecuenciaRequest.remove("cliente");
		frecuenciaRequest.remove("password");
		frecuenciaRequest.remove("token");

		JSONObject tipoEnvio = frecuenciaRequest.getJSONObject("tipoEnvio");
		Map<String,String> values = atributoService.getByTipoInMap("Estafeta_Frecuencia_Cotizador");

		EstafetaFrecuenciaCliente serviceEstafeta = new EstafetaFrecuenciaCliente();
		serviceEstafeta.setUrl(values.get("url").toString());
		serviceEstafeta.setId(values.get("id").toString());
		serviceEstafeta.setUsuario(values.get("usuario").toString());
		serviceEstafeta.setContrasena(values.get("contrasena").toString());

		serviceEstafeta.setOrigen(frecuenciaRequest.getJSONObject("datosOrigen").getJSONArray("string").getString(0));
		serviceEstafeta.setDestino(frecuenciaRequest.getJSONObject("datosDestino").getJSONArray("string").getString(0));
		serviceEstafeta.setEsFrecuencia(frecuenciaRequest.getString("esFrecuencia"));
		serviceEstafeta.setEsLista(frecuenciaRequest.getString("esLista"));
		serviceEstafeta.setEsPaquete(tipoEnvio.getString("EsPaquete"));
		serviceEstafeta.setLargo(tipoEnvio.getString("Largo"));
		serviceEstafeta.setPeso(tipoEnvio.getString("Peso"));
		serviceEstafeta.setAlto(tipoEnvio.getString("Alto"));
		serviceEstafeta.setAncho(tipoEnvio.getString("Ancho"));

		EstafetaFrecuencia serviceFrecuencia = serviceEstafeta.getFrecuencia();
		JSONObject xmlJSONObj = XML.toJSONObject(serviceFrecuencia.getResponse());
		//Deleting nodes from XML
		JSONObject soapEnvelope = xmlJSONObj.getJSONObject("soap:Envelope"); 
		JSONObject soapBody = soapEnvelope.getJSONObject("soap:Body");
		JSONObject frecuenciaCotizadorResponse = soapBody.getJSONObject("FrecuenciaCotizadorResponse");
		JSONObject frecuenciaCotizadorResult = frecuenciaCotizadorResponse.getJSONObject("FrecuenciaCotizadorResult");
		JSONObject respuesta = frecuenciaCotizadorResult.getJSONObject("Respuesta");

		if(serviceFrecuencia.isError()){
			error = respuesta.has("MensajeError") ? String.valueOf(respuesta.get("MensajeError")) : "";
			log.info("\t"+error);
			response.put("error", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}else {
			respuesta = modifyJson(respuesta);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(respuesta.toString(),headers, HttpStatus.OK);
	}
	/**
	 * Modifies the JSON object by restructuring the "Colonias" node.
	 * @param rootObject The root JSON object to be modified.
	 * @return The modified JSON object.
	 */
	public static JSONObject modifyJson(JSONObject rootObject) {
		JSONObject coloniasRootObject = rootObject.getJSONObject("Colonias").getJSONObject("Colonias");
		Object coloniasObject = coloniasRootObject.get("string");
		if(coloniasObject instanceof JSONArray) {
			JSONArray coloniasArray = coloniasRootObject.getJSONArray("string");
			JSONArray newColoniasArray = new JSONArray();
			for (int i = 0; i < coloniasArray.length(); i++) {
				JSONObject newColoniaObject = new JSONObject();
				newColoniaObject.put("nombre", coloniasArray.getString(i));
				newColoniasArray.put(newColoniaObject);
			}
			rootObject.put("Colonias", newColoniasArray);
		}else if (coloniasObject instanceof String){
			String colonia = (String) coloniasObject;
			JSONArray newColoniasArray = new JSONArray();
			JSONObject newColoniaObject = new JSONObject();
			newColoniaObject.put("nombre",colonia);
			newColoniasArray.put(newColoniaObject);
			rootObject.put("Colonias", newColoniasArray);
		}
		return rootObject;
	}
	/**
	 * Handles the frequency estimator request for Estafeta, specifically for colonies.
	 * @param request The HTTP request object.
	 * @param content The request body as a JSON string.
	 * @return ResponseEntity containing a JSON string with either the validation error or the response from the frequency estimator service.
	 * @throws Exception If an error occurs during the processing of the request.
	 */
	@PostMapping(value = "/FrecuenciaCotizador/Colonias")
	public ResponseEntity<String> frecuenciaCotizadorColonias(HttpServletRequest request, @RequestBody String content) throws Exception{
		JSONObject response = new JSONObject();
		JSONObject frecuenciaRequest = new JSONObject();
		String error = "";
		log.info("");
		log.info("== Estafeta RestService / FrecuenciaCotizador Colonias ==>");
		try {
			frecuenciaRequest = new JSONObject(content);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		if(!frecuenciaRequest.has("cliente")) {
			error = "Debe especificar el cliente";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!frecuenciaRequest.has("password")) {
			error = "Debe especificar la contraseña";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!frecuenciaRequest.has("token")) {
			error = "Debe especificar el token";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		AESAlgorithm e = new AESAlgorithm();
		Usuario usuario = usuarioRepository.findByUsuarioAndContrasena(String.valueOf(frecuenciaRequest.get("cliente")), e.encrypt(String.valueOf(frecuenciaRequest.get("password"))));
		if(usuario==null) {
			error = "Usuario o Contraseña invalido";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		com.integrador.models.Token token = tokenRepository.findByIdUsuarioAndToken(usuario.getIdUsuario(), String.valueOf(frecuenciaRequest.get("token")));
		if(token==null) {
			error = "Token invalido";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		frecuenciaRequest.remove("cliente");
		frecuenciaRequest.remove("password");
		frecuenciaRequest.remove("token");
		Map<String,String> params = atributoService.getByTipoInMap("Estafeta_Frecuencia_Cotizador_Rest");
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
		FrecuenciaCotizadorRestConnector conector = new  FrecuenciaCotizadorRestConnector(_url,_apiKey,_token.getToken());
		conector.setIdUsuario(_idUsuario);
		conector.setUsuario(_usuario);
		conector.setContra(_contrasena);
		conector.setEsFrecuencia(frecuenciaRequest.getString("esFrecuencia"));
		conector.setEsLista(frecuenciaRequest.getString("esLista"));
		JSONObject tipoEnvio = frecuenciaRequest.getJSONObject("tipoEnvio");
		conector.setAlto(tipoEnvio.getString("Alto"));
		conector.setAncho(tipoEnvio.getString("Ancho"));
		conector.setEsPaquete(tipoEnvio.getString("EsPaquete"));
		conector.setLargo(tipoEnvio.getString("Largo"));
		conector.setPeso(tipoEnvio.getString("Peso"));
		conector.setCpOrigen(frecuenciaRequest.getJSONObject("datosOrigen").getJSONArray("string").getString(0) );
		conector.setCpDestino(frecuenciaRequest.getJSONObject("datosDestino").getJSONArray("string").getString(0));
		conector.execute();
		JSONArray colonias = conector.getColonias();
		log.info("\tRequest: "+conector.getRequest());
		log.info("\tResponse: "+conector.getResponse());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(colonias.toString(),headers, HttpStatus.OK);
	}
}
