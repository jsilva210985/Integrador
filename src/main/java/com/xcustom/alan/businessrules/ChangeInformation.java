package com.xcustom.alan.businessrules;

import java.io.Console;
import java.math.BigDecimal;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.integrador.util.Util;
import com.integrador.carriers.Estafeta;
import com.integrador.services.AtributoService;
/**
* <b>Alan Business Rule ChangeInformation</b><br/>
* Depending of the account, if the flag for change address is active the information will be replaced.<br/><br/>
* <ul>
* 	<li>Terrestre, if kg is 19 or less use Ventro otherwise Barajas account</li>
* 	<li>Express uses Ventro</li>
* <ul><br/>
*/
@SuppressWarnings("all")
@Component
public class ChangeInformation {
	static final Logger log = LoggerFactory.getLogger(com.xcustom.alan.businessrules.ChangeInformation.class);
	public Map<String,Object> run(Map<String,Object> context) {
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = (com.integrador.xml.services.EstafetaLabelRequest) context.get("xmlRequest");
		AtributoService atributoService = (AtributoService) context.get("atributoService");
		Map<String,String> values = (Map<String,String>) context.get("account");
		if(values.containsKey("cambiar_direccion") && values.get("cambiar_direccion").toString().equalsIgnoreCase("1")){
			String razonSocial = values.containsKey("direccion_razon_social") ? values.get("direccion_razon_social").toString().trim() : "";
			String nombre = values.containsKey("direccion_nombre") ? values.get("direccion_nombre").toString().trim() : "";
			String telefono = values.containsKey("direccion_telefono") ? values.get("direccion_telefono").toString().trim() : "";
			log.info("\t\tRazon social: "+razonSocial);
			log.info("\t\tNombre: "+nombre);
			log.info("\t\tTelefono: "+telefono);
			if(!razonSocial.trim().equalsIgnoreCase(Util.EMPTY_STRING))
				xmlRequest.setOrigenCorporateName(razonSocial);
			if(!nombre.trim().equalsIgnoreCase(Util.EMPTY_STRING))
				xmlRequest.setOrigenContactName(nombre);
			if(!telefono.trim().equalsIgnoreCase(Util.EMPTY_STRING))
				xmlRequest.setOrigenPhoneNumber(telefono);
			context.put("xmlRequest", xmlRequest);
		}
		return context;
	}
}
