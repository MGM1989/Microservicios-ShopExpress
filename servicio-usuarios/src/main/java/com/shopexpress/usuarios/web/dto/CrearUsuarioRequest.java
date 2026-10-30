package com.shopexpress.usuarios.web.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Alta de usuario. Si no se envía id, se genera usr_....")
public class CrearUsuarioRequest {

	@Schema(example = "usr_12345", description = "Opcional. Si se omite, el servicio genera usr_xxxxxxxx")
	private String id;

	@NotBlank
	@JsonProperty("tipo_documento")
	@Schema(example = "DNI")
	private String tipoDocumento;

	@NotBlank
	@JsonProperty("nro_documento")
	@Schema(example = "12345678")
	private String nroDocumento;

	@NotBlank
	@Schema(example = "Juan")
	private String nombre;

	@NotBlank
	@Schema(example = "Pérez")
	private String apellido;

	@NotBlank
	@Email
	@Schema(example = "juan.perez@example.com")
	private String email;

	@Valid
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
