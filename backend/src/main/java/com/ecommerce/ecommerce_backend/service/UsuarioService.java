package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.model.Usuario;
import com.ecommerce.ecommerce_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean validarLogin(String user, String pass) {
        Optional<Usuario> usuario = usuarioRepository.findByUsername(user);
        
        if (usuario.isPresent()) {
            // Comparamos la contraseña (en un proyecto real usaríamos cifrado)
            return usuario.get().getPassword().equals(pass);
        }
        return false;
    }
}
