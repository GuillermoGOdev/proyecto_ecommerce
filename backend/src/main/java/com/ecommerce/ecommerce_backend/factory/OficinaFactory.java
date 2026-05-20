package com.ecommerce.ecommerce_backend.factory;

import com.ecommerce.ecommerce_backend.model.*;

public class OficinaFactory implements ComponentesFactory {

    @Override
    public MemoriaRam crearMemoriaRam() {
        return new MemoriaRam("Value Select", "Corsair", 40.0, 50, "8 GB DDR4");
    }

    @Override
    public Procesador crearProcesador() {
        return new Procesador("Pentium Gold", "Intel", 65.0, 50, "3.5 GHz");
    }

    @Override
    public TarjetaVideo crearTarjetaVideo() {
        return new TarjetaVideo("Gráficos Integrados UHD 730", "Intel", 0.0, 100, "Compartida");
    }

    @Override
    public FuenteAlimentacion crearFuenteAlimentacion() {
        return new FuenteAlimentacion("CV450", "Corsair", 40.0, 30, "450W 80+ Bronze");
    }

    @Override
    public PlacaMadre crearPlacaMadre() {
        return new PlacaMadre("H610M-K D4", "ASUS", 80.0, 20, "LGA 1700 Micro-ATX");
    }

    @Override
    public Case crearCase() {
        return new Case("Versa H15", "Thermaltake", 35.0, 25, "Micro-ATX Mini Tower");
    }

    @Override
    public TarjetaRed crearTarjetaRed() {
        return new TarjetaRed("Realtek GbE LAN", "Realtek", 0.0, 100, "10/100/1000 Mbps Ethernet Integrada");
    }
}