package com.xcustom.puntoactivo.businessrules;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.integrador.carriers.Fedex;
import com.integrador.services.AtributoService;
/**
* <b>Punto Activo Business Rule SetAccount</b><br/>
* Depending of account received is the account that the integrator will use.<br/><br/>
*/
@SuppressWarnings("all")
public class SetAccount {
	static final Logger log = LoggerFactory.getLogger(SetAccount.class);
	static final String _tab3 = "\t\t\t";
	/**
	 * The format in attributes type is like this: Fedex3_Ship where the number is the id of trhe carrier.
	 * @param context
	 * @return
	 */
	public Map<String,Object> run(Map<String,Object> context) {
		log.info("\t\tBusinessRule: com.xcustom.puntoactivo.businessrules.SetAccount");
		com.integrador.xml.fedex.services.FedexLabelRequest xmlRequest = (com.integrador.xml.fedex.services.FedexLabelRequest) context.get("xmlRequest");
		log.info(_tab3+"Gettting xmlRequest - OK");
		AtributoService atributoService = (AtributoService) context.get("atributoService");
		log.info(_tab3+"Gettting atributoService - OK");
		Map<String,String> values = (Map<String,String>) context.get("account");
		log.info(_tab3+"Gettting account - OK");
		log.info(_tab3+"xmlRequest.getCuenta(): "+xmlRequest.getCuenta());
		if(xmlRequest.getCuenta()!=null && !xmlRequest.getCuenta().trim().equalsIgnoreCase("")) {
			int idCuenta = Integer.parseInt(xmlRequest.getCuenta().trim());
			String attrType = "Fedex"+idCuenta+"_Ship";
			log.info(_tab3+"Account to load: "+attrType);
			values = atributoService.getByTipoInMap(attrType);
			if(values.isEmpty()){
				log.info(_tab3+"WARNING! Empty Account "+values);
			}else{
				log.info(_tab3+"Account values: "+values);
			}
			context.put("account", values);
		}
		log.info("\t\tEnd BusinessRule: com.xcustom.puntoactivo.businessrules.SetAccount");
		return context;
	}
}
