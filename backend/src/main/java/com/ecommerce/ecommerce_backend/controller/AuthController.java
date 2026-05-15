package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.model.Usuario;
import com.ecommerce.ecommerce_backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Esto es para que no nos bloquee el navegador
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        boolean esValido = usuarioService.validarLogin(usuario.getUsername(), usuario.getPassword());

        if (esValido) {
            return ResponseEntity.ok("¡Acceso concedido!");
        } else {
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
        }
    }
}