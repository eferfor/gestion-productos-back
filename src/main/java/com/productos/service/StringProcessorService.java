package com.productos.service;

import org.springframework.stereotype.Service;

// Esta clase es un ejercicio que no tiene nada que ver con el gestor de productos

@Service
public class StringProcessorService {

	public boolean isPalindrome(String input) {
		String reversed = new StringBuilder(input).reverse().toString();
		return reversed.equals(input);
	}
	
}
