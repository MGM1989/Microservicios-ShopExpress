package com.shopexpress.catalogo.repo;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.shopexpress.catalogo.config.DynamoDbTableSupport;
import com.shopexpress.catalogo.domain.Producto;
import com.shopexpress.catalogo.web.error.ProductoNoEncontradoException;
import com.shopexpress.catalogo.web.error.StockInsuficienteException;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

@Repository
public class ProductoRepository {

	private static final String PK = "producto_id";

	private final DynamoDbTable<Producto> table;
	private final DynamoDbClient dynamoDbClient;
	private final String tableName;

	public ProductoRepository(
			DynamoDbEnhancedClient enhancedClient,
			DynamoDbClient dynamoDbClient,
			@Value("${aws.dynamodb.table-name}") String tableName) {
		this.table = enhancedClient.table(tableName, TableSchema.fromBean(Producto.class));
		this.dynamoDbClient = dynamoDbClient;
		this.tableName = tableName;
	}

	public void save(Producto producto) {
		try {
			table.putItem(producto);
		} catch (ResourceNotFoundException tableMissing) {
			ensureTable();
			table.putItem(producto);
		}
	}

	public Optional<Producto> findById(String productoId) {
		try {
			return Optional.ofNullable(get(productoId));
		} catch (ResourceNotFoundException tableMissing) {
			ensureTable();
			return Optional.ofNullable(get(productoId));
		}
	}

	public Producto descontarStock(String productoId, int cantidad) {
		try {
			return actualizarStock(
					productoId,
					"SET stock = stock - :qty",
					"attribute_exists(producto_id) AND stock >= :qty",
					cantidad);
		} catch (ResourceNotFoundException tableMissing) {
			ensureTable();
			try {
				return actualizarStock(
						productoId,
						"SET stock = stock - :qty",
						"attribute_exists(producto_id) AND stock >= :qty",
						cantidad);
			} catch (ConditionalCheckFailedException conditionFailed) {
				throw mapDescuentoFallido(productoId, cantidad);
			}
		} catch (ConditionalCheckFailedException conditionFailed) {
			throw mapDescuentoFallido(productoId, cantidad);
		}
	}

	public Producto restaurarStock(String productoId, int cantidad) {
		try {
			return actualizarStock(
					productoId,
					"SET stock = stock + :qty",
					"attribute_exists(producto_id)",
					cantidad);
		} catch (ResourceNotFoundException tableMissing) {
			ensureTable();
			try {
				return actualizarStock(
						productoId,
						"SET stock = stock + :qty",
						"attribute_exists(producto_id)",
						cantidad);
			} catch (ConditionalCheckFailedException conditionFailed) {
				throw new ProductoNoEncontradoException(productoId);
			}
		} catch (ConditionalCheckFailedException conditionFailed) {
			throw new ProductoNoEncontradoException(productoId);
		}
	}

	private RuntimeException mapDescuentoFallido(String productoId, int cantidad) {
		if (findById(productoId).isEmpty()) {
			return new ProductoNoEncontradoException(productoId);
		}
		return new StockInsuficienteException(productoId, cantidad);
	}

	private Producto actualizarStock(String productoId, String updateExpression, String condition, int cantidad) {
		UpdateItemRequest request = UpdateItemRequest.builder()
				.tableName(tableName)
				.key(Map.of(PK, AttributeValue.fromS(productoId)))
				.updateExpression(updateExpression)
				.conditionExpression(condition)
				.expressionAttributeValues(Map.of(":qty", AttributeValue.fromN(Integer.toString(cantidad))))
				.returnValues(ReturnValue.ALL_NEW)
				.build();
		UpdateItemResponse response = dynamoDbClient.updateItem(request);
		return fromItem(response.attributes());
	}

	private Producto get(String productoId) {
		return table.getItem(Key.builder().partitionValue(productoId).build());
	}

	private void ensureTable() {
		DynamoDbTableSupport.ensure(dynamoDbClient, tableName, PK);
	}

	private Producto fromItem(Map<String, AttributeValue> item) {
		Producto producto = new Producto();
		producto.setProductoId(item.get(PK).s());
		producto.setNombre(item.get("nombre").s());
		producto.setCategoria(item.get("categoria").s());
		producto.setPrecio(new BigDecimal(item.get("precio").n()));
		producto.setStock(Integer.valueOf(item.get("stock").n()));
		return producto;
	}
}
