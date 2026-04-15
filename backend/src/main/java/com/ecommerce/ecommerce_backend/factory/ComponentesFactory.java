package com.ecommerce.ecommerce_backend.factory;

import com.ecommerce.ecommerce_backend.model.*;

public interface ComponentesFactory {
    Procesador crearProcesador();
    MemoriaRam crearMemoriaRam();
    TarjetaVideo crearTarjetaVideo();
}
