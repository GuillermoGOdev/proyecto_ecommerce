package com.ecommerce.ecommerce_backend.builder;

import com.ecommerce.ecommerce_backend.model.Categoria;
import com.ecommerce.ecommerce_backend.model.Producto;

// Patrón: Builder
public class ProductoBuilder {

    private String nombre;
    private double precio;
    private String imagenURL;
    private Categoria categoria;
    private int stock;

    public ProductoBuilder() {}

    public ProductoBuilder conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public ProductoBuilder conPrecio(double precio) {
        this.precio = precio;
        return this;
    }

    public ProductoBuilder conImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
        return this;
    }

    public ProductoBuilder conCategoria(Categoria categoria) {
        this.categoria = categoria;
        return this;
    }

    public ProductoBuilder conStock(int stock) {
        this.stock = stock;
        return this;
    }

    public Producto build() {
        Producto producto = new Producto();
        producto.setNombre(this.nombre);
        producto.setPrecio(this.precio);
        producto.setImagenURL(this.imagenURL);
        producto.setCategoria(this.categoria);
        producto.setStock(this.stock);
        return producto;
    }
}
