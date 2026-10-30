package com.shopexpress.catalogo.config;

import java.net.URI;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.BillingMode;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

@Configuration
public class DynamoDbConfig {

	private static final Logger log = LoggerFactory.getLogger(DynamoDbConfig.class);

	@Value("${aws.dynamodb.endpoint}")
	private String endpoint;

	@Value("${aws.region}")
	private String region;

	@Value("${aws.dynamodb.create-tables}")
	private boolean createTables;

	@Value("${aws.dynamodb.table-name}")
	private String tableName;

	@Bean
	public DynamoDbClient dynamoDbClient() {
		log.info("Conectando a DynamoDB endpoint={} table={}", endpoint, tableName);
		System.setProperty("aws.requestChecksumCalculation", "WHEN_REQUIRED");
		System.setProperty("aws.responseChecksumValidation", "WHEN_REQUIRED");
		DynamoDbClient client = DynamoDbClient.builder()
				.endpointOverride(URI.create(endpoint))
				.region(Region.of(region))
				.credentialsProvider(StaticCredentialsProvider.create(
						AwsBasicCredentials.create("local", "local")))
				.httpClientBuilder(ApacheHttpClient.builder()
						.connectionTimeout(Duration.ofSeconds(2))
						.socketTimeout(Duration.ofSeconds(3)))
				.overrideConfiguration(ClientOverrideConfiguration.builder()
						.apiCallTimeout(Duration.ofSeconds(5))
						.apiCallAttemptTimeout(Duration.ofSeconds(3))
						.build())
				.build();
		if (createTables) {
			ensureTable(client, "producto_id");
		}
		return client;
	}

	@Bean
	public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
		return DynamoDbEnhancedClient.builder()
				.dynamoDbClient(dynamoDbClient)
				.build();
	}

	private void ensureTable(DynamoDbClient client, String partitionKey) {
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
		for (int attempt = 1; attempt <= 15; attempt++) {
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
				log.warn("Intento {}/15: DynamoDB no disponible para '{}': {}", attempt, tableName, ex.getMessage());
				sleep();
			}
		}
		throw new IllegalStateException("No se pudo crear la tabla " + tableName, lastError);
	}

	private void sleep() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException interrupted) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException("Interrumpido esperando DynamoDB", interrupted);
		}
	}
}
