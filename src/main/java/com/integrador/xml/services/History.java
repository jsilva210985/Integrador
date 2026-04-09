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
 * <p>Clase Java para History complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="History">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="eventDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="eventId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="eventDescriptionSPA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="eventDescriptionENG" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="eventPlaceAcronym" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="eventPlaceName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="exceptionCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="exceptionCodeDescriptionSPA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="exceptionCodeDescriptionENG" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="exceptionCodeDetails" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "History", propOrder = {
    "eventDateTime",
    "eventId",
    "eventDescriptionSPA",
    "eventDescriptionENG",
    "eventPlaceAcronym",
    "eventPlaceName",
    "exceptionCode",
    "exceptionCodeDescriptionSPA",
    "exceptionCodeDescriptionENG",
    "exceptionCodeDetails"
})
public class History {

    @XmlElement(required = true)
    protected String eventDateTime;
    @XmlElement(required = true)
    protected String eventId;
    @XmlElement(required = true)
    protected String eventDescriptionSPA;
    @XmlElement(required = true)
    protected String eventDescriptionENG;
    @XmlElement(required = true)
    protected String eventPlaceAcronym;
    @XmlElement(required = true)
    protected String eventPlaceName;
    @XmlElement(required = true)
    protected String exceptionCode;
    @XmlElement(required = true)
    protected String exceptionCodeDescriptionSPA;
    @XmlElement(required = true)
    protected String exceptionCodeDescriptionENG;
    @XmlElement(required = true)
    protected String exceptionCodeDetails;

    /**
     * Obtiene el valor de la propiedad eventDateTime.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventDateTime() {
        return eventDateTime;
    }

    /**
     * Define el valor de la propiedad eventDateTime.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventDateTime(String value) {
        this.eventDateTime = value;
    }

    /**
     * Obtiene el valor de la propiedad eventId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Define el valor de la propiedad eventId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventId(String value) {
        this.eventId = value;
    }

    /**
     * Obtiene el valor de la propiedad eventDescriptionSPA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventDescriptionSPA() {
        return eventDescriptionSPA;
    }

    /**
     * Define el valor de la propiedad eventDescriptionSPA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventDescriptionSPA(String value) {
        this.eventDescriptionSPA = value;
    }

    /**
     * Obtiene el valor de la propiedad eventDescriptionENG.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventDescriptionENG() {
        return eventDescriptionENG;
    }

    /**
     * Define el valor de la propiedad eventDescriptionENG.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventDescriptionENG(String value) {
        this.eventDescriptionENG = value;
    }

    /**
     * Obtiene el valor de la propiedad eventPlaceAcronym.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventPlaceAcronym() {
        return eventPlaceAcronym;
    }

    /**
     * Define el valor de la propiedad eventPlaceAcronym.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventPlaceAcronym(String value) {
        this.eventPlaceAcronym = value;
    }

    /**
     * Obtiene el valor de la propiedad eventPlaceName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventPlaceName() {
        return eventPlaceName;
    }

    /**
     * Define el valor de la propiedad eventPlaceName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventPlaceName(String value) {
        this.eventPlaceName = value;
    }

    /**
     * Obtiene el valor de la propiedad exceptionCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExceptionCode() {
        return exceptionCode;
    }

    /**
     * Define el valor de la propiedad exceptionCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExceptionCode(String value) {
        this.exceptionCode = value;
    }

    /**
     * Obtiene el valor de la propiedad exceptionCodeDescriptionSPA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExceptionCodeDescriptionSPA() {
        return exceptionCodeDescriptionSPA;
    }

    /**
     * Define el valor de la propiedad exceptionCodeDescriptionSPA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExceptionCodeDescriptionSPA(String value) {
        this.exceptionCodeDescriptionSPA = value;
    }

    /**
     * Obtiene el valor de la propiedad exceptionCodeDescriptionENG.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExceptionCodeDescriptionENG() {
        return exceptionCodeDescriptionENG;
    }

    /**
     * Define el valor de la propiedad exceptionCodeDescriptionENG.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExceptionCodeDescriptionENG(String value) {
        this.exceptionCodeDescriptionENG = value;
    }

    /**
     * Obtiene el valor de la propiedad exceptionCodeDetails.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExceptionCodeDetails() {
        return exceptionCodeDetails;
    }

    /**
     * Define el valor de la propiedad exceptionCodeDetails.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExceptionCodeDetails(String value) {
        this.exceptionCodeDetails = value;
    }

}
