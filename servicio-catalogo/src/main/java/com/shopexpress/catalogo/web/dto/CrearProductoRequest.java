package com.shopexpress.catalogo.web.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Alta de producto. Si no se envía producto_id, se genera prod_....")
public class CrearProductoRequest {

	@JsonProperty("producto_id")
	@Schema(example = "prod_001")
	private String productoId;

	@NotBlank
	@Schema(example = "Auriculares inalámbricos")
	private String nombre;

	@NotBlank
	@Schema(example = "Audio")
	private String categoria;

	@NotNull
	@DecimalMin("0.0")
	@Schema(example = "25.50")
	private BigDecimal precio;

	@NotNull
	@Min(0)
	@Schema(example = "2")
	private Integer stock;

	public String getProductoId() {
		return productoId;
	}

	public void setProductoId(String productoId) {
		this.productoId = productoId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}
}
