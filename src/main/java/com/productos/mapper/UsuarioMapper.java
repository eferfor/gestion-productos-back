package com.productos.mapper;

import com.productos.dto.UsuarioDTO;
import com.productos.model.Usuario;

public class UsuarioMapper {

	public static UsuarioDTO toDTO(Usuario u) {
			
			if(u == null) {
				return null;
			}
			
			return UsuarioDTO.builder()
					.id(u.getId())
					.nombre(u.getNombre())
					.email(u.getEmail())
					.build();
			
		}
	
}
