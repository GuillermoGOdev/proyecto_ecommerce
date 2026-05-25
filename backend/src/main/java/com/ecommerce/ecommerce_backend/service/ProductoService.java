package com.ecommerce.ecommerce_backend.service;
import com.ecommerce.ecommerce_backend.model.Producto;
import com.ecommerce.ecommerce_backend.repository.ProductoRepository;
import com.ecommerce.ecommerce_backend.factory.ProductoFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> listarTodos() {
        return repository.findAll();
    }

    public Producto obtenerPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Ups! Producto no encontrado con ID: " + id));
    }

    public Producto guardar(Producto producto) {
        // Patrón: Factory
        Producto nuevoProducto = ProductoFactory.crearProducto(
                producto.getNombre(),
                producto.getPrecio(),
                producto.getImagenURL(),
                producto.getCategoria()
        );
        
        return repository.save(nuevoProducto);
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Producto no existe");
        }
        repository.deleteById(id);
    }
}