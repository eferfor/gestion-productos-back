package com.productos.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(NotFoundException ex){
		Map<String, Object> body = new HashMap<>();
		body.put("message", ex.getMessage());
		body.put("status", 404);
		body.put("timestamp", Instant.now().toString());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex){
		Map<String, Object> body = new HashMap<>();
		body.put("message", "El id debe ser numérico");
		body.put("status", 400);
		body.put("parameter", ex.getName());
		body.put("value", String.valueOf(ex.getValue()));
		return ResponseEntity.badRequest().body(body);
	}
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex){
		Map<String, Object> body = new HashMap<>();
		body.put("message", ex.getMessage());
		body.put("status", 400);
		body.put("timestamp", Instant.now().toString());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}
	
	@ExceptionHandler(ProductNotFoundForUpdateException.class)
	public ResponseEntity<Map<String, Object>> handleProductNotFoundForUpdateException(ProductNotFoundForUpdateException ex){
		Map<String, Object> body = new HashMap<>();
		body.put("message", ex.getMessage());
		body.put("status", 404);
		body.put("timestamp", Instant.now().toString());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}
	
}
