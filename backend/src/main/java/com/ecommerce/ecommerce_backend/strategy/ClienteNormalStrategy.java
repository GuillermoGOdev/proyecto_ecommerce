package com.ecommerce.ecommerce_backend.strategy;

public class ClienteNormalStrategy implements DescuentoStrategy {

    @Override
    public double aplicarDescuento(double precio) {
        return precio;
    }

}