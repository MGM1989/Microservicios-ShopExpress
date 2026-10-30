package com.shopexpress.catalogo.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.shopexpress.catalogo.service.ProductoService;
import com.shopexpress.catalogo.web.dto.CrearProductoRequest;
import com.shopexpress.catalogo.web.dto.MovimientoStockRequest;
import com.shopexpress.catalogo.web.dto.ProductoResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Catálogo e inventario. El descuento de stock es condicional (no sobreventa).")
public class ProductoController {

	private final ProductoService productoService;

	public ProductoController(ProductoService productoService) {
		this.productoService = productoService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Crea un producto")
	public ProductoResponse crear(@Valid @RequestBody CrearProductoRequest request) {
		return productoService.crear(request);
	}

	@GetMapping("/{productoId}")
	@Operation(summary = "Recupera un producto y su stock")
	public ProductoResponse obtener(@PathVariable String productoId) {
		return productoService.obtener(productoId);
	}

	@PostMapping("/{productoId}/descontar-stock")
	@Operation(summary = "Descuenta stock (paso SAGA)", description = "Falla con 409 si stock < cantidad. Update condicional en DynamoDB.")
	public ProductoResponse descontar(
			@PathVariable String productoId,
			@Valid @RequestBody MovimientoStockRequest request) {
		return productoService.descontarStock(productoId, request.getCantidad());
	}

	@PostMapping("/{productoId}/restaurar-stock")
	@Operation(summary = "Restaura stock (compensación SAGA)")
	public ProductoResponse restaurar(
			@PathVariable String productoId,
			@Valid @RequestBody MovimientoStockRequest request) {
		return productoService.restaurarStock(productoId, request.getCantidad());
	}
}
