package com.ecommerce.ecommerce_backend.strategy;

public class BlackFridayStrategy implements DescuentoStrategy {

    @Override
    public double aplicarDescuento(double precio) {

        return precio * 0.70;

    }

}