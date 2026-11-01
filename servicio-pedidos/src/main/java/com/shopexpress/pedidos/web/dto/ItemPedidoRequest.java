package com.shopexpress.pedidos.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ItemPedidoRequest {

	@NotBlank
	@JsonProperty("producto_id")
	@Schema(example = "prod_saga_ok")
	private String productoId;

	@NotNull
	@Min(1)
	@Schema(example = "1")
	private Integer cantidad;

	public String getProductoId() {
		return productoId;
	}

	public void setProductoId(String productoId) {
		this.productoId = productoId;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
}
