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
			DynamoDbTableSupport.ensure(client, tableName, "producto_id");
		}
		return client;
	}

	@Bean
	public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
		return DynamoDbEnhancedClient.builder()
				.dynamoDbClient(dynamoDbClient)
				.build();
	}
}
