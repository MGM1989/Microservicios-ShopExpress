package com.shopexpress.pedidos.cor;

import java.util.ArrayList;
import java.util.List;

import com.shopexpress.pedidos.domain.Pedido;
import com.shopexpress.pedidos.web.dto.CrearPedidoRequest;

public class PedidoContext {

	private final CrearPedidoRequest request;
	private final Pedido pedido;
	private final List<ReservaStock> reservas = new ArrayList<>();

	public PedidoContext(CrearPedidoRequest request, Pedido pedido) {
		this.request = request;
		this.pedido = pedido;
	}

	public CrearPedidoRequest getRequest() {
		return request;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public List<ReservaStock> getReservas() {
		return reservas;
	}

	public boolean isStockReservado() {
		return !reservas.isEmpty();
	}

	public void addReserva(String productoId, int cantidad) {
		reservas.add(new ReservaStock(productoId, cantidad));
	}
}
