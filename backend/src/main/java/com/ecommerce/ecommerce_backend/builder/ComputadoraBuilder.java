package com.ecommerce.ecommerce_backend.builder;

import com.ecommerce.ecommerce_backend.model.*;

public class ComputadoraBuilder {
    // Estas son las piezas que el builder irá guardando
    private String nombreConfiguracion;
    private Procesador procesador;
    private MemoriaRam memoriaRam;
    private TarjetaVideo tarjetaVideo;

    
    public ComputadoraBuilder(String nombreConfiguracion) {
        this.nombreConfiguracion = nombreConfiguracion;
    }
    
    public ComputadoraBuilder conProcesador(Procesador procesador){
        this.procesador = procesador;
        return this;
    }
    public ComputadoraBuilder conMemoriaRam(MemoriaRam memoriaRam){
        this.memoriaRam = memoriaRam;
        return this;
    }
    public ComputadoraBuilder conTarjetaVideo(TarjetaVideo tarjetaVideo){
        this.tarjetaVideo = tarjetaVideo;
        return this;
    }

    public Computadora build(){
        return new Computadora(nombreConfiguracion, procesador, memoriaRam, tarjetaVideo);
    }
}
