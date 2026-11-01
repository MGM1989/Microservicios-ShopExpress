package com.shopexpress.pedidos.cor;

public interface PedidoHandler {

	void setNext(PedidoHandler next);

	void handle(PedidoContext ctx);
}
