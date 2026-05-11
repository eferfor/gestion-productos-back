package com.productos.service;

import java.util.List;

import com.productos.dto.UsuarioDTO;

public interface IUsuarioService {

	List<UsuarioDTO> listarUsuarios();
	UsuarioDTO detalleUsuario(Long id);
	
}
