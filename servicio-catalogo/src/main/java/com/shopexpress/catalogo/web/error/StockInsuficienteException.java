package com.shopexpress.catalogo.web.error;

public class StockInsuficienteException extends RuntimeException {

	private final String productoId;
	private final int cantidad;

	public StockInsuficienteException(String productoId, int cantidad) {
		super("Stock insuficiente para " + productoId + " (pedido: " + cantidad + ")");
		this.productoId = productoId;
		this.cantidad = cantidad;
	}

	public String getProductoId() {
		return productoId;
	}

	public int getCantidad() {
		return cantidad;
	}
}
