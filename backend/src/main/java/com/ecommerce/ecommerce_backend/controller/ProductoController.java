package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.model.Producto;
import com.ecommerce.ecommerce_backend.service.IInventarioService;
import com.ecommerce.ecommerce_backend.service.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
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
        return productoService.obtenerTodos().stream().map(p -> {
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