package com.ecommerce.ecommerce_backend.adapter;

// Patrón: Adapter
public class PayPalAdapter implements ProcesadorPago {

    private final PayPalSDK payPalSDK;

    public PayPalAdapter() {
        this.payPalSDK = new PayPalSDK("pagos@tiendaecom.com");
    }

    @Override
    public boolean procesarPago(double monto, String emailCliente) {
        System.out.println("[PayPalAdapter] Iniciando adaptación de pasarela de pago...");
        boolean resultado = payPalSDK.sendPayment(emailCliente, monto);
        System.out.println("[PayPalAdapter] Adaptación de pago completada con éxito.");
        return resultado;
    }
}
