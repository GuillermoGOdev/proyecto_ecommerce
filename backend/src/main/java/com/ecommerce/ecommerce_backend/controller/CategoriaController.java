package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.model.Categoria;
import com.ecommerce.ecommerce_backend.service.CategoriaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/categoria")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Categoria> listar() {
        return service.listarTodas();
    }

    @PostMapping
    public Categoria crear(@RequestBody Categoria categoria) {
        return service.guardar(categoria);
    }
}