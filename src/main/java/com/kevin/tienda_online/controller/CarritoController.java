package com.kevin.tienda_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kevin.tienda_online.dto.request.AgregarProductoCarritoRequest;
import com.kevin.tienda_online.dto.response.CarritoResponse;
import com.kevin.tienda_online.service.CarritoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @PostMapping("/agregar/{usuarioId}")
    public CarritoResponse agregarProducto(
            @PathVariable String usuarioId,
            @RequestBody AgregarProductoCarritoRequest request) {

        return carritoService.agregarProducto(usuarioId, request);
    }

    @GetMapping("/{usuarioId}")
    public CarritoResponse obtenerCarrito(@PathVariable String usuarioId){
        return carritoService.obtenerCarrito(usuarioId);
    }

    @DeleteMapping("/{usuarioId}/producto/{productoId}")
    public CarritoResponse eliminarProducto(
        @PathVariable String usuarioId,
        @PathVariable String productoId
    ){
        return carritoService.eliminarProducto(usuarioId, productoId);
    }

    @DeleteMapping("/vaciar/{usuarioId}")
    public void vaciarCarrito(@PathVariable String usuarioId){
        carritoService.vaciarCarrito(usuarioId);
    }

    @PutMapping("/{usuarioId}/{productoId}/{cantidad}")
    public CarritoResponse actualizarCantidad(
        @PathVariable String usuarioId,
        @PathVariable String productoId,
        @PathVariable Integer cantidad
    ){
        return carritoService.actualizarCantidad(usuarioId, productoId, cantidad);
    }
    
}
