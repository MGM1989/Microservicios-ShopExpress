package com.shopexpress.pedidos.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shopexpress.pedidos.domain.Pedido;

public interface PedidoRepository extends MongoRepository<Pedido, String> {
}
