//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.3.2 
// Visite <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2026.04.10 a las 11:29:11 AM CDT 
//


package com.integrador.xml.services;

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
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="errorCodeDescriptionSPA" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="errorCodeDescriptionENG" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="trackingData" type="{http://www.integrador.com/xml/services}ArrayOfTrackingData"/&gt;
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
    "errorCode",
    "errorCodeDescriptionSPA",
    "errorCodeDescriptionENG",
    "trackingData"
})
@XmlRootElement(name = "ExecuteQueryResponse")
public class ExecuteQueryResponse {

    @XmlElement(required = true)
    protected String errorCode;
    @XmlElement(required = true)
    protected String errorCodeDescriptionSPA;
    @XmlElement(required = true)
    protected String errorCodeDescriptionENG;
    @XmlElement(required = true)
    protected ArrayOfTrackingData trackingData;

    /**
     * Obtiene el valor de la propiedad errorCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Define el valor de la propiedad errorCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorCode(String value) {
        this.errorCode = value;
    }

    /**
     * Obtiene el valor de la propiedad errorCodeDescriptionSPA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorCodeDescriptionSPA() {
        return errorCodeDescriptionSPA;
    }

    /**
     * Define el valor de la propiedad errorCodeDescriptionSPA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorCodeDescriptionSPA(String value) {
        this.errorCodeDescriptionSPA = value;
    }

    /**
     * Obtiene el valor de la propiedad errorCodeDescriptionENG.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorCodeDescriptionENG() {
        return errorCodeDescriptionENG;
    }

    /**
     * Define el valor de la propiedad errorCodeDescriptionENG.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorCodeDescriptionENG(String value) {
        this.errorCodeDescriptionENG = value;
    }

    /**
     * Obtiene el valor de la propiedad trackingData.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTrackingData }
     *     
     */
    public ArrayOfTrackingData getTrackingData() {
        return trackingData;
    }

    /**
     * Define el valor de la propiedad trackingData.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTrackingData }
     *     
     */
    public void setTrackingData(ArrayOfTrackingData value) {
        this.trackingData = value;
    }

}
