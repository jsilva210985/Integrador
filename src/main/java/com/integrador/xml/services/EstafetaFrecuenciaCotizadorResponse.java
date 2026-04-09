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
 *         &lt;element name="TipoEnvio" type="{http://www.integrador.com/xml/services}TipoEnvio"/>
 *         &lt;element name="TiposServicios" type="{http://www.integrador.com/xml/services}TipoSer"/>
 *         &lt;element name="Colonias" type="{http://www.integrador.com/xml/services}Colonia"/>
 *         &lt;element name="ModalidadEntrega" type="{http://www.integrador.com/xml/services}ModalidadEntrega"/>
 *         &lt;element name="DiasEntrega" type="{http://www.integrador.com/xml/services}DiasEntrega"/>
 *         &lt;element name="CostoReexpedicion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ExistenteSiglaOri" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ExistenteSiglaDes" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Destino" type="{http://www.integrador.com/xml/services}Destino"/>
 *         &lt;element name="Origen" type="{http://www.integrador.com/xml/services}Origen"/>
 *         &lt;element name="Error" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MensajeError" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CodigoPosOri" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "tipoEnvio",
    "tiposServicios",
    "colonias",
    "modalidadEntrega",
    "diasEntrega",
    "costoReexpedicion",
    "existenteSiglaOri",
    "existenteSiglaDes",
    "destino",
    "origen",
    "error",
    "mensajeError",
    "codigoPosOri"
})
@XmlRootElement(name = "EstafetaFrecuenciaCotizadorResponse")
public class EstafetaFrecuenciaCotizadorResponse {

    @XmlElement(name = "TipoEnvio", required = true)
    protected TipoEnvio tipoEnvio;
    @XmlElement(name = "TiposServicios", required = true)
    protected TipoSer tiposServicios;
    @XmlElement(name = "Colonias", required = true)
    protected Colonia colonias;
    @XmlElement(name = "ModalidadEntrega", required = true)
    protected ModalidadEntrega modalidadEntrega;
    @XmlElement(name = "DiasEntrega", required = true)
    protected DiasEntrega diasEntrega;
    @XmlElement(name = "CostoReexpedicion", required = true)
    protected String costoReexpedicion;
    @XmlElement(name = "ExistenteSiglaOri", required = true)
    protected String existenteSiglaOri;
    @XmlElement(name = "ExistenteSiglaDes", required = true)
    protected String existenteSiglaDes;
    @XmlElement(name = "Destino", required = true)
    protected Destino destino;
    @XmlElement(name = "Origen", required = true)
    protected Origen origen;
    @XmlElement(name = "Error", required = true)
    protected String error;
    @XmlElement(name = "MensajeError", required = true)
    protected String mensajeError;
    @XmlElement(name = "CodigoPosOri", required = true)
    protected String codigoPosOri;

    /**
     * Obtiene el valor de la propiedad tipoEnvio.
     * 
     * @return
     *     possible object is
     *     {@link TipoEnvio }
     *     
     */
    public TipoEnvio getTipoEnvio() {
        return tipoEnvio;
    }

    /**
     * Define el valor de la propiedad tipoEnvio.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoEnvio }
     *     
     */
    public void setTipoEnvio(TipoEnvio value) {
        this.tipoEnvio = value;
    }

    /**
     * Obtiene el valor de la propiedad tiposServicios.
     * 
     * @return
     *     possible object is
     *     {@link TipoSer }
     *     
     */
    public TipoSer getTiposServicios() {
        return tiposServicios;
    }

    /**
     * Define el valor de la propiedad tiposServicios.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoSer }
     *     
     */
    public void setTiposServicios(TipoSer value) {
        this.tiposServicios = value;
    }

    /**
     * Obtiene el valor de la propiedad colonias.
     * 
     * @return
     *     possible object is
     *     {@link Colonia }
     *     
     */
    public Colonia getColonias() {
        return colonias;
    }

    /**
     * Define el valor de la propiedad colonias.
     * 
     * @param value
     *     allowed object is
     *     {@link Colonia }
     *     
     */
    public void setColonias(Colonia value) {
        this.colonias = value;
    }

    /**
     * Obtiene el valor de la propiedad modalidadEntrega.
     * 
     * @return
     *     possible object is
     *     {@link ModalidadEntrega }
     *     
     */
    public ModalidadEntrega getModalidadEntrega() {
        return modalidadEntrega;
    }

    /**
     * Define el valor de la propiedad modalidadEntrega.
     * 
     * @param value
     *     allowed object is
     *     {@link ModalidadEntrega }
     *     
     */
    public void setModalidadEntrega(ModalidadEntrega value) {
        this.modalidadEntrega = value;
    }

    /**
     * Obtiene el valor de la propiedad diasEntrega.
     * 
     * @return
     *     possible object is
     *     {@link DiasEntrega }
     *     
     */
    public DiasEntrega getDiasEntrega() {
        return diasEntrega;
    }

    /**
     * Define el valor de la propiedad diasEntrega.
     * 
     * @param value
     *     allowed object is
     *     {@link DiasEntrega }
     *     
     */
    public void setDiasEntrega(DiasEntrega value) {
        this.diasEntrega = value;
    }

    /**
     * Obtiene el valor de la propiedad costoReexpedicion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCostoReexpedicion() {
        return costoReexpedicion;
    }

    /**
     * Define el valor de la propiedad costoReexpedicion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCostoReexpedicion(String value) {
        this.costoReexpedicion = value;
    }

    /**
     * Obtiene el valor de la propiedad existenteSiglaOri.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExistenteSiglaOri() {
        return existenteSiglaOri;
    }

    /**
     * Define el valor de la propiedad existenteSiglaOri.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExistenteSiglaOri(String value) {
        this.existenteSiglaOri = value;
    }

    /**
     * Obtiene el valor de la propiedad existenteSiglaDes.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExistenteSiglaDes() {
        return existenteSiglaDes;
    }

    /**
     * Define el valor de la propiedad existenteSiglaDes.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExistenteSiglaDes(String value) {
        this.existenteSiglaDes = value;
    }

    /**
     * Obtiene el valor de la propiedad destino.
     * 
     * @return
     *     possible object is
     *     {@link Destino }
     *     
     */
    public Destino getDestino() {
        return destino;
    }

    /**
     * Define el valor de la propiedad destino.
     * 
     * @param value
     *     allowed object is
     *     {@link Destino }
     *     
     */
    public void setDestino(Destino value) {
        this.destino = value;
    }

    /**
     * Obtiene el valor de la propiedad origen.
     * 
     * @return
     *     possible object is
     *     {@link Origen }
     *     
     */
    public Origen getOrigen() {
        return origen;
    }

    /**
     * Define el valor de la propiedad origen.
     * 
     * @param value
     *     allowed object is
     *     {@link Origen }
     *     
     */
    public void setOrigen(Origen value) {
        this.origen = value;
    }

    /**
     * Obtiene el valor de la propiedad error.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getError() {
        return error;
    }

    /**
     * Define el valor de la propiedad error.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setError(String value) {
        this.error = value;
    }

    /**
     * Obtiene el valor de la propiedad mensajeError.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMensajeError() {
        return mensajeError;
    }

    /**
     * Define el valor de la propiedad mensajeError.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMensajeError(String value) {
        this.mensajeError = value;
    }

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

}
