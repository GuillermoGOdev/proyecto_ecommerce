package com.ecommerce.ecommerce_backend.model;

public class Computadora {
    private String nombreConfiguracion;
    private Procesador procesador;
    private MemoriaRam memoriaRam;
    private TarjetaVideo tarjetaVideo;
    private FuenteAlimentacion fuenteAlimentacion;
    private PlacaMadre placaMadre;
    private Case gabinete;
    private TarjetaRed tarjetaRed;

    public Computadora(String nombreConfiguracion, Procesador procesador, MemoriaRam memoriaRam, 
                       TarjetaVideo tarjetaVideo, FuenteAlimentacion fuenteAlimentacion, 
                       PlacaMadre placaMadre, Case gabinete, TarjetaRed tarjetaRed) {
        this.nombreConfiguracion = nombreConfiguracion;
        this.procesador = procesador;
        this.memoriaRam = memoriaRam;
        this.tarjetaVideo = tarjetaVideo;
        this.fuenteAlimentacion = fuenteAlimentacion;
        this.placaMadre = placaMadre;
        this.gabinete = gabinete;
        this.tarjetaRed = tarjetaRed;
    }

    public String getNombreConfiguracion() {
        return nombreConfiguracion;
    }

    public Procesador getProcesador() {
        return procesador;
    }

    public MemoriaRam getMemoriaRam() {
        return memoriaRam;
    }

    public TarjetaVideo getTarjetaVideo() {
        return tarjetaVideo;
    }

    public FuenteAlimentacion getFuenteAlimentacion() {
        return fuenteAlimentacion;
    }

    public PlacaMadre getPlacaMadre() {
        return placaMadre;
    }

    public Case getGabinete() {
        return gabinete;
    }

    public TarjetaRed getTarjetaRed() {
        return tarjetaRed;
    }

    public double getPrecioTotal() {
        double total = 0;
        
        if (procesador != null) total += procesador.getPrecio();
        if (memoriaRam != null) total += memoriaRam.getPrecio();
        if (tarjetaVideo != null) total += tarjetaVideo.getPrecio();
        if (fuenteAlimentacion != null) total += fuenteAlimentacion.getPrecio();
        if (placaMadre != null) total += placaMadre.getPrecio();
        if (gabinete != null) total += gabinete.getPrecio();
        if (tarjetaRed != null) total += tarjetaRed.getPrecio();
        
        return total;
    }

    public void setNombreConfiguracion(String nombreConfiguracion) {
        this.nombreConfiguracion = nombreConfiguracion;
    }

    public void setProcesador(Procesador procesador) {
        this.procesador = procesador;
    }

    public void setMemoriaRam(MemoriaRam memoriaRam) {
        this.memoriaRam = memoriaRam;
    }

    public void setTarjetaVideo(TarjetaVideo tarjetaVideo) {
        this.tarjetaVideo = tarjetaVideo;
    }

    public void setFuenteAlimentacion(FuenteAlimentacion fuenteAlimentacion) {
        this.fuenteAlimentacion = fuenteAlimentacion;
    }

    public void setPlacaMadre(PlacaMadre placaMadre) {
        this.placaMadre = placaMadre;
    }

    public void setGabinete(Case gabinete) {
        this.gabinete = gabinete;
    }

    public void setTarjetaRed(TarjetaRed tarjetaRed) {
        this.tarjetaRed = tarjetaRed;
    }
}
