package com.ecommerce.ecommerce_backend.service;

import org.springframework.stereotype.Service;
import com.ecommerce.ecommerce_backend.repository.MovimientoRepository;

import jakarta.transaction.Transactional;

import com.ecommerce.ecommerce_backend.model.MovimientoInventario;
import com.ecommerce.ecommerce_backend.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class InventarioService {
    @Autowired
    private MovimientoRepository movimientoRepository;

    @Transactional
    public void registrarMovimiento(Producto producto, int cantidad, String tipo, String observacion) {
        if (tipo.equalsIgnoreCase("SALIDA")) {
            Integer stockActual = movimientoRepository.getStockActual(producto.getId());
            if (stockActual == null || stockActual < cantidad) {
                throw new IllegalArgumentException("Error: Stock insuficiente para "+producto.getNombre());
            }

            cantidad = -Math.abs(cantidad);
        } else if (!tipo.equalsIgnoreCase("ENTRADA")) {
            cantidad = Math.abs(cantidad);
        }

        MovimientoInventario movimiento = new MovimientoInventario(producto, cantidad, tipo.toUpperCase(), observacion);
        movimientoRepository.save(movimiento);
    }

    public Integer obtenerStockActual(Long productoId) {
        Integer stock = movimientoRepository.getStockActual(productoId);
        return (stock != null) ? stock : 0;
    }
}
