package com.shopexpress.usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI servicioUsuariosOpenAPI() {
		return new OpenAPI().info(new Info()
				.title("ShopExpress - Servicio de Usuarios")
				.version("1.0")
				.description("Perfiles de clientes y direcciones de envío embebidas. POST y GET /api/usuarios. Puerto 8081."));
	}
}
