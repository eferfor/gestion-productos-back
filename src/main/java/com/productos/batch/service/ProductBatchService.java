package com.productos.batch.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.productos.dto.ProductDTO;
import com.productos.exception.ProductNotFoundForUpdateException;
import com.productos.service.IProductService;

import jakarta.transaction.Transactional;

@Service
public class ProductBatchService {

	private final IProductService productService;

	public ProductBatchService(IProductService productService) {
		this.productService = productService;
	}
	
	@Transactional
	public void createOrUpdate(ProductDTO dto) {
		if(dto.getId() == null){
			productService.crearProducto(dto);
		}else {
			try {
				productService.modificarProducto(dto.getId(), dto);
			}catch(ResponseStatusException e) {
				if(e.getStatusCode().value() == 404) {
					throw new ProductNotFoundForUpdateException(dto.getId());
				}
				throw e;
			}
			
		}
	}
	
	@Transactional
	public void createOrUpdateAll(List<? extends ProductDTO> dtos) {
		for(ProductDTO dto: dtos) {
			createOrUpdate(dto);
		}
	}
	
	
	
}
