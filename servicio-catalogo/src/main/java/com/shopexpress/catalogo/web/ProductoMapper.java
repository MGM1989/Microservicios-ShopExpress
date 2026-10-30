package com.shopexpress.catalogo.web;

import com.shopexpress.catalogo.domain.Producto;
import com.shopexpress.catalogo.web.dto.CrearProductoRequest;
import com.shopexpress.catalogo.web.dto.ProductoResponse;

public final class ProductoMapper {

	private ProductoMapper() {
	}

	public static Producto toDomain(CrearProductoRequest request, String productoId) {
		Producto producto = new Producto();
		producto.setProductoId(productoId);
		producto.setNombre(request.getNombre());
		producto.setCategoria(request.getCategoria());
		producto.setPrecio(request.getPrecio());
		producto.setStock(request.getStock());
		return producto;
	}

	public static ProductoResponse toResponse(Producto producto) {
		ProductoResponse response = new ProductoResponse();
		response.setProductoId(producto.getProductoId());
		response.setNombre(producto.getNombre());
		response.setCategoria(producto.getCategoria());
		response.setPrecio(producto.getPrecio());
		response.setStock(producto.getStock());
		return response;
	}
}
