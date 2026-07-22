package com.ecommerce.ecommerce_backend.observer;

import com.ecommerce.ecommerce_backend.model.MovimientoInventario;

/**
 *
 * @author yixsoviet
 */
public class NotificacionInventarioObserver implements InventarioObserver {

    @Override
    public void actualizar(MovimientoInventario movimiento) {
        System.out.printf("""
                           [Notificación Inventario] NOTIFICACIÓN: Se genero un movimiento de inventario
                           Producto: %s
                           Tipo: %s
                           Cantidad: %s
                           Observación %s
                           """, movimiento.getProducto().getNombre(), movimiento.getTipo(), Math.abs(movimiento.getCantidad()), movimiento.getObservacion());
    }
}
