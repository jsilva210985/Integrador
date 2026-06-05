package com.xcustom.alan.businessrules;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.integrador.services.AtributoService;

/**
 * <b>Alan Business Rule SetAccount</b><br/>
 * Determines which account configuration to load into the context based on the request information.
 * <p>
 * First queries the new EstafetaRestV2 configuration format by matching account/alias, falling back
 * to the V2 default account "V2_Estafeta_Label_Rest", and finally falling back to legacy direct attributes table lookup.
 */
@Component
public class SetAccount {

	static final Logger log = LoggerFactory.getLogger(com.xcustom.alan.businessrules.SetAccount.class);

	@SuppressWarnings("unchecked")
	public Map<String, Object> run(Map<String, Object> context) {
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = (com.integrador.xml.services.EstafetaLabelRequest) context.get("xmlRequest");
		AtributoService atributoService = (AtributoService) context.get("atributoService");
		String accountToFind = xmlRequest.getAccount();
		Map<String, String> values = new HashMap<String, String>();

		if (accountToFind != null) {
			values = atributoService.getByTipoInMap(accountToFind);
			log.info("\t\tSetAccount BR: Buscando cuenta [" + accountToFind + "] directamente");
		}

		if (values.isEmpty()) {
			log.info("\t\tSetAccount BR: Cuenta no encontrada. Usando default.");
			String defaultAccount = "Estafeta_Label_Rest";
			values = atributoService.getByTipoInMap(defaultAccount);
		}

		mapCompatibleKeys(values);
		context.put("account", values);
		return context;
	}

	private void mapCompatibleKeys(Map<String, String> values) {
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
	}
}
