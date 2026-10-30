package com.shopexpress.usuarios.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.BillingMode;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

public final class DynamoDbTableSupport {

	private static final Logger log = LoggerFactory.getLogger(DynamoDbTableSupport.class);

	private DynamoDbTableSupport() {
	}

	public static void ensure(DynamoDbClient client, String tableName, String partitionKey) {
		CreateTableRequest request = CreateTableRequest.builder()
				.tableName(tableName)
				.attributeDefinitions(AttributeDefinition.builder()
						.attributeName(partitionKey)
						.attributeType(ScalarAttributeType.S)
						.build())
				.keySchema(KeySchemaElement.builder()
						.attributeName(partitionKey)
						.keyType(KeyType.HASH)
						.build())
				.billingMode(BillingMode.PAY_PER_REQUEST)
				.build();

		Exception lastError = null;
		for (int attempt = 1; attempt <= 8; attempt++) {
			try {
				client.createTable(request);
				client.waiter().waitUntilTableExists(b -> b.tableName(tableName));
				log.info("Tabla DynamoDB '{}' creada", tableName);
				return;
			} catch (ResourceInUseException exists) {
				log.info("Tabla DynamoDB '{}' ya existe", tableName);
				return;
			} catch (Exception ex) {
				lastError = ex;
				log.warn("Intento {}/8: DynamoDB no disponible para '{}': {}", attempt, tableName, ex.getMessage());
				sleep();
			}
		}
		throw new IllegalStateException("No se pudo crear la tabla " + tableName, lastError);
	}

	private static void sleep() {
		try {
			Thread.sleep(1500);
		} catch (InterruptedException interrupted) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException("Interrumpido esperando DynamoDB", interrupted);
		}
	}
}
