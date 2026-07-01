package com.kevin.tienda_online.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kevin.tienda_online.dto.request.AgregarProductoCarritoRequest;
import com.kevin.tienda_online.dto.response.CarritoResponse;
import com.kevin.tienda_online.exception.CantidadInvalidaException;
import com.kevin.tienda_online.exception.CarritoNoEncontradoException;
import com.kevin.tienda_online.exception.ProductoNoEncontradoException;
import com.kevin.tienda_online.exception.ProductoNoExisteEnCarritoException;
import com.kevin.tienda_online.exception.StockInsuficienteException;
import com.kevin.tienda_online.model.Carrito;
import com.kevin.tienda_online.model.CarritoItem;
import com.kevin.tienda_online.model.Producto;
import com.kevin.tienda_online.repository.CarritoRepository;
import com.kevin.tienda_online.repository.ProductoRepository;
import com.kevin.tienda_online.utils.Mensajes;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public CarritoResponse agregarProducto(
            String usuarioId,
            AgregarProductoCarritoRequest request
    ) {

        if (request.getCantidad() <= 0) {
            throw new CantidadInvalidaException(Mensajes.CANTIDAD_INVALIDA);
        }

        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId);

        if (carrito == null) {
            carrito = new Carrito();
            carrito.setUsuarioId(usuarioId);
        }

        Producto producto = productoRepository.findById(
                request.getProductoId()
        ).orElseThrow(() ->
                new ProductoNoEncontradoException(
                        Mensajes.PRODUCTO_NO_ENCONTRADO
                )
        );

        CarritoItem itemExistente = null;

        for (CarritoItem item : carrito.getItems()) {
            if (item.getProductoId().equals(producto.getId())) {
                itemExistente = item;
                break;
            }
        }

        if (itemExistente != null) {
            int nuevaCantidad = itemExistente.getCantidad() + request.getCantidad();

            if (nuevaCantidad > producto.getStock()) {
                throw new StockInsuficienteException(Mensajes.STOK_INSUFICIENTE);
            }
            itemExistente.setCantidad(nuevaCantidad);

        } else {

            if (request.getCantidad() > producto.getStock()) {
                throw new StockInsuficienteException(Mensajes.STOK_INSUFICIENTE);
            }

            CarritoItem nuevoItem = new CarritoItem();

            nuevoItem.setProductoId(producto.getId());
            nuevoItem.setNombreProducto(producto.getNombre());

            nuevoItem.setPrecio(producto.getPrecio());
            nuevoItem.setCantidad(request.getCantidad());

            carrito.getItems().add(nuevoItem);
        }

        carritoRepository.save(carrito);

        return convertirAResponse(carrito);
    }

    private CarritoResponse convertirAResponse(Carrito carrito) {

        CarritoResponse response = new CarritoResponse();

        response.setId(carrito.getId());
        response.setUsuarioId(carrito.getUsuarioId());
        response.setItems(carrito.getItems());

        BigDecimal total = BigDecimal.ZERO;

        for (CarritoItem item : carrito.getItems()) {
            total = total.add(item.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));
        }

        response.setTotal(total);

        return response;
    }

    public CarritoResponse obtenerCarrito(String usuarioId){
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId);

        if(carrito == null){
            carrito = new Carrito();
            carrito.setUsuarioId(usuarioId);
            carritoRepository.save(carrito);
        }
        return convertirAResponse(carrito);
    }

    public CarritoResponse eliminarProducto(String usuarioId, String productoId){
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId);
        if(carrito == null){
            throw new RuntimeException("El carrito no existe");
        }
        carrito.getItems().removeIf(
            item ->  item.getProductoId().equals(productoId)
        );
        carritoRepository.save(carrito);
        return convertirAResponse(carrito);
    }

    public CarritoResponse vaciarCarrito(String usuarioId){
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId);
        if(carrito == null){
            throw new CarritoNoEncontradoException(Mensajes.CARRITO_NO_ENCONTRADO);
        }
        carrito.getItems().clear();
        carritoRepository.save(carrito);
        return convertirAResponse(carrito);
    }

    public CarritoResponse actualizarCantidad(
        String usuarioId,
        String productoId,
        Integer cantidad
    ){
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId);
        if(carrito == null){
            throw new CarritoNoEncontradoException(Mensajes.CARRITO_NO_ENCONTRADO);  
        }

        for(CarritoItem item: carrito.getItems()){
            if(item.getProductoId().equals(productoId)){
                if (cantidad <= 0) {
                    throw new CantidadInvalidaException(Mensajes.CANTIDAD_INVALIDA);
                }
                Producto producto = productoRepository.findById(productoId)
                        .orElseThrow(() ->
                                new ProductoNoEncontradoException(Mensajes.PRODUCTO_NO_ENCONTRADO));

                if (cantidad > producto.getStock()) {
                    throw new StockInsuficienteException(Mensajes.STOK_INSUFICIENTE);
                }
                item.setCantidad(cantidad);
                carritoRepository.save(carrito);
                return convertirAResponse(carrito);
            }
        }

        throw new ProductoNoExisteEnCarritoException(Mensajes.PRODUCTO_NO_EXISTE_EN_CARRITO);
    }
}
