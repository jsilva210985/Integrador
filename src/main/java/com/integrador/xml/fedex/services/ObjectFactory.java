//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.7 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2025.12.17 a las 04:15:23 PM CST 
//


package com.integrador.xml.fedex.services;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.integrador.xml.fedex.services package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.integrador.xml.fedex.services
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FedexServiceTypeResponse }
     * 
     */
    public FedexServiceTypeResponse createFedexServiceTypeResponse() {
        return new FedexServiceTypeResponse();
    }

    /**
     * Create an instance of {@link FedexServiceTypeRequest }
     * 
     */
    public FedexServiceTypeRequest createFedexServiceTypeRequest() {
        return new FedexServiceTypeRequest();
    }

    /**
     * Create an instance of {@link FedexLabelRequest }
     * 
     */
    public FedexLabelRequest createFedexLabelRequest() {
        return new FedexLabelRequest();
    }

    /**
     * Create an instance of {@link FedexLabelResponse }
     * 
     */
    public FedexLabelResponse createFedexLabelResponse() {
        return new FedexLabelResponse();
    }

}
