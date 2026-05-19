package com.ecommerce.ecommerce_backend.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_backend.adapter.ProductoCarritoAdapter;
import com.ecommerce.ecommerce_backend.dto.CarritoItemDTO;
import com.ecommerce.ecommerce_backend.model.CartItem;
import com.ecommerce.ecommerce_backend.model.Producto;

@Service
public class CartService {

    private final List<CartItem> carrito = new ArrayList<>();


    // ==========================
    // VER CARRITO NORMAL
    // ==========================
    public List<CartItem> obtenerCarrito() {

        return carrito;
    }


    // ==========================
    // VER CARRITO ADAPTADO (DTO)
    // ==========================
    public List<CarritoItemDTO> obtenerCarritoDTO() {

        List<CarritoItemDTO> listaDTO =
                new ArrayList<>();


        for (CartItem item : carrito) {

            CarritoItemDTO dto =
                    ProductoCarritoAdapter.adaptar(

                            item.getProducto(),

                            item.getCantidad()
                    );

            listaDTO.add(dto);
        }

        return listaDTO;
    }


    // ==========================
    // AGREGAR PRODUCTO
    // ==========================
    public void agregarProducto(
            Producto producto,
            int cantidad
    ) {

        for (CartItem item : carrito) {

            if (
                    item.getProducto()
                            .getId()
                            .equals(producto.getId())
            ) {

                item.setCantidad(
                        item.getCantidad() + cantidad
                );

                return;
            }
        }

        carrito.add(
                new CartItem(producto, cantidad)
        );
    }


    // ==========================
    // ELIMINAR PRODUCTO
    // ==========================
    public void eliminarProducto(Long idProducto) {

        carrito.removeIf(
                item ->
                        item.getProducto()
                                .getId()
                                .equals(idProducto)
        );
    }


    // ==========================
    // CALCULAR TOTAL
    // ==========================
    public double calcularTotal() {

        return carrito.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }


    // ==========================
    // VACIAR CARRITO
    // ==========================
    public void vaciarCarrito() {

        carrito.clear();
    }
}