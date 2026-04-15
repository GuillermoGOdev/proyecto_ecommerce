package com.ecommerce.ecommerce_backend.service;

import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_backend.builder.ComputadoraBuilder;
import com.ecommerce.ecommerce_backend.factory.ComponentesFactory;
import com.ecommerce.ecommerce_backend.model.Computadora;

@Service //// Esta anotación le dice a Spring que esta clase es un componente de lógica de negocio
public class ComputadoraService {
    public Computadora armarComputadora(String nombre, ComponentesFactory fabrica) {
        return new ComputadoraBuilder(nombre)
                .conProcesador(fabrica.crearProcesador())
                .conMemoriaRam(fabrica.crearMemoriaRam())
                .conTarjetaVideo(fabrica.crearTarjetaVideo())
                .build();
    }
}
