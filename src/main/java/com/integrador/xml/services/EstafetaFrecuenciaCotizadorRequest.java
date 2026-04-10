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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="esFrecuencia" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="esLista" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="tipoEnvio" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="esPaquete" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="peso" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="largo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="alto" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ancho" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="listaOrigen" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="listaDestino" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="cliente" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="contrasena" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="token" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "esFrecuencia",
    "esLista",
    "tipoEnvio",
    "esPaquete",
    "peso",
    "largo",
    "alto",
    "ancho",
    "listaOrigen",
    "listaDestino",
    "cliente",
    "contrasena",
    "token"
})
@XmlRootElement(name = "EstafetaFrecuenciaCotizadorRequest")
public class EstafetaFrecuenciaCotizadorRequest {

    @XmlElement(required = true)
    protected String esFrecuencia;
    @XmlElement(required = true)
    protected String esLista;
    @XmlElement(required = true)
    protected String tipoEnvio;
    @XmlElement(required = true)
    protected String esPaquete;
    @XmlElement(required = true)
    protected String peso;
    @XmlElement(required = true)
    protected String largo;
    @XmlElement(required = true)
    protected String alto;
    @XmlElement(required = true)
    protected String ancho;
    @XmlElement(required = true)
    protected String listaOrigen;
    @XmlElement(required = true)
    protected String listaDestino;
    @XmlElement(required = true)
    protected String cliente;
    @XmlElement(required = true)
    protected String contrasena;
    @XmlElement(required = true)
    protected String token;

    /**
     * Obtiene el valor de la propiedad esFrecuencia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEsFrecuencia() {
        return esFrecuencia;
    }

    /**
     * Define el valor de la propiedad esFrecuencia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEsFrecuencia(String value) {
        this.esFrecuencia = value;
    }

    /**
     * Obtiene el valor de la propiedad esLista.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEsLista() {
        return esLista;
    }

    /**
     * Define el valor de la propiedad esLista.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEsLista(String value) {
        this.esLista = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoEnvio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoEnvio() {
        return tipoEnvio;
    }

    /**
     * Define el valor de la propiedad tipoEnvio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoEnvio(String value) {
        this.tipoEnvio = value;
    }

    /**
     * Obtiene el valor de la propiedad esPaquete.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEsPaquete() {
        return esPaquete;
    }

    /**
     * Define el valor de la propiedad esPaquete.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEsPaquete(String value) {
        this.esPaquete = value;
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
     * Obtiene el valor de la propiedad largo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLargo() {
        return largo;
    }

    /**
     * Define el valor de la propiedad largo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLargo(String value) {
        this.largo = value;
    }

    /**
     * Obtiene el valor de la propiedad alto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlto() {
        return alto;
    }

    /**
     * Define el valor de la propiedad alto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlto(String value) {
        this.alto = value;
    }

    /**
     * Obtiene el valor de la propiedad ancho.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAncho() {
        return ancho;
    }

    /**
     * Define el valor de la propiedad ancho.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAncho(String value) {
        this.ancho = value;
    }

    /**
     * Obtiene el valor de la propiedad listaOrigen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getListaOrigen() {
        return listaOrigen;
    }

    /**
     * Define el valor de la propiedad listaOrigen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setListaOrigen(String value) {
        this.listaOrigen = value;
    }

    /**
     * Obtiene el valor de la propiedad listaDestino.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getListaDestino() {
        return listaDestino;
    }

    /**
     * Define el valor de la propiedad listaDestino.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setListaDestino(String value) {
        this.listaDestino = value;
    }

    /**
     * Obtiene el valor de la propiedad cliente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCliente() {
        return cliente;
    }

    /**
     * Define el valor de la propiedad cliente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCliente(String value) {
        this.cliente = value;
    }

    /**
     * Obtiene el valor de la propiedad contrasena.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Define el valor de la propiedad contrasena.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContrasena(String value) {
        this.contrasena = value;
    }

    /**
     * Obtiene el valor de la propiedad token.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * Define el valor de la propiedad token.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

}
