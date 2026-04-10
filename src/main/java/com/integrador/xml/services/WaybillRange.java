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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para WaybillRange complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="WaybillRange"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="initialWaybill" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="finalWaybill" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
