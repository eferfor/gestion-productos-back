package com.productos.catalogo.batch;

import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.productos.catalogo.PdfEnvioResultado;
import com.productos.service.EmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class UsuariosItemWriter {
	
	@Autowired
	private EmailService emailService;
	
	@Bean
	public ItemWriter<PdfEnvioResultado> logWriter(){
		return items -> {
			for(PdfEnvioResultado r : items) {
				if(r.ok()) {
					if(r.emailValid()) {
						emailService.createPendingEmail(
								r.userId(),
								r.email(),
								"Catálogo de productos", "Aquí está la última versión del catálogo:",
								r.rutaPdf()
							);
					}else {
						log.info("El usuario " + r.userId() + " no tiene un email válido. No se incluye en la lista de envío de catálogo.");
					}
					
					log.info(String.format("OK carpeta creada | userId=%d email=%s ruta=%s%n", r.userId(), r.email(), r.rutaPdf()));
				}else {
					log.info(String.format("KO carpeta | userId=%d email=%s ruta=%s%n", r.userId(), r.email(), r.rutaPdf()));
				}
			}
		};
	}

}
