package com.ecommerce.ecommerce_backend.adapter;
import com.ecommerce.ecommerce_backend.dto.CarritoItemDTO;
import com.ecommerce.ecommerce_backend.model.Producto;

public class ProductoCarritoAdapter {

    public static CarritoItemDTO adaptar(
            Producto producto,
            Integer cantidad
    ) {

        Double subtotal =
                producto.getPrecio() * cantidad;

        return new CarritoItemDTO(

                producto.getId(),

                producto.getNombre(),

                producto.getPrecio(),

                cantidad,

                subtotal,

                producto.getImagenURL()
        );
    }
}