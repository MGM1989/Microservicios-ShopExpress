package com.shopexpress.catalogo.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Cantidad a descontar o restaurar en el SAGA")
public class MovimientoStockRequest {

	@NotNull
	@Min(1)
	@Schema(example = "2")
	private Integer cantidad;

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
}
