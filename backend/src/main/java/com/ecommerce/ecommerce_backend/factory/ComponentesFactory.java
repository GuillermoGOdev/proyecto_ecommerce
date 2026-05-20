package com.ecommerce.ecommerce_backend.factory;

import com.ecommerce.ecommerce_backend.model.*;

public interface ComponentesFactory {
    Procesador crearProcesador();
    MemoriaRam crearMemoriaRam();
    TarjetaVideo crearTarjetaVideo();
    FuenteAlimentacion crearFuenteAlimentacion();
    PlacaMadre crearPlacaMadre();
    Case crearCase();
    TarjetaRed crearTarjetaRed();
}
