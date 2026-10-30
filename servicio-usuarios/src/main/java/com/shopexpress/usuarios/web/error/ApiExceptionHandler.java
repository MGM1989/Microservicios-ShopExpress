package com.shopexpress.usuarios.web.error;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(UsuarioNoEncontradoException.class)
	public ResponseEntity<Map<String, String>> noEncontrado(UsuarioNoEncontradoException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
				"error", "Usuario no encontrado",
				"usuario_id", ex.getUsuarioId()));
	}

	@ExceptionHandler(UsuarioYaExisteException.class)
	public ResponseEntity<Map<String, String>> yaExiste(UsuarioYaExisteException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
				"error", "Usuario ya existe",
				"usuario_id", ex.getUsuarioId()));
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
