package com.ecommerce.ecommerce_backend.dto;

public class CarritoItemDTO {

    private Long id;
    private String nombre;
    private Double precio;
    private Integer cantidad;
    private Double subtotal;
    private String imagenURL;

    public CarritoItemDTO() {
    }


    public CarritoItemDTO(
            Long id,
            String nombre,
            Double precio,
            Integer cantidad,
            Double subtotal,
            String imagenURL
    ) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.imagenURL = imagenURL;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }
}