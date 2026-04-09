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
import com.fedex.api.Rate;
import com.fedex.rate.RateRequest;
import com.integrador.repositories.TokenRepository;
import com.integrador.repositories.UsuarioRepository;
import com.integrador.services.AtributoService;
import com.integrador.services.FedexService;
@RestController
@RequestMapping({"/Fedex"})
public class RateRestController {

	@Autowired FedexService fedexService;
	@Autowired TokenRepository tokenRepository;
	@Autowired AtributoService atributoService;
	@Autowired UsuarioRepository usuarioRepository;
	static final Logger log = LoggerFactory.getLogger(RateRestController.class);
	@PostMapping(value = "/Rate", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> executeRate(@RequestBody String content) throws Exception{
		JSONObject response = new JSONObject();
		String error = "";
		log.info("");
		log.info("================================================================");
		log.info("Fedex RestService / Rate");
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
		if(!request.has("RequestedShipment")) {
			error = "You must specify the RequestedShipment node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		JSONObject requestedShipment = request.getJSONObject("RequestedShipment");
		if(!requestedShipment.has("TotalWeight")) {
			error = "You must specify the RequestedShipment->TotalWeight node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		JSONObject totalWeight = requestedShipment.getJSONObject("TotalWeight");
		if(!totalWeight.has("Value")) {
			error = "You must specify the RequestedShipment->TotalWeight->Value node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(String.valueOf(totalWeight.get("Value")).trim().equalsIgnoreCase("")) {
			error = "You must specify the RequestedShipment->TotalWeight->Value is required";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!requestedShipment.has("Shipper")) {
			error = "You must specify the RequestedShipment->Shipper node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		JSONObject shipper = requestedShipment.getJSONObject("Shipper");
		if(!shipper.has("Contact")) {
			error = "You must specify the RequestedShipment->Shipper->Contact node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!shipper.has("Address")) {
			error = "You must specify the RequestedShipment->Shipper->Address node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		JSONObject shipperContact = shipper.getJSONObject("Contact");
		if(!shipperContact.has("PhoneNumber")) {
			error = "You must specify the RequestedShipment->Shipper->Contact->PhoneNumber node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(String.valueOf(shipperContact.get("PhoneNumber")).trim().equalsIgnoreCase("")) {
			error = "Shipper->Contact->PhoneNumber node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!shipperContact.has("PersonName")) {
			error = "You must specify the RequestedShipment->Shipper->Contact->PersonName node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(String.valueOf(shipperContact.get("PersonName")).trim().equalsIgnoreCase("")) {
			error = "Shipper->Contact->PersonName node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		JSONObject shipperAddress = shipper.getJSONObject("Address");
		if(!shipperAddress.has("StreetLines")) {
			error = "You must specify the RequestedShipment->Shipper->Address->StreetLines node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!shipperAddress.has("City")) {
			error = "You must specify the RequestedShipment->Shipper->Address->City node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!shipperAddress.has("PostalCode")) {
			error = "You must specify the RequestedShipment->Shipper->Address->PostalCode node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!shipperAddress.has("StateOrProvinceCode")) {
			error = "You must specify the RequestedShipment->Shipper->Address->StateOrProvinceCode node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!shipperAddress.has("CountryCode")) {
			error = "You must specify the RequestedShipment->Shipper->Address->CountryCode node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!shipperAddress.has("State")) {
			error = "You must specify the RequestedShipment->Shipper->Address->State node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if(String.valueOf(shipperAddress.get("StreetLines")).trim().equalsIgnoreCase("")) {
			error = "RequestedShipment->Shipper->Address->StreetLines node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(String.valueOf(shipperAddress.get("City")).trim().equalsIgnoreCase("")) {
			error = "RequestedShipment->Shipper->Address->City node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(String.valueOf(shipperAddress.get("PostalCode")).trim().equalsIgnoreCase("")) {
			error = "RequestedShipment->Shipper->Address->PostalCode node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(String.valueOf(shipperAddress.get("StateOrProvinceCode")).trim().equalsIgnoreCase("")) {
			error = "RequestedShipment->Shipper->Address->StateOrProvinceCode node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(String.valueOf(shipperAddress.get("CountryCode")).trim().equalsIgnoreCase("")) {
			error = "RequestedShipment->Shipper->Address->CountryCode node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(String.valueOf(shipperAddress.get("State")).trim().equalsIgnoreCase("")) {
			error = "RequestedShipment->Shipper->Address->State node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}


		if(!requestedShipment.has("Recipient")) {
			error = "You must specify the RequestedShipment->Recipient node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		JSONObject recipient = requestedShipment.getJSONObject("Recipient");
		if(!recipient.has("Contact")) {
			error = "You must specify the RequestedShipment->Recipient->Contact node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if(!recipient.has("Address")) {
			error = "You must specify the RequestedShipment->Recipient->Address node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		JSONObject recipientContact = recipient.getJSONObject("Contact");
		if(!recipientContact.has("PhoneNumber")) {
			error = "You must specify the RequestedShipment->Recipient->Contact->PhoneNumber node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(String.valueOf(recipientContact.get("PhoneNumber")).trim().equalsIgnoreCase("")) {
			error = "Recipient->Contact->PhoneNumber node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!recipientContact.has("PersonName")) {
			error = "You must specify the RequestedShipment->Recipient->Contact->PersonName node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(String.valueOf(recipientContact.get("PersonName")).trim().equalsIgnoreCase("")) {
			error = "Recipient->Contact->PersonName node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		JSONObject recipientAddress = recipient.getJSONObject("Address");
		if(!recipientAddress.has("StreetLines")) {
			error = "You must specify the RequestedShipment->Recipient->Address->StreetLines node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!recipientAddress.has("City")) {
			error = "You must specify the RequestedShipment->Recipient->Address->City node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!recipientAddress.has("PostalCode")) {
			error = "You must specify the RequestedShipment->Recipient->Address->PostalCode node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!recipientAddress.has("StateOrProvinceCode")) {
			error = "You must specify the RequestedShipment->Recipient->Address->StateOrProvinceCode node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!recipientAddress.has("CountryCode")) {
			error = "You must specify the RequestedShipment->Recipient->Address->CountryCode node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!recipientAddress.has("Municipality")) {
			error = "You must specify the RequestedShipment->Recipient->Address->Municipality node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!recipientAddress.has("State")) {
			error = "You must specify the RequestedShipment->Recipient->Address->State node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if(String.valueOf(recipientAddress.get("StreetLines")).trim().equalsIgnoreCase("")) {
			error = "RequestedShipment->Recipient->Address->StreetLines node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(String.valueOf(recipientAddress.get("City")).trim().equalsIgnoreCase("")) {
			error = "RequestedShipment->Recipient->Address->City node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(String.valueOf(recipientAddress.get("PostalCode")).trim().equalsIgnoreCase("")) {
			error = "RequestedShipment->Recipient->Address->PostalCode node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(String.valueOf(recipientAddress.get("StateOrProvinceCode")).trim().equalsIgnoreCase("")) {
			error = "RequestedShipment->Recipient->Address->StateOrProvinceCode node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(String.valueOf(recipientAddress.get("CountryCode")).trim().equalsIgnoreCase("")) {
			error = "RequestedShipment->Recipient->Address->CountryCode node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(String.valueOf(recipientAddress.get("Municipality")).trim().equalsIgnoreCase("")) {
			error = "RequestedShipment->Recipient->Address->Municipality node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(String.valueOf(recipientAddress.get("State")).trim().equalsIgnoreCase("")) {
			error = "RequestedShipment->Recipient->Address->State node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		String account = "Fedex1_Rate";
		if(request.has("Account")){
			account = request.getString("Account").trim();
			if(account.trim().equalsIgnoreCase(""))
				account = "Fedex1_Rate";
		}

		log.info("\tCuenta a usar: "+account);

		Map<String,String> params = atributoService.getByTipoInMap(account);
		String url = params.get("url");
		String accountNumber = params.get("account_number");
		String meterNumber =  params.get("meter_number");
		String key = params.get("key");
		String password = params.get("password");
		com.m160185.models.ServiceData data = new com.m160185.models.ServiceData();

		com.m160185.models.Credentials credential = new com.m160185.models.Credentials();
		credential.setAccountNumber(accountNumber);
		credential.setMeterNumber(meterNumber);
		credential.setKey(key);
		credential.setPassword(password);
		credential.setUrl(url);
		data.setCredential(credential);

		com.m160185.models.Address addressOrigen = new com.m160185.models.Address();
		addressOrigen.setAddress(String.valueOf(shipperAddress.get("StreetLines")));
		addressOrigen.setCity(String.valueOf(shipperAddress.get("City")));
		addressOrigen.setStateCode(String.valueOf(shipperAddress.get("StateOrProvinceCode")));
		addressOrigen.setCountryCode(String.valueOf(shipperAddress.get("CountryCode")));
		addressOrigen.setTel(String.valueOf(shipperContact.get("PhoneNumber")));
		addressOrigen.setCp(String.valueOf(shipperAddress.get("PostalCode")));
		data.setOrigen(addressOrigen);

		com.m160185.models.Address addressDest = new com.m160185.models.Address();
		addressDest.setAddress(String.valueOf(recipientAddress.get("StreetLines")));
		addressDest.setCity(String.valueOf(recipientAddress.get("StreetLines")));
		addressDest.setTel(String.valueOf(recipientContact.get("PhoneNumber")));
		addressDest.setMunicipio(String.valueOf(recipientAddress.get("Municipality")));
		addressDest.setStateCode(String.valueOf(recipientAddress.get("StateOrProvinceCode")));
		addressDest.setState(String.valueOf(recipientAddress.get("State")));
		addressDest.setCp(String.valueOf(recipientAddress.get("PostalCode")));
		addressDest.setName(String.valueOf(recipientContact.get("PersonName")));
		data.setDestination(addressOrigen);

		com.m160185.models.StateCode stateCode = new com.m160185.models.StateCode();
		stateCode.setState(String.valueOf(recipientAddress.get("State")));
		stateCode.setCode(String.valueOf(recipientAddress.get("StateOrProvinceCode")));
		data.setStateCodeDest(stateCode);

		com.m160185.conn.Service2Client fedexRateService = new com.m160185.conn.Service2Client(
				data, 
				String.valueOf(shipperAddress.get("PostalCode")), 
				String.valueOf(recipientAddress.get("PostalCode")), 
				String.valueOf(totalWeight.get("Value"))
				);
		response = fedexRateService.cotizar();
		//log.info("\tResponse: "+response);
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@PostMapping(value = "/Rest/Rate", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> executeRateV2(@RequestBody String content) throws Exception {
		JSONObject response = new JSONObject();
		String error = "";
		log.info("");
		log.info("================================================================");
		log.info("Fedex REST / Rate");
		JSONObject request;
		try{
			request = new JSONObject(content);
		}catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		response = fedexService.validateAccess(request);
		if(response.length() > 0) {
			return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!request.has("RequestedShipment")) {
			error = "You must specify the RequestedShipment node";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		JSONObject requestedShipment = request.getJSONObject("RequestedShipment");
		JSONObject totalWeight = requestedShipment.getJSONObject("TotalWeight");
		double weight = Double.parseDouble(String.valueOf(totalWeight.get("Value")));
		JSONObject shipper = requestedShipment.getJSONObject("Shipper");
		JSONObject shipperAddress = shipper.getJSONObject("Address");
		JSONObject recipient = requestedShipment.getJSONObject("Recipient");
		JSONObject recipientAddress = recipient.getJSONObject("Address");
		String originCp = shipperAddress.getString("PostalCode");
		String destCp = recipientAddress.getString("PostalCode");
		String account = "FedexRest_Rate";
		if(request.has("Account") && !request.isNull("Account")){
			String accountRequest = request.getString("Account").trim();
			if(!accountRequest.isEmpty()) {
				account = accountRequest;
			}
		}
		log.info("\tCuenta a usar: " + account);
		Map<String, String> params = atributoService.getByTipoInMap(account);
		String apiKey = params.get("key");
		String secretKey = params.get("password");
		String accountFedex = params.get("account");
		Map<String,String> paramsFedex = atributoService.getByTipoInMap("Fedex");
		try{
			Authorization auth = new Authorization(paramsFedex.get("authorization"),apiKey,secretKey);
			String token = auth.performAndGetToken();
			Rate rate = new Rate(paramsFedex.get("rate"),accountFedex,token);
			RateRequest rateRequest = new RateRequest(originCp, destCp, weight);
			String responseFedex = rate.getAvailableServices(rateRequest);
			return new ResponseEntity<>(responseFedex, HttpStatus.OK);
		}catch(Exception e) {
			log.error("Error calling FedEx REST", e);
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
