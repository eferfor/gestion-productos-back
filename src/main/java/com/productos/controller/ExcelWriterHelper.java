package com.productos.controller;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriterHelper {

	private final XSSFWorkbook workbook;
	private final CellStyle dateStyle;
	
	public ExcelWriterHelper(XSSFWorkbook workbook) {
		this.workbook = workbook;
		
		CreationHelper ch = workbook.getCreationHelper();
		this.dateStyle = workbook.createCellStyle();
		
		this.dateStyle.setDataFormat(ch.createDataFormat().getFormat("yyy-MM-dd"));
	}
	
	public void setCellValue(Cell cell, Object v) {
		if(v == null) cell.setBlank();
		else if(v instanceof Number n) cell.setCellValue(n.doubleValue());
		else if(v instanceof Boolean b) cell.setCellValue(b);
		else if(v instanceof java.util.Date d) {
			cell.setCellValue(d);
			cell.setCellStyle(dateStyle);
		}else if (v instanceof java.time.LocalDate ld) {
            cell.setCellValue(java.sql.Date.valueOf(ld));
            cell.setCellStyle(dateStyle);
        } else if (v instanceof java.time.LocalDateTime ldt) {
            cell.setCellValue(java.sql.Timestamp.valueOf(ldt));
            cell.setCellStyle(dateStyle);
        } else cell.setCellValue(v.toString());

	}
	
}
