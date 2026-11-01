package com.shopexpress.pedidos.cor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.shopexpress.pedidos.client.UsuarioVista;
import com.shopexpress.pedidos.client.UsuariosClient;

@Component
public class ValidadorDeUsuarioHandler extends AbstractPedidoHandler {

	private final UsuariosClient usuariosClient;

	public ValidadorDeUsuarioHandler(UsuariosClient usuariosClient) {
		this.usuariosClient = usuariosClient;
	}

	@Override
	protected void procesar(PedidoContext ctx) {
		String usuarioId = ctx.getRequest().getUsuarioId();
		UsuarioVista usuario = usuariosClient.buscar(usuarioId)
				.orElseThrow(() -> new CadenaPedidoException(HttpStatus.NOT_FOUND, "Usuario no encontrado: " + usuarioId));
		ctx.getPedido().addLog("CoR ValidadorDeUsuario: OK " + usuario.getId() + " (" + usuario.getNombre() + ")");
	}
}
