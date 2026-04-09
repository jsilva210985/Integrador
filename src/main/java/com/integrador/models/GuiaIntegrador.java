package com.integrador.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="guias_integrador")
public class GuiaIntegrador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;

	@Column(name="fecha_creacion")
	private String fechaCreacion;

	@Column(name="remitente_calle")
	private String remitenteCalle;

	@Column(name="remitente_colonia")
	private String remitenteColonia;

	@Column(name="remitente_cp")
	private String remitenteCP;

	@Column(name="remitente_nombre")
	private String remitenteNombre;

	@Column(name="remitente_estado")
	private String remitenteEstado;

	@Column(name="remitente_municipio")
	private String remitenteMunicipio;

	@Column(name="remitente_referencia")
	private String remitenteReferencia;

	@Column(name="remitente_telefono")
	private String remitenteTelefono;

	@Column(name="remitente_numero_exterior")
	private String remitenteNumeroExterior;

	@Column(name="remitente_numero_interior")
	private String remitenteNumeroInterior;

	@Column(name="destinatario_calle")
	private String destinatarioCalle;

	@Column(name="destinatario_colonia")
	private String destinatarioColonia;

	@Column(name="destinatario_cp")
	private String destinatarioCP;

	@Column(name="destinatario_nombre")
	private String destinatarioNombre;

	@Column(name="destinatario_estado")
	private String destinatarioEstado;

	@Column(name="destinatario_municipio")
	private String destinatarioMunicipio;

	@Column(name="destinatario_numero_exterior")
	private String destinatarioNumeroExterior;

	@Column(name="destinatario_numero_interior")
	private String destinatarioNumeroInterior;

	@Column(name="destinatario_referencia")
	private String destinatarioReferencia;

	@Column(name="destinatario_telefono")
	private String destinatarioTelefono;

	@Column(name="informacion_adicional")
	private String informacionAdicional;

	@Column(name="tipo_guia")
	private String tipoGuia;

	@Column(name="contenido")
	private String contenido;

	@Column(name="etiquetas")
	private String etiquetas;

	@Column(name="tipo_contenido")
	private String tipoContenido;

	@Column(name="kilos")
	private String kilos;

	@Column(name="cliente")
	private String cliente;

	@Column(name="tracking")
	private String tracking;

	@Column(name="estatus")
	private String estatus;

	@Column(name="request")
	private String request;

	@Column(name="response")
	private String response;

	@Column(name="via")
	private String via;

	@Column(name="empresa")
	private String empresa;

	@Column(name="largo")
	private String largo;

	@Column(name="alto")
	private String alto;

	@Column(name="fondo")
	private String fondo;

	@Column(name="peso_volumetrico")
	private String pesoVolumetrico;

	@Column(name="reexpedicion")
	private String reexpedicion;

	@Column(name="alias")
	private String alias;

	@Column(name="cuenta")
	private String cuenta;

	@Column(name="servicio")
	private String servicio;

	@Column(name="peso_neto")
	private String pesoNeto;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getRemitenteCalle() {
		return remitenteCalle;
	}

	public void setRemitenteCalle(String remitenteCalle) {
		this.remitenteCalle = remitenteCalle;
	}

	public String getRemitenteColonia() {
		return remitenteColonia;
	}

	public void setRemitenteColonia(String remitenteColonia) {
		this.remitenteColonia = remitenteColonia;
	}

	public String getRemitenteCP() {
		return remitenteCP;
	}

	public void setRemitenteCP(String remitenteCP) {
		this.remitenteCP = remitenteCP;
	}

	public String getRemitenteNombre() {
		return remitenteNombre;
	}

	public void setRemitenteNombre(String remitenteNombre) {
		this.remitenteNombre = remitenteNombre;
	}

	public String getRemitenteEstado() {
		return remitenteEstado;
	}

	public void setRemitenteEstado(String remitenteEstado) {
		this.remitenteEstado = remitenteEstado;
	}

	public String getRemitenteMunicipio() {
		return remitenteMunicipio;
	}

	public void setRemitenteMunicipio(String remitenteMunicipio) {
		this.remitenteMunicipio = remitenteMunicipio;
	}

	public String getRemitenteReferencia() {
		return remitenteReferencia;
	}

	public void setRemitenteReferencia(String remitenteReferencia) {
		this.remitenteReferencia = remitenteReferencia;
	}

	public String getRemitenteTelefono() {
		return remitenteTelefono;
	}

	public void setRemitenteTelefono(String remitenteTelefono) {
		this.remitenteTelefono = remitenteTelefono;
	}

	public String getDestinatarioCalle() {
		return destinatarioCalle;
	}

	public void setDestinatarioCalle(String destinatarioCalle) {
		this.destinatarioCalle = destinatarioCalle;
	}

	public String getDestinatarioColonia() {
		return destinatarioColonia;
	}

	public void setDestinatarioColonia(String destinatarioColonia) {
		this.destinatarioColonia = destinatarioColonia;
	}

	public String getDestinatarioCP() {
		return destinatarioCP;
	}

	public void setDestinatarioCP(String destinatarioCP) {
		this.destinatarioCP = destinatarioCP;
	}

	public String getDestinatarioNombre() {
		return destinatarioNombre;
	}

	public void setDestinatarioNombre(String destinatarioNombre) {
		this.destinatarioNombre = destinatarioNombre;
	}

	public String getDestinatarioEstado() {
		return destinatarioEstado;
	}

	public void setDestinatarioEstado(String destinatarioEstado) {
		this.destinatarioEstado = destinatarioEstado;
	}

	public String getDestinatarioMunicipio() {
		return destinatarioMunicipio;
	}

	public void setDestinatarioMunicipio(String destinatarioMunicipio) {
		this.destinatarioMunicipio = destinatarioMunicipio;
	}

	public String getDestinatarioReferencia() {
		return destinatarioReferencia;
	}

	public void setDestinatarioReferencia(String destinatarioReferencia) {
		this.destinatarioReferencia = destinatarioReferencia;
	}

	public String getDestinatarioTelefono() {
		return destinatarioTelefono;
	}

	public void setDestinatarioTelefono(String destinatarioTelefono) {
		this.destinatarioTelefono = destinatarioTelefono;
	}

	public String getInformacionAdicional() {
		return informacionAdicional;
	}

	public void setInformacionAdicional(String informacionAdicional) {
		this.informacionAdicional = informacionAdicional;
	}

	public String getTipoGuia() {
		return tipoGuia;
	}

	public void setTipoGuia(String tipoGuia) {
		this.tipoGuia = tipoGuia;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public String getEtiquetas() {
		return etiquetas;
	}

	public void setEtiquetas(String etiquetas) {
		this.etiquetas = etiquetas;
	}

	public String getTipoContenido() {
		return tipoContenido;
	}

	public void setTipoContenido(String tipoContenido) {
		this.tipoContenido = tipoContenido;
	}

	public String getKilos() {
		return kilos;
	}

	public void setKilos(String kilos) {
		this.kilos = kilos;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getTracking() {
		return tracking;
	}

	public void setTracking(String tracking) {
		this.tracking = tracking;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getRemitenteNumeroExterior() {
		return remitenteNumeroExterior;
	}

	public void setRemitenteNumeroExterior(String remitenteNumeroExterior) {
		this.remitenteNumeroExterior = remitenteNumeroExterior;
	}

	public String getDestinatarioNumeroExterior() {
		return destinatarioNumeroExterior;
	}

	public void setDestinatarioNumeroExterior(String destinatarioNumeroExterior) {
		this.destinatarioNumeroExterior = destinatarioNumeroExterior;
	}

	public String getRemitenteNumeroInterior() {
		return remitenteNumeroInterior;
	}

	public void setRemitenteNumeroInterior(String remitenteNumeroInterior) {
		this.remitenteNumeroInterior = remitenteNumeroInterior;
	}

	public String getDestinatarioNumeroInterior() {
		return destinatarioNumeroInterior;
	}

	public void setDestinatarioNumeroInterior(String destinatarioNumeroInterior) {
		this.destinatarioNumeroInterior = destinatarioNumeroInterior;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getLargo() {
		return largo;
	}

	public String getAlto() {
		return alto;
	}

	public String getFondo() {
		return fondo;
	}

	public String getPesoVolumetrico() {
		return pesoVolumetrico;
	}

	public void setLargo(String largo) {
		this.largo = largo;
	}

	public void setAlto(String alto) {
		this.alto = alto;
	}

	public void setFondo(String fondo) {
		this.fondo = fondo;
	}

	public void setPesoVolumetrico(String pesoVolumetrico) {
		this.pesoVolumetrico = pesoVolumetrico;
	}

	public String getReexpedicion() {
		return reexpedicion;
	}

	public void setReexpedicion(String reexpedicion) {
		this.reexpedicion = reexpedicion;
	}

	public String getAlias() {
		return alias;
	}

	public String getCuenta() {
		return cuenta;
	}

	public String getServicio() {
		return servicio;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getPesoNeto() {
		return pesoNeto;
	}

	public void setPesoNeto(String pesoNeto) {
		this.pesoNeto = pesoNeto;
	}
}