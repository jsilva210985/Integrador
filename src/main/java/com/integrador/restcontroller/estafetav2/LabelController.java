/*
 * ==============================================================================================
 * Copyright (c) 2026 J. Jose de Jesus Silva A.
 * Contact: jsilva210985@gmail.com
 * 
 * This controller handles generating shipment labels (waybills) using the Estafeta Rest V2 API
 * via the updated Estafeta SDK client library. It handles authentication, validation,
 * execution of dynamic business rules, and mapping of request/response payloads to match
 * the legacy label controller behavior.
 * 
 * All rights reserved.
 * ==============================================================================================
 */

package com.integrador.restcontroller.estafetav2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.estafeta.restconnector.models.Address;
import org.estafeta.restconnector.models.Contact;
import org.estafeta.restconnector.models.Destination;
import org.estafeta.restconnector.models.HomeAddress;
import org.estafeta.restconnector.models.Identification;
import org.estafeta.restconnector.models.Insurance;
import org.estafeta.restconnector.models.ItemDescription;
import org.estafeta.restconnector.models.LabelDefinition;
import org.estafeta.restconnector.models.Location;
import org.estafeta.restconnector.models.Origin;
import org.estafeta.restconnector.models.ReturnDocument;
import org.estafeta.restconnector.models.ServiceConfiguration;
import org.estafeta.restconnector.models.SystemInformation;
import org.estafeta.restconnector.models.WayBill;
import org.estafeta.restconnector.models.WayBillDocument;
import static org.estafeta.restconnector.utilities.Utils.processToJSON;
import org.estafetav2.Label.LabelClient;
import org.estafetav2.authentication.AuthenticatorClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estafeta.webservice.frecuencia.EstafetaFrecuencia;
import com.estafeta.webservice.frecuencia.EstafetaFrecuenciaCliente;
import com.integrador.exceptions.BusinessRuleException;
import com.integrador.models.Configuracion;
import com.integrador.models.GuiaIntegrador;
import com.integrador.models.UsuarioAlan;
import com.integrador.repositories.ConfiguracionRepository;
import com.integrador.repositories.GuiaIntegradorRepository;
import com.integrador.repositories.TokenRepository;
import com.integrador.repositories.UsuarioAlanRepository;
import com.integrador.services.AtributoService;
import com.integrador.services.UsuariosService;
import com.integrador.util.AESAlgorithm;
import com.integrador.util.Util;

/**
 * REST controller for handling Estafeta Rest V2 shipping label generation.
 */
@RestController("estafetaLabelControllerV2")
@RequestMapping({"/EstafetaRest"})
public class LabelController {

	@Autowired TokenRepository tokenRepository;
	@Autowired AtributoService atributoService;
	@Autowired UsuariosService usuariosService;
	@Autowired UsuarioAlanRepository usuarioAlanRepository;
	@Autowired GuiaIntegradorRepository guiasIntegradorRepository;
	@Autowired ConfiguracionRepository configuracionRepository;

	@Value("${estafeta.restservice.br.beforesend}") String beforeSendBR;

	static final Logger log = LoggerFactory.getLogger(LabelController.class);

	/**
	 * Generates a new Estafeta shipping label (waybill) using the Rest V2 API.
	 * <p>
	 * This endpoint performs parameter validation, authenticates the client user,
	 * processes business rules, maps the input to the required V2 payload, invokes
	 * the V2 Label SDK client, saves the PDF waybill locally if configured,
	 * and maps the response to legacy V1 format.
	 *
	 * @param request the HTTP request object.
	 * @param content the raw JSON request body containing shipment and authentication details.
	 * @return a ResponseEntity containing the mapped JSON response with the PDF file and tracking code.
	 */
	@PostMapping(value = "/Label")
	public ResponseEntity<String> labelv3(HttpServletRequest request, @RequestBody String content) {
		JSONObject response = new JSONObject();
		JSONObject serviceParams = new JSONObject();
		String error = "";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		log.info("");
		log.info("== Estafeta RestService V2 / Label ==>");
		try {
			serviceParams = new JSONObject(content);
		} catch (Exception e) {
			return new ResponseEntity<String>("JSON mal formado: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		if (!serviceParams.has("client") || serviceParams.getString("client").trim().equalsIgnoreCase("")) {
			error = "Debe especificar el cliente";
			response.put("response_code", -1);
			response.put("response_description", error);
			log.info("\t" + error);
			return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
		}
		if (!serviceParams.has("password") || serviceParams.getString("password").trim().equalsIgnoreCase("")) {
			error = "Debe especificar la contraseña";
			response.put("response_code", -1);
			response.put("response_description", error);
			log.info("\t" + error);
			return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
		}

		if (!serviceParams.has("token") || serviceParams.getString("token").trim().equalsIgnoreCase("")) {
			error = "Debe especificar el token";
			response.put("response_code", -1);
			response.put("response_description", error);
			log.info("\t" + error);
			return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
		}

		if (serviceParams.has("insurance") && !serviceParams.getString("insurance").trim().equalsIgnoreCase("")) {
			String insurance = serviceParams.getString("insurance").trim().toLowerCase();
			if (!com.integrador.util.Util.isBoolean(insurance)) {
				error = "Valor incorrecto para Isurance, valor esperado [true,false]";
				response.put("response_code", -1);
				response.put("response_description", error);
				log.info("\t" + error);
				return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
			}
			// Si es true, se valida que contenga el nodo insuranceValue
			if (insurance.equalsIgnoreCase("true")) {
				String insuranceValue = serviceParams.has("insuranceValue") ? String.valueOf(serviceParams.get("insuranceValue")).trim() : "";
				if (insuranceValue.equalsIgnoreCase("")) {
					error = "Debe especificar InsuranceValue";
					response.put("response_code", -1);
					response.put("response_description", error);
					log.info("\t" + error);
					return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
				}
				if (!com.integrador.util.Util.isStringNumeric(insuranceValue)) {
					error = "Valor incorrecto para isuranceValue, Patron: '\\d{1,6}.\\d{1,6}'";
					response.put("response_code", -1);
					response.put("response_description", error);
					log.info("\t" + error);
					return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
				}
			}
		}

		if (serviceParams.has("isServiceUsesKilos") && !serviceParams.getString("isServiceUsesKilos").trim().equalsIgnoreCase("")) {
			String isServiceUsesKilosVal = serviceParams.getString("isServiceUsesKilos").trim().toLowerCase();
			if (!com.integrador.util.Util.isBoolean(isServiceUsesKilosVal)) {
				error = "Valor incorrecto para isServiceUsesKilos, valor esperado [true,false]";
				response.put("response_code", -1);
				response.put("response_description", error);
				log.info("\t" + error);
				return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
			}
		}

		GuiaIntegrador g = saveRequest(serviceParams);
		log.info("\tRequest guardado");
		AESAlgorithm e = new AESAlgorithm();
		UsuarioAlan usuario = usuarioAlanRepository.findByUsuarioAndContrasena(serviceParams.getString("client"), e.encrypt(serviceParams.getString("password")));
		if (usuario == null) {
			error = "Usuario o Contraseña invalido";
			response.put("response_code", -1);
			response.put("response_description", error);
			log.info("\t" + error);
			g.setEstatus(error);
			guiasIntegradorRepository.save(g);
			return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
		}

		com.integrador.models.Token token = tokenRepository.findByIdUsuarioAndToken(usuario.getIdUsuario(), serviceParams.getString("token"));
		if (token == null) {
			error = "Token invalido";
			response.put("response_code", -2);
			response.put("response_description", error);
			log.info("\t" + error);
			g.setEstatus(error);
			guiasIntegradorRepository.save(g);
			return new ResponseEntity<String>(response.toString(), headers, HttpStatus.BAD_REQUEST);
		}

		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = new com.integrador.xml.services.EstafetaLabelRequest();
		com.integrador.xml.services.EstafetaLabelResponse xmlResponse = new com.integrador.xml.services.EstafetaLabelResponse();

		// Remitente
		xmlRequest.setOrigenAddress1(serviceParams.optString("origenAddress1", ""));
		xmlRequest.setOrigenNeighborhood(serviceParams.optString("origenNeighborhood", ""));
		xmlRequest.setOrigenZipCode(serviceParams.optString("origenZipCode", ""));
		xmlRequest.setOrigenContactName(serviceParams.optString("origenContactName", ""));
		xmlRequest.setOrigenState(serviceParams.optString("origenState", ""));
		xmlRequest.setOrigenCity(serviceParams.optString("origenCity", ""));
		xmlRequest.setOrigenAddress2(serviceParams.optString("origenAddress2", ""));
		xmlRequest.setOrigenPhoneNumber(serviceParams.optString("origenPhoneNumber", ""));
		xmlRequest.setOrigenExtNum(serviceParams.optString("origenExtNum", ""));
		xmlRequest.setOrigenIntNum(serviceParams.optString("origenIntNum", ""));
		xmlRequest.setOrigenCorporateName(serviceParams.optString("origenCorporateName", ""));
		xmlRequest.setOrigenReference(serviceParams.optString("origenReference", ""));

		// Destinatario
		xmlRequest.setAddress1(serviceParams.optString("address1", ""));
		xmlRequest.setExtNum(serviceParams.optString("extNum", ""));
		xmlRequest.setIntNum(serviceParams.optString("intNum", ""));
		xmlRequest.setNeighborhood(serviceParams.optString("neighborhood", ""));
		xmlRequest.setZipCode(serviceParams.optString("zipCode", ""));
		xmlRequest.setContactName(serviceParams.optString("contactName", ""));
		xmlRequest.setState(serviceParams.optString("state", ""));
		xmlRequest.setCity(serviceParams.optString("city", ""));
		xmlRequest.setAddress2(serviceParams.optString("address2", ""));
		xmlRequest.setPhoneNumber(serviceParams.optString("phoneNumber", ""));
		xmlRequest.setCorporateName(serviceParams.optString("corporateName", ""));
		xmlRequest.setReference(serviceParams.optString("reference", ""));

		// Información adicional
		xmlRequest.setAditionalInfo(serviceParams.optString("aditionalInfo", ""));
		xmlRequest.setServiceTypeId(serviceParams.optString("serviceTypeId", ""));
		xmlRequest.setContent(serviceParams.optString("content", ""));
		xmlRequest.setContentDescription(serviceParams.optString("contentDescription", ""));
		xmlRequest.setNumberOfLabels(serviceParams.optString("numberOfLabels", "1"));
		xmlRequest.setParcelTypeId(serviceParams.optString("parcelTypeId", ""));
		xmlRequest.setWeight(serviceParams.optString("weight", "0.0"));
		xmlRequest.setHeight(serviceParams.optString("height", "0.0"));
		xmlRequest.setLength(serviceParams.optString("length", "0.0"));
		xmlRequest.setWidth(serviceParams.optString("width", "0.0"));
		xmlRequest.setPaperType(serviceParams.optString("paperType", ""));
		xmlRequest.setDeliveryToEstafetaOffice(serviceParams.optString("deliveryToEstafetaOffice", "false"));
		xmlRequest.setClient(serviceParams.optString("client", ""));
		xmlRequest.setPassword(serviceParams.optString("password", ""));
		xmlRequest.setToken(serviceParams.optString("token", ""));

		xmlRequest.setIsServiceUsesKilos(serviceParams.has("isServiceUsesKilos") && !serviceParams.getString("isServiceUsesKilos").trim().isEmpty() ? serviceParams.getString("isServiceUsesKilos").trim() : null);
		xmlRequest.setService(serviceParams.optString("service", ""));
		xmlRequest.setProvider("Estafeta");
		xmlRequest.setVia("Integrador");

		String cuenta = "";
		if (serviceParams.has("cuenta") && !serviceParams.getString("cuenta").trim().isEmpty()) {
			cuenta = serviceParams.getString("cuenta").trim();
			log.info("\tCuenta obtenida del request JSON (cuenta): " + cuenta);
		} else if (serviceParams.has("account") && !serviceParams.getString("account").trim().isEmpty()) {
			cuenta = serviceParams.getString("account").trim();
			log.info("\tCuenta obtenida del request JSON (account): " + cuenta);
		} else {
			try {
				cuenta = getAccount(
						g.getRemitenteCP(),
						g.getDestinatarioCP(),
						xmlRequest.getWeight(),
						xmlRequest.getServiceTypeId(),
						usuario
				);
				log.info("\tCuenta obtenida dinámicamente: " + cuenta);
			} catch (Exception ex) {
				log.error("\tError obteniendo la cuenta: " + ex.getMessage(), ex);
				response.put("response_code", -3);
				response.put("response_description", "Error obteniendo la cuenta: " + ex.getMessage());
				return new ResponseEntity<String>(response.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		xmlRequest.setAccount(cuenta);
		xmlRequest.setInsurance(serviceParams.optString("insurance", "false"));
		xmlRequest.setInsuranceValue(serviceParams.optString("insuranceValue", "0.0"));
		log.info("\tInvocation Type: RestService V2");

		log.info("\tObteniendo credenciales iniciales para cuenta: [" + cuenta + "]");
		Map<String, String> values = atributoService.getByTipoInMap(cuenta);
		if (values.isEmpty()) {
			log.info("\tCuenta [" + cuenta + "] no encontrada. Buscando fallback default Estafeta_Label_Rest...");
			values = atributoService.getByTipoInMap("Estafeta_Label_Rest");
		}

		// Asegurar nombres de propiedades compatibles con V1/V2
		if (values.containsKey("customerNumber") && !values.containsKey("customer_number")) {
			values.put("customer_number", values.get("customerNumber"));
		}
		if (values.containsKey("suscriberId") && !values.containsKey("suscriber_id")) {
			values.put("suscriber_id", values.get("suscriberId"));
		}
		if (values.containsKey("salesOrganization") && !values.containsKey("sales_organization")) {
			values.put("sales_organization", values.get("salesOrganization"));
		}
		if (values.containsKey("serviceTypeIdTerrestre") && !values.containsKey("service_type_id_terrestre")) {
			values.put("service_type_id_terrestre", values.get("serviceTypeIdTerrestre"));
		}
		if (values.containsKey("serviceTypeIdExpress") && !values.containsKey("service_type_id_express")) {
			values.put("service_type_id_express", values.get("serviceTypeIdExpress"));
		}
		if (values.containsKey("systemInformationId") && !values.containsKey("system_information_id")) {
			values.put("system_information_id", values.get("systemInformationId"));
		}
		if (values.containsKey("systemInformationName") && !values.containsKey("system_information_name")) {
			values.put("system_information_name", values.get("systemInformationName"));
		}
		if (values.containsKey("systemInformationVersion") && !values.containsKey("system_information_version")) {
			values.put("system_information_version", values.get("systemInformationVersion"));
		}

		Map<String, Object> context = new HashMap<String, Object>();
		context.put("account", values);
		context.put("guiaIntegrador", g);
		context.put("xmlRequest", xmlRequest);
		context.put("xmlResponse", xmlResponse);
		context.put("guiaIntegradorRepository", guiasIntegradorRepository);
		context.put("atributoService", atributoService);
		context.put("usuariosService", usuariosService);

		// Ejecución de Reglas de Negocio
		if (beforeSendBR != null && !beforeSendBR.trim().equalsIgnoreCase("")) {
			String[] list = beforeSendBR.split(",");
			for (String br : list) {
				log.info("\tBeforeSendBR: " + br);
				try {
					Object tempClass = com.integrador.util.SpringContext.getBean(br);
					Class<?> classLoaded = tempClass.getClass();
					Class<?>[] arguments = new Class<?>[]{Map.class};
					Method metodo = classLoaded.getDeclaredMethod("run", arguments);
					Object result = metodo.invoke(tempClass, context);
					if (result != null) {
						context = (Map<String, Object>) result;
					}
				} catch (BusinessRuleException bre) {
					String msg = bre.getMessage();
					int code = bre.getCode();
					response.put("response_code", code);
					response.put("response_description", msg);
					log.error("\tBusinessRuleException: " + bre.getMessage());
					return new ResponseEntity<String>(response.toString(), headers, HttpStatus.OK);
				} catch (InvocationTargetException ite) {
					Throwable causa = ite.getCause();
					int codigo = -2;
					if (causa instanceof BusinessRuleException) {
						codigo = ((BusinessRuleException) causa).getCode();
					}
					String mensaje = causa != null && causa.getMessage() != null ? causa.getMessage() : String.valueOf(causa);
					response.put("response_code", codigo);
					response.put("response_description", mensaje);
					log.error("\tBusinessRuleException en BR " + br + ": " + mensaje);
					return new ResponseEntity<String>(response.toString(), headers, HttpStatus.OK);
				} catch (Exception ex) {
					Throwable causa = ex.getCause();
					int codigo = -2;
					if (causa instanceof BusinessRuleException) {
						codigo = ((BusinessRuleException) causa).getCode();
					}
					String mensaje = causa != null && causa.getMessage() != null ? causa.getMessage() : ex.getMessage();
					response.put("response_code", codigo);
					response.put("response_description", mensaje);
					log.error("\tError en BR " + br + ": " + mensaje, ex);
					return new ResponseEntity<String>(response.toString(), headers, HttpStatus.OK);
				}
			}
		}

		// Recuperar objetos actualizados por las Business Rules
		xmlRequest = (com.integrador.xml.services.EstafetaLabelRequest) context.get("xmlRequest");
		values = (Map<String, String>) context.get("account");

		String serviceId = "0";
		if (xmlRequest.getServiceTypeId().equalsIgnoreCase("Terrestre")) {
			serviceId = values.get("service_type_id_terrestre") != null ? values.get("service_type_id_terrestre").toString().trim() : "70";
			if (serviceId.equalsIgnoreCase("0") || serviceId.isEmpty()) {
				serviceId = "70";
			}
			log.info("\tService Type: " + serviceId + " [Terrestre]");
		} else {
			serviceId = values.get("service_type_id_express") != null ? values.get("service_type_id_express").toString().trim() : "60";
			if (serviceId.equalsIgnoreCase("0") || serviceId.isEmpty()) {
				serviceId = "60";
			}
			log.info("\tService Type: " + serviceId + " [Express]");
		}

		// Construir los modelos del RestConnector V1 para generar el JSON a enviar al Gateway V2
		Identification identification = new Identification();
		String customerNumber = values.get("customer_number");
		if (customerNumber == null || customerNumber.trim().isEmpty()) {
			customerNumber = "0000000"; // Fallback por defecto (sandbox público)
		}
		identification.setCustomerNumber(customerNumber);

		String suscriberId = values.get("suscriber_id");
		if (suscriberId == null || suscriberId.trim().isEmpty()) {
			suscriberId = "01"; // Fallback por defecto (sandbox público)
		}
		identification.setSuscriberId(suscriberId);

		SystemInformation systemInformation = new SystemInformation();
		String sysId = values.get("system_information_id");
		if (sysId == null || sysId.trim().isEmpty()) {
			sysId = "AP01";
		}
		systemInformation.setId(sysId);

		String sysName = values.get("system_information_name");
		if (sysName == null || sysName.trim().isEmpty()) {
			sysName = "AP01";
		}
		systemInformation.setName(sysName);

		String sysVer = values.get("system_information_version");
		if (sysVer == null || sysVer.trim().isEmpty()) {
			sysVer = "1.0.0";
		}
		systemInformation.setVersion(sysVer);

		String _additionalInfo = Util.removeAccents(xmlRequest.getAditionalInfo());
		_additionalInfo = _additionalInfo.length() > 25 ? _additionalInfo.substring(0, 24) : _additionalInfo.trim();

		String _content = Util.removeAccents(xmlRequest.getContent());

		WayBillDocument wayBillDocument = new WayBillDocument();
		wayBillDocument.setAditionalInfo(_additionalInfo);
		wayBillDocument.setContent(_content);
		wayBillDocument.setCostCenter(null);
		wayBillDocument.setCustomerShipmentId(null);
		wayBillDocument.setGroupShipmentId(null);
		wayBillDocument.setReferenceNumber(null);

		BigDecimal weight = new BigDecimal(xmlRequest.getWeight() == null ? "1.0" : xmlRequest.getWeight());
		BigDecimal height = new BigDecimal(xmlRequest.getHeight() == null ? "1.0" : xmlRequest.getHeight());
		BigDecimal length = new BigDecimal(xmlRequest.getLength() == null ? "1.0" : xmlRequest.getLength());
		BigDecimal width = new BigDecimal(xmlRequest.getWidth() == null ? "1.0" : xmlRequest.getWidth());
		BigDecimal kilos = new BigDecimal(g.getKilos());

		BigDecimal pesoNeto = new BigDecimal(0);

		if (Integer.parseInt(xmlRequest.getParcelTypeId()) == 1) {
			if (length.intValue() == 0) length = new BigDecimal(1);
			if (width.intValue() == 0) width = new BigDecimal(1);
			if (height.intValue() == 0) height = new BigDecimal(1);
		}

		BigDecimal divisor = new BigDecimal("5000");
		BigDecimal volumen = length.multiply(height).multiply(width);
		BigDecimal pesoVolumetrico = volumen.divide(divisor, 0, RoundingMode.CEILING);
		if (kilos.doubleValue() > pesoVolumetrico.doubleValue()) {
			pesoNeto = kilos;
		} else if (pesoVolumetrico.doubleValue() > kilos.doubleValue()) {
			pesoNeto = pesoVolumetrico;
		} else {
			pesoNeto = kilos;
		}

		ItemDescription itemDescription = new ItemDescription();
		itemDescription.setHeight(height.intValue());
		itemDescription.setLength(length.intValue());
		itemDescription.setParcelId(Integer.parseInt(xmlRequest.getParcelTypeId()));
		itemDescription.setWeight(pesoNeto.doubleValue());
		itemDescription.setWidth(width.intValue());

		// Requerimiento de Carta Porte para Estafeta V2: Construir el nodo 'merchandises'
		org.estafeta.restconnector.models.Merchandise merchandise = new org.estafeta.restconnector.models.Merchandise();
		merchandise.setMerchandiseValue(0.1);
		merchandise.setCurrency("MXN");
		merchandise.setProductServiceCode("10131508");
		merchandise.setMerchandiseQuantity(1.0);
		merchandise.setMeasurementUnitCode("F63");
		merchandise.setIsInternational("false");
		merchandise.setIsImport("false");
		merchandise.setPackagingCode("4A");
		merchandise.setWeight(pesoNeto.doubleValue());

		java.util.List<org.estafeta.restconnector.models.Merchandise> merchandiseList = new java.util.ArrayList<>();
		merchandiseList.add(merchandise);

		org.estafeta.restconnector.models.Merchandises merchandises = new org.estafeta.restconnector.models.Merchandises();
		merchandises.setWeight(pesoNeto.doubleValue());
		merchandises.setWeightUnitCode("XLU");
		merchandises.setMerchandise(merchandiseList);

		itemDescription.setMerchandises(merchandises);

		Insurance insurance = new Insurance();
		insurance.setContentDescription(Util.removeAccents(xmlRequest.getContentDescription()));

		if (xmlRequest.getInsurance() != null) {
			String _insurance = xmlRequest.getInsurance().trim().toLowerCase();
			if (_insurance.equalsIgnoreCase("true")) {
				String _insuranceValue = xmlRequest.getInsuranceValue().trim();
				insurance.setDeclaredValue(new BigDecimal(_insuranceValue).doubleValue());
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
		String salesOrgStr = values.get("sales_organization");
		int salesOrg = 112; // Valor por defecto seguro (ej: de los ejemplos oficiales)
		if (salesOrgStr != null && !salesOrgStr.trim().isEmpty()) {
			try {
				salesOrg = Integer.parseInt(salesOrgStr.trim());
			} catch (Exception ex) {
				log.warn("\tSales organization invalido: " + salesOrgStr + ", usando fallback: " + salesOrg);
			}
		} else {
			log.warn("\tSales organization es nulo o vacio, usando fallback por defecto: " + salesOrg);
		}
		serviceConfiguration.setSalesOrganization(salesOrg);
		serviceConfiguration.setServiceTypeId(serviceId);
		if (xmlRequest.getIsServiceUsesKilos() != null) {
			serviceConfiguration.setIsServiceUsesKilos(xmlRequest.getIsServiceUsesKilos());
		}

		// Dirección Origen
		String _addressOrigen = Util.removeAccents(xmlRequest.getOrigenAddress1());
		_addressOrigen = _addressOrigen.length() > 50 ? _addressOrigen.substring(0, 49) : _addressOrigen;

		String _settlementNameOrigen = Util.removeAccents(xmlRequest.getOrigenNeighborhood());
		_settlementNameOrigen = _settlementNameOrigen.length() > 22 ? _settlementNameOrigen.substring(0, 21) : _settlementNameOrigen;

		String _townShipName = Util.removeAccents(xmlRequest.getOrigenCity());
		_townShipName = _townShipName.length() > 40 ? _townShipName.substring(0, 39) : _townShipName;

		String _referenceOrigen = Util.removeAccents(xmlRequest.getOrigenReference());
		_referenceOrigen = _referenceOrigen.length() > 100 ? _referenceOrigen.substring(0, 99) : _referenceOrigen;
		_referenceOrigen = _referenceOrigen.replaceAll("\\.", "");

		String _zipCodeOrigen = xmlRequest.getOrigenZipCode();

		String _indorInformationOrigen = "";
		String addr2 = xmlRequest.getOrigenAddress2();
		String intNum = xmlRequest.getOrigenIntNum();

		if (intNum != null && !intNum.trim().isEmpty()) {
			intNum = Util.removeAccents(intNum.trim());
		}
		if (addr2 != null && !addr2.trim().isEmpty()) {
			addr2 = Util.removeAccents(addr2.trim());
		}
		if (intNum != null && !intNum.isEmpty()) {
			_indorInformationOrigen = intNum;
		}
		if (addr2 != null && !addr2.isEmpty()) {
			_indorInformationOrigen = _indorInformationOrigen.isEmpty() ? addr2 : _indorInformationOrigen + " - " + addr2;
		}
		if (_indorInformationOrigen.length() > 40) {
			_indorInformationOrigen = _indorInformationOrigen.substring(0, 40);
		}

		Address addressOrigin = new Address();
		addressOrigin.setbUsedCode(false);
		addressOrigin.setAddressReference(_referenceOrigen);
		addressOrigin.setExternalNum(xmlRequest.getOrigenExtNum());
		addressOrigin.setIndoorInformation(_indorInformationOrigen);
		addressOrigin.setRoadName(_addressOrigen);
		addressOrigin.setSettlementName(_settlementNameOrigen);
		addressOrigin.setZipCode(_zipCodeOrigen);
		addressOrigin.setCountryName("MEX");
		addressOrigin.setRoadTypeAbbName("XXXXX");
		addressOrigin.setTownshipName(_townShipName);
		addressOrigin.setSettlementTypeAbbName("XXXXX");
		addressOrigin.setStateAbbName(xmlRequest.getOrigenState());

		String _corporateNameOrigen = xmlRequest.getOrigenCorporateName() != null && !xmlRequest.getOrigenCorporateName().toString().trim().equalsIgnoreCase("") ? xmlRequest.getOrigenCorporateName().toString().trim() : "";
		_corporateNameOrigen = Util.removeAccents(_corporateNameOrigen);
		_corporateNameOrigen = _corporateNameOrigen.length() > 49 ? _corporateNameOrigen.substring(0, 49) : _corporateNameOrigen;
		_corporateNameOrigen = _corporateNameOrigen.equalsIgnoreCase("") ? "Sin_Razon_Social" : _corporateNameOrigen;

		String _contactName = Util.removeAccents(xmlRequest.getOrigenContactName());
		String _phoneNumber = xmlRequest.getOrigenPhoneNumber();
		Contact contactOrigin = new Contact();
		contactOrigin.setContactName(_contactName);
		contactOrigin.setCorporateName(_corporateNameOrigen);
		contactOrigin.setTelephone(_phoneNumber);
		Origin origin = new Origin(addressOrigin, contactOrigin);

		// Dirección Destinatario
		String _addressDestination = Util.removeAccents(xmlRequest.getAddress1());
		_addressDestination = _addressDestination.length() > 50 ? _addressDestination.substring(0, 49) : _addressDestination;

		String _settlementNameDestination = Util.removeAccents(xmlRequest.getNeighborhood());
		_settlementNameDestination = _settlementNameDestination.length() > 22 ? _settlementNameDestination.substring(0, 21) : _settlementNameDestination;

		String _townShipNameDestination = Util.removeAccents(xmlRequest.getCity());
		_townShipNameDestination = _townShipNameDestination.length() > 40 ? _townShipNameDestination.substring(0, 39) : _townShipNameDestination;

		String _referenceDestino = Util.removeAccents(xmlRequest.getReference());
		_referenceDestino = _referenceDestino.length() > 100 ? _referenceDestino.substring(0, 99) : _referenceDestino;
		_referenceDestino = _referenceDestino.replaceAll("\\.", "");

		String _zipCodeDestination = xmlRequest.getZipCode();

		String _indorInformationDestino = "";
		String addr2Des = xmlRequest.getAddress2();
		String intNumDes = xmlRequest.getIntNum();

		if (intNumDes != null && !intNumDes.trim().isEmpty()) {
			intNumDes = Util.removeAccents(intNumDes.trim());
		}
		if (addr2Des != null && !addr2Des.trim().isEmpty()) {
			addr2Des = Util.removeAccents(addr2Des.trim());
		}
		if (intNumDes != null && !intNumDes.isEmpty()) {
			_indorInformationDestino = intNumDes;
		}
		if (addr2Des != null && !addr2Des.isEmpty()) {
			_indorInformationDestino = _indorInformationDestino.isEmpty() ? addr2Des : _indorInformationDestino + " - " + addr2Des;
		}
		if (_indorInformationDestino.length() > 40) {
			_indorInformationDestino = _indorInformationDestino.substring(0, 40);
		}

		Address addressDestination = new Address();
		addressDestination.setbUsedCode(false);
		addressDestination.setAddressReference(_referenceDestino);
		addressDestination.setExternalNum(xmlRequest.getExtNum());
		addressDestination.setIndoorInformation(_indorInformationDestino);
		addressDestination.setRoadName(_addressDestination);
		addressDestination.setSettlementName(_settlementNameDestination);
		addressDestination.setZipCode(_zipCodeDestination);
		addressDestination.setCountryName("MEX");
		addressDestination.setRoadTypeAbbName("XXXXX");
		addressDestination.setTownshipName(_townShipNameDestination);
		addressDestination.setSettlementTypeAbbName("XXXXX");
		addressDestination.setStateAbbName(xmlRequest.getState());

		String _corporateNameDestination = xmlRequest.getCorporateName() != null && !xmlRequest.getCorporateName().toString().trim().equalsIgnoreCase("") ? xmlRequest.getCorporateName().toString().trim() : "";
		_corporateNameDestination = Util.removeAccents(_corporateNameDestination);
		_corporateNameDestination = _corporateNameDestination.length() > 49 ? _corporateNameDestination.substring(0, 49) : _corporateNameDestination;
		_corporateNameDestination = _corporateNameDestination.equalsIgnoreCase("") ? "Sin_Razon_Social" : _corporateNameDestination;

		Contact contactDestination = new Contact();
		String _contactNameDestination = xmlRequest.getContactName();
		String _phoneNumberDestination = xmlRequest.getPhoneNumber();
		contactDestination.setContactName(_contactNameDestination);
		contactDestination.setCorporateName(_corporateNameDestination);
		contactDestination.setTelephone(_phoneNumberDestination);

		HomeAddress homeAddress = new HomeAddress(addressDestination, contactDestination);

		Destination destination = new Destination();
		destination.setDeliveryPUDOCode("567");
		destination.setHomeAddress(homeAddress);
		destination.setIsDeliveryToPUDO(false);

		org.estafeta.restconnector.models.DRAAlternative dRAAlternative = new org.estafeta.restconnector.models.DRAAlternative();
		Location location = new Location(dRAAlternative, origin, destination, false);

		LabelDefinition labelDefinition = new LabelDefinition(itemDescription, location, serviceConfiguration, wayBillDocument);
		WayBill wayBill = new WayBill(identification, labelDefinition, systemInformation);

		String jsonPayload = "";
		try {
			jsonPayload = processToJSON(wayBill);
		} catch (Exception ex) {
			log.error("\tError serializando a JSON: " + ex.getMessage(), ex);
			response.put("response_code", -4);
			response.put("response_description", "Error serializando payload: " + ex.getMessage());
			return new ResponseEntity<String>(response.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		g.setVia(xmlRequest.getVia() != null && !xmlRequest.getVia().trim().equalsIgnoreCase("") ? xmlRequest.getVia() : "");
		g.setRequest(jsonPayload);
		g.setEmpresa("Estafeta");

		g.setLargo(length.toString());
		g.setAlto(height.toString());
		g.setFondo(width.toString());
		g.setPesoVolumetrico(pesoVolumetrico.toString());
		g.setReexpedicion((xmlRequest.getReexpedicion() == null || xmlRequest.getReexpedicion().trim().isEmpty()) ? "0" : xmlRequest.getReexpedicion().trim());
		g.setPesoNeto(pesoNeto.toString());

		g.setServicio(serviceId);
		g.setCuenta(xmlRequest.getAccount());
		g.setAlias(xmlRequest.getAlias());
		guiasIntegradorRepository.save(g);

		// Cargar configuracion de credenciales
		String tokenUrl = null;
		String clientId = null;
		String clientSecret = null;
		String scope = null;
		String apiKey = null;
		String baseUrl = null;

		try {
			log.info("\tCargando configuración de la API Estafeta V2...");
			// 1. Intentar cargar desde EstafetaRestV2 (primera entrada del arreglo label/Label)
			Map<String, String> paramsRestV2 = atributoService.getByTipoInMap("EstafetaRestV2");
			if (paramsRestV2 == null || paramsRestV2.isEmpty()) {
				paramsRestV2 = atributoService.getByTipoInMap("estafetarestv2");
			}
			if (paramsRestV2 == null || paramsRestV2.isEmpty()) {
				paramsRestV2 = atributoService.getByTipoInMap("ESTAFETARESTV2");
			}

			JSONObject credentialsRestV2 = getFirstCredentialsInRestV2(paramsRestV2);
			if (credentialsRestV2 != null) {
				log.info("\tConfiguración de API cargada desde EstafetaRestV2");
				tokenUrl = credentialsRestV2.optString("tokenUrl", null);
				clientId = credentialsRestV2.optString("clientId", null);
				clientSecret = credentialsRestV2.optString("clientSecret", null);
				scope = credentialsRestV2.optString("scope", null);
				apiKey = credentialsRestV2.optString("apiKey", null);
				baseUrl = credentialsRestV2.optString("baseUrl", null);
				if (baseUrl == null) {
					baseUrl = credentialsRestV2.optString("url", null);
				}
			}

			// 2. Fallback a EstafetaV2
			if (tokenUrl == null || clientId == null || clientSecret == null || scope == null || apiKey == null || baseUrl == null) {
				Map<String, String> paramsV2 = atributoService.getByTipoInMap("EstafetaV2");
				if (paramsV2 == null || paramsV2.isEmpty()) {
					paramsV2 = atributoService.getByTipoInMap("estafetav2");
				}
				if (paramsV2 != null && !paramsV2.isEmpty()) {
					JSONObject credentialsV2 = getFirstCredentialsInRestV2(paramsV2);
					if (credentialsV2 != null) {
						log.info("\tConfiguración de API cargada de fallback EstafetaV2");
						if (tokenUrl == null) tokenUrl = credentialsV2.optString("tokenUrl", null);
						if (clientId == null) clientId = credentialsV2.optString("clientId", null);
						if (clientSecret == null) clientSecret = credentialsV2.optString("clientSecret", null);
						if (scope == null) scope = credentialsV2.optString("scope", null);
						if (apiKey == null) apiKey = credentialsV2.optString("apiKey", null);
						if (baseUrl == null) {
							baseUrl = credentialsV2.optString("baseUrl", null);
							if (baseUrl == null) {
								baseUrl = credentialsV2.optString("url", null);
							}
						}
					}
				}
			}

			if (tokenUrl == null || clientId == null || clientSecret == null || scope == null || apiKey == null || baseUrl == null) {
				throw new IllegalArgumentException("Faltan parametros requeridos para inicializar la API de Etiquetas V2.");
			}

		} catch (Exception ex) {
			log.error("\tError de configuracion de credenciales: " + ex.getMessage(), ex);
			response.put("response_code", -5);
			response.put("response_description", "Error de configuracion de credenciales Estafeta V2: " + ex.getMessage());
			g.setEstatus("Error de configuracion");
			guiasIntegradorRepository.save(g);
			return new ResponseEntity<String>(response.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Instanciar Clientes SDK Estafeta V2
		AuthenticatorClient authClient;
		LabelClient labelClient;
		try {
			authClient = new AuthenticatorClient(tokenUrl, clientId, clientSecret, scope);
			labelClient = new LabelClient(baseUrl, apiKey, authClient);
		} catch (Exception ex) {
			log.error("\tError inicializando el cliente SDK Estafeta V2: " + ex.getMessage(), ex);
			response.put("response_code", -6);
			response.put("response_description", "Error de inicialización SDK V2: " + ex.getMessage());
			g.setEstatus("Error de inicialización SDK");
			guiasIntegradorRepository.save(g);
			return new ResponseEntity<String>(response.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Determinar formato de etiqueta (Zebra térmico vs PDF carta)
		String outputType = "FILE_PDF";
		if (xmlRequest.getPaperType() != null && xmlRequest.getPaperType().trim().equalsIgnoreCase("2")) {
			outputType = "FILE_THERMAL_SEQUENCE";
		}

		log.info("\tEnviando petición a la API Estafeta V2...");
		try {
			String sdkResponse = labelClient.generateWaybill(
					outputType,
					"REQUEST",
					"SYNC_INLINE",
					"NORMAL_TIPO7_ZEBRAORI",
					jsonPayload
			);
			log.info("\tRespuesta recibida exitosamente.");

			JSONObject sdkResponseJSON = new JSONObject(sdkResponse);
			if (sdkResponseJSON.has("labelPetitionResult")) {
				JSONObject labelPetitionResult = sdkResponseJSON.getJSONObject("labelPetitionResult");
				JSONObject result = labelPetitionResult.getJSONObject("result");
				String code = String.valueOf(result.get("code"));

				if (code.equalsIgnoreCase("0")) {
					JSONArray elements = labelPetitionResult.optJSONArray("elements");
					String tracking = "";
					if (elements != null && elements.length() > 0) {
						JSONObject element = elements.getJSONObject(0);
						tracking = element.optString("wayBill", "");
					}

					response.put("response_code", 0);
					response.put("file", sdkResponseJSON.optString("data", ""));
					response.put("tracking", tracking);
					response.put("response_description", "");

					g.setTracking(tracking);
					g.setEstatus("Generada");
					guiasIntegradorRepository.save(g);

					// Guardar PDF en disco si corresponde
					if (context.get("pathlabel") != null) {
						String fileBase64 = sdkResponseJSON.optString("data", "");
						if (!fileBase64.isEmpty()) {
							try {
								byte[] bytes = java.util.Base64.getDecoder().decode(fileBase64.getBytes("UTF-8"));
								String fileName = "OUTBOUND_LABEL_" + tracking + ".pdf";
								String finalPath = context.get("pathlabel").toString();
								String fileNameFull = finalPath + fileName;
								try (FileOutputStream fout = new FileOutputStream(fileNameFull)) {
									fout.write(bytes);
								}
								log.info("\t\tGuardado en disco: " + fileNameFull);
							} catch (IOException ioex) {
								log.error("\t\tError al generar el archivo PDF en disco: " + ioex.getMessage());
							}
						}
					}
				} else {
					String description = result.optString("description", "");
					g.setResponse(sdkResponse);
					g.setEstatus("Error API: " + description);
					guiasIntegradorRepository.save(g);

					response.put("response_code", code);
					response.put("file", "");
					response.put("tracking", "");
					response.put("response_description", description);
				}
			} else {
				// Respuesta sin nodo labelPetitionResult pero con posibles códigos directos
				g.setResponse(sdkResponse);
				guiasIntegradorRepository.save(g);

				if (sdkResponseJSON.has("code")) {
					response.put("response_code", String.valueOf(sdkResponseJSON.get("code")));
					response.put("response_description", sdkResponseJSON.optString("description", "Error no especificado"));
				} else if (sdkResponseJSON.has("error")) {
					response.put("response_code", "-7");
					response.put("response_description", sdkResponseJSON.optString("error", "Error no especificado"));
				} else {
					response.put("response_code", "-7");
					response.put("response_description", "Respuesta inesperada de la API V2: " + sdkResponse);
				}
				response.put("file", "");
				response.put("tracking", "");
			}

		} catch (Exception ex) {
			log.error("\tExcepcion durante la llamada a la API Estafeta V2: " + ex.getMessage(), ex);
			g.setResponse(ex.getMessage());
			g.setEstatus("Excepcion: " + ex.getMessage());
			guiasIntegradorRepository.save(g);

			response.put("response_code", "-8");
			response.put("file", "");
			response.put("tracking", "");
			response.put("response_description", "Excepcion invocando API Estafeta V2: " + ex.getMessage());
		}

		return new ResponseEntity<String>(response.toString(), headers, HttpStatus.OK);
	}

	/**
	 * Resolves the assigned Estafeta account for the given shipment parameters.
	 * <p>
	 * This method queries the frequency cotizador to check for reexpedicion and resolves
	 * the account based on weight limits, routing, and user cobro configurations (rango/kilada).
	 *
	 * @param origen the postal code of origin.
	 * @param destino the postal code of destination.
	 * @param peso the weight of the shipment.
	 * @param tipoGuia the shipping guide type (Terrestre / Express).
	 * @param usuario the authenticated UsuarioAlan entity.
	 * @return the resolved account name.
	 * @throws Exception if an error occurs during frequency cotizacion lookup.
	 * @throws IOException if an I/O error occurs.
	 */
	public String getAccount(String origen, String destino, String peso, String tipoGuia, UsuarioAlan usuario) throws Exception, IOException {
		Map<String, String> values = atributoService.getByTipoInMap("Estafeta_Frecuencia_Cotizador");

		EstafetaFrecuenciaCliente serviceEstafeta = new EstafetaFrecuenciaCliente();
		serviceEstafeta.setUrl(values.get("url").toString());
		serviceEstafeta.setId(values.get("id").toString());
		serviceEstafeta.setUsuario(values.get("usuario").toString());
		serviceEstafeta.setContrasena(values.get("contrasena").toString());

		serviceEstafeta.setOrigen(origen);
		serviceEstafeta.setDestino(destino);
		serviceEstafeta.setEsFrecuencia("true");
		serviceEstafeta.setEsLista("true");
		serviceEstafeta.setEsPaquete("false");
		serviceEstafeta.setLargo("0");
		serviceEstafeta.setPeso("0");
		serviceEstafeta.setAlto("0");
		serviceEstafeta.setAncho("0");

		EstafetaFrecuencia frecuenciaObj = null;
		try {
			frecuenciaObj = serviceEstafeta.getFrecuencia();
		} catch (Exception e) {
			return null;
		}

		boolean reexpedicion = false;
		if (frecuenciaObj.getReexpedicion() != null && !frecuenciaObj.getReexpedicion().equalsIgnoreCase("NO") && !frecuenciaObj.getReexpedicion().equalsIgnoreCase("SI") && !frecuenciaObj.getReexpedicion().equalsIgnoreCase("")) {
			reexpedicion = true;
		}
		String cuenta = "";
		String cuentaDefault = "cuenta_default";
		String kiladaTerrestre5K = "kilada_terrestre_5kg";
		String kiladaTerrestreOtros = "kilada_terrestre_otros";
		String kiladaExpress1K = "kilada_express_1kg";
		String kiladaExpressOtros = "kilada_express_otros";
		String rangoExpress1K = "rango_express_1kg";
		int kilos = Integer.parseInt(peso);
		JSONObject cuentas = usuario.getCuentasEstafeta() == null ? new JSONObject() : new JSONObject(usuario.getCuentasEstafeta());
		if (!reexpedicion) {
			if (tipoGuia.equalsIgnoreCase("Terrestre")) {
				if (usuario.getTipoCobroTerrestre().equalsIgnoreCase("kilada")) {
					if (kilos <= 5) {
						cuenta = !String.valueOf(cuentas.get(kiladaTerrestre5K)).equalsIgnoreCase("") ? String.valueOf(cuentas.get(kiladaTerrestre5K)) : String.valueOf(cuentas.get(cuentaDefault));
					} else {
						if (cuentas.has(kiladaTerrestreOtros) && !String.valueOf(cuentas.get(kiladaTerrestreOtros)).equalsIgnoreCase("")) {
							cuenta = String.valueOf(cuentas.get(kiladaTerrestreOtros));
						} else {
							cuenta = String.valueOf(cuentas.get(cuentaDefault));
						}
					}
				} else if (usuario.getTipoCobroTerrestre().equalsIgnoreCase("rango")) {
					String cuentaXKG = "rango_terrestre_" + kilos + "kg";
					if (cuentas.has(cuentaXKG) && !cuentas.get(cuentaXKG).toString().equalsIgnoreCase("")) {
						cuenta = !String.valueOf(cuentas.get(cuentaXKG)).equalsIgnoreCase("") ? String.valueOf(cuentas.get(cuentaXKG)) : String.valueOf(cuentas.get(cuentaDefault));
					}
				}
			} else if (tipoGuia.equalsIgnoreCase("Express")) {
				if (usuario.getTipoCobroExpress().equalsIgnoreCase("kilada")) {
					if (kilos == 1) {
						cuenta = !String.valueOf(cuentas.get(kiladaExpress1K)).equalsIgnoreCase("") ? String.valueOf(cuentas.get(kiladaExpress1K)) : String.valueOf(cuentas.get(cuentaDefault));
					} else {
						if (cuentas.has(kiladaExpressOtros) && !String.valueOf(cuentas.get(kiladaExpressOtros)).equalsIgnoreCase("")) {
							cuenta = String.valueOf(cuentas.get(kiladaExpressOtros));
						} else {
							cuenta = String.valueOf(cuentas.get(cuentaDefault));
						}
					}
				} else if (usuario.getTipoCobroExpress().equalsIgnoreCase("rango")) {
					if (kilos == 1) {
						cuenta = !String.valueOf(cuentas.get(rangoExpress1K)).equalsIgnoreCase("") ? String.valueOf(cuentas.get(rangoExpress1K)) : String.valueOf(cuentas.get(cuentaDefault));
					} else {
						String cuentaXKG = "rango_express_" + kilos + "kg";
						if (cuentas.has(cuentaXKG) && !cuentas.get(cuentaXKG).toString().equalsIgnoreCase("")) {
							cuenta = !String.valueOf(cuentas.get(cuentaXKG)).equalsIgnoreCase("") ? String.valueOf(cuentas.get(cuentaXKG)) : String.valueOf(cuentas.get(cuentaDefault));
						}
					}
				}
			}
		} else {
			cuenta = String.valueOf(cuentas.get(cuentaDefault));
			Configuracion conf = null;
			if (tipoGuia.equalsIgnoreCase("Express")) {
				conf = configuracionRepository.findByTipoAndServicio("reexpedicion", "express");
			} else {
				conf = configuracionRepository.findByTipoAndServicio(getReexpedicionValue(kilos), "terrestre");
			}
			if (conf != null)
				cuenta = conf.getCuenta();
		}
		if (tipoGuia.equalsIgnoreCase("Terrestre")) {
			if (cuenta.equalsIgnoreCase("algoritmo_rentabilidad")) {
				Configuracion conf = null;
				if (kilos >= 0 && kilos <= 10) {
					conf = configuracionRepository.findByTipoAndServicio("algoritmo_5_10", "terrestre");
					if (conf == null) {
						if (cuenta == null || cuenta.equalsIgnoreCase("") || cuenta.equalsIgnoreCase("algoritmo_rentabilidad")) {
							cuenta = cuentaDefault;
						}
					} else {
						if (conf.getCuenta() != null && !conf.getCuenta().equalsIgnoreCase("")) {
							cuenta = conf.getCuenta();
						}
					}
				}
				if (kilos > 10 && kilos <= 15) {
					conf = configuracionRepository.findByTipoAndServicio("algoritmo_11_15", "terrestre");
					if (conf == null) {
						if (cuenta == null || cuenta.equalsIgnoreCase("") || cuenta.equalsIgnoreCase("algoritmo_rentabilidad")) {
							cuenta = cuentaDefault;
						}
					} else {
						if (conf.getCuenta() != null && !conf.getCuenta().equalsIgnoreCase("")) {
							cuenta = conf.getCuenta();
						}
					}
				}
				if (kilos > 15 && kilos <= 70) {
					conf = configuracionRepository.findByTipoAndServicio("algoritmo_16_70", "terrestre");
					if (conf == null) {
						if (cuenta == null || cuenta.equalsIgnoreCase("") || cuenta.equalsIgnoreCase("algoritmo_rentabilidad")) {
							cuenta = cuentaDefault;
						}
					} else {
						if (conf.getCuenta() != null && !conf.getCuenta().equalsIgnoreCase("")) {
							cuenta = conf.getCuenta();
						}
					}
				}
			}
		}
		if (reexpedicion) {
			if (tipoGuia.equalsIgnoreCase("Terrestre")) {
				if (cuentas.has("tiene_cuentadirecta_reexpedicion_terreste")) {
					if (cuentas.getInt("tiene_cuentadirecta_reexpedicion_terreste") == 1) {
						cuenta = cuentas.getString("cuentadirecta_reexpedicion_terreste");
					}
				}
			}
			if (tipoGuia.equalsIgnoreCase("Express")) {
				if (cuentas.has("tiene_cuentadirecta_reexpedicion_express")) {
					if (cuentas.getInt("tiene_cuentadirecta_reexpedicion_express") == 1) {
						cuenta = cuentas.getString("cuentadirecta_reexpedicion_express");
					}
				}
			}
		}
		return cuenta;
	}

	/**
	 * Helper method to get the reexpedicion configuration key based on weight.
	 *
	 * @param number the shipment weight.
	 * @return the configuration type string (e.g. reexpedicion_5, reexpedicion_10).
	 */
	public static String getReexpedicionValue(double number) {
		int roundedNumber = roundToNextMultipleOfFive(number);
		if (roundedNumber <= 5) {
			return "reexpedicion_5";
		} else {
			return "reexpedicion_" + roundedNumber;
		}
	}

	/**
	 * Helper method to round numbers to the next multiple of five.
	 *
	 * @param number the input number.
	 * @return the rounded integer value.
	 */
	public static int roundToNextMultipleOfFive(double number) {
		int roundedNumber = (int) Math.ceil(number / 5) * 5;
		return (roundedNumber == 0) ? 5 : roundedNumber;
	}

	/**
	 * Persists the initial label generation request data.
	 *
	 * @param params the JSON parameters of the request.
	 * @return the saved GuiaIntegrador entity.
	 */
	public GuiaIntegrador saveRequest(JSONObject params) {
		String numeroExteriorRemitente = params.optString("origenExtNum", "");
		String numeroInteriorRemitente = params.optString("origenIntNum", "");
		String referenceRemitente = params.optString("origenReference", "");
		GuiaIntegrador g = new GuiaIntegrador();
		g.setRemitenteCalle(params.optString("origenAddress1", ""));
		g.setRemitenteColonia(params.optString("origenNeighborhood", ""));
		g.setRemitenteCP(params.optString("origenZipCode", ""));
		g.setRemitenteNombre(params.optString("origenContactName", ""));
		g.setRemitenteEstado(params.optString("origenState", ""));
		g.setRemitenteMunicipio(params.optString("origenCity", ""));
		g.setRemitenteReferencia(referenceRemitente);
		g.setRemitenteTelefono(params.optString("origenPhoneNumber", ""));
		g.setRemitenteNumeroExterior(numeroExteriorRemitente);
		g.setRemitenteNumeroInterior(numeroInteriorRemitente);
		String numeroExteriorDestinatario = params.optString("extNum", "");
		String numeroInteriorDestinatario = params.optString("intNum", "");
		String referenceDestinatario = params.optString("reference", "");
		g.setDestinatarioCalle(params.optString("address1", ""));
		g.setDestinatarioColonia(params.optString("neighborhood", ""));
		g.setDestinatarioCP(params.optString("zipCode", ""));
		g.setDestinatarioNombre(params.optString("contactName", ""));
		g.setDestinatarioEstado(params.optString("state", ""));
		g.setDestinatarioMunicipio(params.optString("city", ""));
		g.setDestinatarioReferencia(referenceDestinatario);
		g.setDestinatarioTelefono(params.optString("phoneNumber", ""));
		g.setDestinatarioNumeroExterior(numeroExteriorDestinatario);
		g.setDestinatarioNumeroInterior(numeroInteriorDestinatario);
		g.setInformacionAdicional(params.optString("aditionalInfo", ""));
		g.setTipoGuia(params.optString("serviceTypeId", ""));
		g.setContenido(params.optString("content", ""));
		g.setEtiquetas(params.optString("numberOfLabels", "1"));
		g.setTipoContenido(params.optString("parcelTypeId", ""));
		g.setKilos(params.optString("weight", "0.0"));
		g.setCliente(params.optString("client", ""));
		g.setTracking("");
		g.setFechaCreacion(com.integrador.util.Util.getCurrentDataTimeMexico());
		g.setRequest("");
		return guiasIntegradorRepository.save(g);
	}

	private JSONObject getFirstCredentialsInRestV2(Map<String, String> params) {
		if (params == null || params.isEmpty()) {
			log.info("\t\tgetFirstCredentialsInRestV2: params is null or empty");
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
			log.info("\t\tgetFirstCredentialsInRestV2: jsonStr not found in params");
			return null;
		}
		try {
			JSONObject root = new JSONObject(jsonStr);
			if (root.has("label")) {
				JSONArray arr = root.optJSONArray("label");
				if (arr != null && arr.length() > 0) {
					return arr.getJSONObject(0);
				}
			}
			if (root.has("Label")) {
				JSONArray arr = root.optJSONArray("Label");
				if (arr != null && arr.length() > 0) {
					return arr.getJSONObject(0);
				}
			}
			return root;
		} catch (Exception e) {
			log.error("Error al procesar JSON en getFirstCredentialsInRestV2: " + e.getMessage());
		}
		return null;
	}
}
