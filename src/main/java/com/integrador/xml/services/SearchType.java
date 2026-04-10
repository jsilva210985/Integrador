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
 * <p>Clase Java para SearchType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="SearchType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="waybillRange" type="{http://www.integrador.com/xml/services}WaybillRange"/&gt;
 *         &lt;element name="waybillList" type="{http://www.integrador.com/xml/services}WaybillList"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchType", propOrder = {
    "type",
    "waybillRange",
    "waybillList"
})
public class SearchType {

    @XmlElement(required = true)
    protected String type;
    @XmlElement(required = true)
    protected WaybillRange waybillRange;
    @XmlElement(required = true)
    protected WaybillList waybillList;

    /**
     * Obtiene el valor de la propiedad type.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Define el valor de la propiedad type.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Obtiene el valor de la propiedad waybillRange.
     * 
     * @return
     *     possible object is
     *     {@link WaybillRange }
     *     
     */
    public WaybillRange getWaybillRange() {
        return waybillRange;
    }

    /**
     * Define el valor de la propiedad waybillRange.
     * 
     * @param value
     *     allowed object is
     *     {@link WaybillRange }
     *     
     */
    public void setWaybillRange(WaybillRange value) {
        this.waybillRange = value;
    }

    /**
     * Obtiene el valor de la propiedad waybillList.
     * 
     * @return
     *     possible object is
     *     {@link WaybillList }
     *     
     */
    public WaybillList getWaybillList() {
        return waybillList;
    }

    /**
     * Define el valor de la propiedad waybillList.
     * 
     * @param value
     *     allowed object is
     *     {@link WaybillList }
     *     
     */
    public void setWaybillList(WaybillList value) {
        this.waybillList = value;
    }

}
