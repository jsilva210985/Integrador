package com.integrador.restcontroller.estafeta;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
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

@RestController
@RequestMapping({"/Estafeta"})
public class LabelController{

	@Autowired TokenRepository tokenRepository;
	@Autowired AtributoService atributoService;
	@Autowired UsuariosService usuariosService;
	@Autowired UsuarioAlanRepository usuarioAlanRepository;
	@Autowired GuiaIntegradorRepository guiasIntegradorRepository;
	@Autowired ConfiguracionRepository configuracionRepository;

	@Value("${estafeta.restservice.br.beforesend}") String beforeSendBR;

	static final Logger log = LoggerFactory.getLogger(LabelController.class);

	@PostMapping(value = "/LabelV2")
	public ResponseEntity<String> labelv2(HttpServletRequest request, @RequestBody String content) throws Exception{

		JSONObject response = new JSONObject();
		JSONObject serviceParams = new JSONObject();
		String error = "";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		log.info("");
		log.info("== Estafeta RestService / Label ==>");
		try {
			serviceParams = new JSONObject(content);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		if(!serviceParams.has("client") || serviceParams.getString("client").trim().equalsIgnoreCase("")){
			error = "Debe especificar el cliente";
			response.put("response_code", -1);
			response.put("response_description",error);
			log.info("\t"+error);
			return new ResponseEntity<String>(response.toString(),headers, HttpStatus.BAD_REQUEST);
		}
		if(!serviceParams.has("password") || serviceParams.getString("password").trim().equalsIgnoreCase("")){
			error = "Debe especificar la contraseña";
			response.put("response_code", -1);
			response.put("response_description",error);
			log.info("\t"+error);
			return new ResponseEntity<String>(response.toString(),headers, HttpStatus.BAD_REQUEST);
		}

		if(!serviceParams.has("token") || serviceParams.getString("token").trim().equalsIgnoreCase("")){
			error = "Debe especificar el token";
			response.put("response_code", -1);
			response.put("response_description",error);
			log.info("\t"+error);
			return new ResponseEntity<String>(response.toString(),headers, HttpStatus.BAD_REQUEST);
		}
		if(serviceParams.has("insurance") && !serviceParams.getString("insurance").trim().equalsIgnoreCase("")){
			String insurance = serviceParams.getString("insurance").trim().toLowerCase();
			if(!com.integrador.util.Util.isBoolean(insurance)){
				error = "Valor incorrecto para Isurance, valor esperado [true,false]";
				response.put("response_code", -1);
				response.put("response_description",error);
				log.info("\t"+error);
				return new ResponseEntity<String>(response.toString(),headers, HttpStatus.BAD_REQUEST);
			}
			//Si es true, se valida que contenga el nodo insuranceValue
			if(insurance.equalsIgnoreCase("true")){
				String insuranceValue = serviceParams.has("insuranceValue") ? String.valueOf(serviceParams.get("insuranceValue")).trim() : "";
				if(insuranceValue.equalsIgnoreCase("")){
					error = "Debe especificar InsuranceValue";
					response.put("response_code", -1);
					response.put("response_description",error);
					log.info("\t"+error);
					return new ResponseEntity<String>(response.toString(),headers, HttpStatus.BAD_REQUEST);
				}
				if(!com.integrador.util.Util.isStringNumeric(insuranceValue) ){
					error = "Valor incorrecto para isuranceValue, Patron: '\\d{1,6}.\\d{1,6}'";
					response.put("response_code", -1);
					response.put("response_description",error);
					log.info("\t"+error);
					return new ResponseEntity<String>(response.toString(),headers, HttpStatus.BAD_REQUEST);
				}
			}
		}
		GuiaIntegrador g = saveRequest(serviceParams);
		log.info("\tRequest guardado");
		AESAlgorithm e = new AESAlgorithm();
		UsuarioAlan usuario = usuarioAlanRepository.findByUsuarioAndContrasena(serviceParams.getString("client"), e.encrypt(serviceParams.getString("password")));
		if(usuario==null) {
			error = "Usuario o Contraseña invalido";
			response.put("response_code", -1);
			response.put("response_description",error);
			log.info("\t"+error);
			g.setEstatus(error);
			g = guiasIntegradorRepository.save(g);
			return new ResponseEntity<String>(response.toString(),headers, HttpStatus.BAD_REQUEST);
		}
		com.integrador.models.Token token = tokenRepository.findByIdUsuarioAndToken(usuario.getIdUsuario(), serviceParams.getString("token"));
		if(token==null) {
			error = "Token invalido";
			response.put("response_code", -2);
			response.put("response_description",error);
			log.info("\t"+error);
			g.setEstatus(error);
			guiasIntegradorRepository.save(g);
			return new ResponseEntity<String>(response.toString(),headers, HttpStatus.BAD_REQUEST);
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

		xmlRequest.setIsServiceUsesKilos(serviceParams.optString("isServiceUsesKilos", ""));
		xmlRequest.setService(serviceParams.optString("service", ""));
		xmlRequest.setProvider("Estafeta");
		xmlRequest.setVia("Integrador");

		String cuenta = getAccount(
				g.getRemitenteCP(), 
				g.getDestinatarioCP(), 
				xmlRequest.getWeight().toString(), 
				xmlRequest.getServiceTypeId(), 
				usuario
				);
		xmlRequest.setAccount(cuenta);
		xmlRequest.setInsurance(serviceParams.optString("insurance", "false"));
		xmlRequest.setInsuranceValue(serviceParams.optString("insuranceValue", "0.0"));
		log.info("\tInvocation Type: RestService");
		Map<String,String> values = atributoService.getByTipoInMap(cuenta);
		if(values.isEmpty())
			values = atributoService.getByTipoInMap("Estafeta_Label_Rest");

		com.integrador.carriers.Estafeta estafeta = new com.integrador.carriers.Estafeta();
		estafeta.setAccount(values);
		estafeta.setXmlRequest(xmlRequest);
		estafeta.setXmlResponse(xmlResponse);
		estafeta.setGuiaIntegradorRepository(guiasIntegradorRepository);
		estafeta.setGuiaIntegrador(g);
		estafeta.setAtributoService(atributoService);
		estafeta.setUsuariosService(usuariosService);
		estafeta.setBusinessRuleBeforeSend(beforeSendBR);
		try{
			xmlResponse = (com.integrador.xml.services.EstafetaLabelResponse) estafeta.executeRestService();
		}catch(BusinessRuleException bre){
			String msg = bre.getMessage();
			int code = bre.getCode();
			response.put("response_code",code);
			response.put("response_description",msg);
			log.error("\t"+bre.getMessage());
			return new ResponseEntity<>(response.toString(), headers, HttpStatus.OK);
		}

		if(xmlResponse.getFile()!=null)
			response.put("file", xmlResponse.getFile());

		if(xmlResponse.getResponseCode()!=null)
			response.put("response_code", xmlResponse.getResponseCode());

		if(xmlResponse.getResponseDescription()!=null)
			response.put("response_description", xmlResponse.getResponseDescription());

		if(xmlResponse.getTracking()!=null)
			response.put("tracking", xmlResponse.getTracking());

		return new ResponseEntity<String>(response.toString(),headers, HttpStatus.OK);
	}

	public String getAccount(String origen, String destino, String peso, String tipoGuia, UsuarioAlan usuario) throws Exception, IOException{
		Map<String,String> values = atributoService.getByTipoInMap("Estafeta_Frecuencia_Cotizador");

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
		}catch (Exception e) {
			return null;
		}

		boolean reexpedicion = false;
		if(frecuenciaObj.getReexpedicion()!=null && !frecuenciaObj.getReexpedicion().equalsIgnoreCase("NO") && !frecuenciaObj.getReexpedicion().equalsIgnoreCase("SI") && !frecuenciaObj.getReexpedicion().equalsIgnoreCase("")) {
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
		JSONObject cuentas = usuario.getCuentasEstafeta()==null ? new JSONObject() : new JSONObject(usuario.getCuentasEstafeta());
		if(!reexpedicion){
			if(tipoGuia.equalsIgnoreCase("Terrestre")){
				if(usuario.getTipoCobroTerrestre().equalsIgnoreCase("kilada")){
					if(kilos<=5){
						cuenta = !String.valueOf(cuentas.get(kiladaTerrestre5K)).equalsIgnoreCase("") ? String.valueOf(cuentas.get(kiladaTerrestre5K)) : String.valueOf(cuentas.get(cuentaDefault));
					}else{
						if(cuentas.has(kiladaTerrestreOtros) && !String.valueOf(cuentas.get(kiladaTerrestreOtros)).equalsIgnoreCase("")){
							cuenta = String.valueOf(cuentas.get(kiladaTerrestreOtros));
						}else{
							cuenta = String.valueOf(cuentas.get(cuentaDefault));
						}
					}
				}else if(usuario.getTipoCobroTerrestre().equalsIgnoreCase("rango")){
					String cuentaXKG = "rango_terrestre_"+kilos+"kg";
					if(cuentas.has(cuentaXKG) && !cuentas.get(cuentaXKG).toString().equalsIgnoreCase("")){
						cuenta = !String.valueOf(cuentas.get(cuentaXKG)).equalsIgnoreCase("") ? String.valueOf(cuentas.get(cuentaXKG)) : String.valueOf(cuentas.get(cuentaDefault));
					}
				}
			}else if(tipoGuia.equalsIgnoreCase("Express")){
				if(usuario.getTipoCobroExpress().equalsIgnoreCase("kilada")){
					if(kilos==1){
						cuenta = !String.valueOf(cuentas.get(kiladaExpress1K)).equalsIgnoreCase("") ? String.valueOf(cuentas.get(kiladaExpress1K)) : String.valueOf(cuentas.get(cuentaDefault));
					}else{
						if(cuentas.has(kiladaExpressOtros) && !String.valueOf(cuentas.get(kiladaExpressOtros)).equalsIgnoreCase("")){
							cuenta = String.valueOf(cuentas.get(kiladaExpressOtros));
						}else{
							cuenta = String.valueOf(cuentas.get(cuentaDefault));
						}
					}
				}else if(usuario.getTipoCobroExpress().equalsIgnoreCase("rango")) {
					if(kilos==1){
						cuenta = !String.valueOf(cuentas.get(rangoExpress1K)).equalsIgnoreCase("") ? String.valueOf(cuentas.get(rangoExpress1K)) : String.valueOf(cuentas.get(cuentaDefault));
					}else{
						String cuentaXKG = "rango_express_"+kilos+"kg";
						if(cuentas.has(cuentaXKG) && !cuentas.get(cuentaXKG).toString().equalsIgnoreCase("")){
							cuenta = !String.valueOf(cuentas.get(cuentaXKG)).equalsIgnoreCase("") ? String.valueOf(cuentas.get(cuentaXKG)) : String.valueOf(cuentas.get(cuentaDefault));
						}
					}
				}
			}
		}else {
			cuenta = String.valueOf(cuentas.get(cuentaDefault));
			Configuracion conf = null;
			if(tipoGuia.equalsIgnoreCase("Express")){
				conf = configuracionRepository.findByTipoAndServicio("reexpedicion", "express");
			}else{
				conf = configuracionRepository.findByTipoAndServicio(getReexpedicionValue(kilos), "terrestre");
			}
			if(conf!=null)
				cuenta = conf.getCuenta();
		}
		if(tipoGuia.equalsIgnoreCase("Terrestre")){
			if(cuenta.equalsIgnoreCase("algoritmo_rentabilidad")){
				Configuracion conf = null;
				if(kilos>=0 && kilos<=10) {
					conf = configuracionRepository.findByTipoAndServicio("algoritmo_5_10", "terrestre");
					if(conf==null){
						if(cuenta==null || cuenta.equalsIgnoreCase("") || cuenta.equalsIgnoreCase("algoritmo_rentabilidad")){
							cuenta = cuentaDefault;
						}
					}else{
						if(conf.getCuenta()!=null && !conf.getCuenta().equalsIgnoreCase("")){
							cuenta = conf.getCuenta();
						}
					}
				}
				if(kilos>10 && kilos<=15) {
					conf = configuracionRepository.findByTipoAndServicio("algoritmo_11_15", "terrestre");
					if(conf==null){
						if(cuenta==null || cuenta.equalsIgnoreCase("") || cuenta.equalsIgnoreCase("algoritmo_rentabilidad")){
							cuenta = cuentaDefault;
						}
					}else{
						if(conf.getCuenta()!=null && !conf.getCuenta().equalsIgnoreCase("")){
							cuenta = conf.getCuenta();
						}
					}
				}
				if(kilos>15 && kilos<=70) {
					conf = configuracionRepository.findByTipoAndServicio("algoritmo_16_70", "terrestre");
					if(conf==null){
						if(cuenta==null || cuenta.equalsIgnoreCase("") || cuenta.equalsIgnoreCase("algoritmo_rentabilidad")){
							cuenta = cuentaDefault;
						}
					}else{
						if(conf.getCuenta()!=null && !conf.getCuenta().equalsIgnoreCase("")){
							cuenta = conf.getCuenta();
						}
					}
				}
			}
		}
		if(reexpedicion){
			if(tipoGuia.equalsIgnoreCase("Terrestre")){
				if(cuentas.has("tiene_cuentadirecta_reexpedicion_terreste")){
					if(cuentas.getInt("tiene_cuentadirecta_reexpedicion_terreste")==1){
						cuenta = cuentas.getString("cuentadirecta_reexpedicion_terreste");
					}
				}
			}
			if(tipoGuia.equalsIgnoreCase("Express")){
				if(cuentas.has("tiene_cuentadirecta_reexpedicion_express")){
					if(cuentas.getInt("tiene_cuentadirecta_reexpedicion_express")==1){
						cuenta = cuentas.getString("cuentadirecta_reexpedicion_express");
					}
				}
			}
		}
		return cuenta;
	}

	public static String getReexpedicionValue(double number) {
		int roundedNumber = roundToNextMultipleOfFive(number);
		if(roundedNumber <= 5) {
			return "reexpedicion_5";
		}else{
			return "reexpedicion_" + roundedNumber;
		}
	}

	public static int roundToNextMultipleOfFive(double number) {
		int roundedNumber = (int) Math.ceil(number / 5) * 5;
		return (roundedNumber == 0) ? 5 : roundedNumber;
	}

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
		g.setKilos(params.optString("weight","0.0"));
		g.setCliente(params.optString("client", ""));
		g.setTracking("");
		g.setFechaCreacion(com.integrador.util.Util.getCurrentDataTimeMexico());
		g.setRequest("");
		return guiasIntegradorRepository.save(g);
	}
}
