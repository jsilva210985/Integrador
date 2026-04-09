package com.integrador.services;

import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.estafeta.webservice.frecuencia.EstafetaFrecuencia;
import com.estafeta.webservice.frecuencia.EstafetaFrecuenciaCliente;
import com.estafeta.websrvice.consultaenvios.EstafetaConsultaEnvios;
import com.integrador.carriers.BotDHL;
import com.integrador.carriers.BotEstafeta;
import com.integrador.carriers.Estafeta;
import com.integrador.models.GuiaIntegrador;
import com.integrador.models.Token;
import com.integrador.models.Usuario;
import com.integrador.operations.WebServiceEstafeta;
import com.integrador.repositories.GuiaIntegradorRepository;
import com.integrador.repositories.TokenRepository;
import com.integrador.repositories.UsuarioRepository;
import com.integrador.util.AESAlgorithm;
import com.integrador.xml.services.ArrayOfHistory;
import com.integrador.xml.services.ArrayOfTrackingData;
import com.integrador.xml.services.Colonia;
import com.integrador.xml.services.DeliveryData;
import com.integrador.xml.services.Destino;
import com.integrador.xml.services.DiasEntrega;
import com.integrador.xml.services.Dimensions;
import com.integrador.xml.services.EstafetaFrecuenciaCotizadorRequest;
import com.integrador.xml.services.EstafetaFrecuenciaCotizadorResponse;
import com.integrador.xml.services.EstafetaLabelResponse;
import com.integrador.xml.services.ExecuteQueryResponse;
import com.integrador.xml.services.Filter;
import com.integrador.xml.services.History;
import com.integrador.xml.services.HistoryConfiguration;
import com.integrador.xml.services.ModalidadEntrega;
import com.integrador.xml.services.Origen;
import com.integrador.xml.services.PickupData;
import com.integrador.xml.services.SearchConfiguration;
import com.integrador.xml.services.SearchType;
import com.integrador.xml.services.TipoEnvio;
import com.integrador.xml.services.TipoSer;
import com.integrador.xml.services.TipoServicio;
import com.integrador.xml.services.TrackingData;
import com.integrador.xml.services.WaybillList;

@Service
public class EstafetaService {

	@Autowired AtributoService atributoService;
	@Autowired GuiaIntegradorRepository guiasIntegradorRepository;
	@Autowired UsuarioRepository usuarioRepository;
	@Autowired TokenRepository tokenRepository;
	@Autowired UsuariosService usuariosService;

	static final Logger log = LoggerFactory.getLogger(EstafetaService.class);
	public static final String WEBSERVICE = "WEBSERVICE";
	public static final String RESTSERVICE = "RESTSERVICE";
	@Value("${estafeta.restservice.br.beforesend}") String beforeSendBR;

	public EstafetaFrecuenciaCotizadorResponse executeFrecuenciaCotizadorWS(EstafetaFrecuenciaCotizadorRequest xmlRequest) throws Exception{
		log.info("");
		log.info("== EstafetaFrecuenciaCotizador / WebService ==>");
		EstafetaFrecuenciaCotizadorResponse response = new EstafetaFrecuenciaCotizadorResponse();
		String error = "";
		AESAlgorithm e = new AESAlgorithm();
		Usuario usuario = usuarioRepository.findByUsuarioAndContrasena(xmlRequest.getCliente(), e.encrypt(xmlRequest.getContrasena()));
		if(usuario==null) {
			error = "Usuario o Password invalido";
			log.info("\t"+error);
			response.setMensajeError(error);
			response.setError("-1");
			return response;
		}
		Token token = tokenRepository.findByIdUsuarioAndToken(usuario.getIdUsuario(), xmlRequest.getToken());
		if(token==null) {
			error = "Token invalido";
			log.info("\t"+error);
			response.setMensajeError(error);
			response.setError("-2");
			return response;
		}
		Map<String,String> values = atributoService.getByTipoInMap("Estafeta_Frecuencia_Cotizador");
		EstafetaFrecuenciaCliente serviceEstafeta = new EstafetaFrecuenciaCliente();
		serviceEstafeta.setUrl(values.get("url").toString());
		serviceEstafeta.setId(values.get("id").toString());
		serviceEstafeta.setUsuario(values.get("usuario").toString());
		serviceEstafeta.setContrasena(values.get("contrasena").toString());
		serviceEstafeta.setOrigen(xmlRequest.getListaOrigen());
		serviceEstafeta.setDestino(xmlRequest.getListaDestino());
		serviceEstafeta.setEsFrecuencia(xmlRequest.getEsFrecuencia());
		serviceEstafeta.setEsLista(xmlRequest.getEsLista());
		serviceEstafeta.setEsPaquete(xmlRequest.getEsPaquete());
		serviceEstafeta.setLargo(xmlRequest.getLargo());
		serviceEstafeta.setPeso(xmlRequest.getPeso());
		serviceEstafeta.setAlto(xmlRequest.getAlto());
		serviceEstafeta.setAncho(xmlRequest.getAncho());
		EstafetaFrecuencia serviceFrecuencia = serviceEstafeta.getFrecuencia();
		JSONObject xmlJSONObj = XML.toJSONObject(serviceFrecuencia.getResponse());
		JSONObject soapEnvelope = xmlJSONObj.getJSONObject("soap:Envelope"); 
		JSONObject soapBody = soapEnvelope.getJSONObject("soap:Body");
		JSONObject frecuenciaCotizadorResponse = soapBody.getJSONObject("FrecuenciaCotizadorResponse");
		JSONObject frecuenciaCotizadorResult = frecuenciaCotizadorResponse.getJSONObject("FrecuenciaCotizadorResult");
		JSONObject respuesta = frecuenciaCotizadorResult.getJSONObject("Respuesta");
		if(serviceFrecuencia.isError()){
			System.out.print("Error: ["+serviceFrecuencia.getErrorCode()+"] ");
			response.setCodigoPosOri(respuesta.has("Error") ? String.valueOf(respuesta.get("Error")) : "");
			response.setCodigoPosOri(respuesta.has("MensajeError") ? String.valueOf(respuesta.get("MensajeError")) : "");
			log.info("\tError: "+respuesta.get("Error"));
			log.info("\tMensajeError: "+respuesta.get("MensajeError"));
			return response;

		}
		TipoEnvio te = new TipoEnvio();
		if(respuesta.has("TipoEnvio")) {
			JSONObject _tipoEnvio = respuesta.getJSONObject("TipoEnvio");
			te.setAlto(_tipoEnvio.has("Alto") ? String.valueOf(_tipoEnvio.get("Alto")) : "");
			te.setAncho(_tipoEnvio.has("Ancho") ? String.valueOf(_tipoEnvio.get("Ancho")) : "");
			te.setEsPaquete(_tipoEnvio.has("EsPaquete") ? String.valueOf(_tipoEnvio.get("EsPaquete")) : "");
			te.setLargo(_tipoEnvio.has("Largo") ? String.valueOf(_tipoEnvio.get("Largo")) : "");
			te.setPeso(_tipoEnvio.has("Peso") ? String.valueOf(_tipoEnvio.get("Peso")):"");
		}else {
			te.setAlto("");
			te.setAncho("");
			te.setEsPaquete("");
			te.setLargo("");
			te.setPeso("");
		}
		response.setTipoEnvio(te);
		TipoSer ts = new TipoSer();
		if(respuesta.has("TipoServicio")) {
			JSONObject tipoServicios = respuesta.getJSONObject("TipoServicio");
			if(tipoServicios.has("TipoServicio")) {
				JSONArray serviciosList = tipoServicios.getJSONArray("TipoServicio");
				for(int i=0;i< serviciosList.length();i++) {
					JSONObject _tipoServicio = serviciosList.getJSONObject(i);
					TipoServicio ts1 = new TipoServicio();
					ts1.setTarifaBase(_tipoServicio.has("TarifaBase") ? String.valueOf(_tipoServicio.get("TarifaBase")):"");
					ts1.setDescripcionServicio(_tipoServicio.has("DescripcionServicio") ? String.valueOf(_tipoServicio.get("DescripcionServicio")):"");
					ts1.setPeso(_tipoServicio.has("Peso") ? String.valueOf(_tipoServicio.get("Peso")):"");
					ts1.setAplicaCotizacion(_tipoServicio.has("AplicaCotizacion") ? String.valueOf(_tipoServicio.get("AplicaCotizacion")):"");
					ts1.setAplicaServicio(_tipoServicio.has("AplicaServicio") ? String.valueOf(_tipoServicio.get("AplicaServicio")):"");
					ts1.setSobrePeso(_tipoServicio.has("SobrePeso") ? String.valueOf(_tipoServicio.get("SobrePeso")):"");
					ts1.setCcTarifaBase(_tipoServicio.has("CCTarifaBase") ? String.valueOf(_tipoServicio.get("CCTarifaBase")):"");
					ts1.setTipoEnvioRes(_tipoServicio.has("TipoEnvioRes") ? String.valueOf(_tipoServicio.get("TipoEnvioRes")):"");
					ts1.setCcSobrePeso(_tipoServicio.has("CCSobrePeso") ? String.valueOf(_tipoServicio.get("CCSobrePeso")):"");
					ts1.setCostoTotal(_tipoServicio.has("CostoTotal") ? String.valueOf(_tipoServicio.get("CostoTotal")):"");
					ts1.setCargosExtra(_tipoServicio.has("CargosExtra") ? String.valueOf(_tipoServicio.get("CargosExtra")):"");
					ts.getTipoServicio().add(ts1);
				}
			}
		}
		response.setTiposServicios(ts);
		Colonia col = new Colonia();
		if(respuesta.has("Colonias")) {
			JSONObject colonias = respuesta.getJSONObject("Colonias");
			if(colonias.has("Colonias")) {
				JSONObject _coloniasObj = colonias.getJSONObject("Colonias");
				try {
					JSONArray coloniasList = _coloniasObj.getJSONArray("string");
					for(int i=0;i< coloniasList.length();i++) {
						String _colonia = String.valueOf(coloniasList.get(i));
						col.getNombre().add(_colonia);
					}
				}catch (Exception e1) {
					String _colonia = String.valueOf(_coloniasObj.get("string"));
					col.getNombre().add(_colonia);
				}
			}
		}
		response.setColonias(col);
		response.setExistenteSiglaDes(respuesta.has("ExistenteSiglaDes") ? respuesta.getString("ExistenteSiglaDes").trim() : "");
		response.setExistenteSiglaOri(respuesta.has("ExistenteSiglaOri") ? respuesta.getString("ExistenteSiglaOri").trim() : "");
		response.setCodigoPosOri(respuesta.has("CodigoPosOri") ? String.valueOf(respuesta.get("CodigoPosOri")) : "");
		response.setCostoReexpedicion(respuesta.has("CostoReexpedicion") ? String.valueOf(respuesta.get("CostoReexpedicion")) : "");
		Destino destino = new Destino();
		if(respuesta.has("Destino")) {
			JSONObject _destino = respuesta.getJSONObject("Destino");
			destino.setMunicipio(_destino.has("Municipio") ? _destino.getString("Municipio") : "");
			destino.setCpDestino(_destino.has("CpDestino") ? String.valueOf(_destino.get("CpDestino")) : "");
			destino.setPlaza(_destino.has("Plaza1") ? _destino.getString("Plaza1") : "");
			destino.setEstado(_destino.has("Estado") ? _destino.getString("Estado") : "");
			response.setDestino(destino);
		}else{
			destino.setMunicipio("");
			destino.setCpDestino("");
			destino.setPlaza("");
			destino.setEstado("");
		}
		Origen origen = new Origen();
		if(respuesta.has("Origen")) {
			JSONObject _origen = respuesta.getJSONObject("Origen");
			origen.setMunicipioOri(_origen.has("MunicipioOri") ? _origen.getString("MunicipioOri") : "");
			origen.setCodigoPosOri(_origen.has("CodigoPosOri") ? String.valueOf(_origen.get("CodigoPosOri")) : "");
			origen.setPlazaOri(_origen.has("PlazaOri") ? _origen.getString("PlazaOri") : "");
			origen.setEstadoOri(_origen.has("EstadoOri") ? _origen.getString("EstadoOri") : "");
			response.setOrigen(origen);
		}else{
			origen.setMunicipioOri("");
			origen.setCodigoPosOri("");
			origen.setPlazaOri("");
			origen.setEstadoOri("");
		}
		response.setCodigoPosOri(respuesta.has("Error") ? String.valueOf(respuesta.get("Error")) : "");
		response.setCodigoPosOri(respuesta.has("MensajeError") ? String.valueOf(respuesta.get("MensajeError")) : "");
		DiasEntrega de = new DiasEntrega();
		if(respuesta.has("DiasEntrega")) {
			JSONObject _diasEntrega = respuesta.getJSONObject("DiasEntrega");
			de.setLunes(_diasEntrega.has("Lunes") ? _diasEntrega.getString("Lunes"):"");
			de.setMartes(_diasEntrega.has("Martes") ? _diasEntrega.getString("Martes"):"");
			de.setMiercoles(_diasEntrega.has("Miercoles") ? _diasEntrega.getString("Miercoles"):"");
			de.setJueves(_diasEntrega.has("Jueves") ? _diasEntrega.getString("Jueves"):"");
			de.setViernes(_diasEntrega.has("Viernes") ? _diasEntrega.getString("Viernes"):"");
			de.setSabado(_diasEntrega.has("Sabado") ? _diasEntrega.getString("Sabado"):"");
			de.setDomingo(_diasEntrega.has("Domingo") ? _diasEntrega.getString("Domingo"):"");
		}else {
			de.setLunes("");
			de.setMartes("");
			de.setMiercoles("");
			de.setJueves("");
			de.setViernes("");
			de.setSabado("");
			de.setDomingo("");
		}
		response.setDiasEntrega(de);
		ModalidadEntrega me = new ModalidadEntrega();
		if(respuesta.has("ModalidadEntrega")) {
			JSONObject _modalidad = respuesta.getJSONObject("ModalidadEntrega");
			me.setFrecuencia(_modalidad.has("Frecuencia") ? _modalidad.getString("Frecuencia") : "");
			me.setOcurreForzoso(_modalidad.has("OcurreForzoso") ? _modalidad.getString("OcurreForzoso") : "");
		}else {
			me.setFrecuencia("");
			me.setOcurreForzoso("");
		}
		response.setModalidadEntrega(me);
		log.info("\tSatisfactorio");
		return response;
	}
	public com.integrador.xml.services.EstafetaLabelResponse executeLabelWS(com.integrador.xml.services.EstafetaLabelRequest xmlRequest) throws Exception{

		log.info("");
		log.info("== executeLabelWS ==>");
		com.integrador.xml.services.EstafetaLabelResponse response = new com.integrador.xml.services.EstafetaLabelResponse();
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
		if(xmlRequest.getInsurance()!=null){
			String insurance = xmlRequest.getInsurance().trim().toLowerCase();
			if(!com.integrador.util.Util.isBoolean(insurance)){
				String errorMsg = "Valor incorrecto para Isurance, valor esperado [true,false]";
				response.setResponseCode("-1");
				response.setResponseDescription(errorMsg);
				log.info("\t"+errorMsg);
				return response;
			}
			if(insurance.equalsIgnoreCase("true")){
				String insuranceValue = xmlRequest.getInsuranceValue().trim();
				if(insuranceValue==null || insuranceValue.equalsIgnoreCase("")){
					String errorMsg = "Debe especificar InsuranceValue";
					response.setResponseCode("-1");
					response.setResponseDescription(errorMsg);
					log.info("\t"+errorMsg);
					return response;
				}
				if(!com.integrador.util.Util.isStringNumeric(insuranceValue) ){
					String errorMsg = "Valor incorrecto para IsuranceValue, Patron: '\\d{1,6}.\\d{1,6}'";
					response.setResponseCode("-1");
					response.setResponseDescription(errorMsg);
					log.info("\t"+errorMsg);
					return response;
				}
			}
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
			g = guiasIntegradorRepository.save(g);
			return response;
		}
		Token token = tokenRepository.findByIdUsuarioAndToken(usuario.getIdUsuario(), xmlRequest.getToken());
		if(token==null) {
			String errorMsg = "Token invalido";
			response.setResponseCode("-2");
			response.setResponseDescription(errorMsg);
			log.info("\t"+errorMsg);
			g.setEstatus(errorMsg);
			guiasIntegradorRepository.save(g);
			return response;
		}
		Map<String,String> usersLabelService = atributoService.getByTipoInMap("Estafeta_Label_Users");
		String cliente = xmlRequest.getClient().trim();
		String userRestService = usersLabelService.get("users_rest_service").toString().trim();
		String userWebService = usersLabelService.get("users_web_service").toString().trim();
		String type = WEBSERVICE; //Por default ira al WS
		boolean found = false;
		if(userRestService.equalsIgnoreCase("*")) {
			type = RESTSERVICE;
		}else {
			String[] usuariosRestService = userRestService.split(",");
			String[] usuariosWebService = userWebService.split(",");
			for (String usuarioRestService : usuariosRestService) {
				if(cliente.equalsIgnoreCase(usuarioRestService)) {
					found = true;
					type = RESTSERVICE;
					break;
				}
			}
			if(!found){
				if(userWebService.equalsIgnoreCase("*")) {
					type = WEBSERVICE;
				}else {
					for (String usuarioWebService : usuariosWebService) {
						if(cliente.equalsIgnoreCase(usuarioWebService)) {
							found = true;
							type = WEBSERVICE;
							break;
						}
					}
				}
			}
		}
		String provider = xmlRequest.getProvider()!=null ? xmlRequest.getProvider().trim() : "Estafeta";
		if(provider.equalsIgnoreCase("Estafeta")) {
			if(type.equalsIgnoreCase(WEBSERVICE)) {
				log.info("\tInvocation Type: WebService");
				Map<String,String> values = atributoService.getByTipoInMap("Estafeta_Label"); //Valores para el servicio
				response = WebServiceEstafeta.executeLabel(values, xmlRequest, response, guiasIntegradorRepository, g);
			}else if(type.equalsIgnoreCase(RESTSERVICE)){
				log.info("\tInvocation Type: RestService");
				Map<String,String> values = atributoService.getByTipoInMap("Estafeta_Label_Rest");
				Estafeta estafeta = new Estafeta();
				estafeta.setAccount(values);
				estafeta.setXmlRequest(xmlRequest);
				estafeta.setXmlResponse(response);
				estafeta.setGuiaIntegradorRepository(guiasIntegradorRepository);
				estafeta.setGuiaIntegrador(g);
				estafeta.setAtributoService(atributoService);
				estafeta.setUsuariosService(usuariosService);
				estafeta.setBusinessRuleBeforeSend(beforeSendBR);
				response = (EstafetaLabelResponse) estafeta.executeRestService();
			}
		}else if(provider.equalsIgnoreCase("BotEstafeta")) {
			log.info("\tInvocation Type: BotEstafeta");
			Map<String,String> values = atributoService.getByTipoInMap("BotEstafeta");
			BotEstafeta botEstafeta = new BotEstafeta();
			botEstafeta.setAccount(values);
			botEstafeta.setXmlRequest(xmlRequest);
			botEstafeta.setXmlResponse(response);
			botEstafeta.setGuiaIntegradorRepository(guiasIntegradorRepository);
			botEstafeta.setGuiaIntegrador(g);
			botEstafeta.setAtributoService(atributoService);
			botEstafeta.setBusinessRuleBeforeSend("");
			response = (EstafetaLabelResponse) botEstafeta.executeWebService();
		}else if(provider.equalsIgnoreCase("BotDHL")) {
			log.info("\tInvocation Type: BotDHL");
			Map<String,String> values = atributoService.getByTipoInMap("BotDHL");
			BotDHL BotDHL = new BotDHL();
			BotDHL.setAccount(values);
			BotDHL.setXmlRequest(xmlRequest);
			BotDHL.setXmlResponse(response);
			BotDHL.setGuiaIntegradorRepository(guiasIntegradorRepository);
			BotDHL.setGuiaIntegrador(g);
			BotDHL.setAtributoService(atributoService);
			BotDHL.setBusinessRuleBeforeSend("");
			response = (EstafetaLabelResponse) BotDHL.executeWebService();
		}
		return response;
	}
	public GuiaIntegrador saveRequest(com.integrador.xml.services.EstafetaLabelRequest xmlRequest) {

		//Guardado del request
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
		g.setTipoGuia(xmlRequest.getServiceTypeId());
		g.setContenido(xmlRequest.getContent());
		g.setEtiquetas(xmlRequest.getNumberOfLabels());
		g.setTipoContenido(xmlRequest.getParcelTypeId());
		g.setKilos(xmlRequest.getWeight());
		g.setCliente(xmlRequest.getClient());
		g.setTracking("");
		//g.setFechaCreacion(Util.getCurrentDataTime());
		g.setFechaCreacion(com.integrador.util.Util.getCurrentDataTimeMexico());
		g.setRequest("");
		return guiasIntegradorRepository.save(g);
	}

	public com.integrador.xml.services.ExecuteQueryResponse executeConsultaEnviosWS(com.integrador.xml.services.ExecuteQuery xmlRequest) throws Exception{
		ExecuteQueryResponse xmlResponse = new ExecuteQueryResponse();
		if(xmlRequest.getClient()==null || xmlRequest.getClient().toString().trim().equalsIgnoreCase("")) {
			xmlResponse.setErrorCode("-1");
			xmlResponse.setErrorCodeDescriptionENG("Missing Client");
			xmlResponse.setErrorCodeDescriptionSPA("Debe especificar el cliente");
			return xmlResponse;
		}
		if(xmlRequest.getPassword()==null || xmlRequest.getPassword().toString().trim().equalsIgnoreCase("")) {
			xmlResponse.setErrorCode("-1");
			xmlResponse.setErrorCodeDescriptionENG("Missing Password");
			xmlResponse.setErrorCodeDescriptionSPA("Debe especificar el password");
			return xmlResponse;
		}
		if(xmlRequest.getToken()==null || xmlRequest.getToken().toString().trim().equalsIgnoreCase("")) {
			xmlResponse.setErrorCode("-1");
			xmlResponse.setErrorCodeDescriptionENG("Missing Token");
			xmlResponse.setErrorCodeDescriptionSPA("Debe especificar el token");
			return xmlResponse;
		}
		AESAlgorithm e = new AESAlgorithm();
		Usuario usuario = usuarioRepository.findByUsuarioAndContrasena(xmlRequest.getClient(), e.encrypt(xmlRequest.getPassword()));
		if(usuario==null) {
			xmlResponse.setErrorCode("-1");
			xmlResponse.setErrorCodeDescriptionENG("Invalid User o Password");
			xmlResponse.setErrorCodeDescriptionSPA("Usuario o Contraseña invalido");
			return xmlResponse;
		}
		Token token = tokenRepository.findByIdUsuarioAndToken(usuario.getIdUsuario(), xmlRequest.getToken());
		if(token==null) {
			xmlResponse.setErrorCode("-1");
			xmlResponse.setErrorCodeDescriptionENG("Invalid Token");
			xmlResponse.setErrorCodeDescriptionSPA("Token invalido");
			return xmlResponse;
		}
		Map<String,String> values = atributoService.getByTipoInMap("Estafeta_Consulta_Envios");
		EstafetaConsultaEnvios c = new EstafetaConsultaEnvios();
		c.setEndPoint(values.get("url").toString());
		c.setSuscriberId(values.get("suscriber_id").toString());
		c.setLogin(values.get("usuario").toString());
		c.setPassword(values.get("password").toString());
		SearchType searchType =  xmlRequest.getSearchType();
		WaybillList wbl =  searchType.getWaybillList();
		c.setWaybillType(wbl.getWaybillType());
		String allGuias = "";
		List<String> guias = wbl.getWaybills().getString();
		for(String guia : guias) {
			if(allGuias.trim().equalsIgnoreCase("")) {
				allGuias = guia;
			}else {
				allGuias = allGuias+","+guia;
			}
		}
		c.setWaybills(allGuias.split(","));
		c.setType(searchType.getType());
		SearchConfiguration seachConfiguration =  xmlRequest.getSearchConfiguration();
		c.setIncludeDimensions(seachConfiguration.getIncludeDimensions());
		c.setIncludeWaybillReplaceData(seachConfiguration.getIncludeWaybillReplaceData());
		c.setIncludeReturnDocumentData(seachConfiguration.getIncludeReturnDocumentData());
		c.setIncludeMultipleServiceData(seachConfiguration.getIncludeMultipleServiceData());
		c.setIncludeInternationalData(seachConfiguration.getIncludeInternationalData());
		c.setIncludeSignature(seachConfiguration.getIncludeSignature());
		c.setIncludeCustomerInfo(seachConfiguration.getIncludeCustomerInfo());
		HistoryConfiguration historyConfiguration = seachConfiguration.getHistoryConfiguration();
		c.setIncludeHistory(historyConfiguration.getIncludeHistory());
		c.setHistoryType(historyConfiguration.getHistoryType());
		Filter filterType = seachConfiguration.getFilterType();
		c.setFilterInformation(filterType.getFilterInformation());
		c.setFilterType(filterType.getFilterType());
		c.execute();
		JSONObject response = c.getResponseJSON();
		JSONObject executeQueryResponse = response.getJSONObject("ExecuteQueryResponse");
		JSONObject result = executeQueryResponse.getJSONObject("ExecuteQueryResult");
		String errorCodeDescriptionSPA = result.getString("errorCodeDescriptionSPA");
		String errorCode = String.valueOf(result.get("errorCode"));
		String errorCodeDescriptionENG = result.getString("errorCodeDescriptionENG");
		JSONObject trackingData = new JSONObject();
		JSONArray trackingDataList = new JSONArray();
		xmlResponse.setErrorCode(errorCode);
		xmlResponse.setErrorCodeDescriptionENG(errorCodeDescriptionENG);
		xmlResponse.setErrorCodeDescriptionSPA(errorCodeDescriptionSPA);
		if(!errorCode.trim().equalsIgnoreCase("0")) {
			return xmlResponse;
		}else {
			if(!String.valueOf(result.get("trackingData")).equalsIgnoreCase("")) {
				JSONObject trackingDataObj = result.getJSONObject("trackingData"); 
				try{
					trackingData = trackingDataObj.getJSONObject("TrackingData");
					trackingDataList = null;
				}catch (Exception e1) {
					trackingData = null;
					trackingDataList = trackingDataObj.getJSONArray("TrackingData");
				}
				if(trackingData!=null) {
					JSONObject deliveryDataObj = trackingData.getJSONObject("deliveryData");
					String destinationAcronym = deliveryDataObj.has("destinationAcronym") ? deliveryDataObj.getString("destinationAcronym") : "";
					String zipCode = deliveryDataObj.has("zipCode") ? String.valueOf(deliveryDataObj.get("zipCode")) : "";
					String deliveryDateTime = deliveryDataObj.has("deliveryDateTime") ? String.valueOf(deliveryDataObj.get("deliveryDateTime")) : "";
					String receiverName = deliveryDataObj.has("receiverName") ? String.valueOf(deliveryDataObj.get("receiverName")) : "";
					String destinationName = deliveryDataObj.has("destinationName") ? String.valueOf(deliveryDataObj.get("destinationName")) : "";
					DeliveryData delivaryData = new DeliveryData();
					delivaryData.setDestinationAcronym(destinationAcronym);
					delivaryData.setDestinationName(destinationName);
					delivaryData.setZipCode(zipCode);
					delivaryData.setDeliveryDateTime(deliveryDateTime);
					delivaryData.setReceiverName(receiverName);
					TrackingData _trackingData = new TrackingData();
					_trackingData.setDeliveryData(delivaryData);
					_trackingData.setAdditionalInformation(trackingData.has("additionalInformation") ? trackingData.getString("additionalInformation") : "");
					_trackingData.setShortWaybillId(trackingData.has("shortWaybillId") ? String.valueOf(trackingData.get("shortWaybillId")) : "");
					_trackingData.setStatusSPA(trackingData.has("statusSPA") ? String.valueOf(trackingData.get("statusSPA")) : "");
					_trackingData.setCustomerNumber(trackingData.has("customerNumber") ? String.valueOf(trackingData.get("customerNumber")) : "");
					_trackingData.setPackageType(trackingData.has("packageType") ? String.valueOf(trackingData.get("packageType")) : "");
					_trackingData.setServiceDescriptionENG(trackingData.has("serviceDescriptionENG") ? String.valueOf(trackingData.get("serviceDescriptionENG")) : "");
					_trackingData.setWaybill(trackingData.has("waybill") ? String.valueOf(trackingData.get("waybill")) : "");
					_trackingData.setStatusENG(trackingData.has("statusENG") ? String.valueOf(trackingData.get("statusENG")) : "");
					_trackingData.setServiceDescriptionSPA(trackingData.has("serviceDescriptionSPA") ? String.valueOf(trackingData.get("serviceDescriptionSPA")) : "");
					_trackingData.setServiceId(trackingData.has("serviceId") ? String.valueOf(trackingData.get("serviceId")) : "");
					if(trackingData.has("pickupData")) {
						JSONObject pickupDataObj = trackingData.getJSONObject("pickupData");
						PickupData pickupData = new PickupData();
						pickupData.setPickupDateTime(pickupDataObj.has("pickupDateTime")? String.valueOf(pickupDataObj.get("pickupDateTime")) : "");
						pickupData.setOriginAcronym(pickupDataObj.has("originAcronym")? String.valueOf(pickupDataObj.get("originAcronym")) : "");
						pickupData.setOriginName(pickupDataObj.has("originName")? String.valueOf(pickupDataObj.get("originName")) : "");
					}
					if(trackingData.has("history")) {
						JSONObject historyObj = trackingData.getJSONObject("history");
						JSONArray historyList = new JSONArray();
						try {
							historyList = historyObj.getJSONArray("History");
						}catch (Exception e1) {
							JSONObject h = historyObj.getJSONObject("History");
							historyList.put(h);
						}
						ArrayOfHistory arrayOfHistory = new ArrayOfHistory();
						for(int i=0;i<historyList.length();i++) {
							JSONObject obj = historyList.getJSONObject(i);
							History h = new History();
							h.setEventDateTime(obj.has("eventDateTime") ? String.valueOf(obj.get("eventDateTime")) : "");
							h.setEventId(obj.has("eventId") ? String.valueOf(obj.get("eventId")) : "");
							h.setEventDescriptionSPA(obj.has("eventDescriptionSPA") ? String.valueOf(obj.get("eventDescriptionSPA")) : "");
							h.setEventDescriptionENG(obj.has("eventDescriptionENG") ? String.valueOf(obj.get("eventDescriptionENG")) : "");
							h.setEventPlaceAcronym(obj.has("eventPlaceAcronym") ? String.valueOf(obj.get("eventPlaceAcronym")) : "");
							h.setEventPlaceName(obj.has("eventPlaceName") ? String.valueOf(obj.get("eventPlaceName")) : "");
							h.setExceptionCode(obj.has("exceptionCode") ? String.valueOf(obj.get("exceptionCode")) : "");
							h.setExceptionCodeDescriptionSPA(obj.has("exceptionCodeDescriptionSPA") ? String.valueOf(obj.get("exceptionCodeDescriptionSPA")) : "");
							h.setExceptionCodeDescriptionENG(obj.has("exceptionCodeDescriptionENG") ? String.valueOf(obj.get("exceptionCodeDescriptionENG")) : "");
							h.setExceptionCodeDetails(obj.has("exceptionCodeDetails") ? String.valueOf(obj.get("exceptionCodeDetails")) : "");
							arrayOfHistory.getHistory().add(h);
						}
						_trackingData.setHistory(arrayOfHistory);
					}
					if(trackingData.has("dimensions")) {
						JSONObject dimensionsObj =  trackingData.getJSONObject("dimensions");
						Dimensions dimensions = new Dimensions();
						dimensions.setVolumetricWeight(dimensionsObj.has("volumetricWeight") ? String.valueOf(dimensionsObj.get("volumetricWeight")) : "0.00" );
						dimensions.setWidth(dimensionsObj.has("width") ? String.valueOf(dimensionsObj.get("width")) : "0.00" );
						dimensions.setLength(dimensionsObj.has("length") ? String.valueOf(dimensionsObj.get("length")) : "0.00" );
						dimensions.setWeight(dimensionsObj.has("weight") ? String.valueOf(dimensionsObj.get("weight")) : "0.00" );
						dimensions.setHeight(dimensionsObj.has("height") ? String.valueOf(dimensionsObj.get("height")) : "0.00" );
						_trackingData.setDimensions(dimensions);
					}
					ArrayOfTrackingData arrayTrackingData = new ArrayOfTrackingData();
					arrayTrackingData.getTrackingData().add(_trackingData);
					xmlResponse.setTrackingData(arrayTrackingData);
				}
				if(trackingDataList!=null) {
					ArrayOfTrackingData arrayTrackingData = new ArrayOfTrackingData();
					for (int l=0;l< trackingDataList.length(); l++) {
						JSONObject _oblj = trackingDataList.getJSONObject(l);
						trackingData = _oblj;
						JSONObject deliveryDataObj = _oblj.getJSONObject("deliveryData");
						String destinationAcronym = deliveryDataObj.has("destinationAcronym") ? deliveryDataObj.getString("destinationAcronym") : "";
						String zipCode = deliveryDataObj.has("zipCode") ? String.valueOf(deliveryDataObj.get("zipCode")) : "";
						String deliveryDateTime = deliveryDataObj.has("deliveryDateTime") ? deliveryDataObj.getString("deliveryDateTime") : "";
						String receiverName = deliveryDataObj.has("receiverName") ? deliveryDataObj.getString("receiverName") : "";
						String destinationName = deliveryDataObj.has("destinationName") ? deliveryDataObj.getString("destinationName") : "";
						DeliveryData delivaryData = new DeliveryData();
						delivaryData.setDestinationAcronym(destinationAcronym);
						delivaryData.setDestinationName(destinationName);
						delivaryData.setZipCode(zipCode);
						delivaryData.setDeliveryDateTime(deliveryDateTime);
						delivaryData.setReceiverName(receiverName);
						TrackingData _trackingData = new TrackingData();
						_trackingData.setDeliveryData(delivaryData);
						_trackingData.setAdditionalInformation(trackingData.has("additionalInformation") ? trackingData.getString("additionalInformation") : "");
						_trackingData.setShortWaybillId(trackingData.has("shortWaybillId") ? String.valueOf(trackingData.get("shortWaybillId")) : "");
						_trackingData.setStatusSPA(trackingData.has("statusSPA") ? String.valueOf(trackingData.get("statusSPA")) : "");
						_trackingData.setCustomerNumber(trackingData.has("customerNumber") ? String.valueOf(trackingData.get("customerNumber")) : "");
						_trackingData.setPackageType(trackingData.has("packageType") ? String.valueOf(trackingData.get("packageType")) : "");
						_trackingData.setServiceDescriptionENG(trackingData.has("serviceDescriptionENG") ? String.valueOf(trackingData.get("serviceDescriptionENG")) : "");
						_trackingData.setWaybill(trackingData.has("waybill") ? String.valueOf(trackingData.get("waybill")) : "");
						_trackingData.setStatusENG(trackingData.has("statusENG") ? String.valueOf(trackingData.get("statusENG")) : "");
						_trackingData.setServiceDescriptionSPA(trackingData.has("serviceDescriptionSPA") ? String.valueOf(trackingData.get("serviceDescriptionSPA")) : "");
						_trackingData.setServiceId(trackingData.has("serviceId") ? String.valueOf(trackingData.get("serviceId")) : "");
						if(trackingData.has("pickupData")) {
							JSONObject pickupDataObj = trackingData.getJSONObject("pickupData");
							PickupData pickupData = new PickupData();
							pickupData.setPickupDateTime(pickupDataObj.has("pickupDateTime")? String.valueOf(pickupDataObj.get("pickupDateTime")) : "");
							pickupData.setOriginAcronym(pickupDataObj.has("originAcronym")? String.valueOf(pickupDataObj.get("originAcronym")) : "");
							pickupData.setOriginName(pickupDataObj.has("originName")? String.valueOf(pickupDataObj.get("originName")) : "");
						}
						if(trackingData.has("history")) {
							JSONObject historyObj = trackingData.getJSONObject("history");
							JSONArray historyList = historyObj.getJSONArray("History");
							ArrayOfHistory arrayOfHistory = new ArrayOfHistory();
							for(int i=0;i<historyList.length();i++) {
								JSONObject obj = historyList.getJSONObject(i);
								History h = new History();
								h.setEventDateTime(obj.has("eventDateTime") ? String.valueOf(obj.get("eventDateTime")) : "");
								h.setEventId(obj.has("eventId") ? String.valueOf(obj.get("eventId")) : "");
								h.setEventDescriptionSPA(obj.has("eventDescriptionSPA") ? String.valueOf(obj.get("eventDescriptionSPA")) : "");
								h.setEventDescriptionENG(obj.has("eventDescriptionENG") ? String.valueOf(obj.get("eventDescriptionENG")) : "");
								h.setEventPlaceAcronym(obj.has("eventPlaceAcronym") ? String.valueOf(obj.get("eventPlaceAcronym")) : "");
								h.setEventPlaceName(obj.has("eventPlaceName") ? String.valueOf(obj.get("eventPlaceName")) : "");
								h.setExceptionCode(obj.has("exceptionCode") ? String.valueOf(obj.get("exceptionCode")) : "");
								h.setExceptionCodeDescriptionSPA(obj.has("exceptionCodeDescriptionSPA") ? String.valueOf(obj.get("exceptionCodeDescriptionSPA")) : "");
								h.setExceptionCodeDescriptionENG(obj.has("exceptionCodeDescriptionENG") ? String.valueOf(obj.get("exceptionCodeDescriptionENG")) : "");
								h.setExceptionCodeDetails(obj.has("exceptionCodeDetails") ? String.valueOf(obj.get("exceptionCodeDetails")) : "");
								arrayOfHistory.getHistory().add(h);
							}
							_trackingData.setHistory(arrayOfHistory);
						}
						arrayTrackingData.getTrackingData().add(_trackingData);
					}
					xmlResponse.setTrackingData(arrayTrackingData);
				}
			}
		}
		return xmlResponse;
	}
}
