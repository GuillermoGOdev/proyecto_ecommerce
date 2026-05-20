package com.ecommerce.ecommerce_backend.builder;

import com.ecommerce.ecommerce_backend.model.*;

public class ComputadoraBuilder {
    private String nombreConfiguracion;
    private Procesador procesador;
    private MemoriaRam memoriaRam;
    private TarjetaVideo tarjetaVideo;
    private FuenteAlimentacion fuenteAlimentacion;
    private PlacaMadre placaMadre;
    private Case gabinete;
    private TarjetaRed tarjetaRed;
    
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
    public ComputadoraBuilder conFuenteAlimentacion(FuenteAlimentacion fuenteAlimentacion) {
        this.fuenteAlimentacion = fuenteAlimentacion;
        return this;
    }
    public ComputadoraBuilder conPlacaMadre(PlacaMadre placaMadre) {
        this.placaMadre = placaMadre;
        return this;
    }
    public ComputadoraBuilder conCase(Case gabinete) {
        this.gabinete = gabinete;
        return this;
    }
    public ComputadoraBuilder conTarjetaRed(TarjetaRed tarjetaRed) {
        this.tarjetaRed = tarjetaRed;
        return this;
    }

    public Computadora build(){
        return new Computadora(nombreConfiguracion, procesador, memoriaRam, tarjetaVideo, 
                               fuenteAlimentacion, placaMadre, gabinete, tarjetaRed);
    }
}