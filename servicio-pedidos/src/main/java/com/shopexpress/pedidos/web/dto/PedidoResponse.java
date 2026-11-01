package com.shopexpress.pedidos.web.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.shopexpress.pedidos.domain.EstadoPedido;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PedidoResponse {

	@JsonProperty("pedido_id")
	private String pedidoId;
	@JsonProperty("usuario_id")
	private String usuarioId;
	private EstadoPedido estado;
	private BigDecimal total;
	private List<ItemPedidoResponse> items = new ArrayList<>();
	private PagoResponse pago;
	@JsonProperty("saga_log")
	private List<String> sagaLog = new ArrayList<>();
	@JsonProperty("mensaje_error")
	private String mensajeError;
	@JsonProperty("creado_en")
	private Instant creadoEn;
	@JsonProperty("actualizado_en")
	private Instant actualizadoEn;

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
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<ItemPedidoResponse> getItems() {
		return items;
	}

	public void setItems(List<ItemPedidoResponse> items) {
		this.items = items != null ? items : new ArrayList<>();
	}

	public PagoResponse getPago() {
		return pago;
	}

	public void setPago(PagoResponse pago) {
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
