package com.integrador.carriers;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dhl.bot.DHLBot;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrador.models.GuiaIntegrador;
import com.integrador.repositories.GuiaIntegradorRepository;
import com.integrador.services.AtributoService;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class BotDHL implements Carrier{

	private Map<String,String> account;
	private com.integrador.xml.services.EstafetaLabelRequest xmlRequest;
	private com.integrador.xml.services.EstafetaLabelResponse xmlResponse;
	private GuiaIntegradorRepository guiaIntegradorRepository;
	private GuiaIntegrador guiaIntegrador;
	private AtributoService atributoService;
	private String businessRuleBeforeSend;

	static final Logger log = LoggerFactory.getLogger(BotEstafeta.class);

	@Override
	public Object executeWebService() throws Exception {
		//Executing business rule bofore send
		Map<String,Object> context = getContext();
		if(this.getBusinessRuleBeforeSend()!=null && !this.getBusinessRuleBeforeSend().trim().equalsIgnoreCase("")) {
			String[] list = this.getBusinessRuleBeforeSend().split(",");
			for(String br : list){
				log.info("\tBeforeSendBR: "+this.getBusinessRuleBeforeSend());
				Object tempClass = Class.forName(br).newInstance();
				Class classLoaded = tempClass.getClass();
				Class[] arguments = new Class[1];
				arguments[0] = Map.class;
				Method metodo = classLoaded.getDeclaredMethod("run", arguments);
				context = (Map<String,Object>) metodo.invoke(tempClass, context);
			}
			this.setContext(context);
		}

		//Setting variables 
		Map<String,String> account = this.getAccount();
		GuiaIntegrador g = this.getGuiaIntegrador();
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = this.getXmlRequest();
		com.integrador.xml.services.EstafetaLabelResponse response = this.getXmlResponse();
		GuiaIntegradorRepository guiaIntegradorRepository = this.getGuiaIntegradorRepository();

		/*
		 * Validation Type depending of kg.
		 * 1 - 7 guerrero7
		 * mas de 8 guerrero10
		 */
		BigDecimal kg = new BigDecimal(xmlRequest.getWeight());
		if(kg.intValue()<=7) {
			account = atributoService.getByTipoInMap("BotDHL1");
		}else {
			account = atributoService.getByTipoInMap("BotDHL2");
		}

		DHLBot bot = new DHLBot(account.get("url").toString().trim(), account.get("usuario").toString().trim(), account.get("password").toString().trim());
		DHLBot botToken = new DHLBot(account.get("url").toString().trim(), account.get("usuario_bot").toString().trim(), account.get("password_bot").toString().trim());

		com.app.models.Token token = botToken.doSignIn();
		if(token!=null) {
			com.dhl.bot.Label label = new com.dhl.bot.Label();
			com.app.models.Address shipper = new com.app.models.Address();
			shipper.setRegistered_account("");
			shipper.setPostal_code(xmlRequest.getOrigenZipCode());
			shipper.setPhone_number(xmlRequest.getOrigenPhoneNumber());
			shipper.setPerson_name(xmlRequest.getOrigenContactName());
			shipper.setEmail("");
			shipper.setCountry_name("Mexico");
			shipper.setCountry_code("MX");
			shipper.setCompany("no_company");
			shipper.setCity(xmlRequest.getOrigenCity());
			shipper.setAddress3("");
			shipper.setAddress2(xmlRequest.getOrigenAddress2());
			shipper.setAddress3(xmlRequest.getOrigenAddress2());
			shipper.setAddress1(xmlRequest.getOrigenAddress1());
			label.setShipper(shipper);
			String serviceType = xmlRequest.getServiceTypeId().equalsIgnoreCase("Terrestre") ? "standard" : "express";
			if(serviceType.equalsIgnoreCase("standard")) {
				log.info("\tService Type: "+serviceType + " [Terrestre]");
			}else {
				log.info("\tService Type: "+serviceType + " [Express]");
			}
			label.setService_type(serviceType);
			String referencia = xmlRequest.getReference()==null ? "" : xmlRequest.getReference();
			if(referencia.length()>25) {
				referencia = referencia.substring(0,24);
			}
			label.setReference_id(referencia);
			if(account.get("environment").toString().trim().equalsIgnoreCase("QA"))
				label.setReference_id("Prueba");
			com.app.models.Address recipient = new com.app.models.Address();
			recipient.setRegistered_account(null);
			recipient.setPostal_code(xmlRequest.getZipCode());
			recipient.setPhone_number(xmlRequest.getPhoneNumber());
			recipient.setPerson_name(xmlRequest.getContactName());
			recipient.setEmail("");
			recipient.setCountry_name("Mexico");
			recipient.setCountry_code("MX");
			recipient.setCompany("no_company");
			recipient.setCity(xmlRequest.getCity());
			recipient.setAddress3(xmlRequest.getAddress2());
			recipient.setAddress2(xmlRequest.getAddress2());
			recipient.setAddress1(xmlRequest.getAddress1());
			label.setRecipient(recipient);
			com.app.models.ProviderProperty provider_properties = new com.app.models.ProviderProperty();
			provider_properties.setState_recipient(xmlRequest.getState());
			provider_properties.setState_shipper(xmlRequest.getOrigenState());
			label.setProvider_properties(provider_properties);
			label.setProvider("DHL");
			label.setPackaging_type("package");
			com.app.models.Package packages = new com.app.models.Package();
			packages.setWidth(new BigDecimal(xmlRequest.getWidth()));
			packages.setWeight(new BigDecimal(xmlRequest.getWeight()));
			packages.setQuantity(1);
			packages.setHeight(new BigDecimal(xmlRequest.getHeight()));
			packages.setDepth(new BigDecimal(xmlRequest.getLength()));
			label.setPackages(packages);
			label.setLabel_format("pdf");
			label.setDeclared_value(new BigDecimal(0));
			label.setFilename("");
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
			String today = dt.format(new Date()).toString();
			label.setDate(today);
			label.setCurrency_code("MX");
			com.dhl.bot.Credential credentials = new com.dhl.bot.Credential();
			credentials.setEmail(account.get("usuario").toString());
			credentials.setPassword(account.get("password").toString());
			label.setCredentials(credentials);
			label.setContents(xmlRequest.getContent());
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(label);
			JSONObject responseJSON = bot.doCreate(token, json);
			g.setTracking(String.valueOf(responseJSON.get("id")));
			g.setRequest(json);
			g.setVia(xmlRequest.getVia()!=null && !xmlRequest.getVia().trim().equalsIgnoreCase("") ? xmlRequest.getVia() : "");
			guiaIntegradorRepository.save(g);
			log.info("\tId: "+String.valueOf(responseJSON.get("id")));
			log.info("\tAccount");
			for (Iterator<Map.Entry<String, String>> entries = account.entrySet().iterator(); entries.hasNext(); ) {
				Map.Entry<String, String> entry = entries.next();
				log.info("\t\t"+entry.getKey()+" : "+entry.getValue());
			}
			response.setResponseCode(String.valueOf(responseJSON.get("id")));
			response.setFile("");
			response.setTracking("");
			response.setResponseDescription("");
		}
		return response;
	}
	@Override
	public Object executeRestService() throws Exception {
		return null;
	}
	public Map<String, Object> getContext(){
		Map<String,String> values = this.getAccount();
		GuiaIntegrador guiaIntegrador = this.getGuiaIntegrador();
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = this.getXmlRequest();
		com.integrador.xml.services.EstafetaLabelResponse xmlResponse = this.getXmlResponse();
		GuiaIntegradorRepository guiaIntegradorRepository = this.getGuiaIntegradorRepository();
		AtributoService atributoService = this.getAtributoService();
		Map<String,Object> context = new HashMap<String,Object>();
		context.put("account", values);
		context.put("guiaIntegrador", guiaIntegrador);
		context.put("xmlRequest", xmlRequest);
		context.put("xmlResponse", xmlResponse);
		context.put("guiaIntegradorRepository", guiaIntegradorRepository);
		context.put("atributoService", atributoService);
		return context;
	}
	public void setContext(Map<String, Object> context){
		Map<String,String> values = (Map<String,String>) context.get("account");
		this.setAccount(values);
		GuiaIntegrador guiaIntegrador = (GuiaIntegrador) context.get("guiaIntegrador");
		this.setGuiaIntegrador(guiaIntegrador);
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = (com.integrador.xml.services.EstafetaLabelRequest) context.get("xmlRequest");
		this.setXmlRequest(xmlRequest);
		com.integrador.xml.services.EstafetaLabelResponse xmlResponse = (com.integrador.xml.services.EstafetaLabelResponse) context.get("xmlResponse");
		this.setXmlResponse(xmlResponse);
		GuiaIntegradorRepository guiaIntegradorRepository = (GuiaIntegradorRepository) context.get("guiaIntegradorRepository");
		this.setGuiaIntegradorRepository(guiaIntegradorRepository);
		AtributoService atributoService = (AtributoService) context.get("atributoService");
		this.setAtributoService(atributoService);
	}

	public Map<String, String> getAccount() {
		return account;
	}

	public void setAccount(Map<String, String> account) {
		this.account = account;
	}

	public com.integrador.xml.services.EstafetaLabelRequest getXmlRequest() {
		return xmlRequest;
	}

	public void setXmlRequest(com.integrador.xml.services.EstafetaLabelRequest xmlRequest) {
		this.xmlRequest = xmlRequest;
	}

	public com.integrador.xml.services.EstafetaLabelResponse getXmlResponse() {
		return xmlResponse;
	}

	public void setXmlResponse(com.integrador.xml.services.EstafetaLabelResponse xmlResponse) {
		this.xmlResponse = xmlResponse;
	}

	public GuiaIntegradorRepository getGuiaIntegradorRepository() {
		return guiaIntegradorRepository;
	}

	public void setGuiaIntegradorRepository(GuiaIntegradorRepository guiaIntegradorRepository) {
		this.guiaIntegradorRepository = guiaIntegradorRepository;
	}

	public GuiaIntegrador getGuiaIntegrador() {
		return guiaIntegrador;
	}

	public void setGuiaIntegrador(GuiaIntegrador guiaIntegrador) {
		this.guiaIntegrador = guiaIntegrador;
	}

	public AtributoService getAtributoService() {
		return atributoService;
	}

	public void setAtributoService(AtributoService atributoService) {
		this.atributoService = atributoService;
	}

	public String getBusinessRuleBeforeSend() {
		return businessRuleBeforeSend;
	}

	public void setBusinessRuleBeforeSend(String businessRuleBeforeSend) {
		this.businessRuleBeforeSend = businessRuleBeforeSend;
	}

}
