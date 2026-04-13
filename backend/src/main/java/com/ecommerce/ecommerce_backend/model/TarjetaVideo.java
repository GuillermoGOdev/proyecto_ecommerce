package com.ecommerce.ecommerce_backend.model;

public class TarjetaVideo extends Componente{
    private String vram;
    
    public TarjetaVideo(String nombre, String marca, double precio, int stock, String vram){
        super(nombre, marca, precio, stock);
        this.vram = vram;
    }

    public String getVram(){
        return vram;
    }

    public void setVram(String vram){
        this.vram = vram;
    }
}
