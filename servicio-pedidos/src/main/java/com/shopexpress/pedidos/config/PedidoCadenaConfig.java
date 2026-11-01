package com.shopexpress.pedidos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.shopexpress.pedidos.cor.ConfirmarPedidoHandler;
import com.shopexpress.pedidos.cor.PedidoHandler;
import com.shopexpress.pedidos.cor.ProcesadorDePagoHandler;
import com.shopexpress.pedidos.cor.ReservarStockHandler;
import com.shopexpress.pedidos.cor.ValidadorDeDatosHandler;
import com.shopexpress.pedidos.cor.ValidadorDeStockHandler;
import com.shopexpress.pedidos.cor.ValidadorDeUsuarioHandler;

@Configuration
public class PedidoCadenaConfig {

	@Bean
	@Primary
	public PedidoHandler pedidoCadena(
			ValidadorDeDatosHandler datos,
			ValidadorDeUsuarioHandler usuario,
			ValidadorDeStockHandler stock,
			ReservarStockHandler reservar,
			ProcesadorDePagoHandler pago,
			ConfirmarPedidoHandler confirmar) {
		datos.setNext(usuario);
		usuario.setNext(stock);
		stock.setNext(reservar);
		reservar.setNext(pago);
		pago.setNext(confirmar);
		return datos;
	}
}
