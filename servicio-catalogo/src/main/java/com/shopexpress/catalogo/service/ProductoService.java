package com.shopexpress.catalogo.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shopexpress.catalogo.domain.Producto;
import com.shopexpress.catalogo.repo.ProductoRepository;
import com.shopexpress.catalogo.web.ProductoMapper;
import com.shopexpress.catalogo.web.dto.CrearProductoRequest;
import com.shopexpress.catalogo.web.dto.ProductoResponse;
import com.shopexpress.catalogo.web.error.ProductoNoEncontradoException;
import com.shopexpress.catalogo.web.error.ProductoYaExisteException;

@Service
public class ProductoService {

	private final ProductoRepository productoRepository;

	public ProductoService(ProductoRepository productoRepository) {
		this.productoRepository = productoRepository;
	}

	public ProductoResponse crear(CrearProductoRequest request) {
		String id = StringUtils.hasText(request.getProductoId())
				? request.getProductoId().trim()
				: "prod_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);

		if (productoRepository.findById(id).isPresent()) {
			throw new ProductoYaExisteException(id);
		}

		Producto producto = ProductoMapper.toDomain(request, id);
		productoRepository.save(producto);
		return ProductoMapper.toResponse(producto);
	}

	public ProductoResponse obtener(String productoId) {
		return productoRepository.findById(productoId)
				.map(ProductoMapper::toResponse)
				.orElseThrow(() -> new ProductoNoEncontradoException(productoId));
	}

	public ProductoResponse descontarStock(String productoId, int cantidad) {
		return ProductoMapper.toResponse(productoRepository.descontarStock(productoId, cantidad));
	}

	public ProductoResponse restaurarStock(String productoId, int cantidad) {
		return ProductoMapper.toResponse(productoRepository.restaurarStock(productoId, cantidad));
	}
}
