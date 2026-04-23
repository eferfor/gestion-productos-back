package com.productos.service;

import java.time.Year;

import org.springframework.stereotype.Service;

// Otra clase de ejercicio de tests unitarios. Nada que ver con el gestor de productos

@Service
public class DiscountService {

	public float calculateDiscount(float amount, String promoCode) {
		if(promoCode == null) {
			return 0;
		}
		
		if(promoCode.equals("THANKSGIVING")) {
			return amount * .1f;
		}
		
		if(promoCode.equals("XMAS") && getCurrentYear().getValue() == 2026) {
			return amount * .25f;
		}
		
		return 0;
		
	}
	
	Year getCurrentYear() {
		return Year.now();
	}
	
}
