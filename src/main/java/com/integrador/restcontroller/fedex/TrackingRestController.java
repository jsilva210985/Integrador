package com.integrador.restcontroller.fedex;

import java.util.Map;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fedex.api.Authorization;
import com.fedex.api.Tracking;
import com.integrador.services.AtributoService;
import com.integrador.services.FedexService;
@RestController
@RequestMapping({"/Fedex"})
public class TrackingRestController {
	@Autowired FedexService fedexService;
	@Autowired AtributoService atributoService;

	static final Logger log = LoggerFactory.getLogger(TrackingRestController.class);

	@PostMapping(value = "/Tracking", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> executeShip(@RequestBody String content) throws Exception{
		JSONObject response = new JSONObject();
		String error = "";
		log.info("");
		log.info("== Fedex RestService / Tracking ==>");
		JSONObject request = new JSONObject();
		try {
			request = new JSONObject(content);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		response = fedexService.validateAccess(request);
		if(response.length()>0) {
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!request.has("Tracking")) {
			error = "You must specify the RequestedShipment node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		String account = "Fedex1_Ship";
		if(request.has("Account")){
			account = request.getString("Account").trim();
			if(account.trim().equalsIgnoreCase(""))
				account = "Fedex1_Ship";
		}

		log.info("\tTracking: "+request.getString("Tracking"));
		log.info("\tAccount: "+account);
		Map<String,String> params = atributoService.getByTipoInMap(account);
		if(params.isEmpty())
			params = atributoService.getByTipoInMap("Fedex1_Ship");
		com.m160185.conn.Service3Client c = new com.m160185.conn.Service3Client();
		JSONObject r = c.call(params.get("url").toString(), params.get("key").toString(), params.get("password").toString(), params.get("account_number").toString(), params.get("meter_number").toString(), request.getString("Tracking"));
		log.info("\tResponse: ");
		log.info("\t\tSeverity: "+r.getJSONObject("CustomResponse").getString("Severity"));
		log.info("\t\tMessage: "+r.getJSONObject("CustomResponse").getString("Message"));
		log.info("\t\tDescripcion: "+r.getJSONObject("CustomResponse").getString("Descripcion"));
		log.info("\t\tCode: "+r.getJSONObject("CustomResponse").getString("Code"));
		return new ResponseEntity<String>(r.toString(), HttpStatus.OK);
	}

	@PostMapping(value = "/Rest/Tracking", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> executeTrackingV2(@RequestBody String content) throws Exception{
		JSONObject response = new JSONObject();
		String error = "";
		log.info("");
		log.info("== Fedex RestService / Tracking ==>");
		JSONObject request = new JSONObject();
		try {
			request = new JSONObject(content);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		response = fedexService.validateAccess(request);
		if(response.length()>0) {
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!request.has("Tracking")) {
			error = "You must specify the RequestedShipment node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		Map<String,String> paramsFedex = atributoService.getByTipoInMap("Fedex");
		String _default = "FedexRestDEV_Tracking";
		String account = request.has("Account") ? request.optString("Account", "").trim(): _default;

		if(account.isEmpty())
			account = _default;
		Map<String, String> params = atributoService.getByTipoInMap(account);

		if (params == null || params.isEmpty()) {
			params = atributoService.getByTipoInMap(_default);
		}
		if (params == null || params.isEmpty()) {
			params = atributoService.getByTipoInMap("FedexRest_Ship_FedexRestDEV_Tracking");
		}

		log.info("\tTracking: "+request.getString("Tracking"));
		log.info("\tAccount: "+account);
		Authorization auth = new Authorization(paramsFedex.get("authorization"),params.get("key") ,params.get("password"));
		String token = auth.performAndGetToken();
		Tracking t = new Tracking(params.get("url"), token, request.getString("Tracking").trim());
		response = new JSONObject(t.perform());
		log.info("\tResponse: ");
		log.info("\t\t"+response);
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}
}
