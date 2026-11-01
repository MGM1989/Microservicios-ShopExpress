package com.shopexpress.pedidos.domain;

import org.springframework.data.mongodb.core.mapping.Field;

public class Pago {

	private String metodo;
	@Field("estado_pago")
	private EstadoPago estadoPago = EstadoPago.PENDIENTE;
	@Field("transaccion_id")
	private String transaccionId;

	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public EstadoPago getEstadoPago() {
		return estadoPago;
	}

	public void setEstadoPago(EstadoPago estadoPago) {
		this.estadoPago = estadoPago;
	}

	public String getTransaccionId() {
		return transaccionId;
	}

	public void setTransaccionId(String transaccionId) {
		this.transaccionId = transaccionId;
	}
}
