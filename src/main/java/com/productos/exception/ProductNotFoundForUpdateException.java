package com.productos.exception;

public class ProductNotFoundForUpdateException extends RuntimeException{

	public ProductNotFoundForUpdateException(Long id) {
		super("No existe el producto con id = " + id);
	}
	

}
