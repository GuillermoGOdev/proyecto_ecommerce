package com.ecommerce.ecommerce_backend.dto;

import com.ecommerce.ecommerce_backend.model.Categoria;

public class DescuentoDTO {

    private Long id;
    private String nombre;
    private Double precioOriginal;
    private Double precio;
    private String imagenURL;
    private Categoria categoria;
    private Integer stock;

    public DescuentoDTO(Long id, String nombre, Double precioOriginal,
                        Double precio, String imagenURL,
                        Categoria categoria, Integer stock) {

        this.id = id;
        this.nombre = nombre;
        this.precioOriginal = precioOriginal;
        this.precio = precio;
        this.imagenURL = imagenURL;
        this.categoria = categoria;
        this.stock = stock;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Double getPrecioOriginal() { return precioOriginal; }
    public Double getPrecio() { return precio; }
    public String getImagenURL() { return imagenURL; }
    public Categoria getCategoria() { return categoria; }
    public Integer getStock() { return stock; }
}