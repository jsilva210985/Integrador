package com.xcustom.alan.businessrules;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.integrador.util.Util;
import com.estafeta.webservice.frecuencia.EstafetaFrecuencia;
import com.estafeta.webservice.frecuencia.EstafetaFrecuenciaCliente;
import com.integrador.services.AtributoService;

@Component
public class SetReexpedicion{
	static final Logger log = LoggerFactory.getLogger(com.xcustom.alan.businessrules.SetReexpedicion.class);
	public Map<String,Object> run(Map<String,Object> context) {
		com.integrador.xml.services.EstafetaLabelRequest xmlRequest = (com.integrador.xml.services.EstafetaLabelRequest) context.get("xmlRequest");
		AtributoService atributoService = (AtributoService) context.get("atributoService");
		Map<String,String> values = atributoService.getByTipoInMap("Estafeta_Frecuencia_Cotizador");
		EstafetaFrecuenciaCliente serviceEstafeta = new EstafetaFrecuenciaCliente();
		serviceEstafeta.setUrl(values.get("url").toString());
		serviceEstafeta.setId(values.get("id").toString());
		serviceEstafeta.setUsuario(values.get("usuario").toString());
		serviceEstafeta.setContrasena(values.get("contrasena").toString());
		serviceEstafeta.setOrigen(xmlRequest.getOrigenZipCode());
		serviceEstafeta.setDestino(xmlRequest.getZipCode());
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
			boolean reexpedicion = false;
			if(frecuenciaObj.getReexpedicion()!=null && !frecuenciaObj.getReexpedicion().equalsIgnoreCase("NO") && !frecuenciaObj.getReexpedicion().equalsIgnoreCase("SI") && !frecuenciaObj.getReexpedicion().equalsIgnoreCase(Util.EMPTY_STRING))
				reexpedicion = true;
			xmlRequest.setReexpedicion(reexpedicion ? "1" : "0");
			log.info("\t\tReexpedicion: " + (reexpedicion ? "Si" : "No") );
			context.put("xmlRequest", xmlRequest);
		}catch (Exception e) {
			return null;
		}
		return context;
	}
}
