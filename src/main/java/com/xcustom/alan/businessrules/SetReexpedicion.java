/*
 * ==============================================================================================
 * Copyright (c) 2026 J. Jose de Jesus Silva A.
 * Contact: jsilva210985@gmail.com
 * 
 * This business rule class evaluates whether a shipping request requires reforwarding (reexpedición)
 * by invoking Estafeta's V2 REST frequency service and updating the label request.
 * 
 * All rights reserved.
 * ==============================================================================================
 */
package com.xcustom.alan.businessrules;

import java.util.Map;

import org.estafetav2.FrequencyRest.FrequencyClient;
import org.estafetav2.authentication.AuthenticatorClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.integrador.services.AtributoService;

/**
 * Evaluates whether a shipping request requires reforwarding (reexpedición).
 * <p>
 * This business rule queries Estafeta's V2 REST frequency API using the configured client,
 * checks the destination details for reforwarding, and sets the flag in the request context.
 */
@Component
public class SetReexpedicion{
	static final Logger log = LoggerFactory.getLogger(com.xcustom.alan.businessrules.SetReexpedicion.class);

	/**
	 * Executes the business rule to check and set the reforwarding flag.
	 * 
	 * @param context the execution context containing the label request and database service helper.
	 * @return the updated execution context, or {@code null} if an error occurs.
	 */
	public Map<String,Object> run(Map<String,Object> context) {
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = (com.integrador.xml.services.EstafetaLabelRequest) context.get("xmlRequest");
		AtributoService atributoService = (AtributoService) context.get("atributoService");
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
				log.error("No se encontraron atributos de configuracion para EstafetaV2");
				return null;
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
				log.error("No se encontro el JSON de configuracion en los atributos EstafetaV2");
				return null;
			}

			JSONObject jsonObj = new JSONObject(jsonStr);
			if (!jsonObj.has("FrequencyRest")) {
				log.error("El JSON de EstafetaV2 no contiene el nodo 'FrequencyRest'");
				return null;
			}

			JSONArray freqArray = jsonObj.getJSONArray("FrequencyRest");
			if (freqArray.length() == 0) {
				log.error("El nodo 'FrequencyRest' en el JSON de EstafetaV2 esta vacio");
				return null;
			}

			JSONObject freqConfig = freqArray.getJSONObject(0);
			String tokenUrl = freqConfig.optString("tokenUrl", null);
			String clientId = freqConfig.optString("clientId", null);
			String clientSecret = freqConfig.optString("clientSecret", null);
			String scope = freqConfig.optString("scope", null);
			String apiKey = freqConfig.optString("apiKey", null);
			String url = freqConfig.optString("url", null);

			if (tokenUrl == null || clientId == null || clientSecret == null || scope == null || apiKey == null || url == null) {
				log.error("Faltan parametros requeridos en la configuracion 'FrequencyRest' del JSON de EstafetaV2");
				return null;
			}

			String cpOrigen = xmlRequest.getOrigenZipCode();
			String cpDestino = xmlRequest.getZipCode();
			log.info("\t\tInvocando Frequency V2 para CP Origen: " + cpOrigen + " -> CP Destino: " + cpDestino);

			AuthenticatorClient authClient = new AuthenticatorClient(tokenUrl, clientId, clientSecret, scope);
			FrequencyClient freqClient = new FrequencyClient(url, apiKey, authClient);
			String serviceResponse = freqClient.execute(cpOrigen, cpDestino);

			boolean reexpedicion = false;
			JSONObject v2Json = new JSONObject(serviceResponse);
			if (v2Json.has("frequencies")) {
				JSONArray frequencies = v2Json.getJSONArray("frequencies");
				if (frequencies.length() > 0) {
					JSONObject freqObj = frequencies.getJSONObject(0);
					JSONArray destArray = freqObj.optJSONArray("destinations");
					if (destArray != null && destArray.length() > 0) {
						JSONObject destObj = destArray.getJSONObject(0);
						reexpedicion = destObj.optBoolean("isReexpedition", false);
					}
				}
			}

			xmlRequest.setReexpedicion(reexpedicion ? "1" : "0");
			log.info("\t\tReexpedicion: " + (reexpedicion ? "Si" : "No") );
			context.put("xmlRequest", xmlRequest);
		} catch (Exception e) {
			log.error("Error al obtener reexpedición mediante REST V2: " + e.getMessage(), e);
			return null;
		}
		return context;
	}
}