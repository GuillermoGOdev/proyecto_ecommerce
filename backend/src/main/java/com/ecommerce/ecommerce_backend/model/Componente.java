package com.ecommerce.ecommerce_backend.model;

public abstract class Componente {
    private String nombre;
    private String marca;
    private double precio;
    private int stock;

    public Componente(String nombre, String marca, double precio, int stock){
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.stock = stock;
    }

    public String getNombre(){
        return nombre;
    }

    public String getMarca(){
        return marca;
    }

    public double getPrecio(){
        return precio;
    }

    public int getStock(){
        return stock;
    }


    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setMarca(String marca){
        this.marca = marca;
    }
    public void setPrecio(double precio){
        this.precio  = precio;
    }
    public void setStock (int stock){
        this.stock = stock;
    }
}
