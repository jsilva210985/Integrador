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
 * <p>Clase Java para SearchConfiguration complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="SearchConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="includeDimensions" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="includeWaybillReplaceData" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="includeReturnDocumentData" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="includeMultipleServiceData" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="includeInternationalData" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="includeCustomerInfo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="includeSignature" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="historyConfiguration" type="{http://www.integrador.com/xml/services}HistoryConfiguration"/>
 *         &lt;element name="filterType" type="{http://www.integrador.com/xml/services}Filter"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchConfiguration", propOrder = {
    "includeDimensions",
    "includeWaybillReplaceData",
    "includeReturnDocumentData",
    "includeMultipleServiceData",
    "includeInternationalData",
    "includeCustomerInfo",
    "includeSignature",
    "historyConfiguration",
    "filterType"
})
public class SearchConfiguration {

    @XmlElement(required = true)
    protected String includeDimensions;
    @XmlElement(required = true)
    protected String includeWaybillReplaceData;
    @XmlElement(required = true)
    protected String includeReturnDocumentData;
    @XmlElement(required = true)
    protected String includeMultipleServiceData;
    @XmlElement(required = true)
    protected String includeInternationalData;
    @XmlElement(required = true)
    protected String includeCustomerInfo;
    @XmlElement(required = true)
    protected String includeSignature;
    @XmlElement(required = true)
    protected HistoryConfiguration historyConfiguration;
    @XmlElement(required = true)
    protected Filter filterType;

    /**
     * Obtiene el valor de la propiedad includeDimensions.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncludeDimensions() {
        return includeDimensions;
    }

    /**
     * Define el valor de la propiedad includeDimensions.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncludeDimensions(String value) {
        this.includeDimensions = value;
    }

    /**
     * Obtiene el valor de la propiedad includeWaybillReplaceData.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncludeWaybillReplaceData() {
        return includeWaybillReplaceData;
    }

    /**
     * Define el valor de la propiedad includeWaybillReplaceData.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncludeWaybillReplaceData(String value) {
        this.includeWaybillReplaceData = value;
    }

    /**
     * Obtiene el valor de la propiedad includeReturnDocumentData.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncludeReturnDocumentData() {
        return includeReturnDocumentData;
    }

    /**
     * Define el valor de la propiedad includeReturnDocumentData.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncludeReturnDocumentData(String value) {
        this.includeReturnDocumentData = value;
    }

    /**
     * Obtiene el valor de la propiedad includeMultipleServiceData.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncludeMultipleServiceData() {
        return includeMultipleServiceData;
    }

    /**
     * Define el valor de la propiedad includeMultipleServiceData.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncludeMultipleServiceData(String value) {
        this.includeMultipleServiceData = value;
    }

    /**
     * Obtiene el valor de la propiedad includeInternationalData.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncludeInternationalData() {
        return includeInternationalData;
    }

    /**
     * Define el valor de la propiedad includeInternationalData.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncludeInternationalData(String value) {
        this.includeInternationalData = value;
    }

    /**
     * Obtiene el valor de la propiedad includeCustomerInfo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncludeCustomerInfo() {
        return includeCustomerInfo;
    }

    /**
     * Define el valor de la propiedad includeCustomerInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncludeCustomerInfo(String value) {
        this.includeCustomerInfo = value;
    }

    /**
     * Obtiene el valor de la propiedad includeSignature.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncludeSignature() {
        return includeSignature;
    }

    /**
     * Define el valor de la propiedad includeSignature.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncludeSignature(String value) {
        this.includeSignature = value;
    }

    /**
     * Obtiene el valor de la propiedad historyConfiguration.
     * 
     * @return
     *     possible object is
     *     {@link HistoryConfiguration }
     *     
     */
    public HistoryConfiguration getHistoryConfiguration() {
        return historyConfiguration;
    }

    /**
     * Define el valor de la propiedad historyConfiguration.
     * 
     * @param value
     *     allowed object is
     *     {@link HistoryConfiguration }
     *     
     */
    public void setHistoryConfiguration(HistoryConfiguration value) {
        this.historyConfiguration = value;
    }

    /**
     * Obtiene el valor de la propiedad filterType.
     * 
     * @return
     *     possible object is
     *     {@link Filter }
     *     
     */
    public Filter getFilterType() {
        return filterType;
    }

    /**
     * Define el valor de la propiedad filterType.
     * 
     * @param value
     *     allowed object is
     *     {@link Filter }
     *     
     */
    public void setFilterType(Filter value) {
        this.filterType = value;
    }

}
