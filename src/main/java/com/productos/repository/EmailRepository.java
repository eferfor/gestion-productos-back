package com.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.productos.model.Email;

public interface EmailRepository extends JpaRepository<Email, Long>{

}
