package com.shopexpress.pedidos.web.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Alta de pedido. Dispara la cadena CoR y la SAGA HTTP síncrona.")
public class CrearPedidoRequest {

	@NotBlank
	@JsonProperty("usuario_id")
	@Schema(example = "usr_saga")
	private String usuarioId;

	@Valid
	@NotEmpty
	private List<ItemPedidoRequest> items = new ArrayList<>();

	@Valid
	@NotNull
	private PagoRequest pago;

	@JsonProperty("forzar_fallo_pago")
	@Schema(description = "Si es true, el ProcesadorDePago corta la cadena y la SAGA restaura el stock.", example = "false")
	private boolean forzarFalloPago;

	public String getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}

	public List<ItemPedidoRequest> getItems() {
		return items;
	}

	public void setItems(List<ItemPedidoRequest> items) {
		this.items = items != null ? items : new ArrayList<>();
	}

	public PagoRequest getPago() {
		return pago;
	}

	public void setPago(PagoRequest pago) {
		this.pago = pago;
	}

	public boolean isForzarFalloPago() {
		return forzarFalloPago;
	}

	public void setForzarFalloPago(boolean forzarFalloPago) {
		this.forzarFalloPago = forzarFalloPago;
	}
}
