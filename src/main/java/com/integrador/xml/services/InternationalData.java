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
 * <p>Clase Java para InternationalData complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="InternationalData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="internationalWaybill" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="originCountryCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="originCountrySPA" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="originCountryENG" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InternationalData", propOrder = {
    "internationalWaybill",
    "originCountryCode",
    "originCountrySPA",
    "originCountryENG"
})
public class InternationalData {

    @XmlElement(required = true)
    protected String internationalWaybill;
    @XmlElement(required = true)
    protected String originCountryCode;
    @XmlElement(required = true)
    protected String originCountrySPA;
    @XmlElement(required = true)
    protected String originCountryENG;

    /**
     * Obtiene el valor de la propiedad internationalWaybill.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInternationalWaybill() {
        return internationalWaybill;
    }

    /**
     * Define el valor de la propiedad internationalWaybill.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInternationalWaybill(String value) {
        this.internationalWaybill = value;
    }

    /**
     * Obtiene el valor de la propiedad originCountryCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginCountryCode() {
        return originCountryCode;
    }

    /**
     * Define el valor de la propiedad originCountryCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginCountryCode(String value) {
        this.originCountryCode = value;
    }

    /**
     * Obtiene el valor de la propiedad originCountrySPA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginCountrySPA() {
        return originCountrySPA;
    }

    /**
     * Define el valor de la propiedad originCountrySPA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginCountrySPA(String value) {
        this.originCountrySPA = value;
    }

    /**
     * Obtiene el valor de la propiedad originCountryENG.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginCountryENG() {
        return originCountryENG;
    }

    /**
     * Define el valor de la propiedad originCountryENG.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginCountryENG(String value) {
        this.originCountryENG = value;
    }

}
