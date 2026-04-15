package com.ecommerce.ecommerce_backend.factory;

import com.ecommerce.ecommerce_backend.model.MemoriaRam;
import com.ecommerce.ecommerce_backend.model.Procesador;
import com.ecommerce.ecommerce_backend.model.TarjetaVideo;

public class GamerFactory implements ComponentesFactory {

    @Override
    public Procesador crearProcesador() {
        // Nombre, marca, precio, stock, frecuencia
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

}
