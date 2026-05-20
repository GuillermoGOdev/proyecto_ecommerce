package com.ecommerce.ecommerce_backend.adapter;

// Interfaz de abstracción para pasarela de pagos
public interface ProcesadorPago {
    boolean procesarPago(double monto, String emailCliente);
}
