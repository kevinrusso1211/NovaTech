package com.kevin.tienda_online.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kevin.tienda_online.dto.response.FavoritoResponse;
import com.kevin.tienda_online.exception.FavoritoNoEncontradoException;
import com.kevin.tienda_online.exception.FavoritoYaExisteException;
import com.kevin.tienda_online.exception.ProductoNoEncontradoException;
import com.kevin.tienda_online.exception.UsuarioNoEncontradoException;
import com.kevin.tienda_online.model.Favorito;
import com.kevin.tienda_online.model.Producto;
import com.kevin.tienda_online.repository.FavoritoRepository;
import com.kevin.tienda_online.repository.ProductoRepository;
import com.kevin.tienda_online.repository.UsuarioRepository;
import com.kevin.tienda_online.utils.Mensajes;

@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void agregarFavorito(String usuarioId, String productoId) {

        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(Mensajes.USUARIO_NO_ENCONTRADO));

        productoRepository.findById(productoId)
                .orElseThrow(() -> new ProductoNoEncontradoException(Mensajes.PRODUCTO_NO_ENCONTRADO));

        if (favoritoRepository.findByUsuarioIdAndProductoId(usuarioId, productoId).isPresent()) {
            throw new FavoritoYaExisteException(Mensajes.FAVORITO_YA_EXISTE);
        }

        Favorito favorito = new Favorito();

        favorito.setUsuarioId(usuarioId);
        favorito.setProductoId(productoId);
        favorito.setFechaAgregado(LocalDateTime.now());

        favoritoRepository.save(favorito);
    }

    public List<FavoritoResponse> listarFavoritos(String usuarioId) {

        List<Favorito> favoritos = favoritoRepository.findByUsuarioId(usuarioId);
        List<FavoritoResponse> response = new ArrayList<>();

        for (Favorito favorito : favoritos) {
            Producto producto = productoRepository.findById(favorito.getProductoId())
                    .orElseThrow(() ->
                            new ProductoNoEncontradoException(Mensajes.PRODUCTO_NO_ENCONTRADO));

            FavoritoResponse favoritoResponse = new FavoritoResponse();

            favoritoResponse.setId(producto.getId());
            favoritoResponse.setNombre(producto.getNombre());
            favoritoResponse.setDescripcion(producto.getDescripcion());
            favoritoResponse.setPrecio(producto.getPrecio());
            favoritoResponse.setImagenUrl(producto.getImagenUrl());
            favoritoResponse.setCategoria(producto.getCategoria());
            favoritoResponse.setFechaAgregado(favorito.getFechaAgregado());

            response.add(favoritoResponse);
        }
        return response;
    }

    public void eliminarFavorito(String usuarioId, String productoId) {

        Favorito favorito = favoritoRepository
                .findByUsuarioIdAndProductoId(usuarioId, productoId)
                .orElseThrow(() ->
                        new FavoritoNoEncontradoException(Mensajes.FAVORITO_NO_ENCONTRADO));

        favoritoRepository.delete(favorito);
    }

}
