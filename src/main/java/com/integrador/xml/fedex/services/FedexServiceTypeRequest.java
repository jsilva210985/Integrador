//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.7 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2025.12.17 a las 04:15:23 PM CST 
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
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="account" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="origenAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="origenCity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="origenState" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="origenMunicipio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="origenCP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="origenName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="origenPhone" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="destinationAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="destinationCity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="destinationState" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="destinationMunicipio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="destinationCP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="destinationName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="destinationPhone" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="weight" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "account",
    "origenAddress",
    "origenCity",
    "origenState",
    "origenMunicipio",
    "origenCP",
    "origenName",
    "origenPhone",
    "destinationAddress",
    "destinationCity",
    "destinationState",
    "destinationMunicipio",
    "destinationCP",
    "destinationName",
    "destinationPhone",
    "weight"
})
@XmlRootElement(name = "FedexServiceTypeRequest")
public class FedexServiceTypeRequest {

    @XmlElement(required = true)
    protected String account;
    @XmlElement(required = true)
    protected String origenAddress;
    @XmlElement(required = true)
    protected String origenCity;
    @XmlElement(required = true)
    protected String origenState;
    @XmlElement(required = true)
    protected String origenMunicipio;
    @XmlElement(required = true)
    protected String origenCP;
    @XmlElement(required = true)
    protected String origenName;
    @XmlElement(required = true)
    protected String origenPhone;
    @XmlElement(required = true)
    protected String destinationAddress;
    @XmlElement(required = true)
    protected String destinationCity;
    @XmlElement(required = true)
    protected String destinationState;
    @XmlElement(required = true)
    protected String destinationMunicipio;
    @XmlElement(required = true)
    protected String destinationCP;
    @XmlElement(required = true)
    protected String destinationName;
    @XmlElement(required = true)
    protected String destinationPhone;
    @XmlElement(required = true)
    protected String weight;

    /**
     * Obtiene el valor de la propiedad account.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccount() {
        return account;
    }

    /**
     * Define el valor de la propiedad account.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccount(String value) {
        this.account = value;
    }

    /**
     * Obtiene el valor de la propiedad origenAddress.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigenAddress() {
        return origenAddress;
    }

    /**
     * Define el valor de la propiedad origenAddress.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigenAddress(String value) {
        this.origenAddress = value;
    }

    /**
     * Obtiene el valor de la propiedad origenCity.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigenCity() {
        return origenCity;
    }

    /**
     * Define el valor de la propiedad origenCity.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigenCity(String value) {
        this.origenCity = value;
    }

    /**
     * Obtiene el valor de la propiedad origenState.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigenState() {
        return origenState;
    }

    /**
     * Define el valor de la propiedad origenState.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigenState(String value) {
        this.origenState = value;
    }

    /**
     * Obtiene el valor de la propiedad origenMunicipio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigenMunicipio() {
        return origenMunicipio;
    }

    /**
     * Define el valor de la propiedad origenMunicipio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigenMunicipio(String value) {
        this.origenMunicipio = value;
    }

    /**
     * Obtiene el valor de la propiedad origenCP.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigenCP() {
        return origenCP;
    }

    /**
     * Define el valor de la propiedad origenCP.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigenCP(String value) {
        this.origenCP = value;
    }

    /**
     * Obtiene el valor de la propiedad origenName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigenName() {
        return origenName;
    }

    /**
     * Define el valor de la propiedad origenName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigenName(String value) {
        this.origenName = value;
    }

    /**
     * Obtiene el valor de la propiedad origenPhone.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigenPhone() {
        return origenPhone;
    }

    /**
     * Define el valor de la propiedad origenPhone.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigenPhone(String value) {
        this.origenPhone = value;
    }

    /**
     * Obtiene el valor de la propiedad destinationAddress.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationAddress() {
        return destinationAddress;
    }

    /**
     * Define el valor de la propiedad destinationAddress.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationAddress(String value) {
        this.destinationAddress = value;
    }

    /**
     * Obtiene el valor de la propiedad destinationCity.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationCity() {
        return destinationCity;
    }

    /**
     * Define el valor de la propiedad destinationCity.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationCity(String value) {
        this.destinationCity = value;
    }

    /**
     * Obtiene el valor de la propiedad destinationState.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationState() {
        return destinationState;
    }

    /**
     * Define el valor de la propiedad destinationState.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationState(String value) {
        this.destinationState = value;
    }

    /**
     * Obtiene el valor de la propiedad destinationMunicipio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationMunicipio() {
        return destinationMunicipio;
    }

    /**
     * Define el valor de la propiedad destinationMunicipio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationMunicipio(String value) {
        this.destinationMunicipio = value;
    }

    /**
     * Obtiene el valor de la propiedad destinationCP.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationCP() {
        return destinationCP;
    }

    /**
     * Define el valor de la propiedad destinationCP.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationCP(String value) {
        this.destinationCP = value;
    }

    /**
     * Obtiene el valor de la propiedad destinationName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationName() {
        return destinationName;
    }

    /**
     * Define el valor de la propiedad destinationName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationName(String value) {
        this.destinationName = value;
    }

    /**
     * Obtiene el valor de la propiedad destinationPhone.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationPhone() {
        return destinationPhone;
    }

    /**
     * Define el valor de la propiedad destinationPhone.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationPhone(String value) {
        this.destinationPhone = value;
    }

    /**
     * Obtiene el valor de la propiedad weight.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWeight() {
        return weight;
    }

    /**
     * Define el valor de la propiedad weight.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWeight(String value) {
        this.weight = value;
    }

}
