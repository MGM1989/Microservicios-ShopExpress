package com.shopexpress.pedidos.cor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shopexpress.pedidos.web.dto.ItemPedidoRequest;

@Component
public class ValidadorDeDatosHandler extends AbstractPedidoHandler {

	@Override
	protected void procesar(PedidoContext ctx) {
		if (!StringUtils.hasText(ctx.getRequest().getUsuarioId())) {
			throw new CadenaPedidoException(HttpStatus.BAD_REQUEST, "usuario_id es obligatorio");
		}
		if (ctx.getRequest().getItems() == null || ctx.getRequest().getItems().isEmpty()) {
			throw new CadenaPedidoException(HttpStatus.BAD_REQUEST, "El pedido debe tener al menos un ítem");
		}
		if (ctx.getRequest().getPago() == null || !StringUtils.hasText(ctx.getRequest().getPago().getMetodo())) {
			throw new CadenaPedidoException(HttpStatus.BAD_REQUEST, "pago.metodo es obligatorio");
		}
		Set<String> vistos = new HashSet<>();
		for (ItemPedidoRequest item : ctx.getRequest().getItems()) {
			if (item.getCantidad() == null || item.getCantidad() < 1) {
				throw new CadenaPedidoException(HttpStatus.BAD_REQUEST, "Cada ítem debe tener cantidad >= 1");
			}
			if (!StringUtils.hasText(item.getProductoId())) {
				throw new CadenaPedidoException(HttpStatus.BAD_REQUEST, "producto_id es obligatorio en cada ítem");
			}
			if (!vistos.add(item.getProductoId())) {
				throw new CadenaPedidoException(HttpStatus.BAD_REQUEST, "Ítem duplicado: " + item.getProductoId());
			}
		}
		ctx.getPedido().addLog("CoR ValidadorDeDatos: OK");
	}
}
