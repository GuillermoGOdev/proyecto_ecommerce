package com.ecommerce.ecommerce_backend.model;

public class TarjetaRed extends Componente {
    private String velocidad;

    public TarjetaRed(String nombre, String marca, double precio, int stock, String velocidad) {
        super(nombre, marca, precio, stock);
        this.velocidad = velocidad;
    }

    public String getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(String velocidad) {
        this.velocidad = velocidad;
    }
}