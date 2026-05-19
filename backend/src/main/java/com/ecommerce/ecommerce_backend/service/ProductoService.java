package com.ecommerce.ecommerce_backend.service;
import com.ecommerce.ecommerce_backend.model.Producto;
import com.ecommerce.ecommerce_backend.repository.ProductoRepository;   
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    private final InventarioService inventarioService;
    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository, InventarioService inventarioService) {
        this.repository = repository;
        this.inventarioService = inventarioService;
    }

    // LISTAR TODO CON STOCK CALCULADO
    public List<Producto> listarTodos() {
        List<Producto> productos = repository.findAll();
        for (Producto p : productos) {
            // Inyectamos el stock calculado antes de enviarlo al Frontend
            int stockCalculado = inventarioService.obtenerStockActual(p.getId());
            p.setStock(stockCalculado);
        }
        return productos;
    }

    // OBTENER UNO CON STOCK CALCULADO
    public Producto obtenerPorId(Long id) {
        Producto p = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("¡Ups! Producto no encontrado con ID: " + id));
        
        // También calculamos el stock aquí
        p.setStock(inventarioService.obtenerStockActual(p.getId()));
        return p;
    }

    public Producto guardar(Producto producto) {
        // Al guardar, el stock es @Transient así que JPA lo ignorará automáticamente
        return repository.save(producto);
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Producto no existe");
        }
        repository.deleteById(id);
    }
}