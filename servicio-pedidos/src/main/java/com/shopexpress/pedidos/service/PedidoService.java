package com.shopexpress.pedidos.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.shopexpress.pedidos.cor.PedidoContext;
import com.shopexpress.pedidos.domain.EstadoPago;
import com.shopexpress.pedidos.domain.EstadoPedido;
import com.shopexpress.pedidos.domain.Pago;
import com.shopexpress.pedidos.domain.Pedido;
import com.shopexpress.pedidos.repo.PedidoRepository;
import com.shopexpress.pedidos.saga.SagaOrquestador;
import com.shopexpress.pedidos.saga.SagaResult;
import com.shopexpress.pedidos.web.dto.CrearPedidoRequest;
import com.shopexpress.pedidos.web.error.PedidoNoEncontradoException;

@Service
public class PedidoService {

	private final PedidoRepository pedidoRepository;
	private final SagaOrquestador sagaOrquestador;

	public PedidoService(PedidoRepository pedidoRepository, SagaOrquestador sagaOrquestador) {
		this.pedidoRepository = pedidoRepository;
		this.sagaOrquestador = sagaOrquestador;
	}

	public SagaResult crear(CrearPedidoRequest request) {
		Pedido pedido = nuevo(request);
		return sagaOrquestador.ejecutar(new PedidoContext(request, pedido));
	}

	public Pedido obtener(String pedidoId) {
		return pedidoRepository.findById(pedidoId)
				.orElseThrow(() -> new PedidoNoEncontradoException(pedidoId));
	}

	private Pedido nuevo(CrearPedidoRequest request) {
		Instant ahora = Instant.now();
		Pedido pedido = new Pedido();
		pedido.setPedidoId("ped_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8));
		pedido.setUsuarioId(request.getUsuarioId());
		pedido.setEstado(EstadoPedido.PROCESANDO);
		pedido.setCreadoEn(ahora);
		pedido.setActualizadoEn(ahora);
		Pago pago = new Pago();
		pago.setMetodo(request.getPago().getMetodo());
		pago.setEstadoPago(EstadoPago.PENDIENTE);
		pedido.setPago(pago);
		return pedido;
	}
}
