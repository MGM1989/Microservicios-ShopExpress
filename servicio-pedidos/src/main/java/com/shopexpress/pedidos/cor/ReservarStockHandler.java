package com.shopexpress.pedidos.cor;

import org.springframework.stereotype.Component;

import com.shopexpress.pedidos.client.CatalogoClient;
import com.shopexpress.pedidos.client.ProductoVista;
import com.shopexpress.pedidos.domain.ItemPedido;

@Component
public class ReservarStockHandler extends AbstractPedidoHandler {

	private final CatalogoClient catalogoClient;

	public ReservarStockHandler(CatalogoClient catalogoClient) {
		this.catalogoClient = catalogoClient;
	}

	@Override
	protected void procesar(PedidoContext ctx) {
		for (ItemPedido item : ctx.getPedido().getItems()) {
			ProductoVista actualizado = catalogoClient.descontar(item.getProductoId(), item.getCantidad());
			ctx.addReserva(item.getProductoId(), item.getCantidad());
			ctx.getPedido().addLog("CoR ReservarStock: descontado " + item.getProductoId()
					+ " x" + item.getCantidad() + " → stock=" + actualizado.getStock());
		}
	}
}
