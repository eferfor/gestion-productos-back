package com.productos.dto;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProductDTO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty
	@Column(length = 100)
	private String nombre;
	@NotEmpty
	@Column(length = 255)
	private String descripcion;
	@NotNull
	@Min(value = 0, message = "El precio debe ser positivo")
	private Double precio;
	@NotEmpty
	@Column(length = 50)
	private String categoria;
	@Column(length = 50)
	private String marca;
	@NotEmpty
	@Column(length = 50)
	private String referencia;
	@NotNull
	@Column(columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean activo;
	@Column(name = "fecha_alta", updatable = false)
	@CreationTimestamp
	@JsonProperty("fecha_alta")
	private Date fechaAlta;
	@Column(name = "aud_user", length = 50)
	@JsonProperty("aud_user")
	private String audUser;
	
	private String imagen;
}
