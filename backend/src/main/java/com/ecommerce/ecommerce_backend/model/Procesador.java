package com.ecommerce.ecommerce_backend.model;

public class Procesador extends Componente{
    private String frecuencia;

    public Procesador(String nombre, String marca, double precio, int stock, String frecuencia) {
        super(nombre, marca, precio, stock);
        this.frecuencia = frecuencia;
    }

    public String getFrecuencia(){
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia){
        this.frecuencia = frecuencia;
    }
}
