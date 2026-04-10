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
 * <p>Clase Java para HistoryConfiguration complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="HistoryConfiguration"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="includeHistory" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="historyType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HistoryConfiguration", propOrder = {
    "includeHistory",
    "historyType"
})
public class HistoryConfiguration {

    @XmlElement(required = true)
    protected String includeHistory;
    @XmlElement(required = true)
    protected String historyType;

    /**
     * Obtiene el valor de la propiedad includeHistory.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncludeHistory() {
        return includeHistory;
    }

    /**
     * Define el valor de la propiedad includeHistory.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncludeHistory(String value) {
        this.includeHistory = value;
    }

    /**
     * Obtiene el valor de la propiedad historyType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHistoryType() {
        return historyType;
    }

    /**
     * Define el valor de la propiedad historyType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHistoryType(String value) {
        this.historyType = value;
    }

}
