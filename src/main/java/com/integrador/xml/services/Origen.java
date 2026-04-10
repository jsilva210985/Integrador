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
 * <p>Clase Java para Origen complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Origen"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codigoPosOri" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="plazaOri" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="municipioOri" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="estadoOri" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
