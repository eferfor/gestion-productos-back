package com.productos.catalogo.batch;

import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.productos.catalogo.PdfEnvioResultado;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class UsuariosItemWriter {
	
	@Bean
	public ItemWriter<PdfEnvioResultado> logWriter(){
		return items -> {
			for(PdfEnvioResultado r : items) {
				if(r.ok()) {
					log.info("OK carpeta creada | userId=%d email=%s ruta=%s%n", r.userId(), r.email(), r.rutaPdf());
				}else {
					log.info("KO carpeta | userId=%d email=%s ruta=%s%n", r.userId(), r.email(), r.rutaPdf());
				}
			}
		};
	}

}
