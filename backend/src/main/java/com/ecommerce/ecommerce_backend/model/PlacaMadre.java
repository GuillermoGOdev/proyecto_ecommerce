package com.ecommerce.ecommerce_backend.model;

public class PlacaMadre extends Componente {
    private String formato;

    public PlacaMadre(String nombre, String marca, double precio, int stock, String formato) {
        super(nombre, marca, precio, stock);
        this.formato = formato;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }
}
