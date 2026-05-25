package com.ecommerce.ecommerce_backend.service;

import org.springframework.stereotype.Service;
import com.ecommerce.ecommerce_backend.repository.MovimientoRepository;
import com.ecommerce.ecommerce_backend.repository.ProductoRepository;

import jakarta.transaction.Transactional;

import com.ecommerce.ecommerce_backend.model.MovimientoInventario;
import com.ecommerce.ecommerce_backend.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class InventarioService {
    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public void registrarMovimiento(Producto producto, int cantidad, String tipo, String observacion) {
        Integer stockActual = movimientoRepository.getStockActual(producto.getId());
        if (tipo.equalsIgnoreCase("SALIDA")) {
            if (stockActual == null || stockActual < cantidad) {
                throw new IllegalArgumentException("Error: Stock insuficiente para "+producto.getNombre());
            }

            producto.setStock(stockActual - cantidad);
            cantidad = -Math.abs(cantidad);
        } else if (!tipo.equalsIgnoreCase("ENTRADA")) {
            producto.setStock(stockActual + Math.abs(cantidad));
            cantidad = Math.abs(cantidad);
        }

        productoRepository.save(producto);
        MovimientoInventario movimiento = new MovimientoInventario(producto, cantidad, tipo.toUpperCase(), observacion);
        movimientoRepository.save(movimiento);
    }

    public Integer obtenerStockActual(Long productoId) {
        Integer stock = movimientoRepository.getStockActual(productoId);
        return (stock != null) ? stock : 0;
    }
}
