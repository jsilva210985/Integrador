package com.integrador.carriers;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fedex.api.Authorization;
import com.fedex.api.Ship;
import com.fedex.ship.ShipRequest;
import com.fedex.ship.stub.ProcessShipmentReply;
import com.integrador.models.GuiaIntegrador;
import com.integrador.repositories.GuiaIntegradorRepository;
import com.integrador.services.AtributoService;
@SuppressWarnings("all")
public class Fedex implements Carrier{

	private Map<String,String> params;
	private com.integrador.xml.fedex.services.FedexLabelRequest xmlRequest;
	private com.integrador.xml.fedex.services.FedexLabelResponse xmlResponse;
	private JSONObject request;
	private JSONObject response;
	private String businessRuleBeforeSend;
	private String businessRuleAfterSend;
	private Map<String,String> account;
	private GuiaIntegradorRepository guiaIntegradorRepository;
	GuiaIntegrador guiaIntegrador;
	private AtributoService atributoService;
	static final Logger log = LoggerFactory.getLogger(Fedex.class);
	static final String _tab2 = "\t\t";
	private String env;

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object executeWebService() throws Exception{
		try{
			Map<String,Object> context = getContext();
			Boolean hasSetAccountBR = false;
			if(this.getBusinessRuleBeforeSend()!=null && !this.getBusinessRuleBeforeSend().trim().equalsIgnoreCase("")) {
				String[] list = this.getBusinessRuleBeforeSend().split(",");
				log.info("");
				log.info("\tBeforeSendBR: "+this.getBusinessRuleBeforeSend());
				for(String br : list) {
					Object tempClass = Class.forName(br).newInstance();
					Class classLoaded = tempClass.getClass();
					Class[] arguments = new Class[1];
					arguments[0] = Map.class;
					Method metodo = classLoaded.getDeclaredMethod("run", arguments);
					context = (Map<String,Object>) metodo.invoke(tempClass, context);
					if(br.indexOf("SetAccount")>-1)
						hasSetAccountBR = true;
				}
				this.setContext(context);
			}
			Map<String,String> params = new HashMap<String, String>();
			if(hasSetAccountBR) {
				params = this.getAccount()!=null ? this.getAccount() : this.getParams();
			}else {
				params = this.getParams();
			}
			GuiaIntegrador g = this.getGuiaIntegrador();
			com.integrador.xml.fedex.services.FedexLabelRequest xmlRequest = this.getXmlRequest();
			com.integrador.xml.fedex.services.FedexLabelResponse xmlResponse = this.getXmlResponse();
			GuiaIntegradorRepository guiaIntegradorRepository = this.getGuiasIntegradorRepository();
			com.m160185.models.ServiceData data = new com.m160185.models.ServiceData();

			com.m160185.models.Credentials credential = new com.m160185.models.Credentials();
			credential.setAccountNumber(params.get("account_number").toString());
			credential.setMeterNumber(params.get("meter_number").toString());
			credential.setKey(params.get("key").toString());
			credential.setPassword(params.get("password").toString());
			credential.setUrl(params.get("url").toString());
			data.setCredential(credential);

			log.info("");
			log.info("\tAccount Parameters");
			log.info(_tab2+"account_number: "+params.get("account_number").toString());
			log.info(_tab2+"meter_number: "+params.get("meter_number").toString());
			log.info(_tab2+"key: "+params.get("key").toString());
			log.info(_tab2+"password: "+params.get("password").toString());
			log.info(_tab2+"url: "+params.get("url").toString());
			String _addressOrigen = xmlRequest.getOrigenAddress1();
			if(xmlRequest.getOrigenAddress2()!=null && !xmlRequest.getOrigenAddress2().equalsIgnoreCase("")) {
				_addressOrigen += " "+xmlRequest.getOrigenAddress2();
			}
			if(xmlRequest.getOrigenExtNum()!=null && !xmlRequest.getOrigenExtNum().equalsIgnoreCase("")) {
				_addressOrigen += " "+xmlRequest.getOrigenExtNum();
			}

			String addressOrigenColonia = xmlRequest.getOrigenNeighborhood();
			if(xmlRequest.getOrigenIntNum()!=null && !xmlRequest.getOrigenIntNum().equalsIgnoreCase("")) {
				addressOrigenColonia = xmlRequest.getOrigenIntNum()+" "+xmlRequest.getOrigenNeighborhood();
			}

			com.m160185.models.Address addressOrigen = new com.m160185.models.Address();
			addressOrigen.setAddress(_addressOrigen.toUpperCase());
			addressOrigen.setCity(xmlRequest.getOrigenCity().toUpperCase());
			addressOrigen.setState(xmlRequest.getOrigenState().toUpperCase());
			addressOrigen.setCp(xmlRequest.getOrigenZipCode());
			addressOrigen.setName(xmlRequest.getOrigenContactName().toUpperCase());
			addressOrigen.setCompanyName(xmlRequest.getOrigenCorporateName().toUpperCase());
			addressOrigen.setTel(xmlRequest.getOrigenPhoneNumber());
			addressOrigen.setColonia(addressOrigenColonia);
			addressOrigen.setReference(xmlRequest.getOrigenReference());
			data.setOrigen(addressOrigen);

			com.m160185.models.StateCode stateCodeOrigen = new com.m160185.models.StateCode();
			stateCodeOrigen.setState(xmlRequest.getOrigenState());
			stateCodeOrigen.setCode(findStateCode(xmlRequest.getOrigenState()));
			data.setStateCodeOrigen(stateCodeOrigen);
			String address = xmlRequest.getAddress1();
			if(xmlRequest.getAddress2()!=null && !xmlRequest.getAddress2().equalsIgnoreCase("")) {
				address += " "+xmlRequest.getAddress2();
			}
			if(xmlRequest.getExtNum()!=null && !xmlRequest.getExtNum().equalsIgnoreCase("")) {
				address += " "+xmlRequest.getExtNum();
			}
			String addressDestColonia = xmlRequest.getNeighborhood().toUpperCase();
			if(xmlRequest.getIntNum()!=null && !xmlRequest.getIntNum().equalsIgnoreCase("")) {
				addressDestColonia = xmlRequest.getIntNum()+" "+ xmlRequest.getNeighborhood().toUpperCase();
			}

			com.m160185.models.Address addressDest = new com.m160185.models.Address();
			addressDest.setTel(xmlRequest.getPhoneNumber());
			addressDest.setName(xmlRequest.getContactName().toUpperCase());
			addressDest.setCompanyName(xmlRequest.getCorporateName().toUpperCase());
			addressDest.setAddress(address.toUpperCase());
			addressDest.setCity(xmlRequest.getCity().toUpperCase());
			addressDest.setMunicipio(xmlRequest.getNeighborhood().toUpperCase());
			addressDest.setCp(xmlRequest.getZipCode());
			addressDest.setColonia(addressDestColonia);
			addressDest.setReference(xmlRequest.getReference().toUpperCase());
			data.setDestination(addressDest);

			com.m160185.models.StateCode stateCodeDest = new com.m160185.models.StateCode();
			stateCodeDest.setState(xmlRequest.getState());
			stateCodeDest.setCode(findStateCode(xmlRequest.getState()));
			data.setStateCodeDest(stateCodeDest);

			int length  = xmlRequest.getLength()==null ? 1 : new BigDecimal(xmlRequest.getLength()).intValue();
			int height  = xmlRequest.getHeight()==null ? 1 : new BigDecimal(xmlRequest.getHeight()).intValue();
			int width  = xmlRequest.getWidth()==null ? 1 : new BigDecimal(xmlRequest.getWidth()).intValue();

			data.setLength(length);
			data.setHeight(height);
			data.setWidth(width);

			BigDecimal ensureValue = new BigDecimal(0);
			try{
				ensureValue = new BigDecimal(xmlRequest.getInsuredValue()==null ? "0.00" : xmlRequest.getInsuredValue());
			}catch (Exception e) {
				ensureValue = new BigDecimal(0);
			}

			com.m160185.models.Insurance insurance = new com.m160185.models.Insurance();
			Boolean seguro = Boolean.parseBoolean(xmlRequest.getInsured());
			BigDecimal valorEnvio = ensureValue;
			insurance.setHas(seguro);
			insurance.setValue(valorEnvio);
			data.setInsurance(insurance);
			log.info("");
			log.info("\tService Type: "+xmlRequest.getServiceTypeId());
			log.info("");

			com.m160185.conn.Service1Client guiaService = new com.m160185.conn.Service1Client(data, xmlRequest.getServiceTypeId(), new BigDecimal(xmlRequest.getWeight()));
			JSONObject responseService = guiaService.createv2();
			ProcessShipmentReply fedexResponse =  guiaService.getProcessShipmentReply();

			log.info("");
			log.info("\tXMLRequest");
			log.info(_tab2+fedexResponse.getRequestXML());
			log.info("");
			log.info("\tXMLResponse");
			log.info(_tab2+fedexResponse.getResponseXML());

			g.setRequest(fedexResponse.getRequestXML().toString().trim());
			g.setResponse(fedexResponse.getResponseXML().toString().trim());

			context.put("fedexresponse", fedexResponse);

			g.setVia(xmlRequest.getVia()!=null && !xmlRequest.getVia().trim().equalsIgnoreCase("") ? xmlRequest.getVia() : "");
			g = guiaIntegradorRepository.save(g);
			if(responseService==null) {
				String fedexXMLResponse = fedexResponse.getResponseXML();
				fedexXMLResponse= fedexXMLResponse.replaceAll("v25:", "");
				fedexXMLResponse = fedexXMLResponse.substring(fedexXMLResponse.indexOf("<ProcessShipmentReply"), fedexXMLResponse.indexOf("</SOAP"));
				JSONObject responseError = XML.toJSONObject(fedexXMLResponse);
				JSONObject ProcessShipmentReply = responseError.getJSONObject("ProcessShipmentReply");
				JSONObject notifications = new JSONObject();
				Object notificationObj = ProcessShipmentReply.get("Notifications");
				if(notificationObj instanceof JSONObject) {
					notifications = ProcessShipmentReply.getJSONObject("Notifications");
				}else if (notificationObj instanceof JSONArray) {
					JSONArray _temp = ProcessShipmentReply.getJSONArray("Notifications");
					notifications = _temp.getJSONObject(0);
				}
				xmlResponse.setResponseCode(String.valueOf(notifications.get("Code")));
				xmlResponse.setResponseDescription(String.valueOf(notifications.get("Message")));
				log.info("");
				log.info("\tNotifications");
				log.info(_tab2+String.valueOf(notifications.get("Message")));
				g.setEstatus(String.valueOf(notifications.get("Message")));
				g = guiaIntegradorRepository.save(g);
			}else {
				JSONArray packageDetails = responseService.getJSONArray("package_details");
				for (int i = 0; i < packageDetails.length(); i++) {
					JSONObject packageDetail = packageDetails.getJSONObject(i);
					JSONArray labels = packageDetail.getJSONArray("labels");
					JSONObject label = labels.getJSONObject(0);
					String encodedFile = Base64.getEncoder().encodeToString((byte[]) label.get("file"));
					xmlResponse.setFile(encodedFile);
				}
				JSONObject masterTrackingnumber = responseService.has("master_tracking_number") ? responseService.getJSONObject("master_tracking_number") : new JSONObject();
				if(masterTrackingnumber.has("tracking_number")) {
					xmlResponse.setTracking(String.valueOf(masterTrackingnumber.get("tracking_number")));
					log.info("\tTracking Number: "+String.valueOf(masterTrackingnumber.get("tracking_number")));
					g.setTracking(String.valueOf(masterTrackingnumber.get("tracking_number")));
				}
				log.info("\tAccount");
				for (Iterator<Map.Entry<String, String>> entries = params.entrySet().iterator(); entries.hasNext(); ) {
					Map.Entry<String, String> entry = entries.next();
					log.info("\t\t"+entry.getKey()+" : "+entry.getValue());
				}
				g.setEstatus("");
				g = guiaIntegradorRepository.save(g);
			}
			if(this.getBusinessRuleAfterSend()!=null && !this.getBusinessRuleAfterSend().trim().equalsIgnoreCase("")) {
				String[] list = this.getBusinessRuleAfterSend().split(",");
				log.info("");
				log.info("\tAfterSendBR: "+this.getBusinessRuleAfterSend());
				for(String br : list) {
					Object tempClass = Class.forName(br).newInstance();
					Class classLoaded = tempClass.getClass();
					Class[] arguments = new Class[1];
					arguments[0] = Map.class;
					Method metodo = classLoaded.getDeclaredMethod("run", arguments);
					context = (Map<String,Object>) metodo.invoke(tempClass, context);
				}
			}

		}catch (Exception e) {
			log.error(e.getMessage() + " "+e.getCause());
			e.printStackTrace();
		}
		return xmlResponse;
	}

	@Override
	public Object executeRestService() throws Exception {
		this.setResponse(new JSONObject());
		Map<String,String> params = this.getParams();
		GuiaIntegrador g = this.getGuiaIntegrador();
		JSONObject request = this.getRequest();
		GuiaIntegradorRepository guiaIntegradorRepository = this.getGuiasIntegradorRepository();
		com.m160185.models.ServiceData data = new com.m160185.models.ServiceData();

		com.m160185.models.Credentials credential = new com.m160185.models.Credentials();
		credential.setAccountNumber(params.get("account_number").toString());
		credential.setMeterNumber(params.get("meter_number").toString());
		credential.setKey(params.get("key").toString());
		credential.setPassword(params.get("password").toString());
		credential.setUrl(params.get("url").toString());
		data.setCredential(credential);

		if(params.get("ponumber")!=null && !params.get("ponumber").toString().trim().equalsIgnoreCase(""))
			data.setPONumber(params.get("ponumber").toString().trim());

		Map<String,Object> context = getJSONContext();
		Boolean hasSetAccountBR = false;
		Boolean hasBRAlgoritmo = false;
		if(this.getBusinessRuleBeforeSend()!=null && !this.getBusinessRuleBeforeSend().trim().equalsIgnoreCase("")) {
			String[] list = this.getBusinessRuleBeforeSend().split(",");
			log.info("\tBeforeSendBR: "+this.getBusinessRuleBeforeSend());
			for(String br : list) {
				if(br.trim().endsWith("SetAccountAlgoritmoRentabilidad")){
					hasBRAlgoritmo = true;
				}
				Object tempClass = Class.forName(br).newInstance();
				Class classLoaded = tempClass.getClass();
				Class[] arguments = new Class[1];
				arguments[0] = Map.class;
				Method metodo = classLoaded.getDeclaredMethod("run", arguments);
				context = (Map<String,Object>) metodo.invoke(tempClass, context);
				if(br.indexOf("SetAccount")>-1)
					hasSetAccountBR = true;
			}
			this.setContextJSON(context);
		}

		if(hasBRAlgoritmo) {
			JSONObject fedex = (JSONObject) context.get("fedex");
			if(request.getString("ServiceTypeId").equalsIgnoreCase("FEDEX_EXPRESS_SAVER")){
				JSONObject _terrestre = fedex.getJSONObject("terrestre");
				JSONObject conf = _terrestre.getJSONObject("configuracion");
				JSONObject ship = conf.getJSONObject("Fedex_Ship");
				credential.setAccountNumber(ship.get("account_number").toString());
				credential.setMeterNumber(params.get("meter_number").toString());
				credential.setKey(params.get("key").toString());
				credential.setPassword(params.get("password").toString());
				credential.setUrl(params.get("url").toString());

				log.info("\tAlgoritmo de rentabilidad - Cuenta precio mas bajo - Terrestre");
				log.info("\t\tAccountNumber: "+ship.get("account_number").toString());
				log.info("\t\tMeterNumber: "+ship.get("meter_number").toString());
				log.info("\t\tKey: "+ship.get("key").toString());
				log.info("\t\tPassword: "+ship.get("password").toString());
				log.info("\t\tURL: "+ship.get("url").toString());

				data.setCredential(credential);
			}else{
				JSONObject _express = fedex.getJSONObject("express");
				JSONObject conf = _express.getJSONObject("configuracion");
				JSONObject ship = conf.getJSONObject("Fedex_Ship");
				credential.setAccountNumber(ship.get("account_number").toString());
				credential.setMeterNumber(params.get("meter_number").toString());
				credential.setKey(params.get("key").toString());
				credential.setPassword(params.get("password").toString());
				credential.setUrl(params.get("url").toString());

				log.info("\tAlgoritmo de rentabilidad - Cuenta precio mas bajo - Express");
				log.info("\t\tAccountNumber: "+ship.get("account_number").toString());
				log.info("\t\tMeterNumber: "+ship.get("meter_number").toString());
				log.info("\t\tKey: "+ship.get("key").toString());
				log.info("\t\tPassword: "+ship.get("password").toString());
				log.info("\t\tURL: "+ship.get("url").toString());

				data.setCredential(credential);
			}
		}

		if(request.has("Account")){

			credential.setAccountNumber(request.get("Account_account_number").toString());
			credential.setMeterNumber(request.get("Account_meter_number").toString());
			credential.setKey(request.get("Account_key").toString());
			credential.setPassword(request.get("Account_password").toString());
			credential.setUrl(request.get("Account_url").toString());

			log.info("\t\tCuenta: "+request.get("Account"));
			log.info("\t\tAccountNumber: "+request.get("Account_account_number").toString());
			log.info("\t\tMeterNumber: "+request.get("Account_meter_number").toString());
			log.info("\t\tKey: "+request.get("Account_key").toString());
			log.info("\t\tPassword: "+request.get("Account_password").toString());
			log.info("\t\tURL: "+request.get("Account_url").toString());
		}


		String _addressOrigen = String.valueOf(request.get("OrigenAddress1"));
		String addressOrigenColonia = "";
		if(request.get("OrigenExtNum")!=null && !String.valueOf(request.get("OrigenExtNum")).equalsIgnoreCase(""))
			addressOrigenColonia = ("#").concat(String.valueOf(request.get("OrigenExtNum")));
		if(request.get("OrigenExtNum")!=null && !String.valueOf(request.get("OrigenExtNum")).equalsIgnoreCase(""))
			addressOrigenColonia += ","+" "+("Int")+" "+String.valueOf(request.get("OrigenExtNum"));
		addressOrigenColonia += " "+ String.valueOf(request.get("OrigenNeighborhood"));

		com.m160185.models.Address addressOrigen = new com.m160185.models.Address();
		addressOrigen.setAddress(_addressOrigen.toUpperCase());
		addressOrigen.setCity(String.valueOf(request.get("OrigenCity")).toUpperCase());
		addressOrigen.setState(String.valueOf(request.get("OrigenState")).toUpperCase());
		addressOrigen.setCp(String.valueOf(request.get("OrigenZipCode")));
		addressOrigen.setName(String.valueOf(request.get("OrigenContactName")).toUpperCase());
		addressOrigen.setCompanyName(String.valueOf(request.get("OrigenCorporateName")).toUpperCase());
		addressOrigen.setTel(String.valueOf(request.get("OrigenPhoneNumber")));
		addressOrigen.setColonia(addressOrigenColonia.toUpperCase());
		addressOrigen.setReference(String.valueOf(request.get("OrigenReference")).toUpperCase());
		data.setOrigen(addressOrigen);

		com.m160185.models.StateCode stateCodeOrigen = new com.m160185.models.StateCode();
		stateCodeOrigen.setState(String.valueOf(request.get("OrigenState")));
		stateCodeOrigen.setCode(findStateCode(String.valueOf(request.get("OrigenState"))));
		data.setStateCodeOrigen(stateCodeOrigen);

		String address = String.valueOf(request.get("Address1"));

		String addressDestColonia = "";
		if(request.get("ExtNum")!=null && !String.valueOf(request.get("ExtNum")).equalsIgnoreCase(""))
			addressDestColonia = ("#").concat(String.valueOf(request.get("ExtNum")));
		if(request.get("IntNum")!=null && !String.valueOf(request.get("IntNum")).equalsIgnoreCase(""))
			addressDestColonia += ","+" "+("Int")+" "+String.valueOf(request.get("IntNum"));
		addressDestColonia += " "+ String.valueOf(request.get("Neighborhood"));

		com.m160185.models.Address addressDest = new com.m160185.models.Address();
		addressDest.setTel(String.valueOf(request.get("PhoneNumber")));
		addressDest.setName(String.valueOf(request.get("ContactName")).toUpperCase());
		addressDest.setCompanyName(String.valueOf(request.get("CorporateName")).toUpperCase());
		addressDest.setAddress(address.toUpperCase());
		addressDest.setCity(String.valueOf(request.get("City")).toUpperCase());
		addressDest.setMunicipio(String.valueOf(request.get("Neighborhood")).toUpperCase());
		addressDest.setCp(String.valueOf(request.get("ZipCode")));
		addressDest.setColonia(addressDestColonia);
		addressDest.setReference(String.valueOf(request.get("Reference")).toUpperCase());
		data.setDestination(addressDest);

		com.m160185.models.StateCode stateCodeDest = new com.m160185.models.StateCode();
		stateCodeDest.setState(String.valueOf(request.get("State")));
		stateCodeDest.setCode(findStateCode(String.valueOf(request.get("State"))));
		data.setStateCodeDest(stateCodeDest);

		int length  = request.get("Length")==null ? 1 : new BigDecimal(String.valueOf(request.get("Length"))).intValue();
		int height  = request.get("Height")==null ? 1 : new BigDecimal(String.valueOf(request.get("Height"))).intValue();
		int width  = request.get("Width")==null ? 1 : new BigDecimal(String.valueOf(request.get("Width"))).intValue();

		String labelType  = (request.get("PaperType")==null || request.get("PaperType").toString().equalsIgnoreCase("") || request.get("PaperType").toString().equalsIgnoreCase("1"))  ? "paper" : "thermal";

		data.setLength(length);
		data.setHeight(height);
		data.setWidth(width);
		data.setLabelType(labelType);

		log.info("\tService Type: "+String.valueOf(request.get("ServiceTypeId")));

		com.m160185.conn.Service1Client guiaService = new com.m160185.conn.Service1Client(data,String.valueOf(request.get("ServiceTypeId")), new BigDecimal(String.valueOf(request.get("Weight"))));
		JSONObject responseService = guiaService.createv2();
		ProcessShipmentReply fedexResponse =  guiaService.getProcessShipmentReply();
		if(responseService==null) {
			String fedexXMLResponse = fedexResponse.getResponseXML();
			try{
				fedexXMLResponse = fedexXMLResponse.substring(fedexXMLResponse.indexOf("<ProcessShipmentReply"), fedexXMLResponse.indexOf("</SOAP"));
				JSONObject responseError = XML.toJSONObject(fedexXMLResponse);
				JSONObject ProcessShipmentReply = responseError.getJSONObject("ProcessShipmentReply");
				JSONObject notifications = new JSONObject();
				Object notificationObj = ProcessShipmentReply.get("Notifications");
				if(notificationObj instanceof JSONObject) {
					notifications = ProcessShipmentReply.getJSONObject("Notifications");
				}else if (notificationObj instanceof JSONArray) {
					JSONArray _temp = ProcessShipmentReply.getJSONArray("Notifications");
					notifications = _temp.getJSONObject(0);
				}
				this.getResponse().put("ResponseCode", String.valueOf(notifications.get("Code")));
				this.getResponse().put("ResponseDescription", String.valueOf(notifications.get("Message")));

				log.info("\t"+String.valueOf(notifications.get("Message")));
				g.setEstatus(String.valueOf(notifications.get("Message")));

				guiaIntegradorRepository.save(g);
				log.info("\tError: "+String.valueOf(String.valueOf(notifications.get("Message"))));
			}catch (Exception e) {
				fedexXMLResponse = fedexXMLResponse.replace("v25:", "");
				JSONObject obj = XML.toJSONObject(fedexXMLResponse);

				JSONObject envelope = obj.getJSONObject("SOAP-ENV:Envelope");
				JSONObject body = envelope.getJSONObject("SOAP-ENV:Body");
				JSONObject reply = body.getJSONObject("ProcessShipmentReply");
				JSONObject not = reply.getJSONObject("Notifications");

				this.getResponse().put("ResponseCode", String.valueOf(not.get("Code")));
				this.getResponse().put("ResponseDescription", String.valueOf(not.get("Message")));

				g.setEstatus(String.valueOf(not.get("Message")));
				guiaIntegradorRepository.save(g);
				log.info("\tError: "+String.valueOf(String.valueOf(not.get("Message"))));

			}
		}else {
			JSONArray packageDetails = responseService.getJSONArray("package_details");
			for (int i = 0; i < packageDetails.length(); i++) {
				JSONObject packageDetail = packageDetails.getJSONObject(i);
				JSONArray labels = packageDetail.getJSONArray("labels");
				JSONObject label = labels.getJSONObject(0);
				String encodedFile = Base64.getEncoder().encodeToString((byte[]) label.get("file"));
				this.getResponse().put("File", encodedFile);
			}
			JSONObject masterTrackingnumber = responseService.has("master_tracking_number") ? responseService.getJSONObject("master_tracking_number") : new JSONObject();
			if(masterTrackingnumber.has("tracking_number")) {
				this.getResponse().put("Tracking", String.valueOf(masterTrackingnumber.get("tracking_number")));
				log.info("\tTracking Number: "+String.valueOf(masterTrackingnumber.get("tracking_number")));
				g.setTracking(String.valueOf(masterTrackingnumber.get("tracking_number")));
			}

			log.info("\tAccount");
			for (Iterator<Map.Entry<String, String>> entries = params.entrySet().iterator(); entries.hasNext(); ) {
				Map.Entry<String, String> entry = entries.next();
				log.info("\t\t"+entry.getKey()+" : "+entry.getValue());
			}
			g.setEstatus("");
			guiaIntegradorRepository.save(g);
		}
		return this.getResponse();
	}

	public Object executeRestServiceV2() throws Exception {
		this.setResponse(new JSONObject());
		Map<String, String> params = this.getParams();
		GuiaIntegrador g = this.getGuiaIntegrador();
		JSONObject request = this.getRequest();
		GuiaIntegradorRepository guiaIntegradorRepository = this.getGuiasIntegradorRepository();
		//Bussinesrules
		Map<String, Object> context = getJSONContext();
		Boolean hasBRAlgoritmo = false;
		if(this.getBusinessRuleBeforeSend() != null && !this.getBusinessRuleBeforeSend().trim().isEmpty()) {
			String[] list = this.getBusinessRuleBeforeSend().split(",");
			log.info("\tBeforeSendBR: " + this.getBusinessRuleBeforeSend());
			for (String br : list) {
				if (br.trim().endsWith("SetAccountAlgoritmoRentabilidad")) {
					hasBRAlgoritmo = true;
				}
				Object tempClass = Class.forName(br).newInstance();
				Method metodo = tempClass.getClass().getDeclaredMethod("run", Map.class);
				context = (Map<String, Object>) metodo.invoke(tempClass, context);
			}
			this.setContextJSON(context);
		}
		//Algoritmo
		/* TODO Pendiente a revisar
		if (hasBRAlgoritmo) {
			JSONObject fedex = (JSONObject) context.get("fedex");
			if (request.getString("ServiceTypeId").equalsIgnoreCase("FEDEX_EXPRESS_SAVER")) {
				JSONObject ship = fedex.getJSONObject("terrestre")
						.getJSONObject("configuracion")
						.getJSONObject("Fedex_Ship");
				credential.setAccountNumber(ship.getString("account_number"));
				log.info("\tAlgoritmo - Terrestre: " + ship.getString("account_number"));
			} else {
				JSONObject ship = fedex.getJSONObject("express")
						.getJSONObject("configuracion")
						.getJSONObject("Fedex_Ship");
				credential.setAccountNumber(ship.getString("account_number"));
				log.info("\tAlgoritmo - Express: " + ship.getString("account_number"));
			}
		}
		*/
		if(request.has("Account"))
			log.info("\tOverride Account: " + request.optString("Account"));
		try{
			String addressOrigenColonia = "";
			if(request.get("OrigenExtNum")!=null && !String.valueOf(request.get("OrigenExtNum")).equalsIgnoreCase(""))
				addressOrigenColonia = ("#").concat(String.valueOf(request.get("OrigenExtNum")));
			if(request.get("OrigenExtNum")!=null && !String.valueOf(request.get("OrigenExtNum")).equalsIgnoreCase(""))
				addressOrigenColonia += ","+" "+("Int")+" "+String.valueOf(request.get("OrigenExtNum"));
			addressOrigenColonia += " "+ String.valueOf(request.get("OrigenNeighborhood"));
			String addressDestColonia = "";
			if(request.get("ExtNum")!=null && !String.valueOf(request.get("ExtNum")).equalsIgnoreCase(""))
				addressDestColonia = ("#").concat(String.valueOf(request.get("ExtNum")));
			if(request.get("IntNum")!=null && !String.valueOf(request.get("IntNum")).equalsIgnoreCase(""))
				addressDestColonia += ","+" "+("Int")+" "+String.valueOf(request.get("IntNum"));
			addressDestColonia += " "+ String.valueOf(request.get("Neighborhood"));
			String poNumber = request.has("PONumber") ? request.optString("PONumber") : "";
			//Shipper
			ShipRequest.Party shipper = new ShipRequest.Party(
					request.getString("OrigenContactName"),
					request.getString("OrigenCorporateName"),
					request.getString("OrigenPhoneNumber"),
					request.getString("OrigenAddress1"),
					addressOrigenColonia,
					null,
					request.getString("OrigenCity"),
					findStateCode(request.getString("OrigenState")),
					request.getString("OrigenZipCode"),
					"MX"
					);
			//Recipient
			ShipRequest.Party recipient = new ShipRequest.Party(
					request.getString("ContactName"),
					request.getString("CorporateName"),
					request.getString("PhoneNumber"),
					request.getString("Address1"),
					addressDestColonia,
					null,
					request.getString("City"),
					findStateCode(request.getString("State")),
					request.getString("ZipCode"),
					"MX"
					);
			//Dimensions
			int length = new BigDecimal(request.get("Length").toString()).intValue();
			int width = new BigDecimal(request.get("Width").toString()).intValue();
			int height = new BigDecimal(request.get("Height").toString()).intValue();
			ShipRequest.Dimensions dimensions = new ShipRequest.Dimensions(length, width, height);
			//Label Type
			ShipRequest.LabelType labelType = (request.optString("PaperType", "1").equals("1")) ? ShipRequest.LabelType.PAPER : ShipRequest.LabelType.THERMAL;
			//Width
			BigDecimal weight = new BigDecimal(request.get("Weight").toString());
			//Request Body
			ShipRequest shipRequest = new ShipRequest(
					request.getString("ServiceTypeId"), //Validar aqui  "FEDEX_EXPRESS_SAVER", // o FEDEX_EXPRESS_SAVER
					labelType,
					weight,
					dimensions,
					shipper,
					recipient,
					request.optString("Reference"),
					poNumber
					);
			//Token
			Authorization auth = new Authorization(params.get("authorization"), params.get("key"), params.get("password"));
			String token = auth.performAndGetToken();
			//Request
			Ship ship = new Ship(params.get("url"), params.get("account"), token);
			String rawResponse = ship.create(shipRequest);
			log.info("\tFedEx REST Response: " + rawResponse);
			//Response
			JSONObject jsonResponse = new JSONObject(rawResponse);
			JSONObject output = jsonResponse.getJSONObject("output");
			JSONArray shipments = output.getJSONArray("transactionShipments");
			JSONObject shipment = shipments.getJSONObject(0);
			//Tracking
			if(shipment.has("masterTrackingNumber")) {
				String tracking = shipment.getString("masterTrackingNumber");
				this.getResponse().put("Tracking", tracking);
				g.setTracking(tracking);
				log.info("\tTracking: " + tracking);
			}
			//Label
			if(shipment.has("pieceResponses")) {
				JSONArray pieces = shipment.getJSONArray("pieceResponses");
				if(pieces.length() > 0) {
					JSONObject piece = pieces.getJSONObject(0);
					if(piece.has("packageDocuments")) {
						JSONArray docs = piece.getJSONArray("packageDocuments");
						if(docs.length() > 0) {
							JSONObject doc = docs.getJSONObject(0);
							if(doc.has("url")) {
								this.getResponse().put("LabelURL", doc.getString("url"));
							}
							if(doc.has("encodedLabel")) {
								this.getResponse().put("File", doc.getString("encodedLabel"));
							}
						}
					}
				}
			}
			//Save data
			g.setEstatus("");
			guiaIntegradorRepository.save(g);
		} catch (Exception e) {
			log.error("Error FedEx REST", e);
			this.getResponse().put("ResponseCode", "ERROR");
			this.getResponse().put("ResponseDescription", e.getMessage());
			g.setEstatus(e.getMessage());
			guiaIntegradorRepository.save(g);
		}
		return this.getResponse();
	}

	public static String findStateCode(String state) {
		state = state.trim().toLowerCase();
		String code = "";
		switch (state) {
		case "aguascalientes": code="AG"; break;
		case "baja california": 		code="BC"; break;
		case "baja california sur": 	code="BS"; break;
		case "campeche": 				code="CM"; break;
		case "chihuahua": 				code="CH"; break;
		case "chiapas": 				code="CS"; break;
		case "coahuila": 				code="CO"; break;
		case "colima": 					code="CL"; break;
		case "durango": 				code="DG"; break;
		case "guerrero": 				code="GR"; break;
		case "guanajuato": 				code="GT"; break;
		case "hidalgo": 				code="HG"; break;
		case "jalisco": 				code="JA"; break;
		case "distrito federal": 		code="DF"; break;
		case "estado de mexico": 		code="MX"; break;
		case "michoacan": 				code="MI"; break;
		case "morelos": 				code="MO"; break;
		case "nayarit": 				code="NA"; break;
		case "nuevo leon": 				code="NL"; break;
		case "oaxaca": 					code="OA"; break;
		case "puebla": 					code="PU"; break;
		case "queretaro": 				code="QT"; break;
		case "quintana roo": 			code="QR"; break;
		case "san luis potosi": 		code="SL"; break;
		case "sinaloa": 				code="SI"; break;
		case "sonora": 					code="SO"; break;
		case "tabasco": 				code="TB"; break;
		case "tamaulipas": 				code="TM"; break;
		case "tlaxcala": 				code="TL"; break;
		case "veracruz": 				code="VE"; break;
		case "yucatan": 				code="YU"; break;
		case "Zacatecas": 				code="ZA"; break;
		default:
			break;
		}
		return code;
	}

	public Map<String, Object> getContext(){
		Map<String,String> values = this.getAccount();
		GuiaIntegrador guiaIntegrador = this.getGuiaIntegrador();
		com.integrador.xml.fedex.services.FedexLabelRequest xmlRequest = this.getXmlRequest();
		com.integrador.xml.fedex.services.FedexLabelResponse xmlResponse = this.getXmlResponse();
		GuiaIntegradorRepository guiaIntegradorRepository = this.getGuiaIntegradorRepository();
		AtributoService atributoService = this.getAtributoService();

		Map<String,Object> context = new HashMap<String,Object>();
		context.put("account", values);
		context.put("guiaIntegrador", guiaIntegrador);
		context.put("xmlRequest", xmlRequest);
		context.put("xmlResponse", xmlResponse);
		context.put("guiaIntegradorRepository", guiaIntegradorRepository);
		context.put("atributoService", atributoService);
		context.put("env", this.getEnv());

		return context;
	}

	public Map<String, Object> getJSONContext(){
		Map<String,String> values = this.getAccount();
		GuiaIntegrador guiaIntegrador = this.getGuiaIntegrador();
		JSONObject request = this.getRequest();
		JSONObject response = this.getResponse();
		GuiaIntegradorRepository guiaIntegradorRepository = this.getGuiaIntegradorRepository();
		AtributoService atributoService = this.getAtributoService();

		Map<String,Object> context = new HashMap<String,Object>();
		context.put("account", values);
		context.put("guiaIntegrador", guiaIntegrador);
		context.put("request", request);
		context.put("response", response);
		context.put("guiaIntegradorRepository", guiaIntegradorRepository);
		context.put("atributoService", atributoService);
		context.put("env", this.getEnv());

		return context;
	}

	@SuppressWarnings("unchecked")
	public void setContext(Map<String, Object> context){
		Map<String,String> values = (Map<String,String>) context.get("account");
		this.setAccount(values);
		GuiaIntegrador guiaIntegrador = (GuiaIntegrador) context.get("guiaIntegrador");
		this.setGuiaIntegrador(guiaIntegrador);
		com.integrador.xml.fedex.services.FedexLabelRequest xmlRequest = (com.integrador.xml.fedex.services.FedexLabelRequest) context.get("xmlRequest");
		this.setXmlRequest(xmlRequest);
		com.integrador.xml.fedex.services.FedexLabelResponse xmlResponse = (com.integrador.xml.fedex.services.FedexLabelResponse) context.get("xmlResponse");
		this.setXmlResponse(xmlResponse);
		GuiaIntegradorRepository guiaIntegradorRepository = (GuiaIntegradorRepository) context.get("guiaIntegradorRepository");
		this.setGuiaIntegradorRepository(guiaIntegradorRepository);
		AtributoService atributoService = (AtributoService) context.get("atributoService");
		this.setAtributoService(atributoService);
	}

	@SuppressWarnings("unchecked")
	public void setContextJSON(Map<String, Object> context){
		Map<String,String> values = (Map<String,String>) context.get("account");
		this.setAccount(values);
		GuiaIntegrador guiaIntegrador = (GuiaIntegrador) context.get("guiaIntegrador");
		this.setGuiaIntegrador(guiaIntegrador);
		JSONObject request = this.getRequest();
		this.setRequest(request);
		JSONObject response = this.getResponse();
		this.setResponse(response);
		GuiaIntegradorRepository guiaIntegradorRepository = (GuiaIntegradorRepository) context.get("guiaIntegradorRepository");
		this.setGuiaIntegradorRepository(guiaIntegradorRepository);
		AtributoService atributoService = (AtributoService) context.get("atributoService");
		this.setAtributoService(atributoService);
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public com.integrador.xml.fedex.services.FedexLabelRequest getXmlRequest() {
		return xmlRequest;
	}

	public void setXmlRequest(com.integrador.xml.fedex.services.FedexLabelRequest xmlRequest) {
		this.xmlRequest = xmlRequest;
	}

	public com.integrador.xml.fedex.services.FedexLabelResponse getXmlResponse() {
		return xmlResponse;
	}

	public void setXmlResponse(com.integrador.xml.fedex.services.FedexLabelResponse xmlResponse) {
		this.xmlResponse = xmlResponse;
	}

	public GuiaIntegradorRepository getGuiasIntegradorRepository() {
		return guiaIntegradorRepository;
	}

	public void setGuiasIntegradorRepository(GuiaIntegradorRepository guiaIntegradorRepository) {
		this.guiaIntegradorRepository = guiaIntegradorRepository;
	}

	public GuiaIntegrador getGuiaIntegrador() {
		return guiaIntegrador;
	}

	public void setGuiaIntegrador(GuiaIntegrador guiaIntegrador) {
		this.guiaIntegrador = guiaIntegrador;
	}
	public JSONObject getRequest() {
		return request;
	}
	public void setRequest(JSONObject request) {
		this.request = request;
	}
	public JSONObject getResponse() {
		return response;
	}
	public void setResponse(JSONObject response) {
		this.response = response;
	}
	public String getBusinessRuleBeforeSend() {
		return businessRuleBeforeSend;
	}
	public void setBusinessRuleBeforeSend(String businessRuleBeforeSend) {
		this.businessRuleBeforeSend = businessRuleBeforeSend;
	}
	public Map<String, String> getAccount() {
		return account;
	}
	public void setAccount(Map<String, String> account) {
		this.account = account;
	}
	public GuiaIntegradorRepository getGuiaIntegradorRepository() {
		return guiaIntegradorRepository;
	}
	public void setGuiaIntegradorRepository(GuiaIntegradorRepository guiaIntegradorRepository) {
		this.guiaIntegradorRepository = guiaIntegradorRepository;
	}
	public AtributoService getAtributoService() {
		return atributoService;
	}
	public void setAtributoService(AtributoService atributoService) {
		this.atributoService = atributoService;
	}
	public String getEnv() {
		return env;
	}
	public void setEnv(String env) {
		this.env = env;
	}
	public String getBusinessRuleAfterSend() {
		return businessRuleAfterSend;
	}
	public void setBusinessRuleAfterSend(String businessRuleAfterSend) {
		this.businessRuleAfterSend = businessRuleAfterSend;
	}
}