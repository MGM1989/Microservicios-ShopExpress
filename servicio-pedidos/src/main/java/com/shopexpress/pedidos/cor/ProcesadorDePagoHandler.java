package com.shopexpress.pedidos.cor;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.shopexpress.pedidos.domain.EstadoPago;

@Component
public class ProcesadorDePagoHandler extends AbstractPedidoHandler {

	@Override
	protected void procesar(PedidoContext ctx) {
		if (ctx.getRequest().isForzarFalloPago()) {
			ctx.getPedido().getPago().setEstadoPago(EstadoPago.RECHAZADO);
			ctx.getPedido().addLog("CoR ProcesadorDePago: rechazo forzado (forzar_fallo_pago=true)");
			throw new CadenaPedidoException(HttpStatus.CONFLICT, "Pago rechazado (forzado para demo de compensación)");
		}
		String txn = "txn_" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
		ctx.getPedido().getPago().setTransaccionId(txn);
		ctx.getPedido().getPago().setEstadoPago(EstadoPago.APROBADO);
		ctx.getPedido().addLog("CoR ProcesadorDePago: APROBADO " + txn);
	}
}
