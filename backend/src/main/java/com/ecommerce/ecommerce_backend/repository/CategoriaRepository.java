package com.ecommerce.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.ecommerce_backend.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
}
