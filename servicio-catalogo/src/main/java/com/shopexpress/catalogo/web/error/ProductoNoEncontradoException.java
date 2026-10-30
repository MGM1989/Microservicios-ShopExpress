package com.shopexpress.catalogo.web.error;

public class ProductoNoEncontradoException extends RuntimeException {

	private final String productoId;

	public ProductoNoEncontradoException(String productoId) {
		super("Producto no encontrado: " + productoId);
		this.productoId = productoId;
	}

	public String getProductoId() {
		return productoId;
	}
}
