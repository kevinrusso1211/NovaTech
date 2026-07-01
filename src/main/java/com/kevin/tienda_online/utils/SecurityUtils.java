package com.kevin.tienda_online.utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.kevin.tienda_online.model.Usuario;
import com.kevin.tienda_online.repository.UsuarioRepository;

@Component
public class SecurityUtils {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario obtenerUsuarioAutenticado() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return usuarioRepository.findByEmail(email);
    }

}
