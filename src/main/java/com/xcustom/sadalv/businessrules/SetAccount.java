package com.xcustom.sadalv.businessrules;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.integrador.carriers.Estafeta;
import com.integrador.models.Usuario;
import com.integrador.services.AtributoService;
/**
* <b>Sadalv Business Rule SetAccount</b><br/>
* Depending of service type is the account that the integrator will use.<br/><br/>
* <ul>
* 	<li>Terrestre uses REFACCIONARIA ALEMANA DE IMPORTACION SA DE CV</li>
* 	<li>Express uses ERICK EDUARDO HERNANDEZ VERA</li>
* <ul><br/>
*/
@SuppressWarnings("all")
public class SetAccount {
	
	public static final String CUENTA_DEFAULT = "Estafeta_Label_Rest";
	static final Logger log = LoggerFactory.getLogger(SetAccount.class);
	public Map<String,Object> run(Map<String,Object> context) {
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = (com.integrador.xml.services.EstafetaLabelRequest) context.get("xmlRequest");
		AtributoService atributoService = (AtributoService) context.get("atributoService");
		String cuenta = CUENTA_DEFAULT;
		Map<String,String> values = (Map<String,String>) context.get("account");
		values = atributoService.getByTipoInMap(cuenta);
		String usuario = xmlRequest.getClient();
		Usuario user = atributoService.findUsuario(usuario);
		if(user!=null){
			if(xmlRequest.getServiceTypeId().equalsIgnoreCase("Terrestre")) {
				cuenta = user.getCuentaEstafeta()==null || user.getCuentaEstafeta().equalsIgnoreCase("") ? CUENTA_DEFAULT : user.getCuentaEstafeta().trim();
				if(cuenta.equalsIgnoreCase("Estafeta_Label_Rest_Default"))
					cuenta = CUENTA_DEFAULT;
			}else{
				cuenta = user.getCuentaEstafetaExpress()==null || user.getCuentaEstafetaExpress().equalsIgnoreCase("") ? CUENTA_DEFAULT : user.getCuentaEstafetaExpress().trim();
				if(cuenta.equalsIgnoreCase("Estafeta_Label_Rest_Default"))
					cuenta = CUENTA_DEFAULT;
			}
		}
		values = atributoService.getByTipoInMap(cuenta);
		log.info("\tBusinessRule: Setting account:"+cuenta);
		context.put("account", values);
		return context;
	}
}
