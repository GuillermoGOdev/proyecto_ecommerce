package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.model.Categoria;
import com.ecommerce.ecommerce_backend.model.Producto;
import java.util.List;
public class PruebaChito {


    public static void main(String[] args) {
        
        ProductoService servicio = new ProductoService();
        List<Producto> lista = servicio.obtenerCatalogo();

        System.out.println("\n=======================================");
        System.out.println("   PRUEBA DE PRODUCTOS - ECOMMERCE     ");
        System.out.println("=======================================");

        // Recorremos tu ArrayList
        for (Producto p : lista) {
            System.out.println("ID: " + p.getId());
            System.out.println("Nombre: " + p.getNombre());
            System.out.println("Precio: S/." + p.getPrecio());
            System.out.println("---------------------------------------");
        }
    }
}

