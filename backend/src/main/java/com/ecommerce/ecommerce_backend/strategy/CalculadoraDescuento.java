package com.ecommerce.ecommerce_backend.strategy;

public class CalculadoraDescuento {

    private DescuentoStrategy strategy;

    public void setStrategy(DescuentoStrategy strategy) {
        this.strategy = strategy;
    }

    public double calcular(double precio){

        return strategy.aplicarDescuento(precio);

    }

}