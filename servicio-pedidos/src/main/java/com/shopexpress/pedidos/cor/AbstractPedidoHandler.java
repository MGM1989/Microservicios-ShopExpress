package com.shopexpress.pedidos.cor;

public abstract class AbstractPedidoHandler implements PedidoHandler {

	private PedidoHandler next;

	@Override
	public final void setNext(PedidoHandler next) {
		this.next = next;
	}

	@Override
	public final void handle(PedidoContext ctx) {
		procesar(ctx);
		if (next != null) {
			next.handle(ctx);
		}
	}

	protected abstract void procesar(PedidoContext ctx);
}
