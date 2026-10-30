package com.shopexpress.usuarios.repo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.shopexpress.usuarios.config.DynamoDbTableSupport;
import com.shopexpress.usuarios.domain.Usuario;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

@Repository
public class UsuarioRepository {

	private final DynamoDbTable<Usuario> table;
	private final DynamoDbClient dynamoDbClient;
	private final String tableName;

	public UsuarioRepository(
			DynamoDbEnhancedClient enhancedClient,
			DynamoDbClient dynamoDbClient,
			@Value("${aws.dynamodb.table-name}") String tableName) {
		this.table = enhancedClient.table(tableName, TableSchema.fromBean(Usuario.class));
		this.dynamoDbClient = dynamoDbClient;
		this.tableName = tableName;
	}

	public void save(Usuario usuario) {
		try {
			table.putItem(usuario);
		} catch (ResourceNotFoundException tableMissing) {
			DynamoDbTableSupport.ensure(dynamoDbClient, tableName, "id");
			table.putItem(usuario);
		}
	}

	public Optional<Usuario> findById(String id) {
		try {
			return Optional.ofNullable(table.getItem(Key.builder().partitionValue(id).build()));
		} catch (ResourceNotFoundException tableMissing) {
			DynamoDbTableSupport.ensure(dynamoDbClient, tableName, "id");
			return Optional.ofNullable(table.getItem(Key.builder().partitionValue(id).build()));
		}
	}
}
