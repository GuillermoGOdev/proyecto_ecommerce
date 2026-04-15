package com.ecommerce.ecommerce_backend.factory;

import com.ecommerce.ecommerce_backend.model.*;

public class OficinaFactory  implements ComponentesFactory{

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
    
}
