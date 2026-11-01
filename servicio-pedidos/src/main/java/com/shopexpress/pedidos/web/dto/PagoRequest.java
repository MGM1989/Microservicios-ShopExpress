package com.shopexpress.pedidos.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class PagoRequest {

	@NotBlank
	@Schema(example = "TARJETA")
	private String metodo;

	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}
}
