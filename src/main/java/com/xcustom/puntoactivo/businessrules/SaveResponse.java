package com.xcustom.puntoactivo.businessrules;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.integrador.carriers.Fedex;
import com.integrador.services.AtributoService;
/**
 * <b>Punto Activo Business Rule SaveResponse</b><br/>
 * Process response after invoke fedex.<br/><br/>
 */
@SuppressWarnings("all")
public class SaveResponse {

	static final Logger log = LoggerFactory.getLogger(SaveResponse.class);
	static final String _tab3 = "\t\t\t";
	public Map<String,Object> run(Map<String,Object> context) {
		log.info("\t\tBusinessRule: com.xcustom.puntoactivo.businessrules.SaveResponse");
		com.fedex.ship.stub.ProcessShipmentReply fedexResponse = (com.fedex.ship.stub.ProcessShipmentReply) context.get("fedexresponse");
		String env = (String) context.get("env");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String fileName = sdf1.format(timestamp);
		String pathFiles = "";
		if(env.equalsIgnoreCase("Local")){
			pathFiles = "/Users/jesussilva/Documents/Temp/GeneradorPuntoActivo/Request/";
		}else if(env.equalsIgnoreCase("QA")){
			pathFiles = "/home/ubuntu/Generador/guiasrequestqa/";
		}else if(env.equalsIgnoreCase("PRD")){
			pathFiles = "/home/ubuntu/Generador/guiasrequest/";
		}
		/*
		 * Saving request
		 */
		try {
			String ruta = pathFiles+fileName+"_request.xml";
			File file = new File(ruta);
			if (!file.exists())
				file.createNewFile();
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(fedexResponse.getRequestXML().toString().trim());
			bw.close();
			log.info(_tab3+"Request file created: "+ruta);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*
		 * Saving response
		 */
		try {
			String ruta = pathFiles+fileName+"_response.xml";
			File file = new File(ruta);
			if (!file.exists())
				file.createNewFile();
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(fedexResponse.getResponseXML().toString().trim());
			bw.close();
			log.info(_tab3+"Response file created: "+ruta);
		} catch (Exception e) {
			e.printStackTrace();
		}

		log.info("\t\tEnd BusinessRule: com.xcustom.puntoactivo.businessrules.SaveResponse");
		return context;
	}
}
