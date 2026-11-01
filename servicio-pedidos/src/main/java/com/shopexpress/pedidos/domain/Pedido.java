package com.shopexpress.pedidos.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "pedidos")
public class Pedido {

	@Id
	private String pedidoId;
	@Field("usuario_id")
	private String usuarioId;
	private EstadoPedido estado = EstadoPedido.PROCESANDO;
	private BigDecimal total = BigDecimal.ZERO;
	private List<ItemPedido> items = new ArrayList<>();
	private Pago pago;
	@Field("saga_log")
	private List<String> sagaLog = new ArrayList<>();
	@Field("mensaje_error")
	private String mensajeError;
	@Field("creado_en")
	private Instant creadoEn;
	@Field("actualizado_en")
	private Instant actualizadoEn;

	public void addLog(String mensaje) {
		sagaLog.add(mensaje);
		actualizadoEn = Instant.now();
	}

	public String getPedidoId() {
		return pedidoId;
	}

	public void setPedidoId(String pedidoId) {
		this.pedidoId = pedidoId;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}

	public EstadoPedido getEstado() {
		return estado;
	}

	public void setEstado(EstadoPedido estado) {
		this.estado = estado;
		this.actualizadoEn = Instant.now();
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<ItemPedido> getItems() {
		return items;
	}

	public void setItems(List<ItemPedido> items) {
		this.items = items != null ? items : new ArrayList<>();
	}

	public Pago getPago() {
		return pago;
	}

	public void setPago(Pago pago) {
		this.pago = pago;
	}

	public List<String> getSagaLog() {
		return sagaLog;
	}

	public void setSagaLog(List<String> sagaLog) {
		this.sagaLog = sagaLog != null ? sagaLog : new ArrayList<>();
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public Instant getCreadoEn() {
		return creadoEn;
	}

	public void setCreadoEn(Instant creadoEn) {
		this.creadoEn = creadoEn;
	}

	public Instant getActualizadoEn() {
		return actualizadoEn;
	}

	public void setActualizadoEn(Instant actualizadoEn) {
		this.actualizadoEn = actualizadoEn;
	}
}
