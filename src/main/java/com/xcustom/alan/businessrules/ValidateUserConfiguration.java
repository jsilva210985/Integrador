package com.xcustom.alan.businessrules;
import java.io.Console;
import java.math.BigDecimal;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.integrador.util.AESAlgorithm;
import com.integrador.util.Util;
import com.integrador.carriers.Estafeta;
import com.integrador.exceptions.BusinessRuleException;
import com.integrador.models.UsuarioAlan;
import com.integrador.services.AtributoService;
import com.integrador.services.UsuariosService;
@SuppressWarnings("all")
@Component
public class ValidateUserConfiguration {

	static final Logger log = LoggerFactory.getLogger(com.xcustom.alan.businessrules.ValidateUserConfiguration.class);

	public static final String RANGE = "rango";
	public static final String KILADA = "kilada";
	public static final String PERCENTAGE = "porcentaje";
	public static final String EXPRESS = "Express";
	public static final String GROUND = "Terrestre";

	public Map<String,Object> run(Map<String,Object> context) {
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = (com.integrador.xml.services.EstafetaLabelRequest) context.get("xmlRequest");
		AtributoService atributoService = (AtributoService) context.get("atributoService");
		UsuariosService usuariosService = (UsuariosService) context.get("usuariosService");
		Map<String,String> values = (Map<String,String>) context.get("account");
		AESAlgorithm aes = new AESAlgorithm();
		String id = xmlRequest.getId();
		UsuarioAlan usuario = null;
		if(id==null){
			usuario = usuariosService.findUsuarioAlan(xmlRequest.getClient(), aes.encrypt(xmlRequest.getPassword()));
		}else{
			usuario = usuariosService.findUsuarioAlanById(id);
		}
		JSONObject cuentas = new JSONObject(usuario.getCuentasEstafeta() == null || usuario.getCuentasEstafeta().isEmpty() ? usuariosService.getJSONDefaultCuentasEstafeta() : usuario.getCuentasEstafeta());
		boolean hasPermission = false;
		String serviceType = xmlRequest.getServiceTypeId();
		String typeOfPayment = Util.EMPTY_STRING;
		String ranges = usuario.getPermisosRango();
		JSONObject permissionRanges = new JSONObject(ranges == null || ranges.trim().isEmpty() ? "{}" : ranges);
		if(xmlRequest.getWeight() == null || xmlRequest.getWeight().trim().isEmpty())
			throw new BusinessRuleException(-1, BusinessRuleMessages.WEIGHT_REQUIRED.getMessage());

		if(!xmlRequest.getWeight().matches("\\d+(\\.\\d+)?"))
			throw new BusinessRuleException(-1, BusinessRuleMessages.WEIGHT_INVALID_FORAMT.getMessage());
		if(serviceType.equalsIgnoreCase(GROUND)){
			hasPermission = usuario.getPermisoEstafetaTerrestre() == 1;
			typeOfPayment = usuario.getTipoCobroTerrestre();
		}
		if(serviceType.equalsIgnoreCase(EXPRESS)){
			hasPermission = usuario.getPermisoEstafetaExpress() == 1;
			typeOfPayment = usuario.getTipoCobroExpress();
		}
		if(!serviceType.equalsIgnoreCase(EXPRESS) && !serviceType.equalsIgnoreCase(GROUND)){
			sendException(-1,BusinessRuleMessages.NOT_SUPPORTED.getMessage() +"["+serviceType+"]");
		}
		if(typeOfPayment.equalsIgnoreCase(Util.EMPTY_STRING))
			sendException(-1,BusinessRuleMessages.TYPE_OF_PAYMENT_NOT_FOUND.getMessage());
		if(!hasPermission)
			throw new BusinessRuleException(serviceType.equalsIgnoreCase(GROUND) ? BusinessRuleMessages.NO_PERMISSION_TERRESTRE.getCode() : BusinessRuleMessages.NO_PERMISSION_EXPRESS.getCode(), serviceType.equalsIgnoreCase(GROUND) ? BusinessRuleMessages.NO_PERMISSION_TERRESTRE.getMessage() : BusinessRuleMessages.NO_PERMISSION_EXPRESS.getMessage() );
		int weight = Integer.parseInt(xmlRequest.getWeight());
		if(weight<=0)
			throw new BusinessRuleException(1,BusinessRuleMessages.WEIGHT_NOT_ALLOWED_ZERO.getMessage());
		if(weight>70)
			throw new BusinessRuleException(-1,BusinessRuleMessages.WEIGHT_NOT_ALLOWED_MAX.getMessage());
		if(typeOfPayment.equalsIgnoreCase(RANGE)){
			if(ranges == null || ranges.trim().isEmpty() || permissionRanges.length()==0)
				sendException(-1,BusinessRuleMessages.USER_RANGES_NOT_FOUND.getMessage());
			JSONObject serviceRangesNode;
			if(serviceType.equalsIgnoreCase(GROUND)) {
				serviceRangesNode = permissionRanges.optJSONObject("rangos_terrestre");
			}else{
				serviceRangesNode = permissionRanges.optJSONObject("rangos_express");
			}

			if(serviceRangesNode == null || serviceRangesNode.length() == 0)
				sendException(-1,String.format(BusinessRuleMessages.USER_RANGES_NOT_CONFIGURED.getMessage(),serviceType));

			//Build key
			String permissionKey = serviceType.equalsIgnoreCase(GROUND) ? "permiso_terrestre_rango_" + weight: "permiso_express_rango_" + weight;

			//Get value for key
			String hasWeightPermission = serviceRangesNode.optString(permissionKey, null);
			if(hasWeightPermission == null)
				sendException(-1,BusinessRuleMessages.USER_RANGES_NOT_FOUND_VALUE.getMessage());

			//Validate Permission
			if(!hasWeightPermission.equalsIgnoreCase("Si"))
				sendException(-1,String.format(BusinessRuleMessages.USER_NO_PERMISSION_WEIGHT.getMessage(),serviceType,weight));
		}
		return context;
	}

	private void sendException(int code, String msg){
		log.info("\t\tError: "+msg);
		log.info("");
		throw new BusinessRuleException(code,msg);
	}
}

