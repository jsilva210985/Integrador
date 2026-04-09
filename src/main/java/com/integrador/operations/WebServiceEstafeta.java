package com.integrador.operations;

import java.util.Map;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.integrador.models.GuiaIntegrador;
import com.integrador.repositories.GuiaIntegradorRepository;

public class WebServiceEstafeta {

	static final Logger log = LoggerFactory.getLogger(WebServiceEstafeta.class);
	public static com.integrador.xml.services.EstafetaLabelResponse executeLabel(Map<String,String> values, com.integrador.xml.services.EstafetaLabelRequest xmlRequest, com.integrador.xml.services.EstafetaLabelResponse response, GuiaIntegradorRepository guasIntegradorRepository, GuiaIntegrador g) throws Exception {
		log.info("\tWebService: ");
		com.estafeta.models.Estafeta estafeta = new com.estafeta.models.Estafeta();
		//Configuracion de la cuenta
		estafeta.setPortAddress(values.get("url").toString().trim());
		estafeta.setCustomerNumber(values.get("customer_number").toString().trim());
		estafeta.setUser(values.get("usuario").toString().trim());
		estafeta.setPassword(values.get("contrasena").toString().trim());
		estafeta.setSuscriberId(values.get("suscriber_id").toString().trim());
		estafeta.setServiceTypeIdTerrestre(values.get("service_type_terrestre").toString().trim());
		estafeta.setServiceTypeIdExpress(values.get("service_type_express").toString().trim());
		estafeta.setOfficeNum(values.get("office_num").toString().trim());
		//Datos del cliente en estafeta
		estafeta.setShipperStreet(values.get("enviador_calle").toString().trim());
		estafeta.setShipperCity(values.get("enviador_ciudad").toString().trim());
		estafeta.setShipperCompanyName(values.get("enviador_company_name").toString().trim());//TODO
		estafeta.setShipperNeighborhood(values.get("enviador_localidad").toString().trim());//
		estafeta.setShipperTel(values.get("enviador_tel").toString().trim());
		estafeta.setShipperState(values.get("enviador_estado").toString().trim());
		estafeta.setShipperCP(values.get("enviador_cp").toString().trim());
		//Ubicacion de la guia
		estafeta.setLabelLocation("");//No para el integrador
		//Origen
		String _numeroExteriorOrigen = xmlRequest.getOrigenExtNum()!=null && !xmlRequest.getOrigenExtNum().toString().trim().equalsIgnoreCase("") ? xmlRequest.getOrigenExtNum().toString().trim() : "";
		String _numeroInteriorOrigen = xmlRequest.getOrigenIntNum()!=null && !xmlRequest.getOrigenIntNum().toString().trim().equalsIgnoreCase("") ? xmlRequest.getOrigenIntNum().toString().trim() : "";
		String _razonSocialOrigen = xmlRequest.getOrigenCorporateName()!=null && !xmlRequest.getOrigenCorporateName().toString().trim().equalsIgnoreCase("") ? xmlRequest.getOrigenCorporateName().toString().trim() : "";
		com.estafeta.models.Direccion remitente = new com.estafeta.models.Direccion();
		remitente.setCalle(xmlRequest.getOrigenAddress1());
		remitente.setColonia(xmlRequest.getOrigenNeighborhood());
		remitente.setCp(xmlRequest.getOrigenZipCode());
		remitente.setDestinatario(xmlRequest.getOrigenContactName());
		remitente.setEstado(xmlRequest.getOrigenState());
		remitente.setMunicipio(xmlRequest.getOrigenCity());
		remitente.setReferencia(xmlRequest.getOrigenAddress2());
		remitente.setTelefono(xmlRequest.getOrigenPhoneNumber());
		remitente.setNumeroExterior(_numeroExteriorOrigen);
		remitente.setNumeroInterior(_numeroInteriorOrigen);
		remitente.setRazonSocial(_razonSocialOrigen);
		//Destino
		String _numeroExteriorDestinatario = xmlRequest.getExtNum()!=null && !xmlRequest.getExtNum().toString().trim().equalsIgnoreCase("") ? xmlRequest.getExtNum().toString().trim() : "";
		String _numeroInteriorDestinatario = xmlRequest.getIntNum()!=null && !xmlRequest.getIntNum().toString().trim().equalsIgnoreCase("") ? xmlRequest.getIntNum().toString().trim() : "";
		String _razonSocialDestinatario = xmlRequest.getCorporateName()!=null && !xmlRequest.getCorporateName().toString().trim().equalsIgnoreCase("") ? xmlRequest.getCorporateName().toString().trim() : "";
		com.estafeta.models.Direccion destinatario = new com.estafeta.models.Direccion();
		destinatario.setCalle(xmlRequest.getAddress1());
		destinatario.setColonia(xmlRequest.getNeighborhood());
		destinatario.setCp(xmlRequest.getZipCode());
		destinatario.setDestinatario(xmlRequest.getContactName());
		destinatario.setEstado(xmlRequest.getState());
		destinatario.setMunicipio(xmlRequest.getCity());
		destinatario.setReferencia(xmlRequest.getAddress2());
		destinatario.setTelefono(xmlRequest.getPhoneNumber());
		destinatario.setNumeroExterior(_numeroExteriorDestinatario);
		destinatario.setNumeroInterior(_numeroInteriorDestinatario);
		destinatario.setRazonSocial(_razonSocialDestinatario);
		//Invocacion del servicio
		com.estafeta.webservices.estafetalabel.EstafetaClient ws = new com.estafeta.webservices.estafetalabel.EstafetaClient(estafeta, remitente, destinatario);
		ws.setInformacionAdicional(xmlRequest.getAditionalInfo());
		ws.setTipoGuia(xmlRequest.getServiceTypeId());
		ws.setContenido(xmlRequest.getContent());
		ws.setEtiquetas(Integer.parseInt(xmlRequest.getNumberOfLabels()));//Por default 1 Validar este dato
		ws.setTipoContenido(Integer.parseInt(xmlRequest.getParcelTypeId()));//1-sobre 4-Paquete
		ws.setKilos(Integer.parseInt(xmlRequest.getWeight()));

		ws.setPaperType(Integer.parseInt(xmlRequest.getPaperType()));
		ws.setDeliveryToEstafetaOffice(Boolean.parseBoolean(xmlRequest.getDeliveryToEstafetaOffice()));
		ws.setReference(xmlRequest.getReference());
		ws.setContentDescription(xmlRequest.getContentDescription());

		JSONObject serviceResponse = ws.crearGuia();
		g.setRequest(ws.getRequestXML().toString().trim());
		guasIntegradorRepository.save(g);
		if(serviceResponse.has("result_code")) {
			String resultCode = String.valueOf(serviceResponse.get("result_code")).trim();
			if(resultCode.equalsIgnoreCase("0")) {
				String rastreo = serviceResponse.has("confirmation_number") ? serviceResponse.get("confirmation_number").toString() : "";
				response.setResponseCode(resultCode);
				response.setFile(serviceResponse.has("file") ? serviceResponse.get("file").toString() : "");
				response.setTracking(rastreo);
				response.setResponseDescription(serviceResponse.has("result_description") ? serviceResponse.get("result_description").toString() : "");
				//Actualizacion en DB
				g.setTracking(serviceResponse.has("confirmation_number") ? serviceResponse.get("confirmation_number").toString() : "");
				guasIntegradorRepository.save(g);
				log.info("\tGuia Creada: "+rastreo);
			}else {
				String errorMsg = serviceResponse.has("result_description") ? serviceResponse.get("result_description").toString() : "";
				if(errorMsg.length()>1200) {
					errorMsg = errorMsg.substring(0,1199);
				}
				//Actualizacion en DB
				g.setEstatus(errorMsg);
				guasIntegradorRepository.save(g);
				log.info("\tErrorCode: "+resultCode);
				log.info("\tErrorDescription: "+errorMsg);
				response.setResponseCode(resultCode);
				response.setResponseDescription(errorMsg);
			}
		}
		return response;
	}
}
