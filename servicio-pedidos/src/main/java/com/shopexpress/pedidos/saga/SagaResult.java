package com.shopexpress.pedidos.saga;

import org.springframework.http.HttpStatus;

import com.shopexpress.pedidos.domain.Pedido;

public record SagaResult(Pedido pedido, HttpStatus status) {
}
