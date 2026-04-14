package com.productos.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProductDTO {

	private Long id;
	private String nombre;
	private String descripcion;
	private Double precio;
	private String categoria;
	private String marca;
	private String referencia;
	private Boolean activo;
	private Date fechaAlta;
	private String audUser;
	
}
