package com.xcustom.alan.businessrules;

import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.integrador.models.UsuarioAlan;
import com.integrador.services.UsuariosService;
import com.integrador.util.AESAlgorithm;
/**
*<b>Alan Business Rule ParseServiceType</b><br/>
* Parses the service type ID from the provided XML request and updates the account configuration accordingly.<br/><br/>
* <ul>
*   <li>If the account starts with "V2" and the service type is "Terrestre", the numeric part of the service is stored in <code>service_type_id_terrestre</code>.</li>
*   <li>If the account starts with "V2" and the service type is "Express", the numeric part of the service is stored in <code>service_type_id_express</code>.</li>
*   <li>The service field is expected to contain a number followed by a label in brackets, e.g., "78 [Terrestre]". Only the number is extracted.</li>
* </ul><br/>
*/
@Component
public class ParseServiceType {
	static final Logger log = LoggerFactory.getLogger(com.xcustom.alan.businessrules.ParseServiceType.class);
	@SuppressWarnings("unchecked")
	public Map<String,Object> run(Map<String,Object> context) {
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = (com.integrador.xml.services.EstafetaLabelRequest) context.get("xmlRequest");
		UsuariosService usuariosService = (UsuariosService) context.get("usuariosService");
		Map<String,String> values = (Map<String,String>) context.get("account");
		log.info("\t\tAccount: "+xmlRequest.getAccount());
		if(xmlRequest.getService() != null && !xmlRequest.getService().isEmpty()){
			String rawService = xmlRequest.getService().trim();// e.g., "78 [Terrestre]"
			String serviceNumber = rawService.split("\\s|\\[")[0];// "78"
			if(xmlRequest.getAccount().startsWith("V2")){
				if(xmlRequest.getServiceTypeId().equalsIgnoreCase("Terrestre") && !xmlRequest.getService().trim().isEmpty() ) {
					values.put("service_type_id_terrestre",serviceNumber);
					log.info("\t\tService: "+serviceNumber);
				}else if(xmlRequest.getServiceTypeId().equalsIgnoreCase("Express") && !xmlRequest.getService().trim().isEmpty() ) {
					values.put("service_type_id_express",serviceNumber);
					log.info("\t\tService Type: "+serviceNumber);
				}
				context.put("account", values);
			}
			//Reexpedition and direct account.
			String reexpedicionValue = xmlRequest.getReexpedicion();
			if("1".equalsIgnoreCase(reexpedicionValue)){
				AESAlgorithm aes = new AESAlgorithm();
				UsuarioAlan usuario = usuariosService.findUsuarioAlan(xmlRequest.getClient(), aes.encrypt(xmlRequest.getPassword()));
				JSONObject cuentas = new JSONObject(usuario.getCuentasEstafeta() == null || usuario.getCuentasEstafeta().isEmpty() ? usuariosService.getJSONDefaultCuentasEstafeta() : usuario.getCuentasEstafeta());
				if(xmlRequest.getServiceTypeId().equalsIgnoreCase("Terrestre")){
					if(cuentas.has("tiene_cuentadirecta_reexpedicion_terreste")){
						if(cuentas.getInt("tiene_cuentadirecta_reexpedicion_terreste")==1){
							log.info("\t\tDirect Account: True");
							if(cuentas.has("servicio_directo_reexpedicion_terreste")){
								serviceNumber = cuentas.getString("servicio_directo_reexpedicion_terreste");
								if("".equalsIgnoreCase(serviceNumber)){
									log.info("\t\t\tservicio_directo_reexpedicion_terreste is empty, service type not changed.");
								}else {
									values.put("service_type_id_terrestre",serviceNumber);
									log.info("\t\t\tService Type: "+serviceNumber);
								}
							}else {
								log.info("\t\t\tservicio_directo_reexpedicion_terreste param not found, service type not changed.");
							}
						}
					}
				}
				if(xmlRequest.getServiceTypeId().equalsIgnoreCase("Express")){
					if(cuentas.has("tiene_cuentadirecta_reexpedicion_express")){
						if(cuentas.getInt("tiene_cuentadirecta_reexpedicion_express")==1){
							log.info("\t\tDirect Account: True");
							if(cuentas.has("servicio_directo_reexpedicion_express")){
								serviceNumber = cuentas.getString("servicio_directo_reexpedicion_express");
								if("".equalsIgnoreCase(serviceNumber)){
									log.info("\t\t\tservicio_directo_reexpedicion_express is empty, service type not changed.");
								}else{
									values.put("service_type_id_express",serviceNumber);
									log.info("\t\t\tService Type: "+serviceNumber);
								}
							}else{
								log.info("\t\t\tservicio_directo_reexpedicion_express param not found, service type not changed.");
							}
						}
					}
				}
				context.put("account", values);
			}
		}
		return context;
	}
}