package com.integrador.services;

import java.math.BigDecimal;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.integrador.carriers.Fedex;
import com.integrador.models.GuiaIntegrador;
import com.integrador.models.Token;
import com.integrador.models.Usuario;
import com.integrador.repositories.GuiaIntegradorRepository;
import com.integrador.repositories.TokenRepository;
import com.integrador.repositories.UsuarioRepository;
import com.integrador.util.AESAlgorithm;
import com.integrador.util.Util;
import com.integrador.xml.fedex.services.FedexLabelResponse;
@Service
public class FedexService {

	@Autowired AtributoService atributoService;
	@Autowired GuiaIntegradorRepository guasIntegradorRepository;
	@Autowired UsuarioRepository usuarioRepository;
	@Autowired TokenRepository tokenRepository;
	@Value("${fedex.webservice.br.beforesend}") String beforeSendBR;
	@Value("${fedex.webservice.br.aftersend}") String afterSendBR;
	@Value("${app.env}") String env;
	static final Logger log = LoggerFactory.getLogger(FedexService.class);
	public com.integrador.xml.fedex.services.FedexLabelResponse executeFedexLabel(com.integrador.xml.fedex.services.FedexLabelRequest xmlRequest) throws Exception{
		com.integrador.xml.fedex.services.FedexLabelResponse response = new com.integrador.xml.fedex.services.FedexLabelResponse();
		log.info("");
		log.info("== Fedex WebService ==>");
		xmlRequest = com.integrador.util.Util.cleanRequest(xmlRequest);
		if(xmlRequest.getClient()==null) {
			String errorMsg = "Debe especificar el cliente";
			response.setResponseCode("-1");
			response.setResponseDescription(errorMsg);
			log.info("\t"+errorMsg);
			return response;
		}

		if(xmlRequest.getPassword()==null) {
			String errorMsg = "Debe especificar la contraseña";
			response.setResponseCode("-1");
			response.setResponseDescription(errorMsg);
			log.info("\t"+errorMsg);
			return response;
		}

		if(xmlRequest.getToken()==null) {
			String errorMsg = "Debe especificar el token";
			response.setResponseCode("-1");
			response.setResponseDescription(errorMsg);
			log.info("\t"+errorMsg);
			return response;
		}
		GuiaIntegrador g = saveRequest(xmlRequest);
		AESAlgorithm e = new AESAlgorithm();
		Usuario usuario = usuarioRepository.findByUsuarioAndContrasena(xmlRequest.getClient(), e.encrypt(xmlRequest.getPassword()));
		if(usuario==null) {
			String errorMsg = "Usuario o Contraseña invalido";
			response.setResponseCode("-1");
			response.setResponseDescription(errorMsg);
			log.info("\t"+errorMsg);
			g.setEstatus(errorMsg);
			g = guasIntegradorRepository.save(g);
			return response;
		}
		Token token = tokenRepository.findByIdUsuarioAndToken(usuario.getIdUsuario(), xmlRequest.getToken());
		if(token==null) {
			String errorMsg = "Token invalido";
			response.setResponseCode("-2");
			response.setResponseDescription(errorMsg);
			log.info("\t"+errorMsg);
			g.setEstatus(errorMsg);
			guasIntegradorRepository.save(g);
			return response;
		}
		Map<String,String> params = atributoService.getByTipoInMap("Fedex1_Ship");
		Fedex fedex = new Fedex();
		fedex.setParams(params);
		fedex.setXmlRequest(xmlRequest);
		fedex.setXmlResponse(response);
		fedex.setGuiasIntegradorRepository(guasIntegradorRepository);
		fedex.setGuiaIntegrador(g);
		fedex.setBusinessRuleBeforeSend(beforeSendBR);
		fedex.setBusinessRuleAfterSend(afterSendBR);
		fedex.setAtributoService(atributoService);
		fedex.setEnv(env);
		response = (FedexLabelResponse) fedex.executeWebService();
		return response;
	}
	public GuiaIntegrador saveRequest(com.integrador.xml.fedex.services.FedexLabelRequest xmlRequest) {

		String numeroExteriorRemitente = xmlRequest.getOrigenExtNum()!=null ? xmlRequest.getOrigenExtNum() : "";
		String numeroInteriorRemitente = xmlRequest.getOrigenIntNum()!=null ? xmlRequest.getOrigenIntNum() : "";
		String referenceRemitente = xmlRequest.getOrigenReference()!=null ? xmlRequest.getOrigenReference() : "";
		GuiaIntegrador g = new GuiaIntegrador();
		g.setRemitenteCalle(xmlRequest.getOrigenAddress1());
		g.setRemitenteColonia(xmlRequest.getOrigenNeighborhood());
		g.setRemitenteCP(xmlRequest.getOrigenZipCode());
		g.setRemitenteNombre(xmlRequest.getOrigenContactName());
		g.setRemitenteEstado(xmlRequest.getOrigenState());
		g.setRemitenteMunicipio(xmlRequest.getOrigenCity());
		g.setRemitenteReferencia(referenceRemitente);
		g.setRemitenteTelefono(xmlRequest.getOrigenPhoneNumber());
		g.setRemitenteNumeroExterior(numeroExteriorRemitente);
		g.setRemitenteNumeroInterior(numeroInteriorRemitente);
		String numeroExteriorDestinatario = xmlRequest.getExtNum()!=null ? xmlRequest.getExtNum() : "";
		String numeroInteriorDestinatario = xmlRequest.getIntNum()!=null ? xmlRequest.getIntNum() : "";
		String referenceDestinatario = xmlRequest.getReference()!=null ? xmlRequest.getReference() : "";
		g.setDestinatarioCalle(xmlRequest.getAddress1());
		g.setDestinatarioColonia(xmlRequest.getNeighborhood());
		g.setDestinatarioCP(xmlRequest.getZipCode());
		g.setDestinatarioNombre(xmlRequest.getContactName());
		g.setDestinatarioEstado(xmlRequest.getState());
		g.setDestinatarioMunicipio(xmlRequest.getCity());
		g.setDestinatarioReferencia(referenceDestinatario);
		g.setDestinatarioTelefono(xmlRequest.getPhoneNumber());
		g.setDestinatarioNumeroExterior(numeroExteriorDestinatario);
		g.setDestinatarioNumeroInterior(numeroInteriorDestinatario);
		g.setInformacionAdicional(xmlRequest.getAditionalInfo());
		g.setTipoGuia("Fedex");
		g.setContenido(xmlRequest.getContent());
		g.setEtiquetas("-"); //No necesario para fedex
		g.setTipoContenido("-"); //No necesario para fedex
		g.setKilos(xmlRequest.getWeight());
		g.setCliente(xmlRequest.getClient());
		g.setTracking("");
		g.setFechaCreacion(Util.getCurrentDataTime());
		g.setRequest("");
		return guasIntegradorRepository.save(g);
	}
	public GuiaIntegrador saveRequest(JSONObject request) {
		String numeroExteriorRemitente = request.has("OrigenExtNum") ? String.valueOf(request.get("OrigenExtNum")) : "";
		String numeroInteriorRemitente = request.has("OrigenIntNum") ? String.valueOf(request.get("OrigenIntNum")) : "";
		String referenceRemitente = request.has("OrigenReference") ? String.valueOf(request.get("OrigenReference")) : "";
		String origenAddress1 = request.has("OrigenAddress1") ? String.valueOf(request.get("OrigenAddress1")) : "";
		String origenColonia = request.has("OrigenNeighborhood") ? String.valueOf(request.get("OrigenNeighborhood")) : "";
		String origenCP = request.has("OrigenZipCode") ? String.valueOf(request.get("OrigenZipCode")) : "";
		String origenNombre = request.has("OrigenContactName") ? String.valueOf(request.get("OrigenContactName")) : "";
		String origenEstado = request.has("OrigenState") ? String.valueOf(request.get("OrigenState")) : "";
		String origenMunicipio = request.has("OrigenCity") ? String.valueOf(request.get("OrigenCity")) : "";
		String origenTelefono = request.has("OrigenPhoneNumber") ? String.valueOf(request.get("OrigenPhoneNumber")) : "";
		GuiaIntegrador g = new GuiaIntegrador();
		g.setRemitenteCalle(origenAddress1);
		g.setRemitenteColonia(origenColonia);
		g.setRemitenteCP(origenCP);
		g.setRemitenteNombre(origenNombre);
		g.setRemitenteEstado(origenEstado);
		g.setRemitenteMunicipio(origenMunicipio);
		g.setRemitenteReferencia(referenceRemitente);
		g.setRemitenteTelefono(origenTelefono);
		g.setRemitenteNumeroExterior(numeroExteriorRemitente);
		g.setRemitenteNumeroInterior(numeroInteriorRemitente);
		String numeroExteriorDestinatario = request.has("ExtNum") ? String.valueOf(request.get("ExtNum")) : "";
		String numeroInteriorDestinatario = request.has("IntNum") ? String.valueOf(request.get("IntNum")) : "";
		String referenceDestinatario = request.has("Reference") ? String.valueOf(request.get("Reference")) : "";
		String address1 = request.has("Address1") ? String.valueOf(request.get("Address1")) : "";
		String colonia = request.has("Neighborhood") ? String.valueOf(request.get("Neighborhood")) : "";
		String cp = request.has("ZipCode") ? String.valueOf(request.get("ZipCode")) : "";
		String nombre = request.has("ContactName") ? String.valueOf(request.get("ContactName")) : "";
		String estado = request.has("State") ? String.valueOf(request.get("State")) : "";
		String municipio = request.has("City") ? String.valueOf(request.get("City")) : "";
		String telefono = request.has("PhoneNumber") ? String.valueOf(request.get("PhoneNumber")) : "";
		g.setDestinatarioCalle(address1);
		g.setDestinatarioColonia(colonia);
		g.setDestinatarioCP(cp);
		g.setDestinatarioNombre(nombre);
		g.setDestinatarioEstado(estado);
		g.setDestinatarioMunicipio(municipio);
		g.setDestinatarioReferencia(referenceDestinatario);
		g.setDestinatarioTelefono(telefono);
		g.setDestinatarioNumeroExterior(numeroExteriorDestinatario);
		g.setDestinatarioNumeroInterior(numeroInteriorDestinatario);
		String aditionalInfo = request.has("AditionalInfo") ? String.valueOf(request.get("AditionalInfo")) : "";
		String content = request.has("Content") ? String.valueOf(request.get("Content")) : "";
		String weight = request.has("Weight") ? String.valueOf(request.get("Weight")) : "";
		String client = request.has("Client") ? String.valueOf(request.get("Client")) : "";
		String via = request.has("Via") ? String.valueOf(request.get("Via")) : "";
		g.setInformacionAdicional(aditionalInfo);
		g.setTipoGuia("Fedex");
		g.setContenido(content);
		g.setEtiquetas("-");
		g.setTipoContenido("-");
		g.setKilos(weight);
		g.setCliente(client);
		g.setTracking("");
		g.setFechaCreacion(Util.getCurrentDataTime());
		g.setRequest(request.toString());
		g.setVia(via);
		return guasIntegradorRepository.save(g);
	}
	public com.integrador.xml.fedex.services.FedexServiceTypeResponse executeRateServiceType(com.integrador.xml.fedex.services.FedexServiceTypeRequest xmlRequest) throws Exception{
		log.info("");
		log.info("== FedexServiceType / WebService ==>");
		com.integrador.xml.fedex.services.FedexServiceTypeResponse response = new com.integrador.xml.fedex.services.FedexServiceTypeResponse();
		Map<String,String> params = atributoService.getByTipoInMap(xmlRequest.getAccount());
		com.m160185.models.ServiceData data = new com.m160185.models.ServiceData();
		com.m160185.models.Credentials credential = new com.m160185.models.Credentials();
		credential.setAccountNumber(params.get("account_number").toString());
		credential.setMeterNumber(params.get("meter_number").toString());
		credential.setKey(params.get("key").toString());
		credential.setPassword(params.get("password").toString());
		credential.setUrl(params.get("url").toString());
		data.setCredential(credential);
		com.m160185.models.StateCode stateCode = new com.m160185.models.StateCode();
		stateCode.setState(xmlRequest.getDestinationState());
		stateCode.setCode(stateCode.findStateCode(xmlRequest.getDestinationState().toLowerCase().trim()));
		data.setStateCodeDest(stateCode);
		com.m160185.models.Address addressOrigen = new com.m160185.models.Address();
		addressOrigen.setAddress(xmlRequest.getOrigenAddress());
		addressOrigen.setCity(xmlRequest.getOrigenMunicipio());
		addressOrigen.setStateCode(stateCode.findStateCode(xmlRequest.getOrigenState().toLowerCase().trim()));
		addressOrigen.setCountryCode("MX");
		addressOrigen.setTel(xmlRequest.getOrigenPhone());
		addressOrigen.setCp(xmlRequest.getOrigenCP());
		data.setOrigen(addressOrigen);
		com.m160185.models.Address addressDest = new com.m160185.models.Address();
		addressDest.setAddress(xmlRequest.getDestinationAddress());
		addressDest.setCity(xmlRequest.getDestinationMunicipio());
		addressDest.setStateCode(stateCode.findStateCode(xmlRequest.getDestinationState().toLowerCase().trim()));
		addressDest.setCountryCode("MX");
		addressDest.setTel(xmlRequest.getDestinationPhone());
		addressDest.setMunicipio(xmlRequest.getDestinationMunicipio());
		addressDest.setState(xmlRequest.getDestinationState());
		addressDest.setCp(xmlRequest.getDestinationCP());
		addressDest.setName(xmlRequest.getDestinationName());
		data.setDestination(addressOrigen);
		com.m160185.conn.Service2Client fedexRateService = new com.m160185.conn.Service2Client(data, xmlRequest.getOrigenCP(), xmlRequest.getDestinationCP(), xmlRequest.getWeight());
		JSONObject serviceResponse = fedexRateService.cotizar();
		System.out.println(serviceResponse);
		String serviceTypes = "";
		String reexpedicion = "0.00";
		JSONArray rateDetails  = new JSONArray();
		if(serviceResponse.has("rateReplyDetails") ) {
			if(serviceResponse.get("rateReplyDetails")!=null && !serviceResponse.get("rateReplyDetails").toString().equalsIgnoreCase("null")) {
				rateDetails = serviceResponse.getJSONArray("rateReplyDetails");
			}
		}
		if(rateDetails.length()>0) {
			for(int d=0;d<rateDetails.length();d++){
				JSONObject rateDetail = rateDetails.getJSONObject(d);
				if(rateDetail.has("serviceType")){
					String st = String.valueOf(rateDetail.get("serviceType"));
					if(serviceTypes.equalsIgnoreCase("")) {
						serviceTypes = st;
					}else {
						serviceTypes += ","+st;
					}
				}
				if(rateDetail.has("ratedShipmentDetails")){
					JSONArray ratedShipmentDetails = rateDetail.getJSONArray("ratedShipmentDetails");
					outerDetail :
					for(int i = 0; i<ratedShipmentDetails.length(); i++) {
						JSONObject ratedShipmentDetail = ratedShipmentDetails.getJSONObject(i);
						if(ratedShipmentDetail.has("shipmentRateDetail")) {
							JSONObject shipmentRateDetail = ratedShipmentDetail.getJSONObject("shipmentRateDetail");
							if(shipmentRateDetail.has("surcharges")) {
								JSONArray surcharges = shipmentRateDetail.getJSONArray("surcharges");
								for(int m = 0; m<surcharges.length(); m++) {
									JSONObject surcharge = surcharges.getJSONObject(m);
									if(surcharge.has("surchargeType")) {
										JSONObject surchargeType = surcharge.getJSONObject("surchargeType");
										if(surchargeType.has("value") && surchargeType.getString("value").equalsIgnoreCase("OUT_OF_DELIVERY_AREA")) {
											/*
											 * Una vez detectado que si tiene OUT_OF_DELIVERY_AREA, buscamos su precio en moneda MXN 
											 */
											if(surcharge.has("amount")) {
												JSONObject amount = surcharge.getJSONObject("amount");
												if(amount.has("currency") && amount.getString("currency").equalsIgnoreCase("MXN")) {
													BigDecimal value = new BigDecimal( String.valueOf(amount.get("amount")));
													reexpedicion = value.setScale(2).toString();
													break outerDetail;
												}else {
													continue outerDetail;
												}
											}
										}else {
											continue outerDetail;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		response.setServicesTypes(serviceTypes);
		response.setOutOfDeliveryArea(reexpedicion);
		return response;
	}
	public JSONObject validateAccess(JSONObject request) {
		JSONObject response = new JSONObject();
		String error = Util.EMPTY_STRING;
		if(!request.has("Client")) {
			error = "Client node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return response;
		}
		if(!request.has("Password")) {
			error = "Password node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return response;
		}
		if(!request.has("Token")) {
			error = "Token node is required";
			log.info("\t"+error);
			response.put("validation", error);
			return response;
		}
		AESAlgorithm e = new AESAlgorithm();
		Usuario usuario = usuarioRepository.findByUsuarioAndContrasena(String.valueOf(request.get("Client")), e.encrypt(String.valueOf(request.get("Password"))));
		if(usuario==null) {
			error = "Invalid User or Password";
			log.info("\t"+error);
			response.put("validation", error);
			return response;
		}
		com.integrador.models.Token token = tokenRepository.findByIdUsuarioAndToken(usuario.getIdUsuario(), String.valueOf(request.get("Token")));
		if(token==null) {
			error = "Invalid Token";
			log.info("\t"+error);
			response.put("validation", error);
			return response;
		}
		return response;
	}
}
