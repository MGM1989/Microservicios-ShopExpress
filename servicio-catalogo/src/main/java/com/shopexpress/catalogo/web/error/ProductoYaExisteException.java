package com.shopexpress.catalogo.web.error;

public class ProductoYaExisteException extends RuntimeException {

	private final String productoId;

	public ProductoYaExisteException(String productoId) {
		super("Ya existe un producto con id " + productoId);
		this.productoId = productoId;
	}

	public String getProductoId() {
		return productoId;
	}
}
