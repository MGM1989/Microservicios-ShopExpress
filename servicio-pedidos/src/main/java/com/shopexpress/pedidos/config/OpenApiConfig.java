package com.shopexpress.pedidos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI servicioPedidosOpenAPI() {
		return new OpenAPI().info(new Info()
				.title("ShopExpress - Servicio de Pedidos y Pagos")
				.version("1.0")
				.description("Orquestador SAGA y Chain of Responsibility. Puerto 8083."));
	}
}
