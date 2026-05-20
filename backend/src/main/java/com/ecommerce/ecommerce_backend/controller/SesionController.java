package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.singleton.SesionUsuario;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/sesion")
public class SesionController {

    @GetMapping
    public Map<String, String> obtenerSesion() {
        SesionUsuario sesion = SesionUsuario.getInstance();
        return Map.of(
                "nombre", sesion.getNombre(),
                "email", sesion.getEmail(),
                "rol", sesion.getRol()
        );
    }

    @PostMapping("/cambiar-rol")
    public Map<String, String> cambiarRol(@RequestParam String rol) {
        SesionUsuario sesion = SesionUsuario.getInstance();
        
        if ("ADMIN".equalsIgnoreCase(rol)) {
            sesion.setRol("ADMIN");
            sesion.setNombre("Administrador de Tienda");
            sesion.setEmail("admin@tiendaecom.com");
        } else {
            sesion.setRol("CLIENTE");
            sesion.setNombre("Cliente Comprador");
            sesion.setEmail("comprador@correo.com");
        }
        
        System.out.println("[SesionController] Cambiando sesión del Singleton...");
        System.out.println("[SesionController] Nueva sesión: " + sesion.getNombre() + " | Rol: " + sesion.getRol());
        
        return Map.of(
                "nombre", sesion.getNombre(),
                "email", sesion.getEmail(),
                "rol", sesion.getRol()
        );
    }
}
