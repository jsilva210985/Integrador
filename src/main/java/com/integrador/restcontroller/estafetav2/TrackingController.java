package com.integrador.restcontroller.estafetav2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.estafetav2.PullTracking.TrackingClient;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.integrador.models.Usuario;
import com.integrador.repositories.TokenRepository;
import com.integrador.repositories.UsuarioRepository;
import com.integrador.services.AtributoService;
import com.integrador.util.AESAlgorithm;

@RestController("estafetaTrackingControllerV2")
@RequestMapping({"/EstafetaRest"})
public class TrackingController {

	@Autowired AtributoService atributoService;
	@Autowired TokenRepository tokenRepository;
	@Autowired UsuarioRepository usuarioRepository;

	static final Logger log = LoggerFactory.getLogger(TrackingController.class);

	@RequestMapping(value = {"/Tracking", "/Tracking/Status", "/Tracking/History"}, method = {RequestMethod.POST, RequestMethod.GET})
	public ResponseEntity<String> tracking(HttpServletRequest request, @RequestBody String content) {
		log.info("");
		log.info("== Estafeta RestService V2 / PullTracking ==>");
		JSONObject response = new JSONObject();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		try {
			JSONObject trackingRequest;
			try {
				trackingRequest = new JSONObject(content);
			} catch (Exception e) {
				return new ResponseEntity<String>("JSON mal formado: " + e.getMessage(), HttpStatus.BAD_REQUEST);
			}
			// Validaciones basadas en ConsultaEnviosController
			if (!trackingRequest.has("cliente") || trackingRequest.getString("cliente").trim().isEmpty()) {
				String error = "Debe especificar el cliente";
				log.info("\t" + error);
				response.put("validation", error);
				return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
			}
			if (!trackingRequest.has("password") || trackingRequest.getString("password").trim().isEmpty()) {
				String error = "Debe especificar la contraseña";
				log.info("\t" + error);
				response.put("validation", error);
				return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
			}
			if (!trackingRequest.has("token") || trackingRequest.getString("token").trim().isEmpty()) {
				String error = "Debe especificar el token";
				log.info("\t" + error);
				response.put("validation", error);
				return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
			}
			AESAlgorithm aes = new AESAlgorithm();
			Usuario usuario = usuarioRepository.findByUsuarioAndContrasena(
				trackingRequest.getString("cliente"), 
				aes.encrypt(trackingRequest.getString("password"))
			);
			if (usuario == null) {
				String error = "Usuario o Contraseña invalido";
				log.info("\t" + error);
				response.put("validation", error);
				return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
			}
			com.integrador.models.Token tokenObj = tokenRepository.findByIdUsuarioAndToken(
				usuario.getIdUsuario(), 
				trackingRequest.getString("token")
			);
			if (tokenObj == null) {
				String error = "Token invalido";
				log.info("\t" + error);
				response.put("validation", error);
				return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
			}
			List<String> guias = extractWaybills(trackingRequest);
			if (guias.isEmpty()) {
				String error = "Debe especificar al menos una guia o waybill para consultar";
				log.info("\t" + error);
				response.put("validation", error);
				return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
			}
			Map<String, String> params = atributoService.getByTipoInMap("EstafetaV2");
			if (params == null || params.isEmpty()) {
				throw new IllegalStateException("No se encontraron atributos de configuracion para EstafetaV2");
			}
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
			if (jsonStr == null) {
				throw new IllegalStateException("No se encontro el JSON de configuracion en los atributos EstafetaV2");
			}
			JSONObject jsonObj = new JSONObject(jsonStr);
			if (!jsonObj.has("PullTracking")) {
				throw new IllegalStateException("El JSON de EstafetaV2 no contiene el nodo 'PullTracking'");
			}
			JSONArray ptArray = jsonObj.getJSONArray("PullTracking");
			if (ptArray.length() == 0) {
				throw new IllegalStateException("El nodo 'PullTracking' en el JSON de EstafetaV2 esta vacio");
			}
			JSONObject ptConfig = ptArray.getJSONObject(0);
			String tokenUrl = ptConfig.optString("tokenUrl", null);
			String clientId = ptConfig.optString("clientId", null);
			String clientSecret = ptConfig.optString("clientSecret", null);
			String scope = ptConfig.optString("scope", null);
			String apiKey = ptConfig.optString("apiKey", null);
			String statusUrl = ptConfig.optString("statusUrl", null);
			String historyUrl = ptConfig.optString("historyUrl", null);
			if (tokenUrl == null || clientId == null || clientSecret == null || scope == null || apiKey == null || statusUrl == null || historyUrl == null) {
				throw new IllegalArgumentException("Faltan parametros requeridos en la configuracion 'PullTracking' del JSON de EstafetaV2");
			}
			log.info("\tInvocando PullTracking V2 para " + guias.size() + " guia(s).");
			AuthenticatorClient authClient = new AuthenticatorClient(tokenUrl, clientId, clientSecret, scope);
			TrackingClient trackingClient = new TrackingClient(statusUrl, historyUrl, apiKey, authClient);
			String requestUri = request != null ? request.getRequestURI() : "";
			boolean onlyStatus = requestUri != null && requestUri.endsWith("/Status");
			boolean onlyHistory = requestUri != null && requestUri.endsWith("/History");
			if (onlyStatus) {
				String statusResponse = trackingClient.getStatusByWaybill(guias.toArray(new String[0]));
				return new ResponseEntity<String>(statusResponse, headers, HttpStatus.OK);
			} else if (onlyHistory) {
				String historyResponse = trackingClient.getHistoryByWaybill(guias.toArray(new String[0]));
				return new ResponseEntity<String>(historyResponse, headers, HttpStatus.OK);
			} else {
				String statusResponse = trackingClient.getStatusByWaybill(guias.toArray(new String[0]));
				String historyResponse = null;
				boolean includeHistory = shouldIncludeHistory(trackingRequest);
				if (includeHistory) {
					try {
						historyResponse = trackingClient.getHistoryByWaybill(guias.toArray(new String[0]));
					} catch (Exception ex) {
						log.warn("Failed to get history response from V2 client: " + ex.getMessage());
					}
				}
				JSONObject combined = new JSONObject();
				try {
					if (statusResponse != null && !statusResponse.trim().isEmpty()) {
						if (statusResponse.trim().startsWith("[")) {
							combined.put("status", new JSONArray(statusResponse));
						} else {
							combined.put("status", new JSONObject(statusResponse));
						}
					} else {
						combined.put("status", new JSONObject());
					}
				} catch (Exception ex) {
					combined.put("status", statusResponse);
				}
				if (includeHistory) {
					try {
						if (historyResponse != null && !historyResponse.trim().isEmpty()) {
							if (historyResponse.trim().startsWith("[")) {
								combined.put("history", new JSONArray(historyResponse));
							} else {
								combined.put("history", new JSONObject(historyResponse));
							}
						} else {
							combined.put("history", new JSONObject());
						}
					} catch (Exception ex) {
						combined.put("history", historyResponse);
					}
				}
				return new ResponseEntity<String>(combined.toString(4), headers, HttpStatus.OK);
			}
		} catch (IllegalArgumentException | IllegalStateException e) {
			log.error("\tError de validacion/configuracion: " + e.getMessage());
			response.put("error", e.getMessage());
			return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error("\tError en el servicio de PullTracking V2: " + e.getMessage(), e);
			response.put("error", "Error interno en el servicio de PullTracking V2: " + e.getMessage());
			return new ResponseEntity<String>(response.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private boolean shouldIncludeHistory(JSONObject requestJson) {
		if (requestJson == null) {
			return true;
		}
		try {
			JSONObject searchConfig = requestJson.optJSONObject("searchConfiguration");
			if (searchConfig != null) {
				JSONObject historyConfig = searchConfig.optJSONObject("historyConfiguration");
				if (historyConfig != null) {
					Object incHistory = historyConfig.opt("includeHistory");
					if (incHistory instanceof Boolean) {
						return (Boolean) incHistory;
					} else if (incHistory != null) {
						return Boolean.parseBoolean(incHistory.toString().trim());
					}
				}
			}
		} catch (Exception e) {
			log.warn("Error parsing includeHistory: " + e.getMessage());
		}
		return true;
	}

	private List<String> extractWaybills(JSONObject requestJson) {
		List<String> list = new ArrayList<>();
		if (requestJson == null) {
			return list;
		}
		if (requestJson.has("waybill")) {
			String wb = requestJson.optString("waybill", "").trim();
			if (!wb.isEmpty()) {
				list.add(wb);
			}
		}

		if (requestJson.has("waybills")) {
			Object val = requestJson.get("waybills");
			if (val instanceof JSONArray) {
				JSONArray arr = (JSONArray) val;
				for (int i = 0; i < arr.length(); i++) {
					String wb = arr.optString(i, "").trim();
					if (!wb.isEmpty()) {
						list.add(wb);
					}
				}
			} else if (val != null) {
				String s = val.toString().trim();
				if (!s.isEmpty()) {
					for (String part : s.split(",")) {
						String wb = part.trim();
						if (!wb.isEmpty()) {
							list.add(wb);
						}
					}
				}
			}
		}

		try {
			JSONObject searchType = requestJson.optJSONObject("searchType");
			if (searchType != null) {
				JSONObject waybillList = searchType.optJSONObject("waybillList");
				if (waybillList != null) {
					JSONObject waybills = waybillList.optJSONObject("waybills");
					if (waybills != null) {
						if (waybills.has("trackings")) {
							JSONArray arr = waybills.optJSONArray("trackings");
							if (arr != null) {
								for (int i = 0; i < arr.length(); i++) {
									String wb = arr.optString(i, "").trim();
									if (!wb.isEmpty()) {
										list.add(wb);
									}
								}
							}
						}
						if (waybills.has("string")) {
							JSONArray arr = waybills.optJSONArray("string");
							if (arr != null) {
								for (int i = 0; i < arr.length(); i++) {
									String wb = arr.optString(i, "").trim();
									if (!wb.isEmpty()) {
										list.add(wb);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.warn("Error parsing nested waybills: " + e.getMessage());
		}
		return list;
	}
}
