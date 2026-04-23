package com.productos.batch.steps;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import com.productos.dto.ProductDTO;
import com.productos.model.Product;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ProductsItemReader {

	@Bean
	@StepScope
	public FlatFileItemReader<ProductDTO> productReader(
			@Value("#{jobParameters['filePath']}") String filePath ){
		
		log.info("Leyendo lista de productos CSV: {}", filePath);
		
		return new FlatFileItemReaderBuilder<ProductDTO>()
				.name("readProducts")
				.resource(new FileSystemResource(filePath))
				.linesToSkip(1)
				.encoding(StandardCharsets.UTF_8.name())
				.lineMapper(getLineMapper())
				.build();
	}
	
	public LineMapper<ProductDTO> getLineMapper(){
		DefaultLineMapper<ProductDTO> lineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		
		lineTokenizer.setDelimiter(";");
		lineTokenizer.setNames("id", "nombre", "descripcion", "precio", "categoria", "marca", "referencia", "activo", "audUser");
		
		BeanWrapperFieldSetMapper<ProductDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(ProductDTO.class);
		
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		
		return lineMapper;
	}
	
}
