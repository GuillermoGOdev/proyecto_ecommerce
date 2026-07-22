package com.ecommerce.ecommerce_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_backend.dto.DescuentoDTO;
import com.ecommerce.ecommerce_backend.factory.ProductoFactory;
import com.ecommerce.ecommerce_backend.model.Producto;
import com.ecommerce.ecommerce_backend.repository.ProductoRepository;
import com.ecommerce.ecommerce_backend.strategy.BlackFridayStrategy;
import com.ecommerce.ecommerce_backend.strategy.CalculadoraDescuento;
import com.ecommerce.ecommerce_backend.strategy.ClienteNormalStrategy;
import com.ecommerce.ecommerce_backend.strategy.ClientePremiumStrategy;
import com.ecommerce.ecommerce_backend.strategy.ClienteVipStrategy;

import jakarta.transaction.Transactional;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> listarTodos() {
        return repository.findAll();
    }

    public List<DescuentoDTO> aplicarDescuento(String tipo){

    CalculadoraDescuento calculadora = new CalculadoraDescuento();

    switch (tipo.toLowerCase()){

        case "premium":
            calculadora.setStrategy(new ClientePremiumStrategy());
            break;

        case "vip":
            calculadora.setStrategy(new ClienteVipStrategy());
            break;

        case "black":
            calculadora.setStrategy(new BlackFridayStrategy());
            break;

        default:
            calculadora.setStrategy(new ClienteNormalStrategy());

    }

    return repository.findAll().stream()

            .map(p -> new DescuentoDTO(

                    p.getId(),
                    p.getNombre(),
                    p.getPrecio(),
                    calculadora.calcular(p.getPrecio()),
                    p.getImagenURL(),
                    p.getCategoria(),
                    p.getStock()

            ))

            .toList();

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

//    public void eliminar(Long id) {
//        if (!repository.existsById(id)) {
//            throw new RuntimeException("No se puede eliminar: Producto no existe");
//        }
//        repository.deleteById(id);
//    }
    @Transactional
    public void eliminar(Long id) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede eliminar: Producto no existe"));
        repository.delete(producto);
    }

}
