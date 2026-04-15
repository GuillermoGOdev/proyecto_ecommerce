package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.model.Computadora;

public class CalculadorPrecioService {
    public double calcularPrecioFinal(Computadora computadora) {
        double base = computadora.getPrecioTotal();
        // Imagina que aquí aplicamos un impuesto del 18%
        double impuesto = base * 0.18;
        return base + impuesto;
    }

    // comentario de prueba para ver cómo se hace merge entre ramas
}
