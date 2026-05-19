package com.ecommerce.ecommerce_backend.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_backend.model.Producto;
import com.ecommerce.ecommerce_backend.repository.ProductoRepository;

@Service
public class ProductoService {

    
    private final ProductoRepository repository;

    
    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    // MTODOS DE LÓGICA DE NEGOCIO
    public List<Producto> obtenerTodos() {
        return repository.findAll();
    }

    public Producto obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("¡Ups! Producto no encontrado con ID: " + id));
    }

    public Producto guardar(Producto producto) {
        return repository.save(producto);
    }

    public void eliminar(Long id) {
        // Antes de eliminar, podríamos verificar si el producto existe
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Producto no existe");
        }
        repository.deleteById(id);
    }
    
}
