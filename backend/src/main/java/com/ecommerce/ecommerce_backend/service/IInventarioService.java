package com.ecommerce.ecommerce_backend.service;

import java.util.List;

import com.ecommerce.ecommerce_backend.model.MovimientoInventario;
import com.ecommerce.ecommerce_backend.model.Producto;

public interface IInventarioService {
    void registrarMovimiento(Producto producto, int cantidad, String tipo, String observacion);
    Integer obtenerStockActual(Long productoId);
    List<MovimientoInventario> listarMovimientos();
}
