package com.productos.service;

import java.nio.file.Path;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EnviarEmailService {

	public void enviarEmailConAdjunto(String recipient, String subject, String body, Path attachment) {
		log.info("Email imaginario enviado a " + recipient);
	}
	
}
