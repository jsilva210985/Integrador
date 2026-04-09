//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.7 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2025.12.17 a las 04:15:23 PM CST 
//


package com.integrador.xml.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para WaybillRange complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="WaybillRange">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="initialWaybill" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="finalWaybill" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WaybillRange", propOrder = {
    "initialWaybill",
    "finalWaybill"
})
public class WaybillRange {

    @XmlElement(required = true)
    protected String initialWaybill;
    @XmlElement(required = true)
    protected String finalWaybill;

    /**
     * Obtiene el valor de la propiedad initialWaybill.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitialWaybill() {
        return initialWaybill;
    }

    /**
     * Define el valor de la propiedad initialWaybill.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitialWaybill(String value) {
        this.initialWaybill = value;
    }

    /**
     * Obtiene el valor de la propiedad finalWaybill.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinalWaybill() {
        return finalWaybill;
    }

    /**
     * Define el valor de la propiedad finalWaybill.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinalWaybill(String value) {
        this.finalWaybill = value;
    }

}
