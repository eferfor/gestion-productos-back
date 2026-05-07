package com.productos.controller;

import java.util.Date;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.productos.dto.ProductDTO;
import com.productos.service.IProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/productos")
public class POIController {

	private final IProductService productService;

	public POIController(IProductService productService) {
		super();
		this.productService = productService;
	}
	
	@GetMapping("/downloadExcel")
	public ResponseEntity<byte[]> downloadExcel(@RequestParam(required = false) String nombre, @RequestParam(required = false) String categoria){
		
		try(XSSFWorkbook workbook = new XSSFWorkbook();
				ByteArrayOutputStream out = new ByteArrayOutputStream()){
			
			XSSFSheet sheet = workbook.createSheet("ProductosFiltrados");
			
			List<ProductDTO> productos = productService.listarProductos(nombre, categoria);
			
			List<Object[]> rows = productos.stream()
					.map(this::toRow)
					.toList();
			
			ExcelWriterHelper helper = new ExcelWriterHelper(workbook);
			
			int rownum = 0;
			int numcols = 10;
			
			// Cabecera
			Object[] cabecera = {"ID", "Nombre", "Descripción", "Precio", "Categoría",
					"Marca", "Referencia", "Activo", "Fecha alta", "Usuario", "Imagen"};
			writeRow(sheet, rownum++, cabecera, helper);
			
			// Iterar mapa y añadirlo a la hoja
			for(Object[] rowData : rows) {
				writeRow(sheet, rownum++, rowData, helper);
			}
				
			// Autoajustar columnas
			for(int i = 0; i < numcols; i++) {
				sheet.autoSizeColumn(i);
			}
		
			workbook.write(out);
			byte[] bytes = out.toByteArray();
			
			String filename = "productos.xlsx";
			
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
					.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition")
					.contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
					.contentLength(bytes.length)
					.body(bytes);
				
		}catch(Exception e) {
			throw new RuntimeException("Error al generar el Excel", e);
		}
	}
	
	
	private void writeRow(XSSFSheet sheet, int rowIndex, Object[] values, ExcelWriterHelper helper) {
		XSSFRow row = sheet.createRow(rowIndex);
		
		for(int i = 0; i < values.length; i++) {
			XSSFCell cell = row.createCell(i);
			helper.setCellValue(cell, values[i]);
		}
	}
	
	
	private Object[] toRow(ProductDTO p) {
		return new Object[] {
				p.getId(),
				p.getNombre(),
				p.getDescripcion(),
				p.getPrecio(),
				p.getCategoria(),
				p.getMarca(),
				p.getReferencia(),
				p.getActivo(),
				p.getFechaAlta(),
				p.getAudUser(),
				p.getImagen()
		};
	}
	
	
}
