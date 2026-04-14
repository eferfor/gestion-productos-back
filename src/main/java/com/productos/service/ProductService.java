package com.productos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productos.dto.ProductDTO;
import com.productos.exception.NotFoundException;
import com.productos.mapper.Mapper;
import com.productos.model.Product;
import com.productos.repository.ProductRepository;

@Service
public class ProductService implements IProductService {

	@Autowired
	private ProductRepository repo;
	
	@Override
	public List<ProductDTO> listarProductos(){
		return repo.findAll().stream().map(Mapper::toDTO).toList();
	}
	
	@Override
	public List<ProductDTO> listarProductos(String nombre, String categoria) {
		nombre = normalizar(nombre);
		categoria = normalizar(categoria);
		
		List<Product> productos;
		
		if(nombre != null && categoria != null) {
			productos = repo.findByNombreContainingIgnoreCaseAndCategoriaIgnoreCase(nombre, categoria);
		}else if(nombre != null) {
			productos = repo.findByNombreContainingIgnoreCase(nombre);
		}else if (categoria != null) {
			productos = repo.findByCategoriaIgnoreCase(categoria);
		}else {
			productos = repo.findAll();
		}
		
		return productos.stream().map(Mapper::toDTO).toList();
		
	}

	@Override
	public ProductDTO detalleProducto(Long id) {
		Product p = repo.findById(id).orElseThrow(() -> new NotFoundException("No se ha encontrado el producto"));
		return Mapper.toDTO(p);
	}

	@Override
	public ProductDTO crearProducto(ProductDTO p) {
		var prod = Product.builder()
				.nombre(p.getNombre())
				.descripcion(p.getDescripcion())
				.precio(p.getPrecio())
				.categoria(p.getCategoria())
				.marca(p.getMarca())
				.referencia(p.getReferencia())
				.activo(p.getActivo())
				.fechaAlta(p.getFechaAlta())
				.audUser(p.getAudUser())
				.build();
		
		return Mapper.toDTO(repo.save(prod));
	}

	@Override
	public ProductDTO modificarProducto(Long id, ProductDTO p) {
		Product prod = repo.findById(id).orElseThrow(() -> new NotFoundException("No se ha encontrado el producto"));
		
		prod.setNombre(p.getNombre());
		prod.setDescripcion(p.getDescripcion());
		prod.setPrecio(p.getPrecio());
		prod.setCategoria(p.getCategoria());
		prod.setMarca(p.getMarca());
		prod.setReferencia(p.getReferencia());
		prod.setActivo(p.getActivo());
		prod.setFechaAlta(p.getFechaAlta());
		prod.setAudUser(p.getAudUser());
		
		return Mapper.toDTO(repo.save(prod));
	}

	@Override
	public void eliminarPorducto(Long id) {
		if(!repo.existsById(id)) {
			throw new NotFoundException("No se ha encontrado el producto");
		}
		
		repo.deleteById(id);
		
	}

	private String normalizar(String s) {
		if(s == null) return null;
		s = s.trim();
		return s.isEmpty() ? null : s;
	}
	
}
