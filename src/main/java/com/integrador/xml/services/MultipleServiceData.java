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
 * <p>Clase Java para MultipleServiceData complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="MultipleServiceData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="precedingWaybills" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="followingWaybills" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="waybillList" type="{http://www.integrador.com/xml/services}ArrayOfString"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MultipleServiceData", propOrder = {
    "precedingWaybills",
    "followingWaybills",
    "waybillList"
})
public class MultipleServiceData {

    @XmlElement(required = true)
    protected String precedingWaybills;
    @XmlElement(required = true)
    protected String followingWaybills;
    @XmlElement(required = true)
    protected ArrayOfString waybillList;

    /**
     * Obtiene el valor de la propiedad precedingWaybills.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrecedingWaybills() {
        return precedingWaybills;
    }

    /**
     * Define el valor de la propiedad precedingWaybills.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrecedingWaybills(String value) {
        this.precedingWaybills = value;
    }

    /**
     * Obtiene el valor de la propiedad followingWaybills.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFollowingWaybills() {
        return followingWaybills;
    }

    /**
     * Define el valor de la propiedad followingWaybills.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFollowingWaybills(String value) {
        this.followingWaybills = value;
    }

    /**
     * Obtiene el valor de la propiedad waybillList.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getWaybillList() {
        return waybillList;
    }

    /**
     * Define el valor de la propiedad waybillList.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setWaybillList(ArrayOfString value) {
        this.waybillList = value;
    }

}
