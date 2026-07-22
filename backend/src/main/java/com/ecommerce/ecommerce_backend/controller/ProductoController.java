package com.ecommerce.ecommerce_backend.controller;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecommerce_backend.dto.DescuentoDTO;
import com.ecommerce.ecommerce_backend.facade.CompraFacade;
import com.ecommerce.ecommerce_backend.model.Producto;
import com.ecommerce.ecommerce_backend.service.IInventarioService;
import com.ecommerce.ecommerce_backend.service.ProductoService;

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

@GetMapping("/descuento")
public List<DescuentoDTO> aplicarDescuento(@RequestParam String tipo) {

    return productoService.aplicarDescuento(tipo);

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