//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.7 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2021.08.31 a las 04:50:55 PM CDT 
//


package com.integrador.xml.services;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para SearchConfiguration1 complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="SearchConfiguration1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="includeDimensions" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="includeWaybillReplaceData" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="includeReturnDocumentData" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="includeMultipleServiceData" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="includeInternationalData" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="includeSignature" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="includeSignature" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchConfiguration1", propOrder = {
    "content"
})
public class SearchConfiguration1 {

    @XmlElementRefs({
        @XmlElementRef(name = "includeDimensions", namespace = "http://www.integrador.com/xml/services", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "includeWaybillReplaceData", namespace = "http://www.integrador.com/xml/services", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "includeInternationalData", namespace = "http://www.integrador.com/xml/services", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "includeSignature", namespace = "http://www.integrador.com/xml/services", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "includeReturnDocumentData", namespace = "http://www.integrador.com/xml/services", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "includeMultipleServiceData", namespace = "http://www.integrador.com/xml/services", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<String>> content;

    /**
     * Obtiene el resto del modelo de contenido. 
     * 
     * <p>
     * Ha obtenido esta propiedad que permite capturar todo por el siguiente motivo: 
     * El nombre de campo "IncludeSignature" se está utilizando en dos partes diferentes de un esquema. Consulte: 
     * línea 223 de file:/C:/Workspace2/Integrador/src/main/resources/webservice/integador.xsd
     * línea 222 de file:/C:/Workspace2/Integrador/src/main/resources/webservice/integador.xsd
     * <p>
     * Para deshacerse de esta propiedad, aplique una personalización de propiedad a una
     * de las dos declaraciones siguientes para cambiarles de nombre: 
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<JAXBElement<String>> getContent() {
        if (content == null) {
            content = new ArrayList<JAXBElement<String>>();
        }
        return this.content;
    }

}
