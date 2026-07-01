package com.kevin.tienda_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevin.tienda_online.dto.request.LoginRequest;
import com.kevin.tienda_online.dto.request.RegisterRequest;
import com.kevin.tienda_online.dto.response.UsuarioResponse;
import com.kevin.tienda_online.service.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Autenticación", description = "Registro e inicio de sesión")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/registrar")
    public UsuarioResponse registrarUsuario(@Valid @RequestBody RegisterRequest request) {
        return authService.crearUsuario(request);
    }

    @PostMapping("/login")
public ResponseEntity<String> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletResponse response) {

    String token = authService.iniciarSesion(request);

    Cookie cookie = new Cookie("jwt", token);
    cookie.setHttpOnly(true);
    cookie.setSecure(false); // true cuando uses HTTPS
    cookie.setPath("/");
    cookie.setMaxAge(60 * 60 * 24); // 24 horas

    response.addCookie(cookie);

    return ResponseEntity.ok("Inicio de sesión exitoso");
}

}
