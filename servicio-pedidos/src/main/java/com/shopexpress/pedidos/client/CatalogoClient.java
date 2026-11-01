package com.shopexpress.pedidos.client;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.shopexpress.pedidos.cor.CadenaPedidoException;

@Component
public class CatalogoClient {

	private final RestClient restClient;

	public CatalogoClient(
			RestClient.Builder builder,
			@Value("${shopexpress.catalogo.base-url}") String baseUrl) {
		this.restClient = builder.baseUrl(baseUrl).build();
	}

	public Optional<ProductoVista> buscar(String productoId) {
		try {
			return restClient.get()
					.uri("/api/productos/{id}", productoId)
					.exchange((request, response) -> {
						if (response.getStatusCode().value() == 404) {
							return Optional.empty();
						}
						if (response.getStatusCode().isError()) {
							throw new IllegalStateException("Catálogo respondió HTTP " + response.getStatusCode().value());
						}
						return Optional.ofNullable(response.bodyTo(ProductoVista.class));
					});
		} catch (RestClientException ex) {
			throw new IllegalStateException("No se pudo consultar catálogo (" + productoId + "): " + ex.getMessage(), ex);
		}
	}

	public ProductoVista descontar(String productoId, int cantidad) {
		return moverStock(productoId, cantidad, "descontar-stock");
	}

	public ProductoVista restaurar(String productoId, int cantidad) {
		return moverStock(productoId, cantidad, "restaurar-stock");
	}

	private ProductoVista moverStock(String productoId, int cantidad, String accion) {
		try {
			return restClient.post()
					.uri("/api/productos/{id}/{accion}", productoId, accion)
					.body(new MovimientoStockBody(cantidad))
					.exchange((request, response) -> {
						int code = response.getStatusCode().value();
						if (code == 404) {
							throw new CadenaPedidoException(HttpStatus.NOT_FOUND, "Producto no encontrado: " + productoId);
						}
						if (code == 409) {
							throw new CadenaPedidoException(HttpStatus.CONFLICT,
									"Stock insuficiente para " + productoId + " (pedido: " + cantidad + ")");
						}
						if (response.getStatusCode().isError()) {
							throw new IllegalStateException("Catálogo " + accion + " HTTP " + code);
						}
						return response.bodyTo(ProductoVista.class);
					});
		} catch (CadenaPedidoException ex) {
			throw ex;
		} catch (RestClientException ex) {
			throw new IllegalStateException("No se pudo " + accion + " de " + productoId + ": " + ex.getMessage(), ex);
		}
	}
}
