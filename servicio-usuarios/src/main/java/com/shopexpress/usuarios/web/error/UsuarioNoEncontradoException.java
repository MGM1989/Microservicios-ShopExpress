package com.shopexpress.usuarios.web.error;

public class UsuarioNoEncontradoException extends RuntimeException {

	private final String usuarioId;

	public UsuarioNoEncontradoException(String usuarioId) {
		super("Usuario no encontrado: " + usuarioId);
		this.usuarioId = usuarioId;
	}

	public String getUsuarioId() {
		return usuarioId;
	}
}
