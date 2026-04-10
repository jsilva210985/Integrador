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
 * <p>Clase Java para Filter complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Filter"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="filterInformation" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="filterType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Filter", propOrder = {
    "filterInformation",
    "filterType"
})
public class Filter {

    @XmlElement(required = true)
    protected String filterInformation;
    @XmlElement(required = true)
    protected String filterType;

    /**
     * Obtiene el valor de la propiedad filterInformation.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilterInformation() {
        return filterInformation;
    }

    /**
     * Define el valor de la propiedad filterInformation.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilterInformation(String value) {
        this.filterInformation = value;
    }

    /**
     * Obtiene el valor de la propiedad filterType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilterType() {
        return filterType;
    }

    /**
     * Define el valor de la propiedad filterType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilterType(String value) {
        this.filterType = value;
    }

}
