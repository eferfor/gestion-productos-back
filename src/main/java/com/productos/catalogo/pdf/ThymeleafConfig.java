package com.productos.catalogo.pdf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ThymeleafConfig {
	
	@Bean
	public ClassLoaderTemplateResolver catalogoTemplateResolver() {
		ClassLoaderTemplateResolver pdfTemplateResolver = new ClassLoaderTemplateResolver();
		pdfTemplateResolver.setPrefix("pdf-templates/");
		pdfTemplateResolver.setSuffix(".html");
		pdfTemplateResolver.setTemplateMode("HTML5");
		pdfTemplateResolver.setCharacterEncoding("UTF-8");
		pdfTemplateResolver.setOrder(1);
		return pdfTemplateResolver;
	}
	
}
