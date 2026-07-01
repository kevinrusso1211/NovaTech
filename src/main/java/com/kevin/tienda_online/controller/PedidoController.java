package com.kevin.tienda_online.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kevin.tienda_online.dto.response.PedidoResponse;
import com.kevin.tienda_online.model.EstadoPedido;
import com.kevin.tienda_online.model.Usuario;
import com.kevin.tienda_online.service.PedidoService;
import com.kevin.tienda_online.utils.SecurityUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Gestión de pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private SecurityUtils securityUtils;

    @Operation(summary = "Crear pedido", description = "Crea un pedido para el usuario autenticado y aplica un cupón si se proporciona.")
    @PostMapping
    public PedidoResponse crearPedido(
            @RequestParam(required = false) String codigoCupon) {
    
        Usuario usuario = securityUtils.obtenerUsuarioAutenticado();
    
        return pedidoService.crearPedido(
                usuario.getId(),
                codigoCupon);
    }

    @Operation(summary = "Cambiar estado del pedido")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/estado")
    public PedidoResponse cambiarEstadoPedido(
            @PathVariable String id,
            @RequestParam EstadoPedido estado) {

        return pedidoService.cambiarEstadoPedido(id, estado);
    }

    @Operation(summary = "Listar todos los pedidos")
    @GetMapping
    public List<PedidoResponse> listarPedidos() {
        return pedidoService.listarPedidos();
    }

    @Operation(summary = "Obtener pedido por ID")
    @GetMapping("/{id}")
    public PedidoResponse obtenerPedidoPorId(@PathVariable String id) {
        return pedidoService.obtenerPedidoPorId(id);
    }

    @Operation(summary = "Listar pedidos de un usuario")
    @GetMapping("/usuario/{usuarioId}")
    public List<PedidoResponse> listarPedidosPorUsuario(
            @PathVariable String usuarioId) {

        return pedidoService.listarPedidosPorUsuario(usuarioId);
    }
    
}
