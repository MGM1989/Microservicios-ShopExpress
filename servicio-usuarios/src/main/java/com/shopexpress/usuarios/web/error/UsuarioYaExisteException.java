package com.shopexpress.usuarios.web.error;

public class UsuarioYaExisteException extends RuntimeException {

	private final String usuarioId;

	public UsuarioYaExisteException(String usuarioId) {
		super("Ya existe un usuario con id " + usuarioId);
		this.usuarioId = usuarioId;
	}

	public String getUsuarioId() {
		return usuarioId;
	}
}
