//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.3.2 
// Visite <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2026.04.10 a las 11:29:11 AM CDT 
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
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="servicesTypes" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="outOfDeliveryArea" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
