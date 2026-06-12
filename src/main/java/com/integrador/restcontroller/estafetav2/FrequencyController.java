package com.integrador.restcontroller.estafetav2;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.estafetav2.FrequencyRest.FrequencyClient;
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

@RestController("estafetaFrequencyControllerV2")
@RequestMapping({"/EstafetaRest"})
public class FrequencyController {

	@Autowired AtributoService atributoService;
	@Autowired TokenRepository tokenRepository;
	@Autowired UsuarioRepository usuarioRepository;

	static final Logger log = LoggerFactory.getLogger(FrequencyController.class);

	@RequestMapping(value = {"/Frequency", "/Frequency/Original"}, method = {RequestMethod.POST, RequestMethod.GET})
	public ResponseEntity<String> frequency(HttpServletRequest request, @RequestBody String content) {
		log.info("");
		log.info("== Estafeta RestService V2 / Frequency ==>");
		JSONObject response = new JSONObject();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		try {
			JSONObject frecuenciaRequest;
			try {
				frecuenciaRequest = new JSONObject(content);
			} catch (Exception e) {
				return new ResponseEntity<String>("JSON mal formado: " + e.getMessage(), HttpStatus.BAD_REQUEST);
			}

			// Validaciones basicas basadas en com.integrador.restcontroller.estafeta.FrecuenciaCorizadorController
			if (!frecuenciaRequest.has("cliente") || frecuenciaRequest.getString("cliente").trim().isEmpty()) {
				String error = "Debe especificar el cliente";
				log.info("\t" + error);
				response.put("validacion", error);
				return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
			}
			if (!frecuenciaRequest.has("password") || frecuenciaRequest.getString("password").trim().isEmpty()) {
				String error = "Debe especificar la contraseña";
				log.info("\t" + error);
				response.put("validacion", error);
				return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
			}
			if (!frecuenciaRequest.has("token") || frecuenciaRequest.getString("token").trim().isEmpty()) {
				String error = "Debe especificar el token";
				log.info("\t" + error);
				response.put("validacion", error);
				return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
			}

			AESAlgorithm aes = new AESAlgorithm();
			Usuario usuario = usuarioRepository.findByUsuarioAndContrasena(
				frecuenciaRequest.getString("cliente"), 
				aes.encrypt(frecuenciaRequest.getString("password"))
			);
			if (usuario == null) {
				String error = "Usuario o Contraseña invalido";
				log.info("\t" + error);
				response.put("validacion", error);
				return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
			}

			com.integrador.models.Token tokenObj = tokenRepository.findByIdUsuarioAndToken(
				usuario.getIdUsuario(), 
				frecuenciaRequest.getString("token")
			);
			if (tokenObj == null) {
				String error = "Token invalido";
				log.info("\t" + error);
				response.put("validacion", error);
				return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
			}

			// Obtener CPs
			String cpOrigen = null;
			if (frecuenciaRequest.has("datosOrigen")) {
				Object orig = frecuenciaRequest.get("datosOrigen");
				if (orig instanceof JSONObject) {
					JSONObject origObj = (JSONObject) orig;
					if (origObj.has("string")) {
						JSONArray arr = origObj.getJSONArray("string");
						if (arr.length() > 0) {
							cpOrigen = arr.getString(0);
						}
					}
				} else {
					cpOrigen = String.valueOf(orig);
				}
			}

			String cpDestino = null;
			if (frecuenciaRequest.has("datosDestino")) {
				Object dest = frecuenciaRequest.get("datosDestino");
				if (dest instanceof JSONObject) {
					JSONObject destObj = (JSONObject) dest;
					if (destObj.has("string")) {
						JSONArray arr = destObj.getJSONArray("string");
						if (arr.length() > 0) {
							cpDestino = arr.getString(0);
						}
					}
				} else {
					cpDestino = String.valueOf(dest);
				}
			}

			if (cpOrigen == null || cpOrigen.trim().isEmpty()) {
				String error = "Debe especificar el código postal de origen (datosOrigen)";
				log.info("\t" + error);
				response.put("validacion", error);
				return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
			}

			if (cpDestino == null || cpDestino.trim().isEmpty()) {
				String error = "Debe especificar el código postal de destino (datosDestino)";
				log.info("\t" + error);
				response.put("validacion", error);
				return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
			}

			// Cargar configuracion EstafetaV2
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
				throw new IllegalStateException("No se encontraron atributos de configuracion para EstafetaV2");
			}

			// Buscar si alguna de las llaves o valores contiene el JSON directamente
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
			if (!jsonObj.has("FrequencyRest")) {
				throw new IllegalStateException("El JSON de EstafetaV2 no contiene el nodo 'FrequencyRest'");
			}

			JSONArray freqArray = jsonObj.getJSONArray("FrequencyRest");
			if (freqArray.length() == 0) {
				throw new IllegalStateException("El nodo 'FrequencyRest' en el JSON de EstafetaV2 esta vacio");
			}

			JSONObject freqConfig = freqArray.getJSONObject(0);
			String tokenUrl = freqConfig.optString("tokenUrl", null);
			String clientId = freqConfig.optString("clientId", null);
			String clientSecret = freqConfig.optString("clientSecret", null);
			String scope = freqConfig.optString("scope", null);
			String apiKey = freqConfig.optString("apiKey", null);
			String url = freqConfig.optString("url", null);

			if (tokenUrl == null || clientId == null || clientSecret == null || scope == null || apiKey == null || url == null) {
				throw new IllegalArgumentException("Faltan parametros requeridos en la configuracion 'FrequencyRest' del JSON de EstafetaV2");
			}

			log.info("\tInvocando Frequency V2 para CP Origen: " + cpOrigen + " -> CP Destino: " + cpDestino);
			AuthenticatorClient authClient = new AuthenticatorClient(tokenUrl, clientId, clientSecret, scope);
			FrequencyClient freqClient = new FrequencyClient(url, apiKey, authClient);

			String serviceResponse = freqClient.execute(cpOrigen, cpDestino);
			log.info("\tRespuesta recibida exitosamente del servicio de frecuencias V2.");

			if (request != null && request.getRequestURI() != null && request.getRequestURI().endsWith("/Original")) {
				return new ResponseEntity<String>(serviceResponse, headers, HttpStatus.OK);
			} else {
				String transformedResponse = transformV2ResponseToV1(serviceResponse, frecuenciaRequest);
				return new ResponseEntity<String>(transformedResponse, headers, HttpStatus.OK);
			}

		} catch (IllegalArgumentException | IllegalStateException e) {
			log.error("\tError de validacion/configuracion: " + e.getMessage());
			response.put("error", e.getMessage());
			return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error("\tError en el servicio de frecuencias V2: " + e.getMessage(), e);
			response.put("error", "Error interno en el servicio de frecuencias V2: " + e.getMessage());
			return new ResponseEntity<String>(response.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public String transformV2ResponseToV1(String v2ResponseStr, JSONObject requestJson) {
		try {
			JSONObject v2Json = new JSONObject(v2ResponseStr);
			if (!v2Json.has("frequencies")) {
				return createDefaultV1Response("999", "Respuesta de Estafeta V2 sin nodo frequencies: " + v2ResponseStr, requestJson);
			}
			JSONArray frequencies = v2Json.getJSONArray("frequencies");
			if (frequencies.length() == 0) {
				return createDefaultV1Response("999", "Respuesta de Estafeta V2 con frequencies vacio", requestJson);
			}
			JSONObject freqObj = frequencies.getJSONObject(0);

			JSONArray originArray = freqObj.optJSONArray("origin");
			JSONArray destArray = freqObj.optJSONArray("destinations");

			JSONObject originObj = (originArray != null && originArray.length() > 0) ? originArray.getJSONObject(0) : null;
			JSONObject destObj = (destArray != null && destArray.length() > 0) ? destArray.getJSONObject(0) : null;

			JSONObject v1Response = new JSONObject();

			// 1. CodigoPosOri
			int codPosOri = 0;
			if (originObj != null && originObj.has("address")) {
				codPosOri = optIntSafely(originObj.getJSONObject("address").opt("postalCode"), 0);
			} else if (requestJson != null && requestJson.has("datosOrigen")) {
				try {
					JSONArray arr = requestJson.getJSONObject("datosOrigen").getJSONArray("string");
					if (arr.length() > 0) {
						codPosOri = Integer.parseInt(arr.getString(0));
					}
				} catch (Exception e) {
					// ignore
				}
			}
			v1Response.put("CodigoPosOri", codPosOri);

			// 2. Colonias
			JSONArray coloniasArray = new JSONArray();
			if (destObj != null && destObj.has("address")) {
				JSONObject destAddress = destObj.getJSONObject("address");
				if (destAddress.has("settlementName")) {
					Object setNamesObj = destAddress.get("settlementName");
					if (setNamesObj instanceof JSONArray) {
						JSONArray setNames = (JSONArray) setNamesObj;
						for (int i = 0; i < setNames.length(); i++) {
							JSONObject nameObj = setNames.getJSONObject(i);
							JSONObject col = new JSONObject();
							col.put("nombre", nameObj.optString("name", ""));
							coloniasArray.put(col);
						}
					} else if (setNamesObj instanceof JSONObject) {
						JSONObject nameObj = (JSONObject) setNamesObj;
						JSONObject col = new JSONObject();
						col.put("nombre", nameObj.optString("name", ""));
						coloniasArray.put(col);
					}
				}
			}
			v1Response.put("Colonias", coloniasArray);

			// 3. ExistenteSiglaDes
			String existenteSiglaDes = "No";
			if (destObj != null && destObj.has("result")) {
				int code = destObj.getJSONObject("result").optInt("code", -1);
				if (code == 0) {
					existenteSiglaDes = "Si";
				}
			}
			v1Response.put("ExistenteSiglaDes", existenteSiglaDes);

			// 4. Destino
			JSONObject destinoObj = new JSONObject();
			int cpDestino = 0;
			if (destObj != null) {
				JSONObject destAddress = destObj.optJSONObject("address");
				if (destAddress != null) {
					destinoObj.put("Municipio", destAddress.optString("townshipName", ""));
					cpDestino = optIntSafely(destAddress.opt("postalCode"), 0);
					destinoObj.put("CpDestino", cpDestino);
					destinoObj.put("Estado", destAddress.optString("stateCode", ""));
				}
				destinoObj.put("Plaza1", mapWarehouseCodeToPlaza(destObj.optString("warehouseCode", ""), false));
			}
			if (destinoObj.length() == 0) {
				destinoObj.put("Municipio", "");
				if (requestJson != null && requestJson.has("datosDestino")) {
					try {
						JSONArray arr = requestJson.getJSONObject("datosDestino").getJSONArray("string");
						if (arr.length() > 0) {
							cpDestino = Integer.parseInt(arr.getString(0));
						}
					} catch (Exception e) {
						// ignore
					}
				}
				destinoObj.put("CpDestino", cpDestino);
				destinoObj.put("Plaza1", "");
				destinoObj.put("Estado", "");
			}
			v1Response.put("Destino", destinoObj);

			// 5. Error & 13. MensajeError
			String errorVal = "000";
			String mensajeError = "";
			if (destObj != null && destObj.has("result")) {
				JSONObject res = destObj.getJSONObject("result");
				int code = res.optInt("code", 0);
				if (code != 0) {
					errorVal = String.format("%03d", code);
					mensajeError = res.optString("description", "");
				}
			} else if (originObj != null && originObj.has("result")) {
				JSONObject res = originObj.getJSONObject("result");
				int code = res.optInt("code", 0);
				if (code != 0) {
					errorVal = String.format("%03d", code);
					mensajeError = res.optString("description", "");
				}
			}

			// Si hay restriccion en destino, podemos agregarla al mensaje de error si no hay un error previo
			if (destObj != null && destObj.optBoolean("restriction", false)) {
				String restrictionDesc = destObj.optString("restrictionDescription", "");
				if (!restrictionDesc.isEmpty()) {
					if (mensajeError.isEmpty()) {
						mensajeError = "Restricción: " + restrictionDesc;
					} else {
						mensajeError += " | Restricción: " + restrictionDesc;
					}
				}
			}

			v1Response.put("Error", errorVal);
			v1Response.put("MensajeError", mensajeError);

			// 6. TipoEnvio
			JSONObject requestTipoEnvio = requestJson != null ? requestJson.optJSONObject("tipoEnvio") : null;
			JSONObject tipoEnvioObj = new JSONObject();
			if (requestTipoEnvio != null) {
				Object esPaqueteObj = requestTipoEnvio.opt("EsPaquete");
				boolean esPaquete = false;
				if (esPaqueteObj instanceof Boolean) {
					esPaquete = (Boolean) esPaqueteObj;
				} else if (esPaqueteObj != null) {
					esPaquete = Boolean.parseBoolean(esPaqueteObj.toString().trim());
				}
				tipoEnvioObj.put("EsPaquete", esPaquete);
				tipoEnvioObj.put("Peso", parseNumberSafely(requestTipoEnvio.opt("Peso")));
				tipoEnvioObj.put("Largo", parseNumberSafely(requestTipoEnvio.opt("Largo")));
				tipoEnvioObj.put("Alto", parseNumberSafely(requestTipoEnvio.opt("Alto")));
				tipoEnvioObj.put("Ancho", parseNumberSafely(requestTipoEnvio.opt("Ancho")));
			} else {
				tipoEnvioObj.put("EsPaquete", false);
				tipoEnvioObj.put("Peso", 0);
				tipoEnvioObj.put("Largo", 0);
				tipoEnvioObj.put("Alto", 0);
				tipoEnvioObj.put("Ancho", 0);
			}
			v1Response.put("TipoEnvio", tipoEnvioObj);

			// 7. Origen
			JSONObject v1Origen = new JSONObject();
			if (originObj != null) {
				JSONObject origAddress = originObj.optJSONObject("address");
				if (origAddress != null) {
					v1Origen.put("CodigoPosOri", optIntSafely(origAddress.opt("postalCode"), 0));
					v1Origen.put("EstadoOri", origAddress.optString("stateCode", ""));
					v1Origen.put("MunicipioOri", origAddress.optString("townshipName", ""));
				}
				v1Origen.put("PlazaOri", mapWarehouseCodeToPlaza(originObj.optString("warehouseCode", ""), true));
			}
			if (v1Origen.length() == 0 || !v1Origen.has("CodigoPosOri")) {
				v1Origen.put("CodigoPosOri", codPosOri);
				v1Origen.put("PlazaOri", "");
				v1Origen.put("EstadoOri", "");
				v1Origen.put("MunicipioOri", "");
			}
			v1Response.put("Origen", v1Origen);

			// 8. ExistenteSiglaOri
			String existenteSiglaOri = "No";
			if (originObj != null && originObj.has("result")) {
				int code = originObj.getJSONObject("result").optInt("code", -1);
				if (code == 0) {
					existenteSiglaOri = "Si";
				}
			}
			v1Response.put("ExistenteSiglaOri", existenteSiglaOri);

			// 9. DiasEntrega
			JSONObject diasEntregaObj = new JSONObject();
			if (destObj != null) {
				diasEntregaObj.put("Lunes", destObj.optBoolean("isMonday", false) ? "X" : "");
				diasEntregaObj.put("Martes", destObj.optBoolean("isTuesday", false) ? "X" : "");
				diasEntregaObj.put("Miercoles", destObj.optBoolean("isWednesday", false) ? "X" : "");
				diasEntregaObj.put("Jueves", destObj.optBoolean("isThursday", false) ? "X" : "");
				diasEntregaObj.put("Viernes", destObj.optBoolean("isFriday", false) ? "X" : "");
				diasEntregaObj.put("Sabado", destObj.optBoolean("isSaturday", false) ? "X" : "");
				diasEntregaObj.put("Domingo", destObj.optBoolean("isSunday", false) ? "X" : "");
			} else {
				diasEntregaObj.put("Lunes", "");
				diasEntregaObj.put("Martes", "");
				diasEntregaObj.put("Miercoles", "");
				diasEntregaObj.put("Jueves", "");
				diasEntregaObj.put("Viernes", "");
				diasEntregaObj.put("Sabado", "");
				diasEntregaObj.put("Domingo", "");
			}
			v1Response.put("DiasEntrega", diasEntregaObj);

			// 10. CostoReexpedicion
			String costoReexp = "No";
			if (destObj != null && destObj.optBoolean("isReexpedition", false)) {
				costoReexp = "Si";
			}
			v1Response.put("CostoReexpedicion", costoReexp);

			// 11. ModalidadEntrega
			JSONObject modEntregaObj = new JSONObject();
			if (destObj != null) {
				modEntregaObj.put("Frecuencia", destObj.optString("periodicityName", ""));
				modEntregaObj.put("OcurreForzoso", destObj.optBoolean("isOcurre", false) ? "Si" : "No");
			} else {
				modEntregaObj.put("Frecuencia", "");
				modEntregaObj.put("OcurreForzoso", "No");
			}
			v1Response.put("ModalidadEntrega", modEntregaObj);

			// 12. TipoServicio
			JSONObject tipoServicioRoot = new JSONObject();
			JSONArray tipoServicioArray = new JSONArray();
			if (destObj != null && destObj.has("service")) {
				JSONArray services = destObj.getJSONArray("service");
				for (int i = 0; i < services.length(); i++) {
					JSONObject svc = services.getJSONObject(i);
					JSONObject v1Svc = new JSONObject();
					v1Svc.put("TarifaBase", 0);
					v1Svc.put("DescripcionServicio", svc.optString("name", ""));
					v1Svc.put("Peso", 0);
					v1Svc.put("AplicaCotizacion", "No");
					v1Svc.put("AplicaServicio", "Si");
					v1Svc.put("SobrePeso", 0);
					v1Svc.put("CCTarifaBase", 0);
					v1Svc.put("TipoEnvioRes", 0);
					v1Svc.put("CCSobrePeso", 0);
					v1Svc.put("CostoTotal", 0);
					v1Svc.put("CargosExtra", 0);
					tipoServicioArray.put(v1Svc);
				}
			}
			tipoServicioRoot.put("TipoServicio", tipoServicioArray);
			v1Response.put("TipoServicio", tipoServicioRoot);

			return v1Response.toString(4);

		} catch (Exception e) {
			log.error("Error al transformar respuesta V2 a formato V1: " + e.getMessage(), e);
			return createDefaultV1Response("999", "Excepción al transformar respuesta V2 a V1: " + e.getMessage(), requestJson);
		}
	}

	private String createDefaultV1Response(String errorCode, String errorMessage, JSONObject requestJson) {
		JSONObject v1Response = new JSONObject();

		int codPosOri = 0;
		if (requestJson != null && requestJson.has("datosOrigen")) {
			try {
				JSONArray arr = requestJson.getJSONObject("datosOrigen").getJSONArray("string");
				if (arr.length() > 0) {
					codPosOri = Integer.parseInt(arr.getString(0));
				}
			} catch (Exception e) {
				// ignore
			}
		}
		v1Response.put("CodigoPosOri", codPosOri);
		v1Response.put("Colonias", new JSONArray());
		v1Response.put("ExistenteSiglaDes", "No");

		JSONObject destinoObj = new JSONObject();
		int cpDestino = 0;
		if (requestJson != null && requestJson.has("datosDestino")) {
			try {
				JSONArray arr = requestJson.getJSONObject("datosDestino").getJSONArray("string");
				if (arr.length() > 0) {
					cpDestino = Integer.parseInt(arr.getString(0));
				}
			} catch (Exception e) {
				// ignore
			}
		}
		destinoObj.put("Municipio", "");
		destinoObj.put("CpDestino", cpDestino);
		destinoObj.put("Plaza1", "");
		destinoObj.put("Estado", "");
		v1Response.put("Destino", destinoObj);

		v1Response.put("Error", (errorCode != null && !errorCode.isEmpty()) ? errorCode : "999");

		JSONObject tipoEnvioObj = new JSONObject();
		if (requestJson != null && requestJson.has("tipoEnvio")) {
			JSONObject requestTipoEnvio = requestJson.optJSONObject("tipoEnvio");
			if (requestTipoEnvio != null) {
				tipoEnvioObj.put("EsPaquete", Boolean.parseBoolean(requestTipoEnvio.optString("EsPaquete", "false")));
				tipoEnvioObj.put("Peso", parseNumberSafely(requestTipoEnvio.opt("Peso")));
				tipoEnvioObj.put("Largo", parseNumberSafely(requestTipoEnvio.opt("Largo")));
				tipoEnvioObj.put("Alto", parseNumberSafely(requestTipoEnvio.opt("Alto")));
				tipoEnvioObj.put("Ancho", parseNumberSafely(requestTipoEnvio.opt("Ancho")));
			}
		}
		if (tipoEnvioObj.length() == 0) {
			tipoEnvioObj.put("EsPaquete", false);
			tipoEnvioObj.put("Peso", 0);
			tipoEnvioObj.put("Largo", 0);
			tipoEnvioObj.put("Alto", 0);
			tipoEnvioObj.put("Ancho", 0);
		}
		v1Response.put("TipoEnvio", tipoEnvioObj);

		JSONObject v1Origen = new JSONObject();
		v1Origen.put("CodigoPosOri", codPosOri);
		v1Origen.put("PlazaOri", "");
		v1Origen.put("EstadoOri", "");
		v1Origen.put("MunicipioOri", "");
		v1Response.put("Origen", v1Origen);

		v1Response.put("ExistenteSiglaOri", "No");

		JSONObject diasEntregaObj = new JSONObject();
		diasEntregaObj.put("Miercoles", "");
		diasEntregaObj.put("Martes", "");
		diasEntregaObj.put("Viernes", "");
		diasEntregaObj.put("Lunes", "");
		diasEntregaObj.put("Domingo", "");
		diasEntregaObj.put("Jueves", "");
		diasEntregaObj.put("Sabado", "");
		v1Response.put("DiasEntrega", diasEntregaObj);

		v1Response.put("CostoReexpedicion", "No");

		JSONObject modEntregaObj = new JSONObject();
		modEntregaObj.put("Frecuencia", "");
		modEntregaObj.put("OcurreForzoso", "No");
		v1Response.put("ModalidadEntrega", modEntregaObj);

		JSONObject tipoServicioRoot = new JSONObject();
		tipoServicioRoot.put("TipoServicio", new JSONArray());
		v1Response.put("TipoServicio", tipoServicioRoot);

		v1Response.put("MensajeError", (errorMessage != null) ? errorMessage : "");

		return v1Response.toString(4);
	}

	private int optIntSafely(Object val, int defaultVal) {
		if (val == null) {
			return defaultVal;
		}
		if (val instanceof Number) {
			return ((Number) val).intValue();
		}
		String s = val.toString().trim();
		if (s.isEmpty()) {
			return defaultVal;
		}
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return defaultVal;
		}
	}

	private Object parseNumberSafely(Object val) {
		if (val == null) {
			return 0;
		}
		if (val instanceof Number) {
			return val;
		}
		String s = val.toString().trim();
		if (s.isEmpty()) {
			return 0;
		}
		try {
			if (s.contains(".")) {
				return Double.parseDouble(s);
			} else {
				return Integer.parseInt(s);
			}
		} catch (NumberFormatException e) {
			try {
				return Double.parseDouble(s);
			} catch (NumberFormatException ex) {
				return 0;
			}
		}
	}

	private String mapWarehouseCodeToPlaza(String code, boolean isOrigin) {
		if (code == null) return "";
		String clean = code.trim().toUpperCase();
		if (clean.isEmpty()) return "";

		switch (clean) {
			case "GDC":
			case "GDL":
			case "GDJ":
				return isOrigin ? "GUADALAJARA CENTRO" : "Guadalajara";
			case "MEX":
			case "DF1":
			case "DF2":
			case "DF3":
			case "DF4":
			case "DF5":
				return isOrigin ? "MEXICO CENTRO" : "Mexico";
			case "MTY":
			case "MT1":
			case "MT2":
				return isOrigin ? "MONTERREY CENTRO" : "Monterrey";
			case "QRO":
				return isOrigin ? "QUERETARO CENTRO" : "Queretaro";
			case "PUE":
				return isOrigin ? "PUEBLA CENTRO" : "Puebla";
			case "TOL":
				return isOrigin ? "TOLUCA CENTRO" : "Toluca";
			case "LEO":
				return isOrigin ? "LEON CENTRO" : "Leon";
			case "SLP":
				return isOrigin ? "SAN LUIS POTOSI CENTRO" : "San Luis Potosi";
			case "MER":
				return isOrigin ? "MERIDA CENTRO" : "Merida";
			case "TIJ":
				return isOrigin ? "TIJUANA CENTRO" : "Tijuana";
			case "CUU":
				return isOrigin ? "CHIHUAHUA CENTRO" : "Chihuahua";
			case "CJS":
				return isOrigin ? "CIUDAD JUAREZ CENTRO" : "Ciudad Juarez";
			case "VER":
				return isOrigin ? "VERACRUZ CENTRO" : "Veracruz";
			case "CAN":
				return isOrigin ? "CANCUN CENTRO" : "Cancun";
			case "AGS":
				return isOrigin ? "AGUASCALIENTES CENTRO" : "Aguascalientes";
			case "HMO":
				return isOrigin ? "HERMOSILLO CENTRO" : "Hermosillo";
			case "TRC":
				return isOrigin ? "TORREON CENTRO" : "Torreon";
			case "CUL":
				return isOrigin ? "CULIACAN CENTRO" : "Culiacan";
			case "MZT":
				return isOrigin ? "MAZATLAN CENTRO" : "Mazatlan";
			default:
				if (isOrigin) {
					return clean + " CENTRO";
				} else {
					if (clean.length() > 1) {
						return clean.substring(0, 1).toUpperCase() + clean.substring(1).toLowerCase();
					}
					return clean;
				}
		}
	}
}
