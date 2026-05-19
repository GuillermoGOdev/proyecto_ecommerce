package com.ecommerce.ecommerce_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecommerce_backend.dto.CarritoItemDTO;
import com.ecommerce.ecommerce_backend.model.Producto;
import com.ecommerce.ecommerce_backend.service.CartService;
import com.ecommerce.ecommerce_backend.service.ProductoService;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin("*")
public class CartController {

    private final CartService cartService;

    private final ProductoService productoService;


    public CartController(
            CartService cartService,
            ProductoService productoService
    ) {

        this.cartService = cartService;
        this.productoService = productoService;
    }


    // ==========================
    // VER CARRITO
    // ==========================
    @GetMapping
    public List<CarritoItemDTO> obtenerCarrito() {

        return cartService.obtenerCarritoDTO();
    }


    // ==========================
    // AGREGAR PRODUCTO
    // ==========================
    @PostMapping("/agregar/{id}")
    public void agregarProducto(

            @PathVariable Long id,

            @RequestParam int cantidad
    ) {

        Producto producto =
                productoService.obtenerPorId(id);

        cartService.agregarProducto(
                producto,
                cantidad
        );
    }


    // ==========================
    // ELIMINAR PRODUCTO
    // ==========================
    @DeleteMapping("/eliminar/{id}")
    public void eliminarProducto(

            @PathVariable Long id
    ) {

        cartService.eliminarProducto(id);
    }


    // ==========================
    // TOTAL
    // ==========================
    @GetMapping("/total")
    public double obtenerTotal() {

        return cartService.calcularTotal();
    }


    // ==========================
    // CHECKOUT
    // ==========================
    @PostMapping("/checkout")
    public String checkout() {

        cartService.vaciarCarrito();

        return "Compra realizada con éxito";
    }
}