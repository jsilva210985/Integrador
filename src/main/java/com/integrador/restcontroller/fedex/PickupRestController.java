package com.integrador.restcontroller.fedex;

import java.time.LocalDateTime;
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
import com.fedex.api.Pickup;
import com.fedex.pickup.PickupRequest;
import com.integrador.repositories.GuiaIntegradorRepository;
import com.integrador.services.AtributoService;
import com.integrador.services.FedexService;

@RestController
@RequestMapping({"/Fedex"})
public class PickupRestController {
	@Autowired FedexService fedexService;
	@Autowired AtributoService atributoService;
	@Autowired GuiaIntegradorRepository guiaIntegradorRepository;
	static final Logger log = LoggerFactory.getLogger(PickupRestController.class);

	@PostMapping(value = "/Rest/Pickup", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> executePickup(@RequestBody String content) throws Exception {
		JSONObject response = new JSONObject();
		String error = "";
		log.info("");
		log.info("================================================================");
		log.info("Fedex REST / Pickup");
		JSONObject request;
		try {
			request = new JSONObject(content);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		response = fedexService.validateAccess(request);
		if (response.length() > 0)
			return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);

		if (!request.has("OriginDetail")) {
			error = "You must specify the OriginDetail node";
			log.info("\t" + error);
			response.put("validation", error);
			return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		JSONObject originDetail = request.getJSONObject("OriginDetail");
		if (!originDetail.has("PickupLocation")) {
			error = "You must specify the OriginDetail->PickupLocation node";
			log.info("\t" + error);
			response.put("validation", error);
			return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		JSONObject pickupLocation = originDetail.getJSONObject("PickupLocation");
		if (!pickupLocation.has("Contact")) {
			error = "You must specify the OriginDetail->PickupLocation->Contact node";
			log.info("\t" + error);
			response.put("validation", error);
			return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		JSONObject contact = pickupLocation.getJSONObject("Contact");
		if (!contact.has("PersonName") || contact.getString("PersonName").trim().isEmpty()) {
			error = "OriginDetail->PickupLocation->Contact->PersonName is required";
			log.info("\t" + error);
			response.put("validation", error);
			return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (!contact.has("PhoneNumber") || contact.getString("PhoneNumber").trim().isEmpty()) {
			error = "OriginDetail->PickupLocation->Contact->PhoneNumber is required";
			log.info("\t" + error);
			response.put("validation", error);
			return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (!pickupLocation.has("Address")) {
			error = "You must specify the OriginDetail->PickupLocation->Address node";
			log.info("\t" + error);
			response.put("validation", error);
			return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		JSONObject address = pickupLocation.getJSONObject("Address");
		String[] requiredAddressFields = {"StreetLines", "City", "StateOrProvinceCode", "PostalCode", "CountryCode"};
		for (String field : requiredAddressFields) {
			if (!address.has(field) || address.getString(field).trim().isEmpty()) {
				error = "OriginDetail->PickupLocation->Address->" + field + " is required";
				log.info("\t" + error);
				response.put("validation", error);
				return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}
		if (!originDetail.has("ReadyTimestamp") || originDetail.getString("ReadyTimestamp").trim().isEmpty()) {
			error = "OriginDetail->ReadyTimestamp is required";
			log.info("\t" + error);
			response.put("validation", error);
			return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (!originDetail.has("CompanyCloseTime") || originDetail.getString("CompanyCloseTime").trim().isEmpty()) {
			error = "OriginDetail->CompanyCloseTime is required";
			log.info("\t" + error);
			response.put("validation", error);
			return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (!request.has("PackageCount")) {
			error = "PackageCount is required";
			log.info("\t" + error);
			response.put("validation", error);
			return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (!request.has("TotalWeight")) {
			error = "TotalWeight node is required";
			log.info("\t" + error);
			response.put("validation", error);
			return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		JSONObject totalWeight = request.getJSONObject("TotalWeight");
		if (!totalWeight.has("Value")) {
			error = "TotalWeight->Value is required";
			log.info("\t" + error);
			response.put("validation", error);
			return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (!request.has("CarrierCode") || request.getString("CarrierCode").trim().isEmpty()) {
			error = "CarrierCode is required";
			log.info("\t" + error);
			response.put("validation", error);
			return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		String account = "FedexRest_Ship";
		if (request.has("Account") && !request.isNull("Account")) {
			String accountRequest = request.getString("Account").trim();
			if (!accountRequest.isEmpty()) {
				account = accountRequest;
			}
		}
		log.info("\tAccount used: " + account);

		Map<String, String> params = atributoService.getByTipoInMap(account);
		if (params == null || params.isEmpty()) {
			error = "Configuration for account " + account + " not found";
			log.info("\t" + error);
			response.put("validation", error);
			return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		String apiKey = params.get("key");
		String secretKey = params.get("password");
		String accountFedex = params.get("account");
		Map<String, String> paramsFedex = atributoService.getByTipoInMap("Fedex");
		Map<String, String> paramsAccount = atributoService.getByTipoInMap(account);
		try {
			Authorization auth = new Authorization(paramsFedex.get("authorization"), apiKey, secretKey);
			String token = auth.performAndGetToken();
			Pickup pickupClient = new Pickup(paramsFedex.get("pickup"), accountFedex, token);
			log.info("\tMapping request to FedEx Pickup API...");
			PickupRequest.Party origin = new PickupRequest.Party(
					contact.getString("PersonName"),
					contact.optString("CompanyName", ""),
					contact.getString("PhoneNumber"),
					address.getString("StreetLines"),
					null,
					address.getString("City"),
					address.getString("StateOrProvinceCode"),
					address.getString("PostalCode"),
					address.getString("CountryCode")
			);
			origin.setLocationDetails(
					originDetail.optString("PickupLocationType", "NONE"),
					originDetail.optString("BuildingPart", "NONE"),
					originDetail.optString("BuildingPartDescription", "")
			);

			LocalDateTime ready = LocalDateTime.parse(originDetail.getString("ReadyTimestamp"));
			java.time.LocalTime closeTime = java.time.LocalTime.parse(originDetail.getString("CompanyCloseTime"));
			LocalDateTime close = ready.with(closeTime);

			PickupRequest createReq = new PickupRequest(
					request.getString("CarrierCode"),
					request.getInt("PackageCount"),
					Double.parseDouble(String.valueOf(totalWeight.get("Value"))),
					origin,
					ready,
					close
			);
			if (request.has("Remarks"))
				createReq.setRemarks(request.getString("Remarks"));
			String createRes = pickupClient.create(createReq);
			log.info("\tFedEx Response: " + createRes);
			return new ResponseEntity<>(createRes, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error calling FedEx Pickup REST", e);
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}