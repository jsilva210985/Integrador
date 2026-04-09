package com.integrador.models;

/*
 * ==============================================================================================
 * Copyright (c) 2022-2023 J. Jose de Jesus Silva A.
 * Contact: jsilva210985@gmail.com
 * Since: 2023
 * 
 * All rights reserved.
 * This file is part of the GeneradoresService project, which is released under the MIT License.
 * See https://opensource.org/licenses/MIT for more information.
 * ==============================================================================================
 */


import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="usuarios")
public class UsuarioAlan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_usuario")
	Integer idUsuario;

	@Column(name="id_usuario_padre")
	Integer idUsuarioPadre;

	@Column(name="usuario")
	String usuario;

	@Column(name="contrasena")
	String contrasena;

	@Column(name="activo")
	Integer activo;

	@Column(name="nombre")
	String nombre;

	@Column(name="direccion")
	String direccion;

	@Column(name="telefono")
	String telefono;

	@Column(name="contacto")
	String contacto;

	@Column(name="vendedor")
	String vendedor;

	@Column(name="correo")
	String correo;

	@Column(name="rfc")
	String rfc;

	@Column(name="tipo_pago")
	String tipoPago;

	@Column(name="limite")
	BigDecimal limite;

	@Column(name="precio_guia_express")
	BigDecimal precioGuiaExpress;

	@Column(name="precio_guia_terrestre")
	BigDecimal precioGuiaTerrestre;

	@Column(name="precio_kilos_express")
	BigDecimal precioKilosExpress;

	@Column(name="precio_kilos_terrestre")
	BigDecimal precioKilosTerrestre;

	@Column(name="precio_reexpedicion")
	BigDecimal precioReexpedicion;

	@Column(name="fecha_creacion")
	String fechaCreacion;

	@Column(name="fecha_actualizacion")
	String fechaActualizacion;

	@Column(name="creado_por")
	int creadoPor;

	@Column(name="actualizado_por")
	int actualizadoPor;

	@Column(name="activo_web")
	Integer activoWeb;

	@Column(name="permiso_ver_precios")
	Integer permisoVerPrecios;

	@Column(name="comision_guia_terrestre")
	BigDecimal comisionGuiaTerrestre;

	@Column(name="comision_kilos_terrestre")
	BigDecimal comisionKilosTerrestre;

	@Column(name="comision_guia_express")
	BigDecimal comisionGuiaExpress;

	@Column(name="comision_kilos_express")
	BigDecimal comisionKilosExpress;

	@Column(name="comision_reexpedicion")
	BigDecimal comisionReexpedicion;

	@Column(name="costo_cancelacion")
	BigDecimal costoCancelacion;

	@Column(name="comision_cancelacion")
	BigDecimal comisionCancelacion;

	@Column(name="permiso_estafeta_terrestre")
	int permisoEstafetaTerrestre;

	@Column(name="permiso_estafeta_express")
	int permisoEstafetaExpress;

	@Column(name="precio_reexpedicion_terrestre")
	BigDecimal precioReexpedicionTerrestre;

	@Column(name="comision_reexpedicion_terrestre")
	BigDecimal comisionReexpedicionTerrestre;

	@Column(name="precio_reexpedicion_express")
	BigDecimal precioReexpedicionExpress;

	@Column(name="comision_reexpedicion_express")
	BigDecimal comisionReexpedicionExpress;

	@Column(name="tipo_cobro_terrestre")
	String tipoCobroTerrestre;

	@Column(name="tipo_cobro_express")
	String tipoCobroExpress;

	@Column(name="contenido")
	String contenido;

	@Column(name="cuentas_estafeta")
	String cuentasEstafeta;

	@Column(name="permisos_rango")
	String permisosRango;

	@Column(name="precio_guia_express_fedex")
	BigDecimal precioGuiaExpressFedex;

	@Column(name="precio_guia_terrestre_fedex")
	BigDecimal precioGuiaTerrestreFedex;

	@Column(name="precio_kilos_express_fedex")
	BigDecimal precioKilosExpressFedex;

	@Column(name="precio_kilos_terrestre_fedex")
	BigDecimal precioKilosTerrestreFedex;

	@Column(name="precio_reexpedicion_fedex")
	BigDecimal precioReexpedicionFedex;

	@Column(name="comision_guia_terrestre_fedex")
	BigDecimal comisionGuiaTerrestreFedex;

	@Column(name="comision_kilos_terrestre_fedex")
	BigDecimal comisionKilosTerrestreFedex;

	@Column(name="comision_guia_express_fedex")
	BigDecimal comisionGuiaExpressFedex;

	@Column(name="comision_kilos_express_fedex")
	BigDecimal comisionKilosExpressFedex;

	@Column(name="comision_reexpedicion_fedex")
	BigDecimal comisionReexpedicionFedex;

	@Column(name="costo_cancelacion_fedex")
	BigDecimal costoCancelacionFedex;

	@Column(name="comision_cancelacion_fedex")
	BigDecimal comisionCancelacionFedex;

	@Column(name="precio_reexpedicion_terrestre_fedex")
	BigDecimal precioReexpedicionTerrestreFedex;

	@Column(name="comision_reexpedicion_terrestre_fedex")
	BigDecimal comisionReexpedicionTerrestreFedex;

	@Column(name="precio_reexpedicion_express_fedex")
	BigDecimal precioReexpedicionExpressFedex;

	@Column(name="comision_reexpedicion_express_fedex")
	BigDecimal comisionReexpedicionExpressFedex;
	
	@Column(name="permiso_terrestre_fedex")
	int permisoTerrestreFedex;

	@Column(name="permiso_express_fedex")
	int permisoExpressFedex;

	@Column(name="tipo_cobro_terrestre_fedex")
	String tipoCobroTerrestreFedex;

	@Column(name="tipo_cobro_express_fedex")
	String tipoCobroExpressFedex;

	@Column(name="cuenta_fedex")
	String cuentaFedex;

	@Column(name="cuenta_express_fedex")
	String cuentaExpressFedex;

	@Column(name="cuentas_fedex")
	String cuentasFedex;

	@Column(name="permisos_rango_fedex")
	String permisosRangoFedex;

	@Column(name="permiso_crear_recoleccion_fedex")
	int permisoCrearRecoleccionFedex;

	@Column(name="manejo_especial_estafeta")
	int manejoEspecialEstafeta;

	@Column(name="costo_manejo_especial_estafeta")
	BigDecimal costoManejoEspecialEstafeta;

	@Column(name="manejo_especial_fedex")
	int manejoEspecialFedex;

	@Column(name="costo_manejo_especial_fedex")
	BigDecimal costoManejoEspecialFedex;

	@Column(name="generador_reportes")
	String generadorReportes;

	@Column(name="permiso_crear_rfc_fedex")
	int permisoCrearRFCFedex;

	@Column(name="permiso_crear_rfc_estafeta")
	int permisoCrearRFCEstafeta;

	@Column(name="permiso_multiple_estafeta")
	int permisoMultipleEstafeta;

	@Column(name="permiso_multiple_fedex")
	int permisoMultipleFedex;

	@Column(name="fedex_lista_precio_terrestre")
	int fedexListaPrecioTerrestre;

	@Column(name="fedex_lista_precio_express")
	int fedexListaPrecioExpress;

	@Column(name = "estafeta_cuentas_v2")
	Integer estafetaCuentaV2;

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public Integer getActivo() {
		return activo;
	}

	public void setActivo(Integer activo) {
		this.activo = activo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}

	public BigDecimal getLimite() {
		return limite;
	}

	public void setLimite(BigDecimal limite) {
		this.limite = limite;
	}

	public BigDecimal getPrecioGuiaExpress() {
		return precioGuiaExpress;
	}

	public void setPrecioGuiaExpress(BigDecimal precioGuiaExpress) {
		this.precioGuiaExpress = precioGuiaExpress;
	}

	public BigDecimal getPrecioGuiaTerrestre() {
		return precioGuiaTerrestre;
	}

	public void setPrecioGuiaTerrestre(BigDecimal precioGuiaTerrestre) {
		this.precioGuiaTerrestre = precioGuiaTerrestre;
	}

	public BigDecimal getPrecioKilosExpress() {
		return precioKilosExpress;
	}

	public void setPrecioKilosExpress(BigDecimal precioKilosExpress) {
		this.precioKilosExpress = precioKilosExpress;
	}

	public BigDecimal getPrecioKilosTerrestre() {
		return precioKilosTerrestre;
	}

	public void setPrecioKilosTerrestre(BigDecimal precioKilosTerrestre) {
		this.precioKilosTerrestre = precioKilosTerrestre;
	}

	public BigDecimal getPrecioReexpedicion() {
		return precioReexpedicion;
	}

	public void setPrecioReexpedicion(BigDecimal precioReexpedicion) {
		this.precioReexpedicion = precioReexpedicion;
	}

	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Integer getIdUsuarioPadre() {
		return idUsuarioPadre;
	}

	public void setIdUsuarioPadre(Integer idUsuarioPadre) {
		this.idUsuarioPadre = idUsuarioPadre;
	}

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(String fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public int getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(int creadoPor) {
		this.creadoPor = creadoPor;
	}

	public int getActualizadoPor() {
		return actualizadoPor;
	}

	public void setActualizadoPor(int actualizadoPor) {
		this.actualizadoPor = actualizadoPor;
	}

	public boolean esPadre() {
		if(this.getIdUsuarioPadre()==null) {
			return true;
		}else {
			return false;
		}
	}

	public Integer getActivoWeb() {
		return activoWeb;
	}

	public void setActivoWeb(Integer activoWeb) {
		this.activoWeb = activoWeb;
	}

	public BigDecimal getComisionGuiaTerrestre() {
		return comisionGuiaTerrestre;
	}

	public void setComisionGuiaTerrestre(BigDecimal comisionGuiaTerrestre) {
		this.comisionGuiaTerrestre = comisionGuiaTerrestre;
	}

	public BigDecimal getComisionKilosTerrestre() {
		return comisionKilosTerrestre;
	}

	public void setComisionKilosTerrestre(BigDecimal comisionKilosTerrestre) {
		this.comisionKilosTerrestre = comisionKilosTerrestre;
	}

	public BigDecimal getComisionGuiaExpress() {
		return comisionGuiaExpress;
	}

	public void setComisionGuiaExpress(BigDecimal comisionGuiaExpress) {
		this.comisionGuiaExpress = comisionGuiaExpress;
	}

	public BigDecimal getComisionKilosExpress() {
		return comisionKilosExpress;
	}

	public void setComisionKilosExpress(BigDecimal comisionKilosExpress) {
		this.comisionKilosExpress = comisionKilosExpress;
	}

	public BigDecimal getComisionReexpedicion() {
		return comisionReexpedicion;
	}

	public void setComisionReexpedicion(BigDecimal comisionReexpedicion) {
		this.comisionReexpedicion = comisionReexpedicion;
	}

	public BigDecimal getCostoCancelacion() {
		return costoCancelacion;
	}

	public void setCostoCancelacion(BigDecimal costoCancelacion) {
		this.costoCancelacion = costoCancelacion;
	}

	public BigDecimal getComisionCancelacion() {
		return comisionCancelacion;
	}

	public void setComisionCancelacion(BigDecimal comisionCancelacion) {
		this.comisionCancelacion = comisionCancelacion;
	}

	public int getPermisoEstafetaTerrestre() {
		return permisoEstafetaTerrestre;
	}

	public void setPermisoEstafetaTerrestre(int permisoEstafetaTerrestre) {
		this.permisoEstafetaTerrestre = permisoEstafetaTerrestre;
	}

	public int getPermisoEstafetaExpress() {
		return permisoEstafetaExpress;
	}

	public void setPermisoEstafetaExpress(int permisoEstafetaExpress) {
		this.permisoEstafetaExpress = permisoEstafetaExpress;
	}

	public BigDecimal getPrecioReexpedicionTerrestre() {
		return precioReexpedicionTerrestre;
	}

	public void setPrecioReexpedicionTerrestre(BigDecimal precioReexpedicionTerrestre) {
		this.precioReexpedicionTerrestre = precioReexpedicionTerrestre;
	}

	public BigDecimal getComisionReexpedicionTerrestre() {
		return comisionReexpedicionTerrestre;
	}

	public void setComisionReexpedicionTerrestre(BigDecimal comisionReexpedicionTerrestre) {
		this.comisionReexpedicionTerrestre = comisionReexpedicionTerrestre;
	}

	public BigDecimal getPrecioReexpedicionExpress() {
		return precioReexpedicionExpress;
	}

	public void setPrecioReexpedicionExpress(BigDecimal precioReexpedicionExpress) {
		this.precioReexpedicionExpress = precioReexpedicionExpress;
	}

	public BigDecimal getComisionReexpedicionExpress() {
		return comisionReexpedicionExpress;
	}

	public void setComisionReexpedicionExpress(BigDecimal comisionReexpedicionExpress) {
		this.comisionReexpedicionExpress = comisionReexpedicionExpress;
	}

	public String getTipoCobroTerrestre() {
		return tipoCobroTerrestre;
	}

	public void setTipoCobroTerrestre(String tipoCobroTerrestre) {
		this.tipoCobroTerrestre = tipoCobroTerrestre;
	}

	public String getTipoCobroExpress() {
		return tipoCobroExpress;
	}

	public void setTipoCobroExpress(String tipoCobroExpress) {
		this.tipoCobroExpress = tipoCobroExpress;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public String getCuentasEstafeta() {
		return cuentasEstafeta;
	}

	public void setCuentasEstafeta(String cuentasEstafeta) {
		this.cuentasEstafeta = cuentasEstafeta;
	}

	public String getPermisosRango() {
		return permisosRango;
	}

	public void setPermisosRango(String permisosRango) {
		this.permisosRango = permisosRango;
	}

	public BigDecimal getPrecioGuiaExpressFedex() {
		return precioGuiaExpressFedex;
	}

	public void setPrecioGuiaExpressFedex(BigDecimal precioGuiaExpressFedex) {
		this.precioGuiaExpressFedex = precioGuiaExpressFedex;
	}

	public BigDecimal getPrecioGuiaTerrestreFedex() {
		return precioGuiaTerrestreFedex;
	}

	public void setPrecioGuiaTerrestreFedex(BigDecimal precioGuiaTerrestreFedex) {
		this.precioGuiaTerrestreFedex = precioGuiaTerrestreFedex;
	}

	public BigDecimal getPrecioKilosExpressFedex() {
		return precioKilosExpressFedex;
	}

	public void setPrecioKilosExpressFedex(BigDecimal precioKilosExpressFedex) {
		this.precioKilosExpressFedex = precioKilosExpressFedex;
	}

	public BigDecimal getPrecioKilosTerrestreFedex() {
		return precioKilosTerrestreFedex;
	}

	public void setPrecioKilosTerrestreFedex(BigDecimal precioKilosTerrestreFedex) {
		this.precioKilosTerrestreFedex = precioKilosTerrestreFedex;
	}

	public BigDecimal getPrecioReexpedicionFedex() {
		return precioReexpedicionFedex;
	}

	public void setPrecioReexpedicionFedex(BigDecimal precioReexpedicionFedex) {
		this.precioReexpedicionFedex = precioReexpedicionFedex;
	}

	public BigDecimal getComisionGuiaTerrestreFedex() {
		return comisionGuiaTerrestreFedex;
	}

	public void setComisionGuiaTerrestreFedex(BigDecimal comisionGuiaTerrestreFedex) {
		this.comisionGuiaTerrestreFedex = comisionGuiaTerrestreFedex;
	}

	public BigDecimal getComisionKilosTerrestreFedex() {
		return comisionKilosTerrestreFedex;
	}

	public void setComisionKilosTerrestreFedex(BigDecimal comisionKilosTerrestreFedex) {
		this.comisionKilosTerrestreFedex = comisionKilosTerrestreFedex;
	}

	public BigDecimal getComisionGuiaExpressFedex() {
		return comisionGuiaExpressFedex;
	}

	public void setComisionGuiaExpressFedex(BigDecimal comisionGuiaExpressFedex) {
		this.comisionGuiaExpressFedex = comisionGuiaExpressFedex;
	}

	public BigDecimal getComisionKilosExpressFedex() {
		return comisionKilosExpressFedex;
	}

	public void setComisionKilosExpressFedex(BigDecimal comisionKilosExpressFedex) {
		this.comisionKilosExpressFedex = comisionKilosExpressFedex;
	}

	public BigDecimal getComisionReexpedicionFedex() {
		return comisionReexpedicionFedex;
	}

	public void setComisionReexpedicionFedex(BigDecimal comisionReexpedicionFedex) {
		this.comisionReexpedicionFedex = comisionReexpedicionFedex;
	}

	public BigDecimal getCostoCancelacionFedex() {
		return costoCancelacionFedex;
	}

	public void setCostoCancelacionFedex(BigDecimal costoCancelacionFedex) {
		this.costoCancelacionFedex = costoCancelacionFedex;
	}

	public BigDecimal getComisionCancelacionFedex() {
		return comisionCancelacionFedex;
	}

	public void setComisionCancelacionFedex(BigDecimal comisionCancelacionFedex) {
		this.comisionCancelacionFedex = comisionCancelacionFedex;
	}

	public BigDecimal getPrecioReexpedicionTerrestreFedex() {
		return precioReexpedicionTerrestreFedex;
	}

	public void setPrecioReexpedicionTerrestreFedex(BigDecimal precioReexpedicionTerrestreFedex) {
		this.precioReexpedicionTerrestreFedex = precioReexpedicionTerrestreFedex;
	}

	public BigDecimal getComisionReexpedicionTerrestreFedex() {
		return comisionReexpedicionTerrestreFedex;
	}

	public void setComisionReexpedicionTerrestreFedex(BigDecimal comisionReexpedicionTerrestreFedex) {
		this.comisionReexpedicionTerrestreFedex = comisionReexpedicionTerrestreFedex;
	}

	public BigDecimal getPrecioReexpedicionExpressFedex() {
		return precioReexpedicionExpressFedex;
	}

	public void setPrecioReexpedicionExpressFedex(BigDecimal precioReexpedicionExpressFedex) {
		this.precioReexpedicionExpressFedex = precioReexpedicionExpressFedex;
	}

	public BigDecimal getComisionReexpedicionExpressFedex() {
		return comisionReexpedicionExpressFedex;
	}

	public void setComisionReexpedicionExpressFedex(BigDecimal comisionReexpedicionExpressFedex) {
		this.comisionReexpedicionExpressFedex = comisionReexpedicionExpressFedex;
	}

	public int getPermisoTerrestreFedex() {
		return permisoTerrestreFedex;
	}

	public void setPermisoTerrestreFedex(int permisoTerrestreFedex) {
		this.permisoTerrestreFedex = permisoTerrestreFedex;
	}

	public int getPermisoExpressFedex() {
		return permisoExpressFedex;
	}

	public void setPermisoExpressFedex(int permisoExpressFedex) {
		this.permisoExpressFedex = permisoExpressFedex;
	}

	public String getTipoCobroTerrestreFedex() {
		return tipoCobroTerrestreFedex;
	}

	public void setTipoCobroTerrestreFedex(String tipoCobroTerrestreFedex) {
		this.tipoCobroTerrestreFedex = tipoCobroTerrestreFedex;
	}

	public String getTipoCobroExpressFedex() {
		return tipoCobroExpressFedex;
	}

	public void setTipoCobroExpressFedex(String tipoCobroExpressFedex) {
		this.tipoCobroExpressFedex = tipoCobroExpressFedex;
	}

	public String getCuentaEstafetaFedex() {
		return cuentaFedex;
	}

	public void setCuentaFedex(String cuentaFedex) {
		this.cuentaFedex = cuentaFedex;
	}

	public String getCuentaEstafetaExpressFedex() {
		return cuentaExpressFedex;
	}

	public void setCuentaExpressFedex(String cuentaExpressFedex) {
		this.cuentaExpressFedex = cuentaExpressFedex;
	}

	public String getCuentasFedex() {
		return cuentasFedex;
	}

	public void setCuentasFedex(String cuentasFedex) {
		this.cuentasFedex = cuentasFedex;
	}

	public String getPermisosRangoFedex() {
		return permisosRangoFedex;
	}

	public void setPermisosRangoFedex(String permisosRangoFedex) {
		this.permisosRangoFedex = permisosRangoFedex;
	}

	public Integer getPermisoVerPrecios() {
		return permisoVerPrecios;
	}

	public void setPermisoVerPrecios(Integer permisoVerPrecios) {
		this.permisoVerPrecios = permisoVerPrecios;
	}

	public int getPermisoCrearRecoleccionFedex() {
		return permisoCrearRecoleccionFedex;
	}

	public void setPermisoCrearRecoleccionFedex(int permisoCrearRecoleccionFedex) {
		this.permisoCrearRecoleccionFedex = permisoCrearRecoleccionFedex;
	}

	public int getManejoEspecialEstafeta() {
		return manejoEspecialEstafeta;
	}

	public void setManejoEspecialEstafeta(int manejoEspecialEstafeta) {
		this.manejoEspecialEstafeta = manejoEspecialEstafeta;
	}

	public BigDecimal getCostoManejoEspecialEstafeta() {
		return costoManejoEspecialEstafeta;
	}

	public void setCostoManejoEspecialEstafeta(BigDecimal costoManejoEspecialEstafeta) {
		this.costoManejoEspecialEstafeta = costoManejoEspecialEstafeta;
	}

	public String getGeneradorReportes() {
		return generadorReportes;
	}

	public void setGeneradorReportes(String generadorReportes) {
		this.generadorReportes = generadorReportes;
	}

	public int getManejoEspecialFedex() {
		return manejoEspecialFedex;
	}

	public BigDecimal getCostoManejoEspecialFedex() {
		return costoManejoEspecialFedex;
	}

	public void setManejoEspecialFedex(int manejoEspecialFedex) {
		this.manejoEspecialFedex = manejoEspecialFedex;
	}

	public void setCostoManejoEspecialFedex(BigDecimal costoManejoEspecialFedex) {
		this.costoManejoEspecialFedex = costoManejoEspecialFedex;
	}

	public int getPermisoCrearRFCFedex() {
		return permisoCrearRFCFedex;
	}

	public int getPermisoCrearRFCEstafeta() {
		return permisoCrearRFCEstafeta;
	}

	public void setPermisoCrearRFCFedex(int permisoCrearRFCFedex) {
		this.permisoCrearRFCFedex = permisoCrearRFCFedex;
	}

	public void setPermisoCrearRFCEstafeta(int permisoCrearRFCEstafeta) {
		this.permisoCrearRFCEstafeta = permisoCrearRFCEstafeta;
	}

	public int getPermisoMultipleEstafeta() {
		return permisoMultipleEstafeta;
	}

	public int getPermisoMultipleFedex() {
		return permisoMultipleFedex;
	}

	public void setPermisoMultipleEstafeta(int permisoMultipleEstafeta) {
		this.permisoMultipleEstafeta = permisoMultipleEstafeta;
	}

	public void setPermisoMultipleFedex(int permisoMultipleFedex) {
		this.permisoMultipleFedex = permisoMultipleFedex;
	}

	public int getFedexListaPrecioTerrestre() {
		return fedexListaPrecioTerrestre;
	}

	public int getFedexListaPrecioExpress() {
		return fedexListaPrecioExpress;
	}

	public void setFedexListaPrecioTerrestre(int fedexListaPrecioTerrestre) {
		this.fedexListaPrecioTerrestre = fedexListaPrecioTerrestre;
	}

	public void setFedexListaPrecioExpress(int fedexListaPrecioExpress) {
		this.fedexListaPrecioExpress = fedexListaPrecioExpress;
	}

	public Integer getEstafetaCuentaV2() {
		return estafetaCuentaV2;
	}

	public void setEstafetaCuentaV2(Integer estafetaCuentaV2) {
		this.estafetaCuentaV2 = estafetaCuentaV2;
	}

}
