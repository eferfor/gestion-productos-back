package com.productos.catalogo.pdf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.productos.dto.ProductDTO;
import com.productos.service.ProductService;

@Component
public class PdfGeneratorRunner implements CommandLineRunner{

	@Autowired
	private PdfGenerateService pdfGenerateService;
	
	@Autowired
	private ProductService productService;
	
	@Override
	public void run(String... args) throws Exception {
		
		//System.out.println("Generar pdf");
		
		/*
		Map<String, Object> data = new HashMap<>();
		
		List<ProductDTO> productos = productService.listarProductosActivos();
		
		data.put("productos", productos);
		
		pdfGenerateService.generatePdfFile("catalogo", data, "catalogo.pdf");
		*/
	}

}
