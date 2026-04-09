package com.integrador.models;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="usuarios")
public class Usuario {

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

	@Column(name="cuenta_estafeta")
	String cuentaEstafeta;

	@Column(name="cuenta_estafeta_express")
	String cuentaEstafetaExpress;

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

//	public Grupo getGrupo() {
//		return grupo;
//	}
//
//	public void setGrupo(Grupo grupo) {
//		this.grupo = grupo;
//	}

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
	
	@Override
	public String toString() {
		return "Usuario [idUsuario=" + idUsuario + ", idUsuarioPadre=" + idUsuarioPadre + ", usuario=" + usuario
				+ ", contrasena=" + contrasena + ", activo=" + activo + ", nombre=" + nombre + ", direccion="
				+ direccion + ", telefono=" + telefono + ", contacto=" + contacto + ", vendedor=" + vendedor
				+ ", correo=" + correo + ", rfc=" + rfc + ", tipoPago=" + tipoPago + ", limite=" + limite
				+ ", precioGuiaExpress=" + precioGuiaExpress + ", precioGuiaTerrestre=" + precioGuiaTerrestre
				+ ", precioKilosExpress=" + precioKilosExpress + ", precioKilosTerrestre=" + precioKilosTerrestre
				+ ", precioReexpedicion=" + precioReexpedicion + ", fechaCreacion=" + fechaCreacion
				+ ", fechaActualizacion=" + fechaActualizacion + ", creadoPor=" + creadoPor + ", actualizadoPor="
				+ actualizadoPor + "]";
	}

	public Integer getActivoWeb() {
		return activoWeb;
	}

	public void setActivoWeb(Integer activoWeb) {
		this.activoWeb = activoWeb;
	}

	public String getCuentaEstafeta() {
		return cuentaEstafeta;
	}

	public void setCuentaEstafeta(String cuentaEstafeta) {
		this.cuentaEstafeta = cuentaEstafeta;
	}

	public String getCuentaEstafetaExpress() {
		return cuentaEstafetaExpress;
	}

	public void setCuentaEstafetaExpress(String cuentaEstafetaExpress) {
		this.cuentaEstafetaExpress = cuentaEstafetaExpress;
	}
	
}
