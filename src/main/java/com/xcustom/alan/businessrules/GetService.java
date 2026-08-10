package com.xcustom.alan.businessrules;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.integrador.exceptions.BusinessRuleException;
import com.integrador.models.UsuarioAlan;
import com.integrador.services.UsuariosService;
import com.integrador.util.AESAlgorithm;
/**
 * <b>Alan Business Rule SetServiceByWeight</b><br/>
 * Determines the appropriate Estafeta service and whether it uses kilos based on the client's configuration and request data.<br/><br/>
 * <ul>
 *   <li>Only applies when the request's <code>via</code> is <b>"Integrador"</b>.</li>
 *   <li>Retrieves the user's Estafeta account configuration or defaults if not found.</li>
 *   <li>Supports both <b>Terrestre</b> and <b>Express</b> service types, with handling for <b>"kilada"</b> or <b>"rango"</b> billing modes.</li>
 *   <li>Determines the correct service key and whether the service uses kilos, based on request weight.</li>
 *   <li>Modifies the <code>xmlRequest</code> in the context to include the selected service and kilos flag.</li>
 * </ul><br/>
 */
@Component
public class GetService {

	static final Logger log = LoggerFactory.getLogger(com.xcustom.alan.businessrules.GetService.class);
	public Map<String, Object> run(Map<String, Object> context) {
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = (com.integrador.xml.services.EstafetaLabelRequest) context.get("xmlRequest");
		UsuariosService usuariosService = (UsuariosService) context.get("usuariosService");
		if(xmlRequest.getVia() != null && xmlRequest.getVia().equalsIgnoreCase("Integrador")) {
			String account = xmlRequest.getAccount();
			if(account != null && account.startsWith("V2")) {

				AESAlgorithm aes = new AESAlgorithm();
				UsuarioAlan usuario = usuariosService.findUsuarioAlan(xmlRequest.getClient(), aes.encrypt(xmlRequest.getPassword()));

				int kilos = Integer.parseInt(xmlRequest.getWeight());
				JSONObject cuentas = new JSONObject(usuario.getCuentasEstafeta() == null || usuario.getCuentasEstafeta().isEmpty() ? usuariosService.getJSONDefaultCuentasEstafeta() : usuario.getCuentasEstafeta());

				String tipoServicio = xmlRequest.getServiceTypeId();
				log.info("\t\tType: " + tipoServicio);

				if(tipoServicio.equalsIgnoreCase("Terrestre")){
					String tipoCobro = usuario.getTipoCobroTerrestre();

					if(tipoCobro.equalsIgnoreCase("kilada")){
						String cuentaKey = kilos <= 5 ? "kilada_terrestre_5kg" : "kilada_terrestre_otros";
						setServicio(xmlRequest, cuentas, cuentaKey, "servicios_terrestres", usuariosService, context);
					}else if(tipoCobro.equalsIgnoreCase("rango")) {
						String cuentaKey = "rango_terrestre_" + kilos + "kg";
						setServicio(xmlRequest, cuentas, cuentaKey, "servicios_terrestres", usuariosService, context);
					}
				}else if(tipoServicio.equalsIgnoreCase("Express")){
					String tipoCobro = usuario.getTipoCobroExpress();

					if(tipoCobro.equalsIgnoreCase("kilada")) {
						String cuentaKey = kilos <= 1 ? "kilada_express_1kg" : "kilada_express_otros";
						setServicio(xmlRequest, cuentas, cuentaKey, "servicios_express", usuariosService, context);
					}else if(tipoCobro.equalsIgnoreCase("rango")){
						String cuentaKey = "rango_express_" + kilos + "kg";
						setServicio(xmlRequest, cuentas, cuentaKey, "servicios_express", usuariosService, context);
					}
				}
			}
		}

		return context;
	}

	private void setServicio(com.integrador.xml.services.EstafetaLabelRequest xmlRequest,
			JSONObject cuentas,
			String cuentaKey,
			String tipoServicio,
			UsuariosService usuariosService,
			Map<String, Object> context) {

		String servicioKey = "servicio_"+cuentaKey;
		String fallbackServicioKey = cuentaKey + "_servicio";

		boolean usaKilos = false;
		String service = "";
		try {
			String requestAccount = xmlRequest.getAccount();
			String accountName = "";

			if (requestAccount != null && !requestAccount.trim().isEmpty()) {
				accountName = requestAccount.trim();
				log.info("\t\tUsando cuenta del request: " + accountName);
			} else if (cuentas.has(cuentaKey) && !cuentas.getString(cuentaKey).trim().isEmpty()) {
				accountName = cuentas.getString(cuentaKey).trim();
				log.info("\t\tcuentaKey '" + cuentaKey + "' encontrada en DB: " + accountName);
			}

			if (accountName == null || accountName.trim().isEmpty()) {
				accountName = requestAccount;
				log.info("\t\tcuentaKey '" + cuentaKey + "' vacía o no encontrada. Usando cuenta del request: " + accountName);
			}
			xmlRequest.setAccount(accountName);

			com.integrador.services.AtributoService atributoService = (com.integrador.services.AtributoService) context.get("atributoService");
			if (atributoService != null && accountName != null && !accountName.trim().isEmpty()) {
				Map<String, String> updatedValues = atributoService.getByTipoInMap(accountName);
				if (updatedValues != null && !updatedValues.isEmpty()) {
					mapCompatibleKeys(updatedValues);
					context.put("account", updatedValues);
					log.info("\t\tContext 'account' cargado para: " + accountName);
				}
			}

			com.integrador.models.CuentaEstafetaV2 cV2 = usuariosService.getCuentaV2(accountName != null ? accountName.trim() : "");
			if (cV2 == null) {
				throw new Exception("Cuenta V2 [" + accountName + "] no encontrada en base de datos.");
			}
			JSONObject confv2 = new JSONObject(cV2.getConfiguracion());
			
			String requestService = xmlRequest.getService();
			if (requestService != null && !requestService.trim().isEmpty()) {
				service = requestService.trim();
				log.info("\t\tUsando service del request: " + service);
			} else if (cuentas.has(servicioKey) && !cuentas.getString(servicioKey).trim().isEmpty()) {
				service = cuentas.getString(servicioKey).trim();
				log.info("\t\tService: " + service + " [DB]");
			} else if (cuentas.has(fallbackServicioKey) && !cuentas.getString(fallbackServicioKey).trim().isEmpty()) {
				service = cuentas.getString(fallbackServicioKey).trim();
				log.info("\t\tService: " + service + " [DB Fallback]");
			}

			if (service == null || service.trim().isEmpty()) {
				throw new Exception("Clave de servicio no encontrada (" + servicioKey + " o " + fallbackServicioKey + ") y tampoco se especificó en el request.");
			}
			
			int servicioBuscado = Integer.parseInt(service.trim());
			JSONArray servicios = confv2.getJSONArray(tipoServicio);
			boolean servicioEncontrado = false;
			for(int i = 0; i < servicios.length(); i++) {
				JSONObject servicioObj = servicios.getJSONObject(i);
				if(servicioObj.getInt("servicio") == servicioBuscado) {
					int usarKilosDB = servicioObj.optInt("usar_kilos", 0);
					usaKilos = (usarKilosDB == 1);
					log.info("\t\tDB usar_kilos: " + usarKilosDB + " (Servicio " + servicioBuscado + ")");
					servicioEncontrado = true;
					break;
				}
			}
			if (!servicioEncontrado) {
				log.info("\t\tDB usar_kilos: 0 (Servicio " + servicioBuscado + " no config)");
			}
		}catch(Exception e){
			log.info("\t\tError: " + servicioKey + " o " + fallbackServicioKey + " no encontrado en los datos de configuración. " + e.getMessage());
			throw new BusinessRuleException(1, "Servicio no encontrado. (referencia: " + servicioKey + " o " + fallbackServicioKey + ")");
		}
		String useKilos = String.valueOf(usaKilos);
		String servicio = service;

		String requestUseKilos = xmlRequest.getIsServiceUsesKilos();
		if (requestUseKilos != null && (requestUseKilos.equalsIgnoreCase("true") || requestUseKilos.equalsIgnoreCase("false"))) {
			log.info("\t\tIsServiceUsesKilos: " + requestUseKilos + " [Request]");
		} else {
			xmlRequest.setIsServiceUsesKilos(useKilos);
			log.info("\t\tIsServiceUsesKilos: " + useKilos + " [DB]");
		}
		xmlRequest.setService(servicio);
		context.put("xmlRequest", xmlRequest);

		log.info("\t\tService: " + servicio);
	}

	private void mapCompatibleKeys(Map<String, String> values) {
		if (values == null) return;
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