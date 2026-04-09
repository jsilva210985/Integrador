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
 * <p>Clase Java para Destino complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Destino">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cpDestino" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="plaza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="municipio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Destino", propOrder = {
    "cpDestino",
    "plaza",
    "municipio",
    "estado"
})
public class Destino {

    @XmlElement(required = true)
    protected String cpDestino;
    @XmlElement(required = true)
    protected String plaza;
    @XmlElement(required = true)
    protected String municipio;
    @XmlElement(required = true)
    protected String estado;

    /**
     * Obtiene el valor de la propiedad cpDestino.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCpDestino() {
        return cpDestino;
    }

    /**
     * Define el valor de la propiedad cpDestino.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCpDestino(String value) {
        this.cpDestino = value;
    }

    /**
     * Obtiene el valor de la propiedad plaza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlaza() {
        return plaza;
    }

    /**
     * Define el valor de la propiedad plaza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlaza(String value) {
        this.plaza = value;
    }

    /**
     * Obtiene el valor de la propiedad municipio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMunicipio() {
        return municipio;
    }

    /**
     * Define el valor de la propiedad municipio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMunicipio(String value) {
        this.municipio = value;
    }

    /**
     * Obtiene el valor de la propiedad estado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Define el valor de la propiedad estado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstado(String value) {
        this.estado = value;
    }

}
