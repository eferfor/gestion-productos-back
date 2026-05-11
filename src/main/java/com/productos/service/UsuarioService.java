package com.productos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productos.dto.UsuarioDTO;
import com.productos.exception.NotFoundException;
import com.productos.mapper.UsuarioMapper;
import com.productos.model.Usuario;
import com.productos.repository.UsuarioRepository;

@Service
public class UsuarioService implements IUsuarioService{

	@Autowired
	private UsuarioRepository repo;
	
	@Override
	public List<UsuarioDTO> listarUsuarios(){
		return repo.findAll().stream().map(UsuarioMapper::toDTO).toList();
	}

	@Override
	public UsuarioDTO detalleUsuario(Long id) {
		Usuario u = repo.findById(id).orElseThrow(() -> new NotFoundException("No se ha encontrado el usuario"));
		return UsuarioMapper.toDTO(u);
	}
	
}
