package com.integrador.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import com.integrador.services.EstafetaService;
import com.integrador.services.FedexService;
import com.integrador.xml.services.EstafetaFrecuenciaCotizadorRequest;
import com.integrador.xml.services.EstafetaFrecuenciaCotizadorResponse;

@Endpoint
public class IntegradorEndpoint {

	private static final String NAMESPACE_URI = "http://www.integrador.com/xml/services";
	private static final String NAMESPACE_URI_FEDEX = "http://www.integrador.com/xml/fedex/services";

	@Autowired EstafetaService estafetaService;
	@Autowired FedexService fedexService;

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "EstafetaFrecuenciaCotizadorRequest")
	@ResponsePayload
	public EstafetaFrecuenciaCotizadorResponse executeFrecuenciaCotizadorWS(@RequestPayload EstafetaFrecuenciaCotizadorRequest request) throws Exception {
		return estafetaService.executeFrecuenciaCotizadorWS(request);
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "EstafetaLabelRequest")
	@ResponsePayload
	public com.integrador.xml.services.EstafetaLabelResponse executeLabelWS(@RequestPayload com.integrador.xml.services.EstafetaLabelRequest request) throws Exception {
		return estafetaService.executeLabelWS(request);
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "ExecuteQuery")
	@ResponsePayload
	public com.integrador.xml.services.ExecuteQueryResponse executeConsultaWS(@RequestPayload com.integrador.xml.services.ExecuteQuery request) throws Exception {
		return estafetaService.executeConsultaEnviosWS(request);
	}

	@PayloadRoot(namespace = NAMESPACE_URI_FEDEX, localPart = "FedexLabelRequest")
	@ResponsePayload
	public com.integrador.xml.fedex.services.FedexLabelResponse executeFedexLabel(@RequestPayload com.integrador.xml.fedex.services.FedexLabelRequest request) throws Exception {
		return fedexService.executeFedexLabel(request);
	}

	@PayloadRoot(namespace = NAMESPACE_URI_FEDEX, localPart = "FedexServiceTypeRequest")
	@ResponsePayload
	public com.integrador.xml.fedex.services.FedexServiceTypeResponse executeRateServiceType(@RequestPayload com.integrador.xml.fedex.services.FedexServiceTypeRequest request) throws Exception {
		return fedexService.executeRateServiceType(request);
	}
}