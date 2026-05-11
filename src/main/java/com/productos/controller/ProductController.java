package com.productos.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.productos.dto.ProductDTO;
import com.productos.service.IProductService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/productos")
public class ProductController {
	
	@Autowired
	private IProductService service;
	
	@GetMapping
	public ResponseEntity<List<ProductDTO>> listarProductos(@RequestParam(required = false) String nombre, @RequestParam(required = false) String categoria){
		return ResponseEntity.ok(service.listarProductos(nombre, categoria));
	}
	
	@GetMapping("/activos")
	public ResponseEntity<List<ProductDTO>> listarProductosActivos(){
		return ResponseEntity.ok(service.listarProductosActivos());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> detalleProducto(@PathVariable Long id){
		return ResponseEntity.ok(service.detalleProducto(id));
	}
	
	@PostMapping
	public ResponseEntity<ProductDTO> crearProducto(@Valid @RequestBody ProductDTO p){
		ProductDTO productoNuevo = service.crearProducto(p);
		return ResponseEntity.created(URI.create("/api/productos" + productoNuevo.getId())).body(productoNuevo);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ProductDTO> modificarProducto(@PathVariable Long id, @Valid @RequestBody ProductDTO p){
		return ResponseEntity.ok(service.modificarProducto(id, p));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarProducto(@PathVariable Long id){
		service.eliminarProducto(id);
		return ResponseEntity.noContent().build();
	}

}
