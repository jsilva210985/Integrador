package com.integrador.carriers;

import static org.estafeta.restconnector.utilities.Utils.processToJSON;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.estafeta.restconnector.controller.Connector;
import org.estafeta.restconnector.models.Address;
import org.estafeta.restconnector.models.Contact;
import org.estafeta.restconnector.models.DRAAlternative;
import org.estafeta.restconnector.models.Destination;
import org.estafeta.restconnector.models.EstafetaRest;
import org.estafeta.restconnector.models.HomeAddress;
import org.estafeta.restconnector.models.Identification;
import org.estafeta.restconnector.models.Insurance;
import org.estafeta.restconnector.models.ItemDescription;
import org.estafeta.restconnector.models.LabelDefinition;
import org.estafeta.restconnector.models.Location;
import org.estafeta.restconnector.models.Origin;
import org.estafeta.restconnector.models.OutputGroup;
import org.estafeta.restconnector.models.OutputType;
import org.estafeta.restconnector.models.PrintingTemplate;
import org.estafeta.restconnector.models.ResponseMode;
import org.estafeta.restconnector.models.ReturnDocument;
import org.estafeta.restconnector.models.ServiceConfiguration;
import org.estafeta.restconnector.models.SystemInformation;
import org.estafeta.restconnector.models.WayBill;
import org.estafeta.restconnector.models.WayBillDocument;
import org.estafeta.restconnector.oauth.AuthenticatorClient;
import org.estafeta.restconnector.utils.Configurations;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.integrador.exceptions.BusinessRuleException;
import com.integrador.models.GuiaIntegrador;
import com.integrador.repositories.GuiaIntegradorRepository;
import com.integrador.services.AtributoService;
import com.integrador.services.UsuariosService;
import com.integrador.util.Util;

public class Estafeta implements Carrier{

	private Map<String,String> account;
	private com.integrador.xml.services.EstafetaLabelRequest xmlRequest;
	private com.integrador.xml.services.EstafetaLabelResponse xmlResponse;
	private GuiaIntegradorRepository guiaIntegradorRepository;
	private GuiaIntegrador guiaIntegrador;
	private AtributoService atributoService;
	private UsuariosService usuariosService;
	private String businessRuleBeforeSend;

	static final Logger log = LoggerFactory.getLogger(Estafeta.class);

	@Override
	public Object executeWebService() {
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@Override
	public Object executeRestService() throws Exception {
		/*
		 * Executing business rule bofore send
		 */
		Map<String,Object> context = getContext();
		if(this.getBusinessRuleBeforeSend()!=null && !this.getBusinessRuleBeforeSend().trim().equalsIgnoreCase("")) {
			String[] list = this.getBusinessRuleBeforeSend().split(",");
			for(String br : list) {
				log.info("\tBeforeSendBR: "+br);
				//Object tempClass = Class.forName(br).newInstance();
				Object tempClass = com.integrador.util.SpringContext.getBean(br);
				Class classLoaded = tempClass.getClass();
				Class[] arguments = new Class[1];
				arguments[0] = Map.class;
				Method metodo = classLoaded.getDeclaredMethod("run",arguments);
				try{
					Object result = metodo.invoke(tempClass, context);
					if (result == null) {
						throw new BusinessRuleException(-2, "BR " + br + ": La regla devolvió null inesperadamente");
					}
					context = (Map<String,Object>) result;
				}catch(BusinessRuleException bre){
					throw bre;
				}catch (InvocationTargetException ite) {
					Throwable causa = ite.getCause();
					int codigo = -2;
					if(causa instanceof BusinessRuleException) {
						codigo = ((BusinessRuleException) causa).getCode();
					}else if(causa instanceof BusinessRuleException) {
						codigo = ((BusinessRuleException) causa).getCode();
					}
					String mensaje = causa != null && causa.getMessage() != null ? causa.getMessage(): String.valueOf(causa);
					//throw new BusinessRuleException(codigo, "BR " + br + ": Error en Business Rule -> " + mensaje);
					throw new BusinessRuleException(codigo, mensaje);
				}catch(Exception e) {
					Throwable causa = e.getCause();
					int codigo = -2; // fallback
					if(causa instanceof BusinessRuleException) {
						codigo = ((BusinessRuleException) causa).getCode();
					}else if(causa instanceof BusinessRuleException) {
						codigo = ((BusinessRuleException) causa).getCode();
					}
					String mensaje = causa != null && causa.getMessage() != null ? causa.getMessage(): e.getMessage();
					//throw new BusinessRuleException(codigo, "BR " + br + ": Error en ejecución inesperada -> " + mensaje);
					throw new BusinessRuleException(codigo, mensaje);
				}
			}
			this.setContext(context);
			xmlRequest = (com.integrador.xml.services.EstafetaLabelRequest) context.get("xmlRequest");
		}
		Map<String,String> values = this.getAccount();
		Map<String, Object> _context = this.getContext();
		if(_context!=null && _context.containsKey("account"))
			values = (Map<String, String>) _context.get("account");
		GuiaIntegrador guiaIntegrador = this.getGuiaIntegrador();
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = this.getXmlRequest();
		com.integrador.xml.services.EstafetaLabelResponse response = this.getXmlResponse();
		GuiaIntegradorRepository guiaIntegradorRepository = this.getGuiaIntegradorRepository();
		String serviceId = "0";
		if(xmlRequest.getServiceTypeId().equalsIgnoreCase("Terrestre")) {
			serviceId = values.get("service_type_id_terrestre").toString().trim();
			log.info("\tService Type: "+serviceId + " [Terrestre]");
		}else {
			serviceId = values.get("service_type_id_express").toString().trim();
			log.info("\tService Type: "+serviceId + " [Express]");
		}
		Configurations configurations = new Configurations();
		configurations.setUrlToken(values.get("url_token").toString().trim());
		configurations.setUrlLabelService(values.get("url_label_service").toString().trim());
		configurations.setApikey(values.get("api_key").toString().trim());
		configurations.setApisecret(values.get("api_secret").toString().trim());
		configurations.setCustomerNumber(values.get("customer_number").toString().trim());
		configurations.setSuscriberId(values.get("suscriber_id").toString().trim());
		configurations.setSalesOrganization(Integer.parseInt(values.get("sales_organization").toString().trim()));
		configurations.setServiceTypeId(serviceId);
		configurations.setSystemInformationId(values.get("system_information_id").toString().trim());
		configurations.setSystemInformationName(values.get("system_information_name").toString().trim());
		configurations.setSystemInformationVersion(values.get("system_information_version").toString().trim());

		AuthenticatorClient auth = new AuthenticatorClient();
		auth.setAuthURL(configurations.getUrlToken());
		auth.setClientId(configurations.getApikey());
		auth.setClientSecret(configurations.getApisecret());
		org.estafeta.restconnector.models.Token tokenEstafeta = auth.getToken();

		Identification identification = new Identification();
		identification.setCustomerNumber(configurations.getCustomerNumber());
		identification.setSuscriberId(configurations.getSuscriberId());

		SystemInformation systemInformation = new SystemInformation();
		systemInformation.setId(configurations.getSystemInformationId());
		systemInformation.setName(configurations.getSystemInformationName());
		systemInformation.setVersion(configurations.getSystemInformationVersion());

		String _additionalInfo = Util.removeAccents(xmlRequest.getAditionalInfo());
		_additionalInfo = _additionalInfo.length()>25 ? _additionalInfo.substring(0,24) : _additionalInfo.trim();

		String _content = Util.removeAccents(xmlRequest.getContent());

		WayBillDocument wayBillDocument = new WayBillDocument();
		wayBillDocument.setAditionalInfo(_additionalInfo);
		wayBillDocument.setContent(_content);
		wayBillDocument.setCostCenter(null);
		wayBillDocument.setCustomerShipmentId(null);
		wayBillDocument.setGroupShipmentId(null);
		wayBillDocument.setReferenceNumber(null);

		BigDecimal weight = new BigDecimal(xmlRequest.getWeight()==null ? "1.0" : xmlRequest.getWeight());
		BigDecimal height = new BigDecimal(xmlRequest.getHeight()==null ? "1.0" : xmlRequest.getHeight());
		BigDecimal length = new BigDecimal(xmlRequest.getLength()==null ? "1.0" : xmlRequest.getLength());
		BigDecimal width = new BigDecimal(xmlRequest.getWidth()==null ? "1.0" : xmlRequest.getWidth());
		BigDecimal kilos = new BigDecimal(guiaIntegrador.getKilos());

		BigDecimal pesoNeto = new BigDecimal(0);

		if(Integer.parseInt(xmlRequest.getParcelTypeId())==1) {
			if(length.intValue()==0) {
				length = new BigDecimal(1);
			}
			if(width.intValue()==0) {
				width = new BigDecimal(1);
			}
			if(height.intValue()==0) {
				height = new BigDecimal(1);
			}
		}

		BigDecimal divisor = new BigDecimal("5000");
		BigDecimal volumen = length.multiply(height).multiply(width);
		BigDecimal pesoVolumetrico = volumen.divide(divisor, 0, RoundingMode.CEILING);
		if(kilos.doubleValue()>pesoVolumetrico.doubleValue()){
			pesoNeto = kilos;
		}else if(pesoVolumetrico.doubleValue()>kilos.doubleValue()){
			pesoNeto = pesoVolumetrico;
		}else if(pesoVolumetrico.doubleValue()==kilos.doubleValue()){
			pesoNeto = kilos;
		}

		ItemDescription itemDescription = new ItemDescription();
		itemDescription.setHeight(height.intValue());
		itemDescription.setLength(length.intValue());
		itemDescription.setParcelId(Integer.parseInt(xmlRequest.getParcelTypeId()));
		itemDescription.setWeight(pesoNeto.doubleValue());
		itemDescription.setWidth(width.intValue());

		Insurance insurance = new Insurance();
		insurance.setContentDescription(Util.removeAccents(xmlRequest.getContentDescription())); 

		if(xmlRequest.getInsurance()!=null){
			String _insurance = xmlRequest.getInsurance().trim().toLowerCase();
			if(_insurance.equalsIgnoreCase("true")){
				log.info("\tInsurance: true");
				String _insuranceValue = xmlRequest.getInsuranceValue().trim();
				insurance.setDeclaredValue(new BigDecimal(_insuranceValue).doubleValue());
				log.info("\tInsurance Value: "+_insuranceValue);
			}else{
				log.info("\tInsurance: false");
			}
		}

		ReturnDocument returnDocument = new ReturnDocument();
		returnDocument.setType("DRFZ");
		returnDocument.setServiceId(60);

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1);
		Date fechaMasUnMes = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		int fechaEnFormatoInt = Integer.parseInt(sdf.format(fechaMasUnMes));

		ServiceConfiguration serviceConfiguration = new ServiceConfiguration();
		serviceConfiguration.setEffectiveDate(fechaEnFormatoInt);
		serviceConfiguration.setInsurance(insurance);
		serviceConfiguration.setIsInsurance(false);
		serviceConfiguration.setIsReturnDocument(false);
		serviceConfiguration.setOriginZipCodeForRouting(xmlRequest.getOrigenZipCode());
		serviceConfiguration.setQuantityOfLabels(Integer.parseInt(xmlRequest.getNumberOfLabels()));	
		serviceConfiguration.setSalesOrganization(configurations.getSalesOrganization());
		serviceConfiguration.setServiceTypeId(configurations.getServiceTypeId());
		if(xmlRequest.getIsServiceUsesKilos()!=null){
			serviceConfiguration.setIsServiceUsesKilos(xmlRequest.getIsServiceUsesKilos());
			log.info("\tIsServiceUsesKilos: "+xmlRequest.getIsServiceUsesKilos());
		}else{
			log.info("\tIsServiceUsesKilos: [No Recibido]");
		}

		//Direccion Origen - Sin Uso de catalogo de estafeta
		String _addressOrigen = Util.removeAccents(xmlRequest.getOrigenAddress1());
		_addressOrigen = _addressOrigen.length()>50 ? _addressOrigen.substring(0,49) : _addressOrigen;

		String _settlementNameOrigen = Util.removeAccents(xmlRequest.getOrigenNeighborhood());
		_settlementNameOrigen = _settlementNameOrigen.length()>22 ? _settlementNameOrigen.substring(0,21) : _settlementNameOrigen;

		String _townShipName = Util.removeAccents(xmlRequest.getOrigenCity());
		_townShipName = _townShipName.length()>40 ? _townShipName.substring(0,39) : _townShipName;

		String _referenceOrigen = Util.removeAccents(xmlRequest.getOrigenReference());
		_referenceOrigen = _referenceOrigen.length()>100 ? _referenceOrigen.substring(0,99) : _referenceOrigen;
		_referenceOrigen = _referenceOrigen.replaceAll("\\.", "");

		String _zipCodeOrigen = xmlRequest.getOrigenZipCode();

		String _indorInformationOrigen = "";
		String addr2 = xmlRequest.getOrigenAddress2();
		String intNum = xmlRequest.getOrigenIntNum();

		// Limpieza y validación individual
		if (intNum != null && !intNum.trim().isEmpty()) {
			intNum = Util.removeAccents(intNum.trim());
		}

		if (addr2 != null && !addr2.trim().isEmpty()) {
			addr2 = Util.removeAccents(addr2.trim());
		}

		// Concatenación ordenada: primero intNum, luego addr2
		if (intNum != null && !intNum.isEmpty()) {
			_indorInformationOrigen = intNum;
		}

		if (addr2 != null && !addr2.isEmpty()) {
			_indorInformationOrigen = _indorInformationOrigen.isEmpty() ? addr2 : _indorInformationOrigen + " - " + addr2;
		}

		// Validación de longitud máxima permitida (40 chars)
		if (_indorInformationOrigen.length() > 40) {
			_indorInformationOrigen = _indorInformationOrigen.substring(0, 40);
		}

		Address addressOrigin = new Address();
		addressOrigin.setbUsedCode(false);							//Requerido Si 	| Indica si usar catalogos de Estafeta						| 
		addressOrigin.setAddressReference(_referenceOrigen);		//Requerido No 	| Referencia fisica del lugar 								| Datos de la guia 			| Comentario: Longitud max 100
		addressOrigin.setExternalNum(xmlRequest.getOrigenExtNum());	//Requerido Si 	| Numero exterior											| Datos de la guia 			| Comentario: Longitud max 20
		addressOrigin.setIndoorInformation(_indorInformationOrigen);//Requerido No 	| caso de condominios, edificios,info para localizar el dom	|							|
		addressOrigin.setRoadName(_addressOrigen);					//Requerido Si 	| Nombre del camino											| Calle??					|
		addressOrigin.setSettlementName(_settlementNameOrigen);		//Requerido Si 	| Nombre del asentamiento									| Datos de la guia			|
		addressOrigin.setZipCode(_zipCodeOrigen);					//Requerido Si 	| Codigo postal												| Datos de la guia
		addressOrigin.setCountryName("MEX");						//Requerido Si 	| Abreviatura del pais										| Dato de la guia			| Para mexico es MEX
		addressOrigin.setRoadTypeAbbName("XXXXX");					//Requerido Si 	| Abreviacion del tipo de camino
		addressOrigin.setTownshipName(_townShipName);				//Requerido Si 	| Nombre de la ciudad o municipio							| Datos de la guia			| es requerido cuando bUsedCode es FALSE.
		addressOrigin.setSettlementTypeAbbName("XXXXX");			//Requerido Si 	| Abreviacion del tipo de asentamiento						| Validar					| es requerido cuando bUsedCode es FALSE.
		addressOrigin.setStateAbbName(xmlRequest.getOrigenState());	//Requerido Si 	| Abreviacion de la entidad federativa						| Validar					| es requerido cuando bUsedCode es FALSE.

		String _corporateNameOrigen = xmlRequest.getOrigenCorporateName()!=null && !xmlRequest.getOrigenCorporateName().toString().trim().equalsIgnoreCase("") ? xmlRequest.getOrigenCorporateName().toString().trim() : "";
		_corporateNameOrigen = Util.removeAccents(_corporateNameOrigen);
		_corporateNameOrigen = _corporateNameOrigen.length()>49 ? _corporateNameOrigen.substring(0,49) : _corporateNameOrigen;
		_corporateNameOrigen = _corporateNameOrigen.equalsIgnoreCase("") ? "Sin_Razon_Social" : _corporateNameOrigen;

		String _contactName = xmlRequest.getOrigenContactName();
		_contactName = Util.removeAccents(_contactName);
		String _phoneNumber = xmlRequest.getOrigenPhoneNumber();
		Contact contactOrigin = new Contact();
		contactOrigin.setContactName(_contactName);
		contactOrigin.setCorporateName(_corporateNameOrigen);
		contactOrigin.setTelephone(_phoneNumber);
		Origin origin = new Origin(addressOrigin,contactOrigin);

		String _addressDestination = Util.removeAccents(xmlRequest.getAddress1());
		_addressDestination = _addressDestination.length()>50 ? _addressDestination.substring(0,49) : _addressDestination;

		String _settlementNameDestination = Util.removeAccents(xmlRequest.getNeighborhood());
		_settlementNameDestination = _settlementNameDestination.length()>22 ? _settlementNameDestination.substring(0,21) : _settlementNameDestination;

		String _townShipNameDestination = Util.removeAccents(xmlRequest.getCity());
		_townShipNameDestination = _townShipNameDestination.length()>40 ? _townShipNameDestination.substring(0,39) : _townShipNameDestination;

		String _referenceDestino = Util.removeAccents(xmlRequest.getReference());
		_referenceDestino = _referenceDestino.length()>100 ? _referenceDestino.substring(0,99) : _referenceDestino;
		_referenceDestino = _referenceDestino.replaceAll("\\.", "");

		String _zipCodeDestination = xmlRequest.getZipCode();

		String _indorInformationDestino = "";
		String addr2Des = xmlRequest.getAddress2(); // Campo Address2 destino
		String intNumDes = xmlRequest.getIntNum();  // Campo interior destino

		// Limpieza y validación individual
		if (intNumDes != null && !intNumDes.trim().isEmpty()) {
			intNumDes = Util.removeAccents(intNumDes.trim());
		}

		if (addr2Des != null && !addr2Des.trim().isEmpty()) {
			addr2Des = Util.removeAccents(addr2Des.trim());
		}

		// Concatenación ordenada: primero interior, luego address2
		if (intNumDes != null && !intNumDes.isEmpty()) {
			_indorInformationDestino = intNumDes;
		}

		if (addr2Des != null && !addr2Des.isEmpty()) {
			_indorInformationDestino = _indorInformationDestino.isEmpty() ? addr2Des : _indorInformationDestino + " - " + addr2Des;
		}

		// Validación de longitud máxima (40 chars)
		if (_indorInformationDestino.length() > 40) {
			_indorInformationDestino = _indorInformationDestino.substring(0, 40);
		}

		Address addressDestination = new Address();
		addressDestination.setbUsedCode(false);							//Requerido Si 	| Indica si usar catalogos de Estafeta						|
		addressDestination.setAddressReference(_referenceDestino);		//Requerido No 	| Referencia fisica del lugar 								| Datos de la guia 			| Comentario: Longitud max 100
		addressDestination.setExternalNum(xmlRequest.getExtNum());		//Requerido Si 	| Numero exterior											| Datos de la guia 			| Comentario: Longitud max 20
		addressDestination.setIndoorInformation(_indorInformationDestino);//Requerido No 	| caso de condominios, edificios,info para localizar el dom	|							|
		addressDestination.setRoadName(_addressDestination);			//Requerido Si 	| Nombre del camino											| Calle??					|
		addressDestination.setSettlementName(_settlementNameDestination);//Requerido Si | Nombre del asentamiento									| Datos de la guia			|
		addressDestination.setZipCode(_zipCodeDestination);				//Requerido Si 	| Codigo postal												| Datos de la guia
		addressDestination.setCountryName("MEX");						//Requerido Si 	| Abreviatura del pais										| Dato de la guia			| Para mexico es MEX
		addressDestination.setRoadTypeAbbName("XXXXX");					//Requerido Si 	| Abreviacion del tipo de camino
		addressDestination.setTownshipName(_townShipNameDestination);	//Requerido Si 	| Nombre de la ciudad o municipio							| Datos de la guia			| es requerido cuando bUsedCode es FALSE.
		addressDestination.setSettlementTypeAbbName("XXXXX");			//Requerido Si 	| Abreviacion del tipo de asentamiento						| Validar					| es requerido cuando bUsedCode es FALSE.
		addressDestination.setStateAbbName(xmlRequest.getState());		//Requerido Si 	| Abreviacion de la entidad federativa						| Validar					| es requerido cuando bUsedCode es FALSE.

		String _corporateNameDestination = xmlRequest.getCorporateName()!=null && !xmlRequest.getCorporateName().toString().trim().equalsIgnoreCase("") ? xmlRequest.getCorporateName().toString().trim() : "";
		_corporateNameDestination = Util.removeAccents(_corporateNameDestination);
		_corporateNameDestination = _corporateNameDestination.length()>49 ? _corporateNameDestination.substring(0,49) : _corporateNameDestination;
		_corporateNameDestination = _corporateNameDestination.equalsIgnoreCase("") ? "Sin_Razon_Social" : _corporateNameDestination;

		Contact contactDestination = new Contact();
		String _contactNameDestination = xmlRequest.getContactName();
		String _phoneNumberDestination = xmlRequest.getPhoneNumber();
		contactDestination.setContactName(_contactNameDestination);		//Requerido Si 	| Nombre de la persona de contacto	| Datos de la guia
		contactDestination.setCorporateName(_corporateNameDestination);	//Requerido Si 	|
		contactDestination.setTelephone(_phoneNumberDestination);		//Requerido No 	|

		HomeAddress homeAddress = new HomeAddress(addressDestination,contactDestination);

		Destination destination = new Destination();
		destination.setDeliveryPUDOCode("567");		//Requerido No 	| Indica el PUDO donde se hace la entrega, es obligatorio cuando isDeliveryPUDO es TRUE	
		destination.setHomeAddress(homeAddress);	//Requerido Si 	|
		destination.setIsDeliveryToPUDO(false);		//Requerido Si 	| Indica si la entrega sera en un PUDO

		DRAAlternative dRAAlternative = new DRAAlternative();//Enviamos direccion alternativa en blanco
		Location location = new Location(dRAAlternative, origin, destination, false);

		LabelDefinition labelDefinition = new LabelDefinition(itemDescription,location,serviceConfiguration,wayBillDocument);
		WayBill wayBill = new WayBill(identification,labelDefinition,systemInformation);
		String jsonString = processToJSON(wayBill);

		guiaIntegrador.setVia(xmlRequest.getVia()!=null && !xmlRequest.getVia().trim().equalsIgnoreCase("") ? xmlRequest.getVia() : "");
		guiaIntegrador.setRequest(jsonString);
		guiaIntegrador.setEmpresa("Estafeta");

		guiaIntegrador.setLargo(length.toString());
		guiaIntegrador.setAlto(height.toString());
		guiaIntegrador.setFondo(width.toString());
		guiaIntegrador.setPesoVolumetrico(pesoVolumetrico.toString());
		guiaIntegrador.setReexpedicion((xmlRequest.getReexpedicion() == null || xmlRequest.getReexpedicion().trim().isEmpty()) ? "0" : xmlRequest.getReexpedicion().trim());
		guiaIntegrador.setPesoNeto(pesoNeto.toString());

		guiaIntegrador.setServicio(serviceId);
		guiaIntegrador.setCuenta(xmlRequest.getAccount());
		guiaIntegrador.setAlias(xmlRequest.getAlias());
		guiaIntegradorRepository.save(guiaIntegrador);

		/*
		 * Objecto con todo el request
		 * Para PDF Tradicional: FILE_PDF
		 * Para Etiqueta termica: FILE_PDF_SC
		 */
		EstafetaRest estafetaRest = new EstafetaRest();
		estafetaRest.setServer(configurations.getUrlLabelService());
		if(xmlRequest.getPaperType()==null || xmlRequest.getPaperType().trim().equalsIgnoreCase("") || xmlRequest.getPaperType().trim().equalsIgnoreCase("1")){
			//Bond
			estafetaRest.setOutputType(OutputType.FILE_PDF.getText());
		}else if(xmlRequest.getPaperType().trim().equalsIgnoreCase("2")){
			//Termico
			estafetaRest.setOutputType(OutputType.FILE_PDF_SC.getText());
		}
		estafetaRest.setOutputGroup(OutputGroup.REQUEST.getText());
		estafetaRest.setResponseMode(ResponseMode.SYNC_INLINE.getText());
		estafetaRest.setPrintingTemplate(PrintingTemplate.NORMAL_TIPO7_ZEBRAORI.getText());
		estafetaRest.setApikey(configurations.getApikey());
		estafetaRest.setAuthorization(tokenEstafeta.getToken());
		estafetaRest.setBodyJSON(jsonString);

		Connector service = new Connector();
		service.doWayBill(estafetaRest);

		JSONObject responseJSON = new JSONObject();
		try {
			String _response = service.getResponse().trim();
			responseJSON = new JSONObject(_response);
			if(responseJSON.has("labelPetitionResult")){
				JSONObject labelPetitionResult = responseJSON.getJSONObject("labelPetitionResult");
				JSONObject result = labelPetitionResult.getJSONObject("result");
				String code = String.valueOf(result.get("code"));
				if(code.equalsIgnoreCase("0")) {
					String rastreo = result.has("description") ? result.get("description").toString() : "";
					response.setResponseCode(code);
					response.setFile(responseJSON.has("data") ? responseJSON.get("data").toString() : "");
					response.setTracking(rastreo);
					response.setResponseDescription("");
					guiaIntegrador.setTracking(rastreo);
					guiaIntegradorRepository.save(guiaIntegrador);
					log.info("\tTrackingId: "+rastreo);
					log.info("\tAccount");
					for (Iterator<Map.Entry<String, String>> entries = values.entrySet().iterator(); entries.hasNext(); ) {
						Map.Entry<String, String> entry = entries.next();
						log.info("\t\t"+entry.getKey()+" : "+entry.getValue());
					}
					log.info("\t\tPaperType : "+xmlRequest.getPaperType());
					if(context.get("pathlabel")!=null){
						String fileBase64 = responseJSON.has("data") ? responseJSON.get("data").toString() : "";
						if (fileBase64 != null && !fileBase64.isEmpty()) {
							try{
								byte[] bytes = Base64.getDecoder().decode(fileBase64.getBytes("UTF-8"));
								String fileName = "OUTBOUND_LABEL_" + rastreo + ".pdf";
								String finalPath = context.get("pathlabel").toString();
								String fileNameFull = finalPath + fileName;
								FileOutputStream fout = new FileOutputStream(fileNameFull);
								fout.write(bytes);
								fout.close();
								log.info("\t\tPath: "+fileNameFull);
							}catch (IOException e) {
								log.info("\t\tError al generar el archivo PDF desde base64:"+e.getMessage());
							}
						}else{
							log.info("\t\tNo se encontro contenido base64 en responseJSON.get(\"data\")");
						}
					}
				}else {
					//Guardado de la respuesta
					guiaIntegrador.setResponse(_response);
					guiaIntegradorRepository.save(guiaIntegrador);

					String description = "";
					if(result.has("description")) {
						description = String.valueOf(result.get("description"));
					}
					response.setResponseCode(code);
					response.setFile("");
					response.setTracking("");
					response.setResponseDescription(description);
				}
			}else{

				//Guardado de la respuesta
				guiaIntegrador.setResponse(_response);
				guiaIntegradorRepository.save(guiaIntegrador);

				if(responseJSON.has("code")){
					String description = "";
					if(responseJSON.has("description")) {
						description = String.valueOf(responseJSON.get("description"));
					}
					response.setResponseCode(String.valueOf(responseJSON.get("code")));
					response.setFile("");
					response.setTracking("");
					response.setResponseDescription(description);
				}
				if(responseJSON.has("error")){
					String description = "";
					if(responseJSON.has("error")) {
						description = String.valueOf(responseJSON.get("error"));
					}
					response.setResponseCode(String.valueOf(responseJSON.get("error")));
					response.setFile("");
					response.setTracking("");
					response.setResponseDescription(description);
				}
			}
		}catch (Exception e1){
			try {
				String rawResponse = service.getResponse();
				guiaIntegrador.setResponse(rawResponse);
				guiaIntegradorRepository.save(guiaIntegrador);
				if (rawResponse != null && !rawResponse.trim().isEmpty()) {
					JSONArray responseError = new JSONArray(rawResponse);
					if (responseError.length() > 0) {
						JSONObject error = responseError.getJSONObject(0);
						response.setResponseCode(String.valueOf(error.get("code")));
						response.setResponseDescription(error.optString("description", "Error desconocido"));
					} else {
						response.setResponseCode("ERROR");
						response.setResponseDescription("Error desconocido en respuesta vacia del servicio.");
					}
				} else {
					throw new JSONException("Respuesta vacia del servicio");
				}
			} catch (Exception ex) {
				// Maneja errores JSON o respuestas inválidas
				response.setResponseCode("EXCEPTION");
				response.setResponseDescription(e1.getMessage() != null ? e1.getMessage() : "Error inesperado");
			}
		}
		return response;
	}
	public Map<String, Object> getContext(){
		Map<String,String> values = this.getAccount();
		GuiaIntegrador guiaIntegrador = this.getGuiaIntegrador();
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = this.getXmlRequest();
		com.integrador.xml.services.EstafetaLabelResponse xmlResponse = this.getXmlResponse();
		GuiaIntegradorRepository guiaIntegradorRepository = this.getGuiaIntegradorRepository();
		AtributoService atributoService = this.getAtributoService();
		UsuariosService usuariosService = this.getUsuariosService();
		Map<String,Object> context = new HashMap<String,Object>();
		context.put("account", values);
		context.put("guiaIntegrador", guiaIntegrador);
		context.put("xmlRequest", xmlRequest);
		context.put("xmlResponse", xmlResponse);
		context.put("guiaIntegradorRepository", guiaIntegradorRepository);
		context.put("atributoService", atributoService);
		context.put("usuariosService", usuariosService);
		return context;
	}

	@SuppressWarnings("unchecked")
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
		UsuariosService usuariosService = (UsuariosService) context.get("usuariosService");
		this.setUsuariosService(usuariosService);
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

	public String getBusinessRuleBeforeSend() {
		return businessRuleBeforeSend;
	}

	public void setBusinessRuleBeforeSend(String businessRuleBeforeSend) {
		this.businessRuleBeforeSend = businessRuleBeforeSend;
	}

	public AtributoService getAtributoService() {
		return atributoService;
	}

	public void setAtributoService(AtributoService atributoService) {
		this.atributoService = atributoService;
	}

	public UsuariosService getUsuariosService() {
		return usuariosService;
	}

	public void setUsuariosService(UsuariosService usuarioService) {
		this.usuariosService = usuarioService;
	}
}