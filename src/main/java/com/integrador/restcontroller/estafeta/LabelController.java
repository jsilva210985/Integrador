package com.integrador.restcontroller.estafeta;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.estafeta.webservice.frecuencia.EstafetaFrecuencia;
import com.estafeta.webservice.frecuencia.EstafetaFrecuenciaCliente;
import com.integrador.exceptions.BusinessRuleException;
import com.integrador.models.Configuracion;
import com.integrador.models.GuiaIntegrador;
import com.integrador.models.UsuarioAlan;
import com.integrador.repositories.ConfiguracionRepository;
import com.integrador.repositories.GuiaIntegradorRepository;
import com.integrador.repositories.TokenRepository;
import com.integrador.repositories.UsuarioAlanRepository;
import com.integrador.services.AtributoService;
import com.integrador.services.UsuariosService;
import com.integrador.util.AESAlgorithm;

@RestController
@RequestMapping({"/Estafeta"})
public class LabelController{

	@Autowired TokenRepository tokenRepository;
	@Autowired AtributoService atributoService;
	@Autowired UsuariosService usuariosService;
	@Autowired UsuarioAlanRepository usuarioAlanRepository;
	@Autowired GuiaIntegradorRepository guiasIntegradorRepository;
	@Autowired ConfiguracionRepository configuracionRepository;

	@Autowired
	@Qualifier("estafetaLabelControllerV2")
	com.integrador.restcontroller.estafetav2.LabelController labelControllerV2;

	@Value("${estafeta.restservice.br.beforesend}") String beforeSendBR;

	static final Logger log = LoggerFactory.getLogger(LabelController.class);

	@PostMapping(value = "/LabelV2")
	public ResponseEntity<String> labelv2(HttpServletRequest request, @RequestBody String content) throws Exception{
		log.info("");
		log.info("== Triangulando petición /Estafeta/LabelV2 -> EstafetaRest V2 ==");
		return labelControllerV2.labelv3(request, content);
	}

	public String getAccount(String origen, String destino, String peso, String tipoGuia, UsuarioAlan usuario) throws Exception, IOException{
		Map<String,String> values = atributoService.getByTipoInMap("Estafeta_Frecuencia_Cotizador");

		EstafetaFrecuenciaCliente serviceEstafeta = new EstafetaFrecuenciaCliente();
		serviceEstafeta.setUrl(values.get("url").toString());
		serviceEstafeta.setId(values.get("id").toString());
		serviceEstafeta.setUsuario(values.get("usuario").toString());
		serviceEstafeta.setContrasena(values.get("contrasena").toString());

		serviceEstafeta.setOrigen(origen);
		serviceEstafeta.setDestino(destino);
		serviceEstafeta.setEsFrecuencia("true");
		serviceEstafeta.setEsLista("true");
		serviceEstafeta.setEsPaquete("false");
		serviceEstafeta.setLargo("0");
		serviceEstafeta.setPeso("0");
		serviceEstafeta.setAlto("0");
		serviceEstafeta.setAncho("0");

		EstafetaFrecuencia frecuenciaObj = null;
		try {
			frecuenciaObj = serviceEstafeta.getFrecuencia();
		}catch (Exception e) {
			return null;
		}

		boolean reexpedicion = false;
		if(frecuenciaObj.getReexpedicion()!=null && !frecuenciaObj.getReexpedicion().equalsIgnoreCase("NO") && !frecuenciaObj.getReexpedicion().equalsIgnoreCase("SI") && !frecuenciaObj.getReexpedicion().equalsIgnoreCase("")) {
			reexpedicion = true;
		}
		String cuenta = "";
		String cuentaDefault = "cuenta_default";
		String kiladaTerrestre5K = "kilada_terrestre_5kg";
		String kiladaTerrestreOtros = "kilada_terrestre_otros";
		String kiladaExpress1K = "kilada_express_1kg";
		String kiladaExpressOtros = "kilada_express_otros";
		String rangoExpress1K = "rango_express_1kg";
		double pesoDouble = 0.0;
		try {
			pesoDouble = Double.parseDouble(peso);
		} catch (Exception ex) {
			pesoDouble = 1.0;
		}
		int kilos = (int) Math.round(pesoDouble);
		if (kilos == 0) kilos = 1;
		JSONObject cuentas = usuario.getCuentasEstafeta()==null ? new JSONObject() : new JSONObject(usuario.getCuentasEstafeta());
		if(!reexpedicion){
			if(tipoGuia.equalsIgnoreCase("Terrestre")){
				if(usuario.getTipoCobroTerrestre().equalsIgnoreCase("kilada")){
					if(kilos<=5){
						cuenta = !String.valueOf(cuentas.get(kiladaTerrestre5K)).equalsIgnoreCase("") ? String.valueOf(cuentas.get(kiladaTerrestre5K)) : String.valueOf(cuentas.get(cuentaDefault));
					}else{
						if(cuentas.has(kiladaTerrestreOtros) && !String.valueOf(cuentas.get(kiladaTerrestreOtros)).equalsIgnoreCase("")){
							cuenta = String.valueOf(cuentas.get(kiladaTerrestreOtros));
						}else{
							cuenta = String.valueOf(cuentas.get(cuentaDefault));
						}
					}
				}else if(usuario.getTipoCobroTerrestre().equalsIgnoreCase("rango")){
					String cuentaXKG = "rango_terrestre_"+kilos+"kg";
					if(cuentas.has(cuentaXKG) && !cuentas.get(cuentaXKG).toString().equalsIgnoreCase("")){
						cuenta = !String.valueOf(cuentas.get(cuentaXKG)).equalsIgnoreCase("") ? String.valueOf(cuentas.get(cuentaXKG)) : String.valueOf(cuentas.get(cuentaDefault));
					}
				}
			}else if(tipoGuia.equalsIgnoreCase("Express")){
				if(usuario.getTipoCobroExpress().equalsIgnoreCase("kilada")){
					if(kilos==1){
						cuenta = !String.valueOf(cuentas.get(kiladaExpress1K)).equalsIgnoreCase("") ? String.valueOf(cuentas.get(kiladaExpress1K)) : String.valueOf(cuentas.get(cuentaDefault));
					}else{
						if(cuentas.has(kiladaExpressOtros) && !String.valueOf(cuentas.get(kiladaExpressOtros)).equalsIgnoreCase("")){
							cuenta = String.valueOf(cuentas.get(kiladaExpressOtros));
						}else{
							cuenta = String.valueOf(cuentas.get(cuentaDefault));
						}
					}
				}else if(usuario.getTipoCobroExpress().equalsIgnoreCase("rango")) {
					if(kilos==1){
						cuenta = !String.valueOf(cuentas.get(rangoExpress1K)).equalsIgnoreCase("") ? String.valueOf(cuentas.get(rangoExpress1K)) : String.valueOf(cuentas.get(cuentaDefault));
					}else{
						String cuentaXKG = "rango_express_"+kilos+"kg";
						if(cuentas.has(cuentaXKG) && !cuentas.get(cuentaXKG).toString().equalsIgnoreCase("")){
							cuenta = !String.valueOf(cuentas.get(cuentaXKG)).equalsIgnoreCase("") ? String.valueOf(cuentas.get(cuentaXKG)) : String.valueOf(cuentas.get(cuentaDefault));
						}
					}
				}
			}
		}else {
			cuenta = String.valueOf(cuentas.get(cuentaDefault));
			Configuracion conf = null;
			if(tipoGuia.equalsIgnoreCase("Express")){
				conf = configuracionRepository.findByTipoAndServicio("reexpedicion", "express");
			}else{
				conf = configuracionRepository.findByTipoAndServicio(getReexpedicionValue(kilos), "terrestre");
			}
			if(conf!=null)
				cuenta = conf.getCuenta();
		}
		if(tipoGuia.equalsIgnoreCase("Terrestre")){
			if(cuenta.equalsIgnoreCase("algoritmo_rentabilidad")){
				Configuracion conf = null;
				if(kilos>=0 && kilos<=10) {
					conf = configuracionRepository.findByTipoAndServicio("algoritmo_5_10", "terrestre");
					if(conf==null){
						if(cuenta==null || cuenta.equalsIgnoreCase("") || cuenta.equalsIgnoreCase("algoritmo_rentabilidad")){
							cuenta = cuentaDefault;
						}
					}else{
						if(conf.getCuenta()!=null && !conf.getCuenta().equalsIgnoreCase("")){
							cuenta = conf.getCuenta();
						}
					}
				}
				if(kilos>10 && kilos<=15) {
					conf = configuracionRepository.findByTipoAndServicio("algoritmo_11_15", "terrestre");
					if(conf==null){
						if(cuenta==null || cuenta.equalsIgnoreCase("") || cuenta.equalsIgnoreCase("algoritmo_rentabilidad")){
							cuenta = cuentaDefault;
						}
					}else{
						if(conf.getCuenta()!=null && !conf.getCuenta().equalsIgnoreCase("")){
							cuenta = conf.getCuenta();
						}
					}
				}
				if(kilos>15 && kilos<=70) {
					conf = configuracionRepository.findByTipoAndServicio("algoritmo_16_70", "terrestre");
					if(conf==null){
						if(cuenta==null || cuenta.equalsIgnoreCase("") || cuenta.equalsIgnoreCase("algoritmo_rentabilidad")){
							cuenta = cuentaDefault;
						}
					}else{
						if(conf.getCuenta()!=null && !conf.getCuenta().equalsIgnoreCase("")){
							cuenta = conf.getCuenta();
						}
					}
				}
			}
		}
		if(reexpedicion){
			if(tipoGuia.equalsIgnoreCase("Terrestre")){
				if(cuentas.has("tiene_cuentadirecta_reexpedicion_terreste")){
					if(cuentas.getInt("tiene_cuentadirecta_reexpedicion_terreste")==1){
						cuenta = cuentas.getString("cuentadirecta_reexpedicion_terreste");
					}
				}
			}
			if(tipoGuia.equalsIgnoreCase("Express")){
				if(cuentas.has("tiene_cuentadirecta_reexpedicion_express")){
					if(cuentas.getInt("tiene_cuentadirecta_reexpedicion_express")==1){
						cuenta = cuentas.getString("cuentadirecta_reexpedicion_express");
					}
				}
			}
		}
		return cuenta;
	}

	public static String getReexpedicionValue(double number) {
		int roundedNumber = roundToNextMultipleOfFive(number);
		if(roundedNumber <= 5) {
			return "reexpedicion_5";
		}else{
			return "reexpedicion_" + roundedNumber;
		}
	}

	public static int roundToNextMultipleOfFive(double number) {
		int roundedNumber = (int) Math.ceil(number / 5) * 5;
		return (roundedNumber == 0) ? 5 : roundedNumber;
	}

	public GuiaIntegrador saveRequest(JSONObject params) {
		String numeroExteriorRemitente = params.optString("origenExtNum", "");
		String numeroInteriorRemitente = params.optString("origenIntNum", "");
		String referenceRemitente = params.optString("origenReference", "");
		GuiaIntegrador g = new GuiaIntegrador();
		g.setRemitenteCalle(params.optString("origenAddress1", ""));
		g.setRemitenteColonia(params.optString("origenNeighborhood", ""));
		g.setRemitenteCP(params.optString("origenZipCode", ""));
		g.setRemitenteNombre(params.optString("origenContactName", ""));
		g.setRemitenteEstado(params.optString("origenState", ""));
		g.setRemitenteMunicipio(params.optString("origenCity", ""));
		g.setRemitenteReferencia(referenceRemitente);
		g.setRemitenteTelefono(params.optString("origenPhoneNumber", ""));
		g.setRemitenteNumeroExterior(numeroExteriorRemitente);
		g.setRemitenteNumeroInterior(numeroInteriorRemitente);
		String numeroExteriorDestinatario = params.optString("extNum", "");
		String numeroInteriorDestinatario = params.optString("intNum", "");
		String referenceDestinatario = params.optString("reference", "");
		g.setDestinatarioCalle(params.optString("address1", ""));
		g.setDestinatarioColonia(params.optString("neighborhood", ""));
		g.setDestinatarioCP(params.optString("zipCode", ""));
		g.setDestinatarioNombre(params.optString("contactName", ""));
		g.setDestinatarioEstado(params.optString("state", ""));
		g.setDestinatarioMunicipio(params.optString("city", ""));
		g.setDestinatarioReferencia(referenceDestinatario);
		g.setDestinatarioTelefono(params.optString("phoneNumber", ""));
		g.setDestinatarioNumeroExterior(numeroExteriorDestinatario);
		g.setDestinatarioNumeroInterior(numeroInteriorDestinatario);
		g.setInformacionAdicional(params.optString("aditionalInfo", ""));
		g.setTipoGuia(params.optString("serviceTypeId", ""));
		g.setContenido(params.optString("content", ""));
		g.setEtiquetas(params.optString("numberOfLabels", "1"));
		g.setTipoContenido(params.optString("parcelTypeId", ""));
		g.setKilos(params.optString("weight","0.0"));
		g.setCliente(params.optString("client", ""));
		g.setTracking("");
		g.setFechaCreacion(com.integrador.util.Util.getCurrentDataTimeMexico());
		g.setRequest("");
		return guiasIntegradorRepository.save(g);
	}
}
