package com.integrador.restcontroller.estafetav2;

import java.util.Map;

import org.estafetav2.authentication.AuthenticatorClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.integrador.services.AtributoService;

@RestController("estafetaTokenControllerV2")
@RequestMapping({"/EstafetaRest"})
public class TokenController {

	@Autowired AtributoService atributoService;

	static final Logger log = LoggerFactory.getLogger(TokenController.class);

	@GetMapping(value = "/Token")
	public ResponseEntity<String> token() {
		log.info("");
		log.info("== Estafeta RestService V2 / Token ==>");
		JSONObject response = new JSONObject();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		try {
			Map<String, String> params = atributoService.getByTipoInMap("EstafetaRestV2");
			if (params == null || params.isEmpty()) {
				params = atributoService.getByTipoInMap("estafetarestv2");
			}
			if (params == null || params.isEmpty()) {
				params = atributoService.getByTipoInMap("ESTAFETARESTV2");
			}
			if (params == null || params.isEmpty()) {
				params = atributoService.getByTipoInMap("EstafetaV2");
			}
			if (params == null || params.isEmpty()) {
				params = atributoService.getByTipoInMap("estafetav2");
			}
			if (params == null || params.isEmpty()) {
				String errorMsg = "No se encontraron atributos de configuracion para EstafetaRestV2 o EstafetaV2";
				log.error("\t" + errorMsg);
				response.put("error", errorMsg);
				return new ResponseEntity<String>(response.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			String tokenUrl = null;
			String clientId = null;
			String clientSecret = null;
			String scope = null;
			String jsonStr = null;
			for (Map.Entry<String, String> entry : params.entrySet()) {
				String val = entry.getValue();
				if (val != null && val.trim().startsWith("{")) {
					jsonStr = val.trim();
					break;
				}
				String key = entry.getKey();
				if (key != null && key.trim().startsWith("{")) {
					jsonStr = key.trim();
					break;
				}
			}
			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					JSONArray authArray = null;
					if (jsonObj.has("FrequencyRest")) {
						authArray = jsonObj.getJSONArray("FrequencyRest");
					} else if (jsonObj.has("PullTracking")) {
						authArray = jsonObj.getJSONArray("PullTracking");
					}
					if (authArray != null && authArray.length() > 0) {
						JSONObject authObj = authArray.getJSONObject(0);
						tokenUrl = authObj.optString("tokenUrl", null);
						clientId = authObj.optString("clientId", null);
						clientSecret = authObj.optString("clientSecret", null);
						scope = authObj.optString("scope", null);
					}
				} catch (Exception e) {
					log.error("\tError parseando JSON de EstafetaV2: " + e.getMessage());
				}
			}
			// Fallbacks a llaves directas del mapa si no se extrajo del JSON
			if (tokenUrl == null) tokenUrl = params.get("tokenUrl");
			if (tokenUrl == null) tokenUrl = params.get("url_token");
			if (clientId == null) clientId = params.get("clientId");
			if (clientId == null) clientId = params.get("client_id");
			if (clientId == null) clientId = params.get("api_key");
			if (clientSecret == null) clientSecret = params.get("clientSecret");
			if (clientSecret == null) clientSecret = params.get("client_secret");
			if (clientSecret == null) clientSecret = params.get("api_secret");
			if (scope == null) scope = params.get("scope");
			if (tokenUrl == null || tokenUrl.trim().isEmpty()) {
				throw new IllegalArgumentException("El parametro 'tokenUrl' es requerido.");
			}
			if (clientId == null || clientId.trim().isEmpty()) {
				throw new IllegalArgumentException("El parametro 'clientId' es requerido.");
			}
			if (clientSecret == null || clientSecret.trim().isEmpty()) {
				throw new IllegalArgumentException("El parametro 'clientSecret' es requerido.");
			}
			if (scope == null || scope.trim().isEmpty()) {
				throw new IllegalArgumentException("El parametro 'scope' es requerido.");
			}
			log.info("\tConfiguracion cargada correctamente para Autenticacion:");
			log.info("\ttokenUrl: " + tokenUrl);
			log.info("\tclientId: " + clientId);
			log.info("\tscope: " + scope);
			AuthenticatorClient authClient = new AuthenticatorClient(tokenUrl, clientId, clientSecret, scope);
			String token = authClient.getAccessToken();
			if (token == null || token.trim().isEmpty()) {
				throw new IllegalStateException("El cliente de autenticacion devolvio un token vacio o nulo.");
			}
			response.put("token", token);
			log.info("\tToken generado exitosamente: " + (token.length() > 30 ? token.substring(0, 30) + "..." : token));
			return new ResponseEntity<String>(response.toString(), headers, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			log.error("\tError de validacion: " + e.getMessage());
			response.put("error", e.getMessage());
			return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error("\tError al obtener el token de EstafetaV2: " + e.getMessage(), e);
			response.put("error", "Error interno al obtener el token: " + e.getMessage());
			return new ResponseEntity<String>(response.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
