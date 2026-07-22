package com.ecommerce.ecommerce_backend.observer;

import com.ecommerce.ecommerce_backend.model.MovimientoInventario;

/**
 *
 * @author yixsoviet
 */
public interface InventarioObserver {
    void actualizar(MovimientoInventario movimiento);
}
