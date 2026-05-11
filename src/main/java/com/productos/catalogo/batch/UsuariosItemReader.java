package com.productos.catalogo.batch;

import java.util.List;
import java.util.Map;

import org.springframework.batch.infrastructure.item.data.RepositoryItemReader;
import org.springframework.batch.infrastructure.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import com.productos.model.Usuario;
import com.productos.repository.UsuarioRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class UsuariosItemReader {

	@Bean
	public RepositoryItemReader<Usuario> usuarioReader(UsuarioRepository repo){
		
		log.info("Leyendo lista de usuarios...");
		
		return new RepositoryItemReaderBuilder<Usuario>()
				.name("reader")
				.repository(repo)
				.methodName("findAll")
				.pageSize(500)
				.arguments(List.of())
				.sorts(Map.of("id", Sort.Direction.ASC))
				.build();
	}
	
}
