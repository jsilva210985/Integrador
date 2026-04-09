package com.integrador.app;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class Config extends WsConfigurerAdapter {
	@Bean
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(servlet, "/Service/*");
	}

	@Bean(name = "integradorWsdl")
	public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema integradorSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("IntegradorDetailsPort");
		wsdl11Definition.setLocationUri("/Service/Integrador");
		wsdl11Definition.setTargetNamespace("http://www.integrador.com/xml/services");
		wsdl11Definition.setSchema(integradorSchema);
		return wsdl11Definition;
	}

	@Bean(name = "fedexWsdl")
	public DefaultWsdl11Definition fedexDefinition(XsdSchema fedexSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("IntegradorDetailsPort");
		wsdl11Definition.setLocationUri("/Service/Integrador");
		wsdl11Definition.setTargetNamespace("http://www.integrador.com/xml/fedex/services");
		wsdl11Definition.setSchema(fedexSchema);
		return wsdl11Definition;
	}

	@Bean
	public XsdSchema integradorSchema() {
		return new SimpleXsdSchema(new ClassPathResource("webservice/estafeta.xsd"));
	}

	@Bean
	public XsdSchema fedexSchema() {
		return new SimpleXsdSchema(new ClassPathResource("webservice/fedex.xsd"));
	}
}