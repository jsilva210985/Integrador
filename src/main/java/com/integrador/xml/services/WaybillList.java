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
 * <p>Clase Java para WaybillList complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="WaybillList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="waybillType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="waybills" type="{http://www.integrador.com/xml/services}ArrayOfString"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WaybillList", propOrder = {
    "waybillType",
    "waybills"
})
public class WaybillList {

    @XmlElement(required = true)
    protected String waybillType;
    @XmlElement(required = true)
    protected ArrayOfString waybills;

    /**
     * Obtiene el valor de la propiedad waybillType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWaybillType() {
        return waybillType;
    }

    /**
     * Define el valor de la propiedad waybillType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWaybillType(String value) {
        this.waybillType = value;
    }

    /**
     * Obtiene el valor de la propiedad waybills.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getWaybills() {
        return waybills;
    }

    /**
     * Define el valor de la propiedad waybills.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setWaybills(ArrayOfString value) {
        this.waybills = value;
    }

}
