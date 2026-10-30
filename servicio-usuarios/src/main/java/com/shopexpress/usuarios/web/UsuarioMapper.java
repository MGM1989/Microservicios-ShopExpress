package com.shopexpress.usuarios.web;

import java.util.List;

import com.shopexpress.usuarios.domain.Direccion;
import com.shopexpress.usuarios.domain.Usuario;
import com.shopexpress.usuarios.web.dto.CrearUsuarioRequest;
import com.shopexpress.usuarios.web.dto.DireccionDto;
import com.shopexpress.usuarios.web.dto.UsuarioResponse;

public final class UsuarioMapper {

	private UsuarioMapper() {
	}

	public static Usuario toDomain(CrearUsuarioRequest request, String id) {
		Usuario usuario = new Usuario();
		usuario.setId(id);
		usuario.setTipoDocumento(request.getTipoDocumento());
		usuario.setNroDocumento(request.getNroDocumento());
		usuario.setNombre(request.getNombre());
		usuario.setApellido(request.getApellido());
		usuario.setEmail(request.getEmail());
		usuario.setDirecciones(toDirecciones(request.getDirecciones()));
		return usuario;
	}

	public static UsuarioResponse toResponse(Usuario usuario) {
		UsuarioResponse response = new UsuarioResponse();
		response.setId(usuario.getId());
		response.setTipoDocumento(usuario.getTipoDocumento());
		response.setNroDocumento(usuario.getNroDocumento());
		response.setNombre(usuario.getNombre());
		response.setApellido(usuario.getApellido());
		response.setEmail(usuario.getEmail());
		response.setDirecciones(toDireccionDtos(usuario.getDirecciones()));
		return response;
	}

	private static List<Direccion> toDirecciones(List<DireccionDto> dtos) {
		if (dtos == null) {
			return List.of();
		}
		return dtos.stream().map(dto -> {
			Direccion direccion = new Direccion();
			direccion.setCalle(dto.getCalle());
			direccion.setCiudad(dto.getCiudad());
			direccion.setCodigoPostal(dto.getCodigoPostal());
			return direccion;
		}).toList();
	}

	private static List<DireccionDto> toDireccionDtos(List<Direccion> direcciones) {
		if (direcciones == null) {
			return List.of();
		}
		return direcciones.stream().map(direccion -> {
			DireccionDto dto = new DireccionDto();
			dto.setCalle(direccion.getCalle());
			dto.setCiudad(direccion.getCiudad());
			dto.setCodigoPostal(direccion.getCodigoPostal());
			return dto;
		}).toList();
	}
}
