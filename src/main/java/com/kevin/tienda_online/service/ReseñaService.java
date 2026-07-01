package com.kevin.tienda_online.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kevin.tienda_online.repository.PedidoRepository;
import com.kevin.tienda_online.repository.ProductoRepository;
import com.kevin.tienda_online.repository.ReseñaRepository;

import java.time.LocalDateTime;

import com.kevin.tienda_online.model.EstadoPedido;
import com.kevin.tienda_online.model.Reseña;
import com.kevin.tienda_online.exception.CalificacionInvalidaException;
import com.kevin.tienda_online.exception.CompraNoVerificadaException;
import com.kevin.tienda_online.exception.ProductoNoEncontradoException;
import com.kevin.tienda_online.exception.ReseñaYaExisteException;
import com.kevin.tienda_online.repository.UsuarioRepository;
import com.kevin.tienda_online.model.Usuario;
import com.kevin.tienda_online.dto.response.ReseñaResponse;
import com.kevin.tienda_online.exception.UsuarioNoEncontradoException;
import com.kevin.tienda_online.model.Producto;

import java.util.ArrayList;
import java.util.List;

import com.kevin.tienda_online.utils.Mensajes;

@Service
public class ReseñaService {

    @Autowired
    private ReseñaRepository reseñaRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void crearReseña(
            String usuarioId,
            String productoId,
            Integer calificacion,
            String comentario) {
    
        productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new ProductoNoEncontradoException(
                                Mensajes.PRODUCTO_NO_ENCONTRADO));
    
        if (!pedidoRepository.existeCompraVerificada(
                usuarioId,
                EstadoPedido.ENTREGADO,
                productoId)) {
    
            throw new CompraNoVerificadaException(
                    Mensajes.COMPRA_NO_VERIFICADA);
        }
    
        if (reseñaRepository.findByUsuarioIdAndProductoId(
                usuarioId,
                productoId).isPresent()) {
    
            throw new ReseñaYaExisteException(
                    Mensajes.RESEÑA_YA_EXISTE);
        }
    
        if (calificacion < 1 || calificacion > 5) {
    
            throw new CalificacionInvalidaException(
                    Mensajes.CALIFICACION_INVALIDA);
        }
    
        Reseña reseña = new Reseña();
    
        reseña.setUsuarioId(usuarioId);
        reseña.setProductoId(productoId);
        reseña.setCalificacion(calificacion);
        reseña.setComentario(comentario);
        reseña.setFecha(LocalDateTime.now());
    
        reseñaRepository.save(reseña);
        actualizarCalificacionProducto(productoId);
    }

    public List<ReseñaResponse> listarReseñas(String productoId) {
    
        List<Reseña> reseñas = reseñaRepository.findByProductoId(productoId);
    
        List<ReseñaResponse> response = new ArrayList<>();
    
        for (Reseña reseña : reseñas) {
    
            Usuario usuario = usuarioRepository.findById(reseña.getUsuarioId())
                    .orElseThrow(() ->
                            new UsuarioNoEncontradoException(
                                    Mensajes.USUARIO_NO_ENCONTRADO));
    
            ReseñaResponse reseñaResponse = new ReseñaResponse();
    
            reseñaResponse.setId(reseña.getId());
            reseñaResponse.setNombreUsuario(
                    ocultarNombre(usuario));
            reseñaResponse.setCalificacion(reseña.getCalificacion());
            reseñaResponse.setComentario(reseña.getComentario());
            reseñaResponse.setFecha(reseña.getFecha());
    
            response.add(reseñaResponse);
        }
    
        return response;
    }

    private void actualizarCalificacionProducto(String productoId) {
    
        List<Reseña> reseñas = reseñaRepository.findByProductoId(productoId);
    
        double promedio = reseñas.stream()
                .mapToInt(Reseña::getCalificacion)
                .average()
                .orElse(0);
    
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new ProductoNoEncontradoException(
                                Mensajes.PRODUCTO_NO_ENCONTRADO));
    
        producto.setCalificacionPromedio(promedio);
        producto.setTotalReseñas(reseñas.size());
    
        productoRepository.save(producto);
    }

    private String ocultarNombre(Usuario usuario) {

        String nombre = usuario.getNombre();

        if (usuario.getApellido() == null || usuario.getApellido().isBlank()) {
                return nombre;
        }

        return nombre + " " + usuario.getApellido().charAt(0) + ".";
    }

}