package com.ecommerce.ecommerce_backend.factory;

import com.ecommerce.ecommerce_backend.builder.ProductoBuilder;
import com.ecommerce.ecommerce_backend.model.Categoria;
import com.ecommerce.ecommerce_backend.model.Producto;

// Patrón: Factory
public class ProductoFactory {

    public static Producto crearProducto(String nombre, double precio, String imagenURL, Categoria categoria) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio del producto no puede ser negativo.");
        }

        String urlFinal = (imagenURL == null || imagenURL.trim().isEmpty()) 
                ? "https://images.unsplash.com/photo-1523474253046-8cd2748b5fd2" 
                : imagenURL;

        return new ProductoBuilder()
                .conNombre(nombre)
                .conPrecio(precio)
                .conImagenURL(urlFinal)
                .conCategoria(categoria)
                .conStock(0)
                .build();
    }
}
