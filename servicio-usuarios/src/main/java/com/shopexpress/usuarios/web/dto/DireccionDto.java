package com.shopexpress.usuarios.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dirección de envío embebida en el usuario")
public class DireccionDto {

	@NotBlank
	@Schema(example = "Av. Principal 123")
	private String calle;

	@NotBlank
	@Schema(example = "Montevideo")
	private String ciudad;

	@NotBlank
	@JsonProperty("codigo_postal")
	@Schema(example = "11000")
	private String codigoPostal;

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
}
