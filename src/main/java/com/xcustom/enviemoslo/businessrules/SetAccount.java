package com.xcustom.enviemoslo.businessrules;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.integrador.carriers.Estafeta;
import com.integrador.services.AtributoService;
/**
* <b>Enviemoslo Business Rule SetAccount</b><br/>
* Depending of service kg is the account that the integrator will use.<br/><br/>
* <ul>
* 	<li>Terrestre, if kg is 19 or less use Ventro otherwise Barajas account</li>
* 	<li>Express uses Ventro</li>
* <ul><br/>
*/
@SuppressWarnings("all")
public class SetAccount {
	static final Logger log = LoggerFactory.getLogger(com.xcustom.alan.businessrules.SetAccount.class);
	public Map<String,Object> run(Map<String,Object> context) {
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = (com.integrador.xml.services.EstafetaLabelRequest) context.get("xmlRequest");
		AtributoService atributoService = (AtributoService) context.get("atributoService");
		Map<String,String> values = (Map<String,String>) context.get("account");
		String account = "Estafeta_Label_Rest";
		log.info("\t\tDefault Account: "+account);
		if(xmlRequest.getAccount()!=null){
			values = atributoService.getByTipoInMap(xmlRequest.getAccount());
			log.info("\t\tRequest Account: "+xmlRequest.getAccount());
		}
		if(values.isEmpty()){
			values = atributoService.getByTipoInMap(account);
		}
		context.put("account", values);
		return context;
	}
}
