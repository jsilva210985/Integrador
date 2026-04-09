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
 * <p>Clase Java para Origen complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Origen">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoPosOri" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="plazaOri" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="municipioOri" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="estadoOri" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Origen", propOrder = {
    "codigoPosOri",
    "plazaOri",
    "municipioOri",
    "estadoOri"
})
public class Origen {

    @XmlElement(required = true)
    protected String codigoPosOri;
    @XmlElement(required = true)
    protected String plazaOri;
    @XmlElement(required = true)
    protected String municipioOri;
    @XmlElement(required = true)
    protected String estadoOri;

    /**
     * Obtiene el valor de la propiedad codigoPosOri.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoPosOri() {
        return codigoPosOri;
    }

    /**
     * Define el valor de la propiedad codigoPosOri.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoPosOri(String value) {
        this.codigoPosOri = value;
    }

    /**
     * Obtiene el valor de la propiedad plazaOri.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlazaOri() {
        return plazaOri;
    }

    /**
     * Define el valor de la propiedad plazaOri.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlazaOri(String value) {
        this.plazaOri = value;
    }

    /**
     * Obtiene el valor de la propiedad municipioOri.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMunicipioOri() {
        return municipioOri;
    }

    /**
     * Define el valor de la propiedad municipioOri.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMunicipioOri(String value) {
        this.municipioOri = value;
    }

    /**
     * Obtiene el valor de la propiedad estadoOri.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstadoOri() {
        return estadoOri;
    }

    /**
     * Define el valor de la propiedad estadoOri.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstadoOri(String value) {
        this.estadoOri = value;
    }

}
