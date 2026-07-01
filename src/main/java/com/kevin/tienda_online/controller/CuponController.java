package com.kevin.tienda_online.controller;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.kevin.tienda_online.dto.request.CuponRequest;
import com.kevin.tienda_online.dto.response.CuponResponse;
import com.kevin.tienda_online.service.CuponService;

@RestController
@RequestMapping("/api/cupones")
@Tag(name = "Cupones", description = "Gestión de cupones")
public class CuponController {

    @Autowired
    private CuponService cuponService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear cupón")
    @PostMapping
    public CuponResponse crearCupon(
            @Valid @RequestBody CuponRequest request) {

        return cuponService.crearCupon(request);
    }

    @Operation(summary = "Listar cupones")
    @GetMapping
    public List<CuponResponse> listarCupones() {

        return cuponService.listarCupones();
    }

    @Operation(summary = "Obtener cupón por código")
    @GetMapping("/{codigo}")
    public CuponResponse obtenerCuponPorCodigo(
            @PathVariable String codigo) {

        return cuponService.obtenerCuponPorCodigo(codigo);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar cupón")
    @PutMapping("/{id}")
    public CuponResponse actualizarCupon(
            @PathVariable String id,
            @Valid @RequestBody CuponRequest request) {

        return cuponService.actualizarCupon(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar cupón")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCupon(
            @PathVariable String id) {

        cuponService.eliminarCupon(id);

        return ResponseEntity.noContent().build();
    }
}
