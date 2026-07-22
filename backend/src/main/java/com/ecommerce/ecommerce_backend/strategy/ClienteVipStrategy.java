package com.ecommerce.ecommerce_backend.strategy;

public class ClienteVipStrategy implements DescuentoStrategy {

    @Override
    public double aplicarDescuento(double precio) {

        return precio * 0.80;

    }

}