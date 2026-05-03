package com.ecommerce.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecommerce_backend.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>{
    
}
