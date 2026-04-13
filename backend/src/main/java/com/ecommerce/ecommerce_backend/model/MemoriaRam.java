package com.ecommerce.ecommerce_backend.model;

public class MemoriaRam extends Componente{
    private String capacidad;

    public MemoriaRam(String nombre, String marca, double precio, int stock, String capacidad){
        super(nombre, marca, precio, stock);
        this.capacidad = capacidad;
    }

    public String getCapacidad(){
        return capacidad;
    } 

    public void setCapacidad(String capacidad){
        this.capacidad = capacidad;
    }
}