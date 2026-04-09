package com.integrador.restcontroller.app;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.integrador.services.AppService;

@RestController
@RequestMapping({"/App"})
public class AppRestController {
	@Autowired AppService appService;
	static final Logger log = LoggerFactory.getLogger(AppRestController.class);
	@GetMapping(value = "/Version")
	public ResponseEntity<String> version() throws Exception{
		JSONObject response = new JSONObject();
		response.put("Version", appService.getVersion());
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}
}
