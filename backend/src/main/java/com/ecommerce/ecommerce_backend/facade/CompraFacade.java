package com.ecommerce.ecommerce_backend.facade;

import com.ecommerce.ecommerce_backend.adapter.PayPalAdapter;
import com.ecommerce.ecommerce_backend.adapter.ProcesadorPago;
import com.ecommerce.ecommerce_backend.model.Producto;
import com.ecommerce.ecommerce_backend.service.IInventarioService;
import com.ecommerce.ecommerce_backend.service.ProductoService;
import com.ecommerce.ecommerce_backend.singleton.SesionUsuario;
import org.springframework.stereotype.Component;

// Patrón: Facade
@Component
public class CompraFacade {

    private final ProductoService productoService;
    private final IInventarioService inventarioService;

    public CompraFacade(ProductoService productoService, IInventarioService inventarioService) {
        this.productoService = productoService;
        this.inventarioService = inventarioService;
    }

    public String procesarCompra(Long productoId, int cantidad) {
        System.out.println("[CompraFacade] Iniciando flujo simplificado de compra...");

        SesionUsuario sesion = SesionUsuario.getInstance();
        System.out.println("[CompraFacade] Consultando Singleton SesionUsuario...");
        System.out.println("[CompraFacade] Usuario activo: " + sesion.getNombre() + " (" + sesion.getRol() + ")");

        if ("ADMIN".equalsIgnoreCase(sesion.getRol())) {
            throw new RuntimeException("Error de Negocio: Los usuarios con rol ADMINISTRADOR no pueden realizar compras. Cambie a rol COMPRADOR.");
        }

        Producto producto = productoService.obtenerPorId(productoId);
        System.out.println("[CompraFacade] Producto solicitado: " + producto.getNombre() + " - Precio: S/. " + producto.getPrecio());

        int stockDisponible = inventarioService.obtenerStockActual(productoId);
        System.out.println("[CompraFacade] Validando stock. Disponible: " + stockDisponible + " - Solicitado: " + cantidad);
        if (stockDisponible < cantidad) {
            throw new RuntimeException("Stock insuficiente para " + producto.getNombre() + ". Disponible: " + stockDisponible);
        }

        double total = producto.getPrecio() * cantidad;
        System.out.println("[CompraFacade] Monto total a cobrar: S/. " + total);

        ProcesadorPago procesador = new PayPalAdapter();
        System.out.println("[CompraFacade] Invocando procesador de pagos (Adapter)...");
        boolean pagoExitoso = procesador.procesarPago(total, sesion.getEmail());

        if (!pagoExitoso) {
            throw new RuntimeException("Error: La pasarela de pago rechazó la transacción.");
        }

        System.out.println("[CompraFacade] Registrando movimiento de salida en el inventario...");
        inventarioService.registrarMovimiento(
                producto, 
                cantidad, 
                "SALIDA", 
                "Venta Online - Cliente: " + sesion.getEmail() + " (" + sesion.getNombre() + ")"
        );

        System.out.println("[CompraFacade] ¡Compra finalizada con éxito!");
        return "Compra exitosa de " + cantidad + "x " + producto.getNombre() + " por S/. " + total + " para " + sesion.getEmail();
    }
}
