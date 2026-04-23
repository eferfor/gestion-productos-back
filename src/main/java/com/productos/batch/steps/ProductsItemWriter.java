package com.productos.batch.steps;

import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.productos.batch.service.ProductBatchService;
import com.productos.dto.ProductDTO;
import com.productos.model.Product;
import com.productos.service.IProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductsItemWriter implements ItemWriter<ProductDTO> {
	
	private final ProductBatchService batchService;

	public ProductsItemWriter(ProductBatchService batchService) {
		this.batchService = batchService;
	}
	
	@Override
	public void write(Chunk<? extends ProductDTO> chunk) throws Exception {
		
		batchService.createOrUpdateAll(chunk.getItems());
		
	}

}
