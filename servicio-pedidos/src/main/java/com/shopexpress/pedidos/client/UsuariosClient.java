package com.shopexpress.pedidos.client;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class UsuariosClient {

	private final RestClient restClient;

	public UsuariosClient(
			RestClient.Builder builder,
			@Value("${shopexpress.usuarios.base-url}") String baseUrl) {
		this.restClient = builder.baseUrl(baseUrl).build();
	}

	public Optional<UsuarioVista> buscar(String usuarioId) {
		try {
			return restClient.get()
					.uri("/api/usuarios/{id}", usuarioId)
					.exchange((request, response) -> {
						if (response.getStatusCode().value() == 404) {
							return Optional.empty();
						}
						if (response.getStatusCode().isError()) {
							throw new IllegalStateException("Usuarios respondió HTTP " + response.getStatusCode().value());
						}
						return Optional.ofNullable(response.bodyTo(UsuarioVista.class));
					});
		} catch (RestClientException ex) {
			throw new IllegalStateException("No se pudo consultar usuarios (" + usuarioId + "): " + ex.getMessage(), ex);
		}
	}
}
