package com.shopexpress.usuarios.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shopexpress.usuarios.domain.Usuario;
import com.shopexpress.usuarios.repo.UsuarioRepository;
import com.shopexpress.usuarios.web.UsuarioMapper;
import com.shopexpress.usuarios.web.dto.CrearUsuarioRequest;
import com.shopexpress.usuarios.web.dto.UsuarioResponse;
import com.shopexpress.usuarios.web.error.UsuarioNoEncontradoException;
import com.shopexpress.usuarios.web.error.UsuarioYaExisteException;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	public UsuarioResponse crear(CrearUsuarioRequest request) {
		String id = StringUtils.hasText(request.getId())
				? request.getId().trim()
				: "usr_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);

		if (usuarioRepository.findById(id).isPresent()) {
			throw new UsuarioYaExisteException(id);
		}

		Usuario usuario = UsuarioMapper.toDomain(request, id);
		usuarioRepository.save(usuario);
		return UsuarioMapper.toResponse(usuario);
	}

	public UsuarioResponse obtener(String usuarioId) {
		return usuarioRepository.findById(usuarioId)
				.map(UsuarioMapper::toResponse)
				.orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));
	}
}
