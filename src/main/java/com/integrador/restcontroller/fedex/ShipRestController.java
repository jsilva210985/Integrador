package com.integrador.restcontroller.fedex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.estafeta.util.Util;
import com.integrador.carriers.Fedex;
import com.integrador.models.GuiaIntegrador;
import com.integrador.repositories.GuiaIntegradorRepository;
import com.integrador.services.AtributoService;
import com.integrador.services.FedexService;
@RestController
@RequestMapping({"/Fedex"})
public class ShipRestController {
	@Autowired FedexService fedexService;
	@Autowired AtributoService atributoService;
	@Autowired GuiaIntegradorRepository guiaIntegradorRepository;

	@Value("${fedex.restservice.br.beforesend}") String beforeSendBRRest;
	@Value("${app.env}") String env;

	static final Logger log = LoggerFactory.getLogger(ShipRestController.class);

	@PostMapping(value = "/Ship", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> executeShip(@RequestBody String content) throws Exception{
		JSONObject response = new JSONObject();
		log.info("");
		log.info("================================================================");
		log.info("Fedex RestService / Ship ==>");
		JSONObject request = new JSONObject();
		try {
			request = new JSONObject(content);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		request = com.integrador.util.Util.cleanRequest(request);
		GuiaIntegrador g = fedexService.saveRequest(request);
		response = fedexService.validateAccess(request);
		if(response.length()>0) {
			g.setEstatus(response.has("validation") ? String.valueOf(response.get("validation")) : "");
			guiaIntegradorRepository.save(g);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		response = validateMandatoryInRequest(request);
		if(response.length()>0) {
			g.setEstatus(response.has("validation") ? String.valueOf(response.get("validation")) : "");
			guiaIntegradorRepository.save(g);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		response = validateMandatoryValues(request);
		if(response.length()>0) {
			g.setEstatus(response.has("validation") ? String.valueOf(response.get("validation")) : "");
			guiaIntegradorRepository.save(g);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		String account = "Fedex1_Ship";
		if(request.has("Account")){
			account = request.getString("Account").trim();
			if(account.trim().equalsIgnoreCase(""))
				account = "Fedex1_Ship";
		}

		Map<String,String> params = atributoService.getByTipoInMap(account);
		if(params.isEmpty())
			params = atributoService.getByTipoInMap("Fedex_Ship");

		log.info("\tCuenta a usar: "+account);

		String url = params.get("url");
		String accountNumber = params.get("account_number");
		String meterNumber =  params.get("meter_number");
		String key = params.get("key");
		String password = params.get("password");

		log.info("\tAccount: "+url);
		log.info("\tURL: "+accountNumber);
		log.info("\tAccount Number: "+meterNumber);
		log.info("\tKey: "+key);
		log.info("\tPassword: "+password);

		Fedex fedex = new Fedex();
		fedex.setParams(params);
		fedex.setRequest(request);
		fedex.setGuiasIntegradorRepository(guiaIntegradorRepository);
		fedex.setGuiaIntegrador(g);
		fedex.setBusinessRuleBeforeSend(beforeSendBRRest);
		fedex.setEnv(env);
		response = (JSONObject) fedex.executeRestService();
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@PostMapping(value = "/Rest/Ship", consumes = {MediaType.APPLICATION_JSON_VALUE},  produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> executeShipRest(@RequestBody String content) throws Exception {
		JSONObject response = new JSONObject();
		log.info("");
		log.info("================================================================");
		log.info("Fedex RestService / Rest/Ship ==>");

		JSONObject request = new JSONObject();
		try{
			request = new JSONObject(content);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		request = com.integrador.util.Util.cleanRequest(request);
		GuiaIntegrador g = fedexService.saveRequest(request);
		response = fedexService.validateAccess(request);
		if(response.length() > 0) {
			g.setEstatus(response.has("validation") ? String.valueOf(response.get("validation")) : Util.EMPTY_STRING);
			guiaIntegradorRepository.save(g);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		response = validateMandatoryInRequest(request);
		if(response.length() > 0) {
			g.setEstatus(response.has("validation") ? String.valueOf(response.get("validation")) : Util.EMPTY_STRING);
			guiaIntegradorRepository.save(g);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		response = validateMandatoryValues(request);
		if(response.length() > 0) {
			g.setEstatus(response.has("validation") ? String.valueOf(response.get("validation")) : Util.EMPTY_STRING);
			guiaIntegradorRepository.save(g);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		String account = "FedexRest_Ship";
		if(request.has("Account") && !request.isNull("Account")){
			String accountRequest = request.getString("Account").trim();
			if(!accountRequest.isEmpty()) {
				account = accountRequest;
			}
		}

		Map<String, String> params = atributoService.getByTipoInMap(account);
		Map<String, String> fedexParams = atributoService.getByTipoInMap("Fedex");
		if(params.isEmpty())
			params = atributoService.getByTipoInMap("FedexRest_Ship");

		log.info("\tCuenta a usar: " + account);
		String url = params.get("url");
		String accountNumber = params.get("account");
		String secretKey = params.get("key");
		String secretPassword = params.get("password");
		log.info("\tAccount: " + url);
		log.info("\tURL: " + accountNumber);
		log.info("\tKey: " + secretKey);
		log.info("\tPassword: " + secretPassword);
		params.put("authorization", fedexParams.get("authorization").trim());
		Fedex fedex = new Fedex();
		fedex.setParams(params);
		fedex.setRequest(request);
		fedex.setGuiasIntegradorRepository(guiaIntegradorRepository);
		fedex.setGuiaIntegrador(g);
		fedex.setBusinessRuleBeforeSend(beforeSendBRRest);
		fedex.setEnv(env);
		response = (JSONObject) fedex.executeRestServiceV2();
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	public JSONObject validateMandatoryInRequest(JSONObject request) {
		JSONObject response = new JSONObject();
		List<String> fieldList = new ArrayList<String>();

		fieldList.add("OrigenContactName");
		fieldList.add("OrigenAddress1");
		fieldList.add("OrigenAddress2");
		fieldList.add("OrigenExtNum");
		fieldList.add("OrigenIntNum");
		fieldList.add("OrigenCity");
		fieldList.add("OrigenState");
		fieldList.add("OrigenNeighborhood");
		fieldList.add("OrigenZipCode");
		fieldList.add("OrigenCorporateName");
		fieldList.add("OrigenPhoneNumber");
		fieldList.add("OrigenReference");
		fieldList.add("ContactName");
		fieldList.add("Address1");
		fieldList.add("Address2");
		fieldList.add("ExtNum");
		fieldList.add("IntNum");
		fieldList.add("Neighborhood");
		fieldList.add("City");
		fieldList.add("State");
		fieldList.add("ZipCode");
		fieldList.add("CorporateName");
		fieldList.add("PhoneNumber");
		fieldList.add("Reference");
		fieldList.add("AditionalInfo");
		fieldList.add("ServiceTypeId");
		fieldList.add("Content");
		fieldList.add("NumberOfLabels");
		fieldList.add("ParcelTypeId");
		fieldList.add("Weight");
		fieldList.add("Height");
		fieldList.add("Length");
		fieldList.add("Width");
		fieldList.add("PaperType");
		fieldList.add("ContentDescription");
		fieldList.add("DeliveryToEstafetaOffice");
		for(String field : fieldList) {
			if(!request.has(field)) {
				String error = "You must specify the "+field+" node";
				log.info("\t"+error);
				response.put("validation", error);
				return response;
			}
		}
		return response;
	}

	public JSONObject validateMandatoryValues(JSONObject request) {
		JSONObject response = new JSONObject();
		List<String> fieldList = new ArrayList<String>();

		fieldList.add("OrigenContactName");
		fieldList.add("OrigenAddress1");
		fieldList.add("OrigenExtNum");
		fieldList.add("OrigenCity");
		fieldList.add("OrigenState");
		fieldList.add("OrigenNeighborhood");
		fieldList.add("OrigenZipCode");
		fieldList.add("OrigenPhoneNumber");
		fieldList.add("OrigenReference");
		fieldList.add("ContactName");
		fieldList.add("Address1");
		fieldList.add("ExtNum");
		fieldList.add("Neighborhood");
		fieldList.add("City");
		fieldList.add("State");
		fieldList.add("ZipCode");
		fieldList.add("PhoneNumber");
		fieldList.add("Reference");
		fieldList.add("ServiceTypeId");
		fieldList.add("Content");
		fieldList.add("NumberOfLabels");
		fieldList.add("Weight");
		fieldList.add("Height");
		fieldList.add("Length");
		fieldList.add("Width");
		fieldList.add("PaperType");
		for(String field : fieldList) {
			if(String.valueOf(request.get(field)).trim().equalsIgnoreCase("") || request.get(field) == null || String.valueOf(request.get(field)).equalsIgnoreCase("null") ) {
				String error = "The "+field+" is required";
				log.info("\t"+error);
				response.put("validation", error);
				return response;
			}
		}
		return response;
	}
}
