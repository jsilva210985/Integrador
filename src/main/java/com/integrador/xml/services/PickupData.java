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
 * <p>Clase Java para PickupData complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="PickupData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="originAcronym" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="originName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="pickupDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PickupData", propOrder = {
    "originAcronym",
    "originName",
    "pickupDateTime"
})
public class PickupData {

    @XmlElement(required = true)
    protected String originAcronym;
    @XmlElement(required = true)
    protected String originName;
    @XmlElement(required = true)
    protected String pickupDateTime;

    /**
     * Obtiene el valor de la propiedad originAcronym.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginAcronym() {
        return originAcronym;
    }

    /**
     * Define el valor de la propiedad originAcronym.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginAcronym(String value) {
        this.originAcronym = value;
    }

    /**
     * Obtiene el valor de la propiedad originName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginName() {
        return originName;
    }

    /**
     * Define el valor de la propiedad originName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginName(String value) {
        this.originName = value;
    }

    /**
     * Obtiene el valor de la propiedad pickupDateTime.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPickupDateTime() {
        return pickupDateTime;
    }

    /**
     * Define el valor de la propiedad pickupDateTime.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPickupDateTime(String value) {
        this.pickupDateTime = value;
    }

}
