package com.xcustom.alan.businessrules;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.integrador.services.AtributoService;
/**
* <b>Alan Business Rule SetAccount</b><br/>
* Determines which account configuration to load into the context based on the request information.<br/><br/>
* <ul>
*   <li>If the request specifies an account, the configuration is retrieved using that account name.</li>
*   <li>If no account is found or the retrieved configuration is empty, the default account <code>"Estafeta_Label_Rest"</code> is loaded.</li>
*   <li>The selected account data is stored in the context under the key <code>"account"</code>.</li>
* </ul><br/>
*/
@Component
public class SetAccount {

	static final Logger log = LoggerFactory.getLogger(com.xcustom.alan.businessrules.SetAccount.class);

	@SuppressWarnings("unchecked")
	public Map<String,Object> run(Map<String,Object> context) {
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = (com.integrador.xml.services.EstafetaLabelRequest) context.get("xmlRequest");
		AtributoService atributoService = (AtributoService) context.get("atributoService");
		Map<String,String> values = (Map<String,String>) context.get("account");
		String account = "Estafeta_Label_Rest";
		if(xmlRequest.getAccount()!=null){
			values = atributoService.getByTipoInMap(xmlRequest.getAccount());
			log.info("\t\tRequest Account: "+xmlRequest.getAccount());
		}
		if(values.isEmpty()){
			log.info("\t\tAccount not found");
			log.info("\t\tLoading Default Account: "+account);
			values = atributoService.getByTipoInMap(account);
		}
		context.put("account", values);
		return context;
	}
}
