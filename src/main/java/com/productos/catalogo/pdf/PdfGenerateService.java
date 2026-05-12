package com.productos.catalogo.pdf;

import java.io.FileOutputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

@Service
public class PdfGenerateService implements IPdfGenerateService {

	private final TemplateEngine templateEngine;
	
	@Value("${pdf.directory}")
	private String pdfDirectory;
	
	public PdfGenerateService(TemplateEngine templateEngine) {
		this.templateEngine= templateEngine;
	}
	
	@Override
	public void generatePdfFile(String templateName, Map<String, Object> data, String pdfFileName) {
		Context context = new Context();
		context.setVariables(data);
		
		String htmlContent = templateEngine.process(templateName, context);
		
		String baseUri = getClass().getResource("/static/").toExternalForm();
		
		try (FileOutputStream os = new FileOutputStream(pdfDirectory + pdfFileName)){
			PdfRendererBuilder builder = new PdfRendererBuilder();
			
			builder.withHtmlContent(htmlContent, baseUri);
			builder.toStream(os);
			builder.useFastMode();
			builder.run();
		}catch(Exception e) {
			throw new RuntimeException("Error generando PDF", e);
		}
		
	}

}
