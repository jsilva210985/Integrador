package com.integrador.restcontroller.dhl;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.integrador.models.Usuario;
import com.integrador.repositories.GuiaIntegradorRepository;
import com.integrador.repositories.TokenRepository;
import com.integrador.repositories.UsuarioRepository;
import com.integrador.services.AtributoService;
import com.integrador.services.UsuariosService;
import com.integrador.util.AESAlgorithm;
@RestController
@RequestMapping({"/DHL"})
public class Rate{

	@Autowired TokenRepository tokenRepository;
	@Autowired AtributoService atributoService;
	@Autowired UsuariosService usuariosService;
	@Autowired UsuarioRepository usuarioRepository;
	@Autowired GuiaIntegradorRepository guiasIntegradorRepository;

	static final Logger log = LoggerFactory.getLogger(Rate.class);

	@PostMapping(value = "/Rate")
	public ResponseEntity<String> rate(HttpServletRequest request, @RequestBody String content) throws Exception{
		JSONObject response = new JSONObject();
		JSONObject requestBody = new JSONObject();
		String error = "";
		log.info("");
		log.info("== DHL / Rate ==>");
		try {
			requestBody = new JSONObject(content);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		if(!requestBody.has("client")) {
			error = "Debe especificar el cliente";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!requestBody.has("password")) {
			error = "Debe especificar la contraseña";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if(!requestBody.has("token")) {
			error = "Debe especificar el token";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		AESAlgorithm e = new AESAlgorithm();
		Usuario usuario = usuarioRepository.findByUsuarioAndContrasena(String.valueOf(requestBody.get("client")), e.encrypt(String.valueOf(requestBody.get("password"))));
		if(usuario==null) {
			error = "Usuario o Contraseña invalido";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		com.integrador.models.Token token = tokenRepository.findByIdUsuarioAndToken(usuario.getIdUsuario(), String.valueOf(requestBody.get("token")));
		if(token==null) {
			error = "Token invalido";
			log.info("\t"+error);
			response.put("validation", error);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		requestBody.remove("client");
		requestBody.remove("password");
		requestBody.remove("token");
		com.integrador.carriers.DHL dhl = new com.integrador.carriers.DHL();
		dhl.setAccount(atributoService.getByTipoInMap("DHL_Rate"));
		dhl.setXmlRequest(null);
		dhl.setXmlResponse(null);
		dhl.setGuiaIntegradorRepository(guiasIntegradorRepository);
		dhl.setGuiaIntegrador(null);
		dhl.setAtributoService(atributoService);
		dhl.setUsuariosService(usuariosService);
		dhl.setBusinessRuleBeforeSend("");
		JSONObject rateResponse = (JSONObject) dhl.performRate(requestBody);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(rateResponse.toString(),headers, HttpStatus.OK);
	}
}
