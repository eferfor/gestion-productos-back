package com.productos.batch.steps;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.infrastructure.item.ItemProcessor;

import com.productos.dto.ProductDTO;
import com.productos.model.Product;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductsItemProcessor implements ItemProcessor<ProductDTO, ProductDTO>, StepExecutionListener {@Override
	
	public @Nullable ProductDTO process(ProductDTO dto) throws Exception {
	
	// Si no tiene nombre, no hay producto
	if(dto.getNombre() == null || dto.getNombre().isBlank()) {
		throw new IllegalArgumentException("Producto sin nombre");
	}
	
	if(dto.getPrecio() != null && dto.getPrecio() < 0) {
		throw new IllegalArgumentException("Producto con precio negativo");
	}
	
	return dto;
	
	}
}
