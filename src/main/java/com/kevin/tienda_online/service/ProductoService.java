package com.kevin.tienda_online.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kevin.tienda_online.dto.ProductoRequest;
import com.kevin.tienda_online.dto.ProductoResponse;
import com.kevin.tienda_online.exception.ProductoNoEncontradoException;
import com.kevin.tienda_online.model.Producto;
import com.kevin.tienda_online.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public ProductoResponse crearProducto(ProductoRequest request) {
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setImagenUrl(request.getImagenUrl());
        producto.setCategoria(request.getCategoria());
        return convertirAResponse(productoRepository.save(producto));
    }

    public List<ProductoResponse> listarProductos() {
        List<Producto> productos = productoRepository.findAll();
        List<ProductoResponse> respuestas = new ArrayList<>();

        for(Producto producto : productos) {
            respuestas.add(convertirAResponse(producto));
        }
        return respuestas;
    }

    public ProductoResponse obtenerProductoPorId(String id) {
        return convertirAResponse(obtenerEntidadPorId(id));
    }

    public ProductoResponse actualizarProducto(String id, ProductoRequest request) {
        Producto producto = obtenerEntidadPorId(id);

        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setImagenUrl(request.getImagenUrl());
        producto.setCategoria(request.getCategoria());
        Producto actualizado = productoRepository.save(producto);
        return convertirAResponse(actualizado);
    }

    public void eliminarProducto(String id) {
        productoRepository.delete(obtenerEntidadPorId(id));
    }

    private ProductoResponse convertirAResponse(Producto producto){
        ProductoResponse response = new ProductoResponse();
        response.setId(producto.getId());
        response.setNombre(producto.getNombre());
        response.setDescripcion(producto.getDescripcion());
        response.setPrecio(producto.getPrecio());
        response.setStock(producto.getStock());
        response.setImagenUrl(producto.getImagenUrl());
        response.setCategoria(producto.getCategoria());
        return response;
    }

    private Producto obtenerEntidadPorId(String id){
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado"));
    }
}
