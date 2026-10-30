package com.shopexpress.catalogo.web;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@RestController
@RequestMapping("/api")
public class HealthController {

	private final DynamoDbClient dynamoDbClient;

	public HealthController(DynamoDbClient dynamoDbClient) {
		this.dynamoDbClient = dynamoDbClient;
	}

	@GetMapping("/health")
	public Map<String, String> health() {
		return Map.of(
				"status", "UP",
				"service", "servicio-catalogo",
				"dynamodb", dynamoDbClient.listTables().tableNames().toString());
	}
}
