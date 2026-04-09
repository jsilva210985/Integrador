package com.xcustom.alan.businessrules;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SetLabelPath{
	@Value("${app.estafeta.path}") String pathLabel;
	static final Logger log = LoggerFactory.getLogger(com.xcustom.alan.businessrules.SetLabelPath.class);
	public Map<String,Object> run(Map<String,Object> context) {
		context.put("pathlabel", pathLabel);
		return context;
	}
}
