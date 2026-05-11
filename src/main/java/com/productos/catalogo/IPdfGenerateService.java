package com.productos.catalogo;

import java.util.Map;

public interface IPdfGenerateService {

	void generatePdfFile(String templateName, Map<String, Object> data, String pdfFileName);
	
}
