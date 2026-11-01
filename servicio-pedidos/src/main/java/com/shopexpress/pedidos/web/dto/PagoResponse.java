package com.shopexpress.pedidos.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.shopexpress.pedidos.domain.EstadoPago;

public class PagoResponse {

	private String metodo;
	@JsonProperty("estado_pago")
	private EstadoPago estadoPago;
	@JsonProperty("transaccion_id")
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
