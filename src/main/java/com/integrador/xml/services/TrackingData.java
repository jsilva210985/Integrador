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
 * <p>Clase Java para TrackingData complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="TrackingData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="waybill" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="shortWaybillId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="serviceId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="serviceDescriptionSPA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="serviceDescriptionENG" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="customerNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="packageType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="additionalInformation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="statusSPA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="statusENG" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pickupData" type="{http://www.integrador.com/xml/services}PickupData"/>
 *         &lt;element name="deliveryData" type="{http://www.integrador.com/xml/services}DeliveryData"/>
 *         &lt;element name="dimensions" type="{http://www.integrador.com/xml/services}Dimensions"/>
 *         &lt;element name="waybillReplaceData" type="{http://www.integrador.com/xml/services}WaybillReplaceData"/>
 *         &lt;element name="returnDocumentData" type="{http://www.integrador.com/xml/services}ReturnDocumentData"/>
 *         &lt;element name="multipleServiceData" type="{http://www.integrador.com/xml/services}MultipleServiceData"/>
 *         &lt;element name="internationalData" type="{http://www.integrador.com/xml/services}InternationalData"/>
 *         &lt;element name="customerInfo" type="{http://www.integrador.com/xml/services}CustomerInfo"/>
 *         &lt;element name="history" type="{http://www.integrador.com/xml/services}ArrayOfHistory"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrackingData", propOrder = {
    "waybill",
    "shortWaybillId",
    "serviceId",
    "serviceDescriptionSPA",
    "serviceDescriptionENG",
    "customerNumber",
    "packageType",
    "additionalInformation",
    "statusSPA",
    "statusENG",
    "pickupData",
    "deliveryData",
    "dimensions",
    "waybillReplaceData",
    "returnDocumentData",
    "multipleServiceData",
    "internationalData",
    "customerInfo",
    "history"
})
public class TrackingData {

    @XmlElement(required = true)
    protected String waybill;
    @XmlElement(required = true)
    protected String shortWaybillId;
    @XmlElement(required = true)
    protected String serviceId;
    @XmlElement(required = true)
    protected String serviceDescriptionSPA;
    @XmlElement(required = true)
    protected String serviceDescriptionENG;
    @XmlElement(required = true)
    protected String customerNumber;
    @XmlElement(required = true)
    protected String packageType;
    @XmlElement(required = true)
    protected String additionalInformation;
    @XmlElement(required = true)
    protected String statusSPA;
    @XmlElement(required = true)
    protected String statusENG;
    @XmlElement(required = true)
    protected PickupData pickupData;
    @XmlElement(required = true)
    protected DeliveryData deliveryData;
    @XmlElement(required = true)
    protected Dimensions dimensions;
    @XmlElement(required = true)
    protected WaybillReplaceData waybillReplaceData;
    @XmlElement(required = true)
    protected ReturnDocumentData returnDocumentData;
    @XmlElement(required = true)
    protected MultipleServiceData multipleServiceData;
    @XmlElement(required = true)
    protected InternationalData internationalData;
    @XmlElement(required = true)
    protected CustomerInfo customerInfo;
    @XmlElement(required = true)
    protected ArrayOfHistory history;

    /**
     * Obtiene el valor de la propiedad waybill.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWaybill() {
        return waybill;
    }

    /**
     * Define el valor de la propiedad waybill.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWaybill(String value) {
        this.waybill = value;
    }

    /**
     * Obtiene el valor de la propiedad shortWaybillId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortWaybillId() {
        return shortWaybillId;
    }

    /**
     * Define el valor de la propiedad shortWaybillId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortWaybillId(String value) {
        this.shortWaybillId = value;
    }

    /**
     * Obtiene el valor de la propiedad serviceId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Define el valor de la propiedad serviceId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceId(String value) {
        this.serviceId = value;
    }

    /**
     * Obtiene el valor de la propiedad serviceDescriptionSPA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceDescriptionSPA() {
        return serviceDescriptionSPA;
    }

    /**
     * Define el valor de la propiedad serviceDescriptionSPA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceDescriptionSPA(String value) {
        this.serviceDescriptionSPA = value;
    }

    /**
     * Obtiene el valor de la propiedad serviceDescriptionENG.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceDescriptionENG() {
        return serviceDescriptionENG;
    }

    /**
     * Define el valor de la propiedad serviceDescriptionENG.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceDescriptionENG(String value) {
        this.serviceDescriptionENG = value;
    }

    /**
     * Obtiene el valor de la propiedad customerNumber.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerNumber() {
        return customerNumber;
    }

    /**
     * Define el valor de la propiedad customerNumber.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerNumber(String value) {
        this.customerNumber = value;
    }

    /**
     * Obtiene el valor de la propiedad packageType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackageType() {
        return packageType;
    }

    /**
     * Define el valor de la propiedad packageType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackageType(String value) {
        this.packageType = value;
    }

    /**
     * Obtiene el valor de la propiedad additionalInformation.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * Define el valor de la propiedad additionalInformation.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalInformation(String value) {
        this.additionalInformation = value;
    }

    /**
     * Obtiene el valor de la propiedad statusSPA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusSPA() {
        return statusSPA;
    }

    /**
     * Define el valor de la propiedad statusSPA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusSPA(String value) {
        this.statusSPA = value;
    }

    /**
     * Obtiene el valor de la propiedad statusENG.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusENG() {
        return statusENG;
    }

    /**
     * Define el valor de la propiedad statusENG.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusENG(String value) {
        this.statusENG = value;
    }

    /**
     * Obtiene el valor de la propiedad pickupData.
     * 
     * @return
     *     possible object is
     *     {@link PickupData }
     *     
     */
    public PickupData getPickupData() {
        return pickupData;
    }

    /**
     * Define el valor de la propiedad pickupData.
     * 
     * @param value
     *     allowed object is
     *     {@link PickupData }
     *     
     */
    public void setPickupData(PickupData value) {
        this.pickupData = value;
    }

    /**
     * Obtiene el valor de la propiedad deliveryData.
     * 
     * @return
     *     possible object is
     *     {@link DeliveryData }
     *     
     */
    public DeliveryData getDeliveryData() {
        return deliveryData;
    }

    /**
     * Define el valor de la propiedad deliveryData.
     * 
     * @param value
     *     allowed object is
     *     {@link DeliveryData }
     *     
     */
    public void setDeliveryData(DeliveryData value) {
        this.deliveryData = value;
    }

    /**
     * Obtiene el valor de la propiedad dimensions.
     * 
     * @return
     *     possible object is
     *     {@link Dimensions }
     *     
     */
    public Dimensions getDimensions() {
        return dimensions;
    }

    /**
     * Define el valor de la propiedad dimensions.
     * 
     * @param value
     *     allowed object is
     *     {@link Dimensions }
     *     
     */
    public void setDimensions(Dimensions value) {
        this.dimensions = value;
    }

    /**
     * Obtiene el valor de la propiedad waybillReplaceData.
     * 
     * @return
     *     possible object is
     *     {@link WaybillReplaceData }
     *     
     */
    public WaybillReplaceData getWaybillReplaceData() {
        return waybillReplaceData;
    }

    /**
     * Define el valor de la propiedad waybillReplaceData.
     * 
     * @param value
     *     allowed object is
     *     {@link WaybillReplaceData }
     *     
     */
    public void setWaybillReplaceData(WaybillReplaceData value) {
        this.waybillReplaceData = value;
    }

    /**
     * Obtiene el valor de la propiedad returnDocumentData.
     * 
     * @return
     *     possible object is
     *     {@link ReturnDocumentData }
     *     
     */
    public ReturnDocumentData getReturnDocumentData() {
        return returnDocumentData;
    }

    /**
     * Define el valor de la propiedad returnDocumentData.
     * 
     * @param value
     *     allowed object is
     *     {@link ReturnDocumentData }
     *     
     */
    public void setReturnDocumentData(ReturnDocumentData value) {
        this.returnDocumentData = value;
    }

    /**
     * Obtiene el valor de la propiedad multipleServiceData.
     * 
     * @return
     *     possible object is
     *     {@link MultipleServiceData }
     *     
     */
    public MultipleServiceData getMultipleServiceData() {
        return multipleServiceData;
    }

    /**
     * Define el valor de la propiedad multipleServiceData.
     * 
     * @param value
     *     allowed object is
     *     {@link MultipleServiceData }
     *     
     */
    public void setMultipleServiceData(MultipleServiceData value) {
        this.multipleServiceData = value;
    }

    /**
     * Obtiene el valor de la propiedad internationalData.
     * 
     * @return
     *     possible object is
     *     {@link InternationalData }
     *     
     */
    public InternationalData getInternationalData() {
        return internationalData;
    }

    /**
     * Define el valor de la propiedad internationalData.
     * 
     * @param value
     *     allowed object is
     *     {@link InternationalData }
     *     
     */
    public void setInternationalData(InternationalData value) {
        this.internationalData = value;
    }

    /**
     * Obtiene el valor de la propiedad customerInfo.
     * 
     * @return
     *     possible object is
     *     {@link CustomerInfo }
     *     
     */
    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    /**
     * Define el valor de la propiedad customerInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerInfo }
     *     
     */
    public void setCustomerInfo(CustomerInfo value) {
        this.customerInfo = value;
    }

    /**
     * Obtiene el valor de la propiedad history.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfHistory }
     *     
     */
    public ArrayOfHistory getHistory() {
        return history;
    }

    /**
     * Define el valor de la propiedad history.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfHistory }
     *     
     */
    public void setHistory(ArrayOfHistory value) {
        this.history = value;
    }

}
