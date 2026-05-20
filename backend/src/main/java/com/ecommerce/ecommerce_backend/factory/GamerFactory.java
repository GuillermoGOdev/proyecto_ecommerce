package com.ecommerce.ecommerce_backend.factory;

import com.ecommerce.ecommerce_backend.model.Case;
import com.ecommerce.ecommerce_backend.model.FuenteAlimentacion;
import com.ecommerce.ecommerce_backend.model.MemoriaRam;
import com.ecommerce.ecommerce_backend.model.PlacaMadre;
import com.ecommerce.ecommerce_backend.model.Procesador;
import com.ecommerce.ecommerce_backend.model.TarjetaRed;
import com.ecommerce.ecommerce_backend.model.TarjetaVideo;

public class GamerFactory implements ComponentesFactory {

    @Override
    public Procesador crearProcesador() {
        return new Procesador("Ryzen 9 7900X", "AMD", 450.0, 5, "4.7 GHz");
    }

    @Override
    public MemoriaRam crearMemoriaRam() {
        return new MemoriaRam("Vengeance RGB Pro", "Corsair", 150.0, 20, "32 GB DDR5");
    }

    @Override
    public TarjetaVideo crearTarjetaVideo() {
        return new TarjetaVideo("RTX 4080 Super", "NVIDIA", 1199.99, 3, "16 GB GDDR6X");
    }

    @Override
    public FuenteAlimentacion crearFuenteAlimentacion() {
        return new FuenteAlimentacion("RM850x", "Corsair", 130.0, 10, "850W 80+ Gold");
    }

    @Override
    public PlacaMadre crearPlacaMadre() {
        return new PlacaMadre("X670E AORUS MASTER", "Gigabyte", 350.0, 5, "AM5 E-ATX");
    }

    @Override
    public Case crearCase() {
        return new Case("O11 Dynamic", "Lian Li", 150.0, 8, "Mid Tower");
    }

    @Override
    public TarjetaRed crearTarjetaRed() {
        return new TarjetaRed("Archer TX3000E", "TP-Link", 50.0, 15, "Wi-Fi 6 + Bluetooth 5.0");
    }
}