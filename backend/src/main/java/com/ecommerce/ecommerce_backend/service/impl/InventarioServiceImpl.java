package com.ecommerce.ecommerce_backend.service.impl;

import com.ecommerce.ecommerce_backend.model.MovimientoInventario;
import com.ecommerce.ecommerce_backend.model.Producto;
import com.ecommerce.ecommerce_backend.observer.InventarioSubject;
import com.ecommerce.ecommerce_backend.observer.NotificacionInventarioObserver;
import com.ecommerce.ecommerce_backend.repository.MovimientoRepository;
import com.ecommerce.ecommerce_backend.repository.ProductoRepository;
import com.ecommerce.ecommerce_backend.service.IInventarioService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventarioServiceImpl implements IInventarioService {

    @Autowired
    private MovimientoRepository movimientoRepository;
    @Autowired
    private ProductoRepository productoRepository;
    private  final InventarioSubject inventarioSubject = new InventarioSubject();

    public InventarioServiceImpl() {
        inventarioSubject.agregarObservador(new NotificacionInventarioObserver());
    }

    @Override
    @Transactional 
    public void registrarMovimiento(Producto producto, int cantidad, String tipo, String observacion) {
        Integer stockActual = obtenerStockActual(producto.getId());
        if (tipo.equalsIgnoreCase("SALIDA")) {
            if (stockActual < cantidad) {
                throw new RuntimeException("Stock insuficiente para " + producto.getNombre() + 
                                     ". Disponible: " + stockActual + ", Solicitado: " + cantidad);
            }
            producto.setStock(stockActual - cantidad);
            cantidad = -Math.abs(cantidad);
        } else {
            producto.setStock(stockActual + Math.abs(cantidad));
            cantidad = Math.abs(cantidad);
        }

        productoRepository.save(producto);
        MovimientoInventario movimiento = new MovimientoInventario(producto, cantidad, tipo, observacion);
        movimientoRepository.save(movimiento);
        inventarioSubject.notificar(movimiento);
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