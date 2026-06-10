package com.kevin.tienda_online.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kevin.tienda_online.dto.LoginRequest;
import com.kevin.tienda_online.dto.RegisterRequest;
import com.kevin.tienda_online.dto.UsuarioResponse;
import com.kevin.tienda_online.exception.UsuarioYaExisteException;
import com.kevin.tienda_online.model.Rol;
import com.kevin.tienda_online.model.Usuario;
import com.kevin.tienda_online.repository.UsuarioRepository;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UsuarioResponse crearUsuario(RegisterRequest request) {

        Usuario usuario = new Usuario();
        Usuario usuarioExistente = usuarioRepository.findByEmail(request.getEmail());

        if(usuarioExistente != null){ 
            throw new UsuarioYaExisteException("El usuario ya está registrado");
        }

        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setDireccion(request.getDireccion());
        usuario.setRol(Rol.CLIENTE);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return convertirAResponse(usuarioGuardado);
    }

    public String iniciarSesion(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail());
        if(usuario == null){
            return "Usuario no encontrado";
        }else if(passwordEncoder.matches(request.getPassword(), usuario.getPassword())){
            return "login exitoso";
        }
        return "Contraseña incorrecta";
        
    }

    private UsuarioResponse convertirAResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setNombre(usuario.getNombre());
        response.setApellido(usuario.getApellido());
        response.setEmail(usuario.getEmail());
        response.setDireccion(usuario.getDireccion());
        response.setRol(usuario.getRol());
        return response;
    }
}
