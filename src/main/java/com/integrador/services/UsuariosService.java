package com.integrador.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.integrador.models.UsuarioAlan;
import com.integrador.repositories.CuentaEstafetaRepository;
import com.integrador.repositories.CuentaEstafetaV2Repository;
import com.integrador.repositories.UsuarioAlanRepository;

@Service
public class UsuariosService {

	@Autowired MessageSource messageSource;
	@Autowired UsuarioAlanRepository usuarioAlanRepository;
	@Autowired CuentaEstafetaV2Repository cuentaEstafetaV2Repository;
	@Autowired CuentaEstafetaRepository cuentaEstafetaRepository;
	public UsuarioAlan findUsuarioAlan(String usuario, String contrasena) {
		return usuarioAlanRepository.findByUsuarioAndContrasena(usuario, contrasena);
	}
	public UsuarioAlan findUsuarioAlanById(String id) {
		return usuarioAlanRepository.findByIdUsuario(Integer.parseInt(id));
	}
	public boolean verificaUsoKilos(JSONObject cuentas, String cuentaKey, String servicioKey, String arregloServicios) {
		try {
			com.integrador.models.CuentaEstafetaV2 cV2 = getCuentaV2(cuentas.getString(cuentaKey));
			JSONObject confv2 = new JSONObject(cV2.getConfiguracion());
			String service = cuentas.getString(servicioKey);
			int servicioBuscado = Integer.parseInt(service);
			JSONArray servicios = confv2.getJSONArray(arregloServicios);

			for (int i = 0; i < servicios.length(); i++) {
				JSONObject servicioObj = servicios.getJSONObject(i);
				if (servicioObj.getInt("servicio") == servicioBuscado) {
					return servicioObj.optInt("usar_kilos", 0) == 1;
				}
			}
		} catch (Exception e) {
			System.err.println("Error verificando uso de kilos: " + e.getMessage());
		}
		return false;
	}
	public com.integrador.models.CuentaEstafetaV2 getCuentaV2(String name){
		return findByCuentaV2(name);
	}
	public com.integrador.models.CuentaEstafeta getCuenta(String name){
		return findByCuenta(name);
	}
	public com.integrador.models.CuentaEstafetaV2 findByCuentaV2(String cuenta){
		return cuentaEstafetaV2Repository.findByCuenta(cuenta);
	}
	public com.integrador.models.CuentaEstafeta findByCuenta(String cuenta){
		return cuentaEstafetaRepository.findByCuenta(cuenta);
	}
	public String getJSONDefaultCuentasEstafeta(){
		JSONObject o = new JSONObject();
		o.put("cuenta_default", "Estafeta_Label_Rest");
		o.put("kilada_express_1kg", "");
		o.put("kilada_terrestre_5kg", "");
		o.put("rango_express_1kg", "");
		o.put("rango_terrestre_5kg", "");
		o.put("tiene_cuentadirecta_reexpedicion_terreste", "");
		o.put("tiene_cuentadirecta_reexpedicion_express", "");
		o.put("cuentadirecta_reexpedicion_terreste", "");
		o.put("cuentadirecta_reexpedicion_express", "");
		return o.toString();
	}
}