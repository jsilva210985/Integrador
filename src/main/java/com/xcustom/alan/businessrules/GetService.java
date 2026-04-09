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

		//boolean usaKilos = usuariosService.verificaUsoKilos(cuentas, cuentaKey, servicioKey, tipoServicio);
		boolean usaKilos = false;
		String service = "";
		try {
			com.integrador.models.CuentaEstafetaV2 cV2 = usuariosService.getCuentaV2(cuentas.getString(cuentaKey));
			JSONObject confv2 = new JSONObject(cV2.getConfiguracion());
			service = cuentas.getString(servicioKey);
			int servicioBuscado = Integer.parseInt(service);
			JSONArray servicios = confv2.getJSONArray(tipoServicio);
			for(int i = 0; i < servicios.length(); i++) {
				JSONObject servicioObj = servicios.getJSONObject(i);
				if(servicioObj.getInt("servicio") == servicioBuscado) {
					usaKilos =  servicioObj.optInt("usar_kilos", 0) == 1;
				}
			}
		}catch(Exception e){
			log.info("\t\tError: "+servicioKey+ " not found in configuration data");
			throw new BusinessRuleException(1, "Servicio no encontrado. (referenia: "+servicioKey+")");
		}
		String useKilos = String.valueOf(usaKilos);
		String servicio = null;

		if(cuentas.has(servicioKey)) {
			servicio = cuentas.getString(servicioKey);
		}else if (cuentas.has(fallbackServicioKey)) {
			servicio = cuentas.getString(fallbackServicioKey);
			//servicioKey = fallbackServicioKey; // actualiza la clave para verificar uso de kilos
		}

		xmlRequest.setIsServiceUsesKilos(useKilos);
		xmlRequest.setService(servicio);
		context.put("xmlRequest", xmlRequest);

		log.info("\t\tIsServiceUsesKilos: " + useKilos);
		log.info("\t\tService: " + cuentas.getString(servicioKey));
	}
}