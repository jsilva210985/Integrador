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
	/**
	 * 
	 * @param context
	 * @return
	 */
	public Map<String,Object> run(Map<String,Object> context) {
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = (com.integrador.xml.services.EstafetaLabelRequest) context.get("xmlRequest");
		AtributoService atributoService = (AtributoService) context.get("atributoService");
		/*
		 * Cuenta Default
		 */
		String cuenta = CUENTA_DEFAULT;
		Map<String,String> values = (Map<String,String>) context.get("account");
		values = atributoService.getByTipoInMap(cuenta);
		/*
		 * Logica previa, dependiendo si es terrestre o express se elegia una cuenta
		 */
		/*
		if(xmlRequest.getServiceTypeId().equalsIgnoreCase("Terrestre")) {
			values = atributoService.getByTipoInMap("Estafeta_Label_Rest");
		}else {
			values = atributoService.getByTipoInMap("Estafeta_Label_Rest_2");
		}
		*/
		
		/*
		 * Regla: La cuenta de estafeta se determina de la que se asigno en el perfil de usuario de la consola del integrador.
		 * Debemos cargar el usuario que tenemos en el request y de alli sacarle su cuenta asignada, de no tener una 
		 * se asgina cuenta por default Estafeta_Label_Rest.
		 */
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
