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
 * <p>Clase Java para DiasEntrega complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DiasEntrega"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="lunes" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="martes" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="miercoles" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="jueves" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="viernes" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sabado" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="domingo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DiasEntrega", propOrder = {
    "lunes",
    "martes",
    "miercoles",
    "jueves",
    "viernes",
    "sabado",
    "domingo"
})
public class DiasEntrega {

    @XmlElement(required = true)
    protected String lunes;
    @XmlElement(required = true)
    protected String martes;
    @XmlElement(required = true)
    protected String miercoles;
    @XmlElement(required = true)
    protected String jueves;
    @XmlElement(required = true)
    protected String viernes;
    @XmlElement(required = true)
    protected String sabado;
    @XmlElement(required = true)
    protected String domingo;

    /**
     * Obtiene el valor de la propiedad lunes.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLunes() {
        return lunes;
    }

    /**
     * Define el valor de la propiedad lunes.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLunes(String value) {
        this.lunes = value;
    }

    /**
     * Obtiene el valor de la propiedad martes.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMartes() {
        return martes;
    }

    /**
     * Define el valor de la propiedad martes.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMartes(String value) {
        this.martes = value;
    }

    /**
     * Obtiene el valor de la propiedad miercoles.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMiercoles() {
        return miercoles;
    }

    /**
     * Define el valor de la propiedad miercoles.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMiercoles(String value) {
        this.miercoles = value;
    }

    /**
     * Obtiene el valor de la propiedad jueves.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJueves() {
        return jueves;
    }

    /**
     * Define el valor de la propiedad jueves.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJueves(String value) {
        this.jueves = value;
    }

    /**
     * Obtiene el valor de la propiedad viernes.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getViernes() {
        return viernes;
    }

    /**
     * Define el valor de la propiedad viernes.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setViernes(String value) {
        this.viernes = value;
    }

    /**
     * Obtiene el valor de la propiedad sabado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSabado() {
        return sabado;
    }

    /**
     * Define el valor de la propiedad sabado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSabado(String value) {
        this.sabado = value;
    }

    /**
     * Obtiene el valor de la propiedad domingo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomingo() {
        return domingo;
    }

    /**
     * Define el valor de la propiedad domingo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomingo(String value) {
        this.domingo = value;
    }

}
