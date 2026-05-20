package com.ecommerce.ecommerce_backend.model;

public class Case extends Componente {
    private String tipo;

    public Case(String nombre, String marca, double precio, int stock, String tipo) {
        super(nombre, marca, precio, stock);
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}