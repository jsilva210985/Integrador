package com.integrador.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AppService {

	@Value("${application.version}") String version;

	static final Logger log = LoggerFactory.getLogger(AppService.class);
	public String getVersion() throws Exception{
		return version;
	}
}
