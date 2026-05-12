package com.productos.catalogo.batch;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.productos.catalogo.pdf.PdfEnvioResultado;
import com.productos.catalogo.pdf.PdfGenerateService;
import com.productos.dto.ProductDTO;
import com.productos.model.Usuario;
import com.productos.service.EmailService;
import com.productos.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@StepScope
public class UsuariosItemProcessor implements ItemProcessor<Usuario, PdfEnvioResultado>, StepExecutionListener {

	@Value("${pdf.directory}")
	private String pdfDirectory;
	private String pdfFileName = "catalogo.pdf";
	
	private final PdfGenerateService pdfGenerateService;
	private final ProductService productService;
	private final EmailService emailService;
	
	public UsuariosItemProcessor(PdfGenerateService pdfGenerateService, ProductService productService, EmailService emailService) {
		this.pdfGenerateService = pdfGenerateService;
		this.productService = productService;
		this.emailService = emailService;
	}
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		try {
			Map<String, Object> data = new HashMap<>();
			List<ProductDTO> productos = productService.listarProductosActivos();
			data.put("productos", productos);
			
			pdfGenerateService.generatePdfFile("catalogo", data, pdfFileName);
			
		}catch (Exception e) {
			throw new RuntimeException("No se pudo preparar el PDF", e);
		}
	}
	
	@Override
	public @Nullable PdfEnvioResultado process(Usuario usuario) throws Exception {
		// Crea las carpetas de usuarios y el futuro archivo pdf
		Path base = Paths.get("salida", "usuarios");
		Path userDir = base.resolve(String.valueOf(usuario.getId()));
		Files.createDirectories(userDir);
		Path destino = userDir.resolve("catalogo.pdf");
		
		// Carpeta donde se creó el pdf (desde pdfGenerateService)
		Path plantilla = Paths.get(pdfDirectory, pdfFileName);
		
		if(Files.notExists(destino)) {
			try {
				Files.createLink(destino, plantilla);
			}catch(Exception e) {
				Files.copy(plantilla, destino, StandardCopyOption.REPLACE_EXISTING);
			}
		}
		
		boolean emailValid = true;
		
		if(!isValidEmailAddress(usuario.getEmail())) {
			emailValid = false;
		}
			
		return new PdfEnvioResultado(usuario.getId(), usuario.getEmail(), emailValid, true, destino.toString(), null);
	}
	
	public static boolean isValidEmailAddress(String email) {
		return Pattern.compile("^(.+)@(\\S+)$")
				.matcher(email)
				.matches();
	}
	
}
