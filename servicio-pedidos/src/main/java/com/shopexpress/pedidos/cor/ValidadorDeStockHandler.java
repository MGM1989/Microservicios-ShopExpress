package com.shopexpress.pedidos.cor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.shopexpress.pedidos.client.CatalogoClient;
import com.shopexpress.pedidos.client.ProductoVista;
import com.shopexpress.pedidos.domain.ItemPedido;
import com.shopexpress.pedidos.web.dto.ItemPedidoRequest;

@Component
public class ValidadorDeStockHandler extends AbstractPedidoHandler {

	private final CatalogoClient catalogoClient;

	public ValidadorDeStockHandler(CatalogoClient catalogoClient) {
		this.catalogoClient = catalogoClient;
	}

	@Override
	protected void procesar(PedidoContext ctx) {
		List<ItemPedido> items = new ArrayList<>();
		BigDecimal total = BigDecimal.ZERO;
		for (ItemPedidoRequest solicitado : ctx.getRequest().getItems()) {
			ProductoVista producto = catalogoClient.buscar(solicitado.getProductoId())
					.orElseThrow(() -> new CadenaPedidoException(
							HttpStatus.NOT_FOUND, "Producto no encontrado: " + solicitado.getProductoId()));
			int stock = producto.getStock() == null ? 0 : producto.getStock();
			if (stock < solicitado.getCantidad()) {
				throw new CadenaPedidoException(HttpStatus.CONFLICT,
						"Stock insuficiente para " + producto.getProductoId()
								+ " (disponible: " + stock + ", pedido: " + solicitado.getCantidad() + ")");
			}
			ItemPedido item = new ItemPedido();
			item.setProductoId(producto.getProductoId());
			item.setNombre(producto.getNombre());
			item.setCantidad(solicitado.getCantidad());
			item.setPrecioUnitario(producto.getPrecio());
			items.add(item);
			total = total.add(producto.getPrecio().multiply(BigDecimal.valueOf(solicitado.getCantidad())));
			ctx.getPedido().addLog("CoR ValidadorDeStock: " + producto.getProductoId()
					+ " stock=" + stock + " >= " + solicitado.getCantidad());
		}
		ctx.getPedido().setItems(items);
		ctx.getPedido().setTotal(total);
	}
}
