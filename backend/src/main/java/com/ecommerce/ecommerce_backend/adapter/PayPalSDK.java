package com.ecommerce.ecommerce_backend.adapter;

public class PayPalSDK {

    private String cuentaNegocioDestino;

    public PayPalSDK(String cuentaNegocioDestino) {
        this.cuentaNegocioDestino = cuentaNegocioDestino;
    }

    public boolean sendPayment(String receiverEmail, double amountInUSD) {
        System.out.println("[PayPalSDK] Conectando de forma segura con servidores de PayPal...");
        System.out.println("[PayPalSDK] Cobrando S/. " + amountInUSD + " a la cuenta: " + receiverEmail);
        System.out.println("[PayPalSDK] Destino del abono: " + this.cuentaNegocioDestino);
        System.out.println("[PayPalSDK] Transacción aprobada satisfactoriamente por PayPal.");
        return true;
    }
}
