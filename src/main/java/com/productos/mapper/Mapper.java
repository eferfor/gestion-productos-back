package com.productos.mapper;

import com.productos.dto.ProductDTO;
import com.productos.model.Product;

public class Mapper {

	public static ProductDTO toDTO(Product p) {
		
		if(p == null) {
			return null;
		}
		
		return ProductDTO.builder()
				.id(p.getId())
				.nombre(p.getNombre())
				.descripcion(p.getDescripcion())
				.precio(p.getPrecio())
				.categoria(p.getCategoria())
				.marca(p.getMarca())
				.referencia(p.getReferencia())
				.activo(p.getActivo())
				.fechaAlta(p.getFechaAlta())
				.audUser(p.getAudUser())
				.build();
		
	}
	
}
