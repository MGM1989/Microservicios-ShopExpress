package com.shopexpress.pedidos.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopexpress.pedidos.saga.SagaResult;
import com.shopexpress.pedidos.service.PedidoService;
import com.shopexpress.pedidos.web.dto.CrearPedidoRequest;
import com.shopexpress.pedidos.web.dto.PedidoResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Orquestador SAGA + Chain of Responsibility. El pedido se embebe (items[] y pago) en MongoDB.")
public class PedidoController {

	private final PedidoService pedidoService;

	public PedidoController(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	@PostMapping
	@Operation(
			summary = "Crea un pedido (SAGA + CoR)",
			description = "Persiste PROCESANDO, recorre ValidadorDeDatos → Usuario → Stock → ReservarStock → Pago → Confirmar. "
					+ "Si el pago se fuerza a fallar, restaura stock y deja COMPENSADO. GET posterior muestra saga_log.")
	public ResponseEntity<PedidoResponse> crear(@Valid @RequestBody CrearPedidoRequest request) {
		SagaResult result = pedidoService.crear(request);
		return ResponseEntity.status(result.status()).body(PedidoMapper.toResponse(result.pedido()));
	}

	@GetMapping("/{pedidoId}")
	@Operation(summary = "Recupera un pedido", description = "Sirve para ver COMPLETADO / COMPENSADO y el saga_log.")
	public PedidoResponse obtener(@PathVariable String pedidoId) {
		return PedidoMapper.toResponse(pedidoService.obtener(pedidoId));
	}
}
