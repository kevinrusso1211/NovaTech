package com.kevin.tienda_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import com.kevin.tienda_online.dto.response.ReseñaResponse;
import jakarta.validation.Valid;

import com.kevin.tienda_online.dto.request.ReseñaRequest;
import com.kevin.tienda_online.model.Usuario;

import com.kevin.tienda_online.service.ReseñaService;
import com.kevin.tienda_online.utils.SecurityUtils;

@RestController
@RequestMapping("/api/resenas")
public class ReseñaController {

    @Autowired
    private ReseñaService reseñaService;

    @Autowired
    private SecurityUtils securityUtils;

    @PostMapping("/{productoId}")
    public ResponseEntity<Void> crearReseña(
            @PathVariable String productoId,
            @Valid @RequestBody ReseñaRequest request) {
    
        Usuario usuario = securityUtils.obtenerUsuarioAutenticado();
    
        reseñaService.crearReseña(
                usuario.getId(),
                productoId,
                request.getCalificacion(),
                request.getComentario());
    
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{productoId}")
    public ResponseEntity<List<ReseñaResponse>> listarReseñas(
            @PathVariable String productoId) {
    
        return ResponseEntity.ok(
                reseñaService.listarReseñas(productoId));
    }

}
