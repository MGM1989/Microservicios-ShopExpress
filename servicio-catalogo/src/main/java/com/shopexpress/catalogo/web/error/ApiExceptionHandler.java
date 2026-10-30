package com.shopexpress.catalogo.web.error;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(ProductoNoEncontradoException.class)
	public ResponseEntity<Map<String, String>> noEncontrado(ProductoNoEncontradoException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
				"error", "Producto no encontrado",
				"producto_id", ex.getProductoId()));
	}

	@ExceptionHandler(ProductoYaExisteException.class)
	public ResponseEntity<Map<String, String>> yaExiste(ProductoYaExisteException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
				"error", "Producto ya existe",
				"producto_id", ex.getProductoId()));
	}

	@ExceptionHandler(StockInsuficienteException.class)
	public ResponseEntity<Map<String, Object>> stockInsuficiente(StockInsuficienteException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
				"error", "Stock insuficiente",
				"producto_id", ex.getProductoId(),
				"cantidad", ex.getCantidad()));
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
