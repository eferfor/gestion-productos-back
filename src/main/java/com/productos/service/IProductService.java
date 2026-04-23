package com.productos.service;

import java.util.List;

import com.productos.dto.ProductDTO;
import com.productos.model.Product;

public interface IProductService {

	List<ProductDTO> listarProductos();
	List<ProductDTO> listarProductos(String nombre, String categoria);
	ProductDTO detalleProducto(Long id);
	ProductDTO crearProducto(ProductDTO p);
	ProductDTO modificarProducto(Long id, ProductDTO p);
	void eliminarProducto(Long id);
	
}
