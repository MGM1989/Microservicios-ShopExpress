package com.shopexpress.pedidos.saga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.shopexpress.pedidos.client.CatalogoClient;
import com.shopexpress.pedidos.cor.CadenaPedidoException;
import com.shopexpress.pedidos.cor.PedidoContext;
import com.shopexpress.pedidos.cor.PedidoHandler;
import com.shopexpress.pedidos.cor.ReservaStock;
import com.shopexpress.pedidos.domain.EstadoPago;
import com.shopexpress.pedidos.domain.EstadoPedido;
import com.shopexpress.pedidos.domain.Pedido;
import com.shopexpress.pedidos.repo.PedidoRepository;

@Component
public class SagaOrquestador {

	private static final Logger log = LoggerFactory.getLogger(SagaOrquestador.class);

	private final PedidoHandler cadena;
	private final PedidoRepository pedidoRepository;
	private final CatalogoClient catalogoClient;

	public SagaOrquestador(
			PedidoHandler cadena,
			PedidoRepository pedidoRepository,
			CatalogoClient catalogoClient) {
		this.cadena = cadena;
		this.pedidoRepository = pedidoRepository;
		this.catalogoClient = catalogoClient;
	}

	public SagaResult ejecutar(PedidoContext ctx) {
		Pedido pedido = ctx.getPedido();
		pedido.addLog("SAGA: persistido PROCESANDO");
		pedidoRepository.save(pedido);
		try {
			cadena.handle(ctx);
			pedidoRepository.save(pedido);
			return new SagaResult(pedido, HttpStatus.CREATED);
		} catch (CadenaPedidoException ex) {
			compensar(ctx, ex.getMessage());
			pedidoRepository.save(pedido);
			return new SagaResult(pedido, ex.getStatus());
		} catch (RuntimeException ex) {
			log.error("SAGA falló de forma inesperada para {}", pedido.getPedidoId(), ex);
			compensar(ctx, ex.getMessage());
			pedidoRepository.save(pedido);
			return new SagaResult(pedido, HttpStatus.BAD_GATEWAY);
		}
	}

	private void compensar(PedidoContext ctx, String motivo) {
		Pedido pedido = ctx.getPedido();
		if (ctx.isStockReservado()) {
			List<ReservaStock> invertidas = new ArrayList<>(ctx.getReservas());
			Collections.reverse(invertidas);
			for (ReservaStock reserva : invertidas) {
				try {
					catalogoClient.restaurar(reserva.productoId(), reserva.cantidad());
					pedido.addLog("SAGA compensación: restaurado " + reserva.productoId() + " x" + reserva.cantidad());
				} catch (RuntimeException restoreError) {
					log.error("No se pudo restaurar {} x{}", reserva.productoId(), reserva.cantidad(), restoreError);
					pedido.addLog("SAGA compensación FALLÓ " + reserva.productoId() + ": " + restoreError.getMessage());
				}
			}
		} else {
			pedido.addLog("SAGA: corte antes de reservar stock; no hay compensación remota");
		}
		if (pedido.getPago() != null && pedido.getPago().getEstadoPago() != EstadoPago.APROBADO) {
			pedido.getPago().setEstadoPago(EstadoPago.RECHAZADO);
		}
		pedido.setMensajeError(motivo);
		pedido.setEstado(EstadoPedido.COMPENSADO);
		pedido.addLog("SAGA: estado COMPENSADO");
	}
}
