package com.ecommerce.ecommerce_backend.repository;

import com.ecommerce.ecommerce_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Este método es "mágico", Spring sabe que debe buscar por la columna 'username'
    Optional<Usuario> findByUsername(String username);
}
