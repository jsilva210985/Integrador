package com.integrador.restcontroller.fedex;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.integrador.models.Usuario;
import com.integrador.repositories.TokenRepository;
import com.integrador.repositories.UsuarioRepository;
import com.integrador.services.AtributoService;
import com.integrador.util.AESAlgorithm;
@RestController
@RequestMapping({"/Fedex"})
public class CartaPorteRestController {
	@Autowired TokenRepository tokenRepository;
	@Autowired AtributoService atributoService;
	@Autowired UsuarioRepository usuarioRepository;
	static final Logger log = LoggerFactory.getLogger(CartaPorteRestController.class);
	@PostMapping(value = "/CartaPorte")
	public ResponseEntity<String> cartaPorte(HttpServletRequest request, @RequestBody String content) throws Exception{
		JSONObject response = new JSONObject();
		JSONObject cartaPorte = new JSONObject();
		String error = "";
		log.info("");
		log.info("== Fedex RestService / Carta Porte ==>");
		try {
			cartaPorte = new JSONObject(content);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		if(!cartaPorte.has("cliente")) {
			error = "Debe especificar el cliente";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!cartaPorte.has("password")) {
			error = "Debe especificar la contraseña";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!cartaPorte.has("token")) {
			error = "Debe especificar el token";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		AESAlgorithm e = new AESAlgorithm();
		Usuario usuario = usuarioRepository.findByUsuarioAndContrasena(String.valueOf(cartaPorte.get("cliente")), e.encrypt(String.valueOf(cartaPorte.get("password"))));
		if(usuario==null) {
			error = "Usuario o Contraseña invalido";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		com.integrador.models.Token token = tokenRepository.findByIdUsuarioAndToken(usuario.getIdUsuario(), String.valueOf(cartaPorte.get("token")));
		if(token==null) {
			error = "Token invalido";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		cartaPorte.remove("cliente");
		cartaPorte.remove("password");
		cartaPorte.remove("token");
		Map<String,String> params = atributoService.getByTipoInMap("FedexCartaPorte");
		URL url = new URL(params.get("url").toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type","application/json");
		OutputStream os = conn.getOutputStream();
		os.write(cartaPorte.toString().getBytes(Charset.forName("UTF-8")));
		os.flush();
		StringBuffer r = new StringBuffer();
		BufferedReader in;
		if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 299) {
			in = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		} else {
			in = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
		}
		try{
			String output;
			while ((output = in.readLine()) != null) {
				r.append(output);
			}
			in.close();
			response = new JSONObject(r.toString());
		}catch (Exception e1) {
			e1.printStackTrace();
		}
		log.info("\tResponse: "+response.toString());
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}
}
