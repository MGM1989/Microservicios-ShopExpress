package com.shopexpress.usuarios.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.shopexpress.usuarios.service.UsuarioService;
import com.shopexpress.usuarios.web.dto.CrearUsuarioRequest;
import com.shopexpress.usuarios.web.dto.UsuarioResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Alta y consulta de perfiles. Direcciones embebidas.")
public class UsuarioController {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Crea un usuario", description = "Modelo embebido: el documento incluye direcciones[].")
	public UsuarioResponse crear(@Valid @RequestBody CrearUsuarioRequest request) {
		return usuarioService.crear(request);
	}

	@GetMapping("/{usuarioId}")
	@Operation(summary = "Recupera un usuario por id")
	public UsuarioResponse obtener(@PathVariable String usuarioId) {
		return usuarioService.obtener(usuarioId);
	}
}
