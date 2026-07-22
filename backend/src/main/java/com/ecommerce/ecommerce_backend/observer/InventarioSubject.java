package com.ecommerce.ecommerce_backend.observer;

import com.ecommerce.ecommerce_backend.model.MovimientoInventario;
import java.util.*;

/**
 *
 * @author yixsoviet
 */
public class InventarioSubject {

    private final List<InventarioObserver> observadores = new ArrayList();
    
    public void agregarObservador(InventarioObserver observador) {
        if (observadores != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }
    
    public void eliminarObservador(InventarioObserver observador) {
        if (observadores != null && observadores.contains(observador)) {
            observadores.remove(observador);
        }
    }
    
    public void notificar(MovimientoInventario movimiento) {
        for (InventarioObserver observador: observadores) {
            observador.actualizar(movimiento);
        }
    }

}
