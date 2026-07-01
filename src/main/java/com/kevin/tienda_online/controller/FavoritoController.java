package com.kevin.tienda_online.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kevin.tienda_online.utils.SecurityUtils;

import com.kevin.tienda_online.dto.response.FavoritoResponse;
import com.kevin.tienda_online.model.Usuario;
import com.kevin.tienda_online.service.FavoritoService;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @Autowired
    private SecurityUtils securityUtils;


    @PostMapping("/{productoId}")
    public ResponseEntity<Void> agregarFavorito(
            @PathVariable String productoId) {

        Usuario usuario = securityUtils.obtenerUsuarioAutenticado();

        favoritoService.agregarFavorito(
                usuario.getId(),
                productoId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoritoResponse>> listarFavoritos() {
    
        Usuario usuario = securityUtils.obtenerUsuarioAutenticado();
    
        return ResponseEntity.ok(
                favoritoService.listarFavoritos(usuario.getId()));
    }

    @DeleteMapping("/{productoId}")
    public ResponseEntity<Void> eliminarFavorito(
            @PathVariable String productoId) {
    
        Usuario usuario = securityUtils.obtenerUsuarioAutenticado();
    
        favoritoService.eliminarFavorito(
                usuario.getId(),
                productoId);
    
        return ResponseEntity.noContent().build();
    }

}
