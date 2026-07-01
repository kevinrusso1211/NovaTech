package com.kevin.tienda_online.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kevin.tienda_online.dto.request.ProductoRequest;
import com.kevin.tienda_online.dto.response.ProductoResponse;
import com.kevin.tienda_online.exception.ProductoNoEncontradoException;
import com.kevin.tienda_online.model.Categoria;
import com.kevin.tienda_online.model.Producto;
import com.kevin.tienda_online.repository.ProductoRepository;
import com.kevin.tienda_online.utils.Mensajes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
        response.setCalificacionPromedio(producto.getCalificacionPromedio());
        response.setTotalReseñas(producto.getTotalReseñas());
        return response;
    }

    private Producto obtenerEntidadPorId(String id){
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(Mensajes.PRODUCTO_NO_ENCONTRADO));
    }

    public Page<ProductoResponse> listarProductosPaginados(Pageable pageable){
        Page<ProductoResponse> productos = productoRepository.findAll(pageable)
                .map(this::convertirAResponse);
        System.out.println("Cantidad de productos: " + productos.getTotalElements());
        return productos;
    }

    //Busquedas
    public List<ProductoResponse> buscarPorNombre(String nombre){
        List<Producto> productos =
                productoRepository.findByNombreContainingIgnoreCase(nombre);

        return productos.stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<ProductoResponse> buscarPorCategoria(Categoria categoria) {
        List<Producto> productos = productoRepository.findByCategoriaContainingIgnoreCase(categoria);

        return productos.stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<ProductoResponse> buscarPorPrecio(BigDecimal min, BigDecimal max) {
        List<Producto> productos = productoRepository.findByPrecioBetween(min, max);

        return productos.stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<ProductoResponse> buscarConStock() {
        List<Producto> productos = productoRepository.findByStockGreaterThan(0);

        return productos.stream()
                .map(this::convertirAResponse)
                .toList();
    }
}
