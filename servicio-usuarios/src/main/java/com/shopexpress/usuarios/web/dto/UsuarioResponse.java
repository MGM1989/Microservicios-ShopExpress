package com.shopexpress.usuarios.web.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsuarioResponse {

	private String id;

	@JsonProperty("tipo_documento")
	private String tipoDocumento;

	@JsonProperty("nro_documento")
	private String nroDocumento;

	private String nombre;
	private String apellido;
	private String email;
	private List<DireccionDto> direcciones = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<DireccionDto> getDirecciones() {
		return direcciones;
	}

	public void setDirecciones(List<DireccionDto> direcciones) {
		this.direcciones = direcciones != null ? direcciones : new ArrayList<>();
	}
}
