package com.ecommerce.ecommerce_backend.model;

public class Computadora {
    private String nombreConfiguracion;
    private Procesador procesador;
    private MemoriaRam memoriaRam;
    private TarjetaVideo tarjetaVideo;

    public Computadora(String nombreConfiguracion, Procesador procesador, MemoriaRam memoriaRam, TarjetaVideo tarjetaVideo){
        this.nombreConfiguracion = nombreConfiguracion;
        this.procesador = procesador;
        this.memoriaRam = memoriaRam;
        this.tarjetaVideo = tarjetaVideo;
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

    public double getPrecioTotal(){
        double total = 0;
        
        if (procesador != null) total += procesador.getPrecio();
        if (memoriaRam != null) total += memoriaRam.getPrecio();
        if (tarjetaVideo != null) total += tarjetaVideo.getPrecio();
        
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

    
}
