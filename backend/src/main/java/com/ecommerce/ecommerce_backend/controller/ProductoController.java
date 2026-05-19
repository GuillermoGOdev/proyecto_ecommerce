package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.model.Producto;
import com.ecommerce.ecommerce_backend.service.IInventarioService;
import com.ecommerce.ecommerce_backend.service.ProductoService;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}) // Permite CORS para todos los orígenes y encabezados
@RequestMapping("/producto")
public class ProductoController {
    
    private IInventarioService inventarioService;
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService, IInventarioService inventarioService) {
        this.productoService = productoService;
        this.inventarioService = inventarioService;
    }


    @GetMapping
    public List<Map<String, Object>> getProductosConStock() {
        return productoService.listarTodos().stream().map(p -> {
        Map<String, Object> productoMap = new java.util.HashMap<>();
        productoMap.put("id", p.getId());
        productoMap.put("nombre", p.getNombre());
        productoMap.put("precio", p.getPrecio());
        productoMap.put("imagenURL", p.getImagenURL());
        productoMap.put("categoria", p.getCategoria());
        productoMap.put("stock", inventarioService.obtenerStockActual(p.getId()));
        return productoMap;
        }).collect(Collectors.toList());
    }

    @GetMapping("/movimientos")
    public List<Map<String, Object>> getHistorial() {
        return inventarioService.listarMovimientos().stream().map(m -> Map.<String, Object>of(
        "fecha", m.getFecha().toString(),
        "producto", m.getProducto().getNombre(),
        "cantidad", m.getCantidad(),
        "tipo", m.getTipo(),
        "obs", m.getObservacion()
        )).collect(Collectors.toList());
    }

    /*@GetMapping
    public List<Producto> getProducto() {
        return productoService.obtenerTodos();
    }*/

    @GetMapping("/{id}")
    public Producto getProducto(@PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }

    @PostMapping
    public Producto agregarProducto(@RequestBody Producto producto) {
        return productoService.guardar(producto);
    }


    // AQUÍ DEBE ESTAR LA FUNCIÓN DEL MOVIMIENTO
    @PostMapping("/{id}/movimiento")
    public void registrarMovimiento(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        // 1. Buscamos el producto en la DB
        Producto producto = productoService.obtenerPorId(id);
        
        // 2. Extraemos los datos del JSON que mandó el Frontend
        int cantidad = Integer.parseInt(payload.get("cantidad").toString());
        String tipo = payload.get("tipo").toString(); // "ENTRADA" o "SALIDA"
        String observacion = payload.get("observacion").toString();

        // 3. Llamamos al servicio para que guarde el movimiento
        inventarioService.registrarMovimiento(producto, cantidad, tipo, observacion);
    }

    @PutMapping("/{id}")
    public Producto actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto existente = productoService.obtenerPorId(id);
        existente.setNombre(producto.getNombre());
        existente.setPrecio(producto.getPrecio());
        existente.setImagenURL(producto.getImagenURL());
        existente.setCategoria(producto.getCategoria());
        
        return productoService.guardar(existente);
    }

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminar(id);
    }
}