package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.model.Producto;
import com.ecommerce.ecommerce_backend.service.IInventarioService;
import com.ecommerce.ecommerce_backend.service.ProductoService;
import com.ecommerce.ecommerce_backend.facade.CompraFacade;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}) // Permite CORS para todos los orígenes y encabezados
@RequestMapping("/producto")
public class ProductoController {
    
    private final IInventarioService inventarioService;
    private final ProductoService productoService;
    private final CompraFacade compraFacade;

    public ProductoController(ProductoService productoService, 
                              IInventarioService inventarioService,
                              CompraFacade compraFacade) {
        this.productoService = productoService;
        this.inventarioService = inventarioService;
        this.compraFacade = compraFacade;
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
        productoMap.put("stock", p.getStock());
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


    @PostMapping("/{id}/movimiento")
    public org.springframework.http.ResponseEntity<?> registrarMovimiento(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            int cantidad = Integer.parseInt(payload.get("cantidad").toString());
            String tipo = payload.get("tipo").toString(); // "ENTRADA" o "SALIDA"
            String observacion = payload.get("observacion").toString();

            // Patrón: Facade
            if ("SALIDA".equalsIgnoreCase(tipo)) {
                System.out.println("[ProductoController] Solicitud de compra recibida. Delegando a CompraFacade...");
                compraFacade.procesarCompra(id, cantidad);
            } else {
                Producto producto = productoService.obtenerPorId(id);
                inventarioService.registrarMovimiento(producto, cantidad, tipo, observacion);
            }
            
            return org.springframework.http.ResponseEntity.ok().build();
            
        } catch (Exception e) {
            System.err.println("[ProductoController] Error al procesar movimiento: " + e.getMessage());
            return org.springframework.http.ResponseEntity.badRequest().body(e.getMessage());
        }
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