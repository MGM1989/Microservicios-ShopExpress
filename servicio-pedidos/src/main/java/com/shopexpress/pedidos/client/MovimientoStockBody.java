package com.shopexpress.pedidos.client;

public class MovimientoStockBody {

	private int cantidad;

	public MovimientoStockBody() {
	}

	public MovimientoStockBody(int cantidad) {
		this.cantidad = cantidad;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
}
