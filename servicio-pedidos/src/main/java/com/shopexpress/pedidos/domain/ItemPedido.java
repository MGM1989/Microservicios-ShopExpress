package com.shopexpress.pedidos.domain;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.mapping.Field;

public class ItemPedido {

	@Field("producto_id")
	private String productoId;
	private String nombre;
	private int cantidad;
	@Field("precio_unitario")
	private BigDecimal precioUnitario;

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

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
}
