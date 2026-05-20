package com.ecommerce.ecommerce_backend.model;

public class FuenteAlimentacion extends Componente {
    private String potencia;

    public FuenteAlimentacion(String nombre, String marca, double precio, int stock, String potencia) {
        super(nombre, marca, precio, stock);
        this.potencia = potencia;
    }

    public String getPotencia() {
        return potencia;
    }

    public void setPotencia(String potencia) {
        this.potencia = potencia;
    }
}
