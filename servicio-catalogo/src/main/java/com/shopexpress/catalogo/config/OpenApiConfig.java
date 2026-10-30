package com.shopexpress.catalogo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI servicioCatalogoOpenAPI() {
		return new OpenAPI().info(new Info()
				.title("ShopExpress - Servicio de Catálogo e Inventario")
				.version("1.0")
				.description("Productos, precios y stock. Puerto 8082."));
	}
}
