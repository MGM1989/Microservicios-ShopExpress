package com.shopexpress.pedidos.web.error;

public class PedidoNoEncontradoException extends RuntimeException {

	private final String pedidoId;

	public PedidoNoEncontradoException(String pedidoId) {
		super("Pedido no encontrado: " + pedidoId);
		this.pedidoId = pedidoId;
	}

	public String getPedidoId() {
		return pedidoId;
	}
}
