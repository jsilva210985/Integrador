package com.integrador.restcontroller.estafeta;
import java.util.Map;
import org.estafeta.restconnector.oauth.AuthenticatorClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.integrador.repositories.TokenRepository;
import com.integrador.repositories.UsuarioRepository;
import com.integrador.services.AtributoService;
@RestController
@RequestMapping({"/Estafeta"})
public class TokenController {
	@Autowired TokenRepository tokenRepository;
	@Autowired AtributoService atributoService;
	@Autowired UsuarioRepository usuarioRepository;
	static final Logger log = LoggerFactory.getLogger(TokenController.class);
	@GetMapping(value = "/Token")
	public ResponseEntity<String> token() throws Exception{
		log.info("");
		log.info("== Estafeta RestService / Token ==>");
		JSONObject response = new JSONObject();
		Map<String,String> params = atributoService.getByTipoInMap("Estafeta_Frecuencia_Cotizador_Rest");
		String _apiKey = params.get("api_key");
		String _apiSecret = params.get("api_secret");
		String _urlToken = params.get("url_token");
		AuthenticatorClient auth = new AuthenticatorClient();
		auth.setAuthURL(_urlToken);
		auth.setClientId(_apiKey);
		auth.setClientSecret(_apiSecret);
		org.estafeta.restconnector.models.Token _token = auth.getToken();
		response.put("token", _token.getToken());
		log.info("\tToken: "+_token.getToken());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(response.toString(),headers, HttpStatus.OK);
	}
}
