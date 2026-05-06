package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.model.Producto;

public interface IInventarioService {
    void registrarMovimiento(Producto producto, int cantidad, String tipo, String observacion);
    Integer obtenerStockActual(Long productoId);
}
