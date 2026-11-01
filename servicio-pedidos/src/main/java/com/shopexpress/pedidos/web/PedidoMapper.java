package com.shopexpress.pedidos.web;

import java.util.ArrayList;
import java.util.List;

import com.shopexpress.pedidos.domain.ItemPedido;
import com.shopexpress.pedidos.domain.Pago;
import com.shopexpress.pedidos.domain.Pedido;
import com.shopexpress.pedidos.web.dto.ItemPedidoResponse;
import com.shopexpress.pedidos.web.dto.PagoResponse;
import com.shopexpress.pedidos.web.dto.PedidoResponse;

public final class PedidoMapper {

	private PedidoMapper() {
	}

	public static PedidoResponse toResponse(Pedido pedido) {
		PedidoResponse response = new PedidoResponse();
		response.setPedidoId(pedido.getPedidoId());
		response.setUsuarioId(pedido.getUsuarioId());
		response.setEstado(pedido.getEstado());
		response.setTotal(pedido.getTotal());
		response.setSagaLog(new ArrayList<>(pedido.getSagaLog()));
		response.setMensajeError(pedido.getMensajeError());
		response.setCreadoEn(pedido.getCreadoEn());
		response.setActualizadoEn(pedido.getActualizadoEn());
		List<ItemPedidoResponse> items = new ArrayList<>();
		for (ItemPedido item : pedido.getItems()) {
			ItemPedidoResponse itemResponse = new ItemPedidoResponse();
			itemResponse.setProductoId(item.getProductoId());
			itemResponse.setNombre(item.getNombre());
			itemResponse.setCantidad(item.getCantidad());
			itemResponse.setPrecioUnitario(item.getPrecioUnitario());
			items.add(itemResponse);
		}
		response.setItems(items);
		if (pedido.getPago() != null) {
			Pago pago = pedido.getPago();
			PagoResponse pagoResponse = new PagoResponse();
			pagoResponse.setMetodo(pago.getMetodo());
			pagoResponse.setEstadoPago(pago.getEstadoPago());
			pagoResponse.setTransaccionId(pago.getTransaccionId());
			response.setPago(pagoResponse);
		}
		return response;
	}
}
