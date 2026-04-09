package com.integrador.restcontroller.fedex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.integrador.repositories.GuiaIntegradorRepository;
import com.integrador.services.AtributoService;
import com.integrador.services.FedexService;
@RestController
@RequestMapping({"/Fedex"})
public class PickupRestController {
	@Autowired FedexService fedexService;
	@Autowired AtributoService atributoService;
	@Autowired GuiaIntegradorRepository guiaIntegradorRepository;
	static final Logger log = LoggerFactory.getLogger(PickupRestController.class);
}
