package com.xcustom.alan.businessrules;

import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.integrador.models.CuentaEstafeta;
import com.integrador.models.CuentaEstafetaV2;
import com.integrador.services.UsuariosService;
/**
 * <b>Alan Business Rule GetAliasAccount</b><br/>
 * Retrieves the alias of an Estafeta V2 account if applicable. This business rule is executed during the label 
 * request preparation and updates the xmlRequest object with the alias if found in the account configuration.<br/><br/>
 * <ul>
 * 	<li>Applies only to accounts whose identifier starts with "V2".</li>
 * 	<li>Looks up the account using the UsuariosService.</li>
 * 	<li>If the account exists and its configuration contains a valid alias, it is assigned to the xmlRequest.</li>
 * 	<li>If the account is not found or the configuration is empty or invalid, no alias is assigned.</li>
 * </ul><br/>
 */
@Component
public class GetAliasAccount {

	static final Logger log = LoggerFactory.getLogger(com.xcustom.alan.businessrules.GetAliasAccount.class);

	public Map<String,Object> run(Map<String,Object> context) {
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = (com.integrador.xml.services.EstafetaLabelRequest) context.get("xmlRequest");
		UsuariosService usuariosService = (UsuariosService) context.get("usuariosService");
		try{
			String account = xmlRequest.getAccount();
			if (account != null && account.startsWith("V2")) {
				CuentaEstafetaV2 cuenta = usuariosService.getCuentaV2(account);
				log.info("\t\tCuenta V2 detected.");
				if (cuenta != null) {
					String conf = cuenta.getConfiguracion();
					if (conf != null && !"".equalsIgnoreCase(conf.trim())) {
						JSONObject c = new JSONObject(conf);
						if (c.has("alias")) {
							String alias = c.getString("alias").trim();
							xmlRequest.setAlias(alias);
							context.put("xmlRequest", xmlRequest);
							log.info("\t\tAlias: " + alias);
						}
					} else {
						log.info("\t\tConfiguration (estafeta_cuentas_v2) not found or empty");
					}
				} else {
					log.info("\t\tCuentaEstafetaV2 not found");
				}
			}else{
				CuentaEstafeta cuenta = usuariosService.getCuenta(account);
				if (cuenta != null) {
					log.info("\t\tCuenta V1 detected.");
					String conf = cuenta.getConfiguracion();
					if (conf != null && !"".equalsIgnoreCase(conf.trim())) {
						JSONObject c = new JSONObject(conf);
						if (c.has("alias")) {
							String alias = c.getString("alias").trim();
							xmlRequest.setAlias(alias);
							context.put("xmlRequest", xmlRequest);
							log.info("\t\tAlias: " + alias);
						}
					} else {
						log.info("\t\tConfiguration (estafeta_cuentas) not found or empty");
					}
				} else {
					log.info("\t\tCuentaEstafeta not found");
				}
			}
		} catch (Exception e) {
			//
		}
		return context;
	}
}