package com.ecommerce.ecommerce_backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecommerce_backend.factory.*;
import com.ecommerce.ecommerce_backend.model.Computadora;
import com.ecommerce.ecommerce_backend.service.ComputadoraService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/computadoras")
public class ComputadoraController {

    // Esta variable servirá como dependencia
    private final ComputadoraService computadoraService;

    public ComputadoraController(ComputadoraService computadoraService) {
        this.computadoraService = computadoraService;
    }

    @GetMapping("/gamer")
    public Computadora crearPcGamer() {
        // El controlador solo dice QUÉ quiere, el servicio sabe CÓMO armarlo
        return computadoraService.armarComputadora("PC Gaming Extreme", new GamerFactory());
    }

    @GetMapping("/oficina")
    public Computadora crearPcOficina() {
        return computadoraService.armarComputadora("PC de Oficina Básica", new OficinaFactory());
    }
}
