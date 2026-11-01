package com.shopexpress.pedidos.cor;

import org.springframework.http.HttpStatus;

public class CadenaPedidoException extends RuntimeException {

	private final HttpStatus status;

	public CadenaPedidoException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
