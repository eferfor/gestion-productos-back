package com.productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.productos.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

	List<Product> findByNombreContainingIgnoreCase(String nombre);
	List<Product> findByCategoriaIgnoreCase(String categoria);
	List<Product> findByNombreContainingIgnoreCaseAndCategoriaIgnoreCase(String nombre, String categoria);
	Product findByReferencia(String referencia);
	
}
