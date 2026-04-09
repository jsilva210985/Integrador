//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.7 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2025.12.17 a las 04:15:23 PM CST 
//


package com.integrador.xml.fedex.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="servicesTypes" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="outOfDeliveryArea" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "servicesTypes",
    "outOfDeliveryArea"
})
@XmlRootElement(name = "FedexServiceTypeResponse")
public class FedexServiceTypeResponse {

    @XmlElement(required = true)
    protected String servicesTypes;
    @XmlElement(required = true)
    protected String outOfDeliveryArea;

    /**
     * Obtiene el valor de la propiedad servicesTypes.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServicesTypes() {
        return servicesTypes;
    }

    /**
     * Define el valor de la propiedad servicesTypes.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServicesTypes(String value) {
        this.servicesTypes = value;
    }

    /**
     * Obtiene el valor de la propiedad outOfDeliveryArea.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutOfDeliveryArea() {
        return outOfDeliveryArea;
    }

    /**
     * Define el valor de la propiedad outOfDeliveryArea.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutOfDeliveryArea(String value) {
        this.outOfDeliveryArea = value;
    }

}
