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
 * <p>Clase Java para WaybillReplaceData complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="WaybillReplaceData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="originalWaybill" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="replaceWaybill" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WaybillReplaceData", propOrder = {
    "originalWaybill",
    "replaceWaybill"
})
public class WaybillReplaceData {

    @XmlElement(required = true)
    protected String originalWaybill;
    @XmlElement(required = true)
    protected String replaceWaybill;

    /**
     * Obtiene el valor de la propiedad originalWaybill.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalWaybill() {
        return originalWaybill;
    }

    /**
     * Define el valor de la propiedad originalWaybill.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalWaybill(String value) {
        this.originalWaybill = value;
    }

    /**
     * Obtiene el valor de la propiedad replaceWaybill.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReplaceWaybill() {
        return replaceWaybill;
    }

    /**
     * Define el valor de la propiedad replaceWaybill.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReplaceWaybill(String value) {
        this.replaceWaybill = value;
    }

}
