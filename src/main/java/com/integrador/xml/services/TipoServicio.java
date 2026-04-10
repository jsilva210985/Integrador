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
 * <p>Clase Java para TipoServicio complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="TipoServicio"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="descripcionServicio" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="tipoEnvioRes" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="aplicaCotizacion" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="tarifaBase" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ccTarifaBase" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="cargosExtra" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sobrePeso" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ccSobrePeso" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="costoTotal" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="peso" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="aplicaServicio" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TipoServicio", propOrder = {
    "descripcionServicio",
    "tipoEnvioRes",
    "aplicaCotizacion",
    "tarifaBase",
    "ccTarifaBase",
    "cargosExtra",
    "sobrePeso",
    "ccSobrePeso",
    "costoTotal",
    "peso",
    "aplicaServicio"
})
public class TipoServicio {

    @XmlElement(required = true)
    protected String descripcionServicio;
    @XmlElement(required = true)
    protected String tipoEnvioRes;
    @XmlElement(required = true)
    protected String aplicaCotizacion;
    @XmlElement(required = true)
    protected String tarifaBase;
    @XmlElement(required = true)
    protected String ccTarifaBase;
    @XmlElement(required = true)
    protected String cargosExtra;
    @XmlElement(required = true)
    protected String sobrePeso;
    @XmlElement(required = true)
    protected String ccSobrePeso;
    @XmlElement(required = true)
    protected String costoTotal;
    @XmlElement(required = true)
    protected String peso;
    @XmlElement(required = true)
    protected String aplicaServicio;

    /**
     * Obtiene el valor de la propiedad descripcionServicio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionServicio() {
        return descripcionServicio;
    }

    /**
     * Define el valor de la propiedad descripcionServicio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionServicio(String value) {
        this.descripcionServicio = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoEnvioRes.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoEnvioRes() {
        return tipoEnvioRes;
    }

    /**
     * Define el valor de la propiedad tipoEnvioRes.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoEnvioRes(String value) {
        this.tipoEnvioRes = value;
    }

    /**
     * Obtiene el valor de la propiedad aplicaCotizacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAplicaCotizacion() {
        return aplicaCotizacion;
    }

    /**
     * Define el valor de la propiedad aplicaCotizacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAplicaCotizacion(String value) {
        this.aplicaCotizacion = value;
    }

    /**
     * Obtiene el valor de la propiedad tarifaBase.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTarifaBase() {
        return tarifaBase;
    }

    /**
     * Define el valor de la propiedad tarifaBase.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTarifaBase(String value) {
        this.tarifaBase = value;
    }

    /**
     * Obtiene el valor de la propiedad ccTarifaBase.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcTarifaBase() {
        return ccTarifaBase;
    }

    /**
     * Define el valor de la propiedad ccTarifaBase.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCcTarifaBase(String value) {
        this.ccTarifaBase = value;
    }

    /**
     * Obtiene el valor de la propiedad cargosExtra.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCargosExtra() {
        return cargosExtra;
    }

    /**
     * Define el valor de la propiedad cargosExtra.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCargosExtra(String value) {
        this.cargosExtra = value;
    }

    /**
     * Obtiene el valor de la propiedad sobrePeso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSobrePeso() {
        return sobrePeso;
    }

    /**
     * Define el valor de la propiedad sobrePeso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSobrePeso(String value) {
        this.sobrePeso = value;
    }

    /**
     * Obtiene el valor de la propiedad ccSobrePeso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcSobrePeso() {
        return ccSobrePeso;
    }

    /**
     * Define el valor de la propiedad ccSobrePeso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCcSobrePeso(String value) {
        this.ccSobrePeso = value;
    }

    /**
     * Obtiene el valor de la propiedad costoTotal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCostoTotal() {
        return costoTotal;
    }

    /**
     * Define el valor de la propiedad costoTotal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCostoTotal(String value) {
        this.costoTotal = value;
    }

    /**
     * Obtiene el valor de la propiedad peso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeso() {
        return peso;
    }

    /**
     * Define el valor de la propiedad peso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeso(String value) {
        this.peso = value;
    }

    /**
     * Obtiene el valor de la propiedad aplicaServicio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAplicaServicio() {
        return aplicaServicio;
    }

    /**
     * Define el valor de la propiedad aplicaServicio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAplicaServicio(String value) {
        this.aplicaServicio = value;
    }

}
