package com.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.productos.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
