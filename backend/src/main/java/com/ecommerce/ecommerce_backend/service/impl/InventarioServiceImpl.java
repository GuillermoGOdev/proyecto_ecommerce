package com.ecommerce.ecommerce_backend.service.impl;

import com.ecommerce.ecommerce_backend.model.MovimientoInventario;
import com.ecommerce.ecommerce_backend.model.Producto;
import com.ecommerce.ecommerce_backend.repository.MovimientoRepository;
import com.ecommerce.ecommerce_backend.service.IInventarioService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventarioServiceImpl implements IInventarioService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Override
    @Transactional 
    public void registrarMovimiento(Producto producto, int cantidad, String tipo, String observacion) {
        
        if (tipo.equalsIgnoreCase("SALIDA")) {
            Integer stockActual = obtenerStockActual(producto.getId());
            if (stockActual < cantidad) {
                throw new RuntimeException("No hay suficiente stock para " + producto.getNombre());
            }
            cantidad = -Math.abs(cantidad);
        } else {
            cantidad = Math.abs(cantidad);
        }

        MovimientoInventario movimiento = new MovimientoInventario(producto, cantidad, tipo, observacion);
        movimientoRepository.save(movimiento);
    }

    @Override
    public Integer obtenerStockActual(Long productoId) {
        Integer stock = movimientoRepository.getStockActual(productoId);
        return (stock != null) ? stock : 0;
    }

    @Override
    public List<MovimientoInventario> listarMovimientos() {
        return movimientoRepository.findAll(); // O puedes usar uno ordenado por fecha
    }
}