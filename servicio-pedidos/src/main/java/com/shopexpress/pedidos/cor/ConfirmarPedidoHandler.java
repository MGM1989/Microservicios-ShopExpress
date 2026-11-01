package com.shopexpress.pedidos.cor;

import org.springframework.stereotype.Component;

import com.shopexpress.pedidos.domain.EstadoPedido;

@Component
public class ConfirmarPedidoHandler extends AbstractPedidoHandler {

	@Override
	protected void procesar(PedidoContext ctx) {
		ctx.getPedido().setEstado(EstadoPedido.COMPLETADO);
		ctx.getPedido().addLog("CoR ConfirmarPedido: COMPLETADO");
	}
}
