package com.ecommerce.ecommerce_backend.strategy;

public class ClientePremiumStrategy implements DescuentoStrategy {

    @Override
    public double aplicarDescuento(double precio) {

        return precio * 0.90;

    }

}