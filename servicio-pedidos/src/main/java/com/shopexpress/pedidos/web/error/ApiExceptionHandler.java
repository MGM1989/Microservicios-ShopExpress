package com.shopexpress.pedidos.web.error;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(PedidoNoEncontradoException.class)
	public ResponseEntity<Map<String, String>> noEncontrado(PedidoNoEncontradoException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
				"error", "Pedido no encontrado",
				"pedido_id", ex.getPedidoId()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> validacion(MethodArgumentNotValidException ex) {
		String detalle = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.collect(Collectors.joining("; "));
		return ResponseEntity.badRequest().body(Map.of(
				"error", "Datos inválidos",
				"detalle", detalle));
	}
}
