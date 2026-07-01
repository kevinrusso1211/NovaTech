package com.kevin.tienda_online.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kevin.tienda_online.dto.request.LoginRequest;
import com.kevin.tienda_online.dto.request.RegisterRequest;
import com.kevin.tienda_online.dto.response.UsuarioResponse;
import com.kevin.tienda_online.exception.CredencialesInvalidasException;
import com.kevin.tienda_online.exception.UsuarioNoEncontradoException;
import com.kevin.tienda_online.exception.UsuarioYaExisteException;
import com.kevin.tienda_online.model.Rol;
import com.kevin.tienda_online.model.Usuario;
import com.kevin.tienda_online.repository.UsuarioRepository;
import com.kevin.tienda_online.security.JwtService;
import com.kevin.tienda_online.utils.Mensajes;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UsuarioResponse crearUsuario(RegisterRequest request) {

        Usuario usuario = new Usuario();
        Usuario usuarioExistente = usuarioRepository.findByEmail(request.getEmail());

        if(usuarioExistente != null){ 
            throw new UsuarioYaExisteException(Mensajes.USUARIO_YA_EXISTE);
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

        System.out.println("Email recibido: '" + request.getEmail() + "'");
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail());
        System.out.println("Usuario encontrado: " + usuario);

        if (usuario == null) {
            throw new UsuarioNoEncontradoException("El usuario no existe");
        }

        if (passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            String token = jwtService.generarToken(usuario.getEmail());
            usuario.setTokenActivo(token);
            usuarioRepository.save(usuario);

            return token;
        }
        throw new CredencialesInvalidasException(Mensajes.CREDENCIALES_INVALIDAS);
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
