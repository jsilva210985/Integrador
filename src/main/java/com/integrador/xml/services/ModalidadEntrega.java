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
 * <p>Clase Java para ModalidadEntrega complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ModalidadEntrega"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ocurreForzoso" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="frecuencia" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModalidadEntrega", propOrder = {
    "ocurreForzoso",
    "frecuencia"
})
public class ModalidadEntrega {

    @XmlElement(required = true)
    protected String ocurreForzoso;
    @XmlElement(required = true)
    protected String frecuencia;

    /**
     * Obtiene el valor de la propiedad ocurreForzoso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOcurreForzoso() {
        return ocurreForzoso;
    }

    /**
     * Define el valor de la propiedad ocurreForzoso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOcurreForzoso(String value) {
        this.ocurreForzoso = value;
    }

    /**
     * Obtiene el valor de la propiedad frecuencia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrecuencia() {
        return frecuencia;
    }

    /**
     * Define el valor de la propiedad frecuencia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrecuencia(String value) {
        this.frecuencia = value;
    }

}
