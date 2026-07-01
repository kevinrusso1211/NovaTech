package com.kevin.tienda_online.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kevin.tienda_online.dto.response.PedidoResponse;
import com.kevin.tienda_online.exception.CarritoVacioException;
import com.kevin.tienda_online.exception.EstadoPedidoInvalidoException;
import com.kevin.tienda_online.exception.PedidoNoEncontradoException;
import com.kevin.tienda_online.exception.ProductoNoEncontradoException;
import com.kevin.tienda_online.exception.StockInsuficienteException;
import com.kevin.tienda_online.exception.UsuarioNoEncontradoException;
import com.kevin.tienda_online.model.Carrito;
import com.kevin.tienda_online.model.CarritoItem;
import com.kevin.tienda_online.model.EstadoPedido;
import com.kevin.tienda_online.model.Pedido;
import com.kevin.tienda_online.model.PedidoItem;
import com.kevin.tienda_online.model.Producto;
import com.kevin.tienda_online.repository.CarritoRepository;
import com.kevin.tienda_online.repository.PedidoRepository;
import com.kevin.tienda_online.repository.ProductoRepository;
import com.kevin.tienda_online.repository.UsuarioRepository;
import com.kevin.tienda_online.utils.Mensajes;

import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CuponService cuponService;

    @Transactional
    public PedidoResponse crearPedido(String usuarioId, String codigoCupon){

        usuarioRepository.findById(usuarioId)
        .orElseThrow(() ->
                new UsuarioNoEncontradoException(Mensajes.USUARIO_NO_ENCONTRADO));

        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId);

        if(carrito == null || carrito.getItems().isEmpty()){
            throw new CarritoVacioException(Mensajes.CARRITO_VACIO);

        }

        Pedido pedido = new Pedido();

        pedido.setUsuarioId(usuarioId);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);

        List<PedidoItem> itemsPedido = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;

        for(CarritoItem item : carrito.getItems()){
            Producto producto = productoRepository.findById(item.getProductoId())
            .orElseThrow(()->new ProductoNoEncontradoException(Mensajes.PRODUCTO_NO_ENCONTRADO));
        

            if(producto.getStock()<item.getCantidad()){
                throw new StockInsuficienteException(Mensajes.STOCK_INSUFICIENTE + producto.getNombre());
            }

            PedidoItem pedidoItem = new PedidoItem();

            pedidoItem.setProductoId(producto.getId());
            pedidoItem.setNombreProducto(producto.getNombre());
            pedidoItem.setPrecio(producto.getPrecio());
            pedidoItem.setCantidad(item.getCantidad());

            itemsPedido.add(pedidoItem);

            total = total.add(producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));
        }

        pedido.setItems(itemsPedido);
                if (codigoCupon != null && !codigoCupon.isBlank()) {

            total = cuponService.aplicarCupon(
                    codigoCupon,
                    total);
        }
        for (CarritoItem item : carrito.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() ->
                            new ProductoNoEncontradoException(
                                    Mensajes.PRODUCTO_NO_ENCONTRADO));
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);
        }
        pedido.setTotal(total);
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return convertirAResponse(pedidoGuardado);

    }

    private PedidoResponse convertirAResponse(Pedido pedido){

        PedidoResponse response = new PedidoResponse();
        response.setId(pedido.getId());
        response.setUsuarioId(pedido.getUsuarioId());
        response.setItems(pedido.getItems());
        response.setTotal(pedido.getTotal());
        response.setFecha(pedido.getFecha());
        response.setEstado(pedido.getEstado());

        return response;
    }

    private Pedido obtenerEntidadPorId(String id){
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(Mensajes.PEDIDO_NO_ENCONTRADO));
    }

    public PedidoResponse cambiarEstadoPedido(String id, EstadoPedido nuevoEstado){
        Pedido pedido = obtenerEntidadPorId(id);
        switch (pedido.getEstado()) {
            case PENDIENTE -> {
                if (nuevoEstado == EstadoPedido.PAGADO) {
                    pedido.setEstado(nuevoEstado);

                } else if (nuevoEstado == EstadoPedido.CANCELADO) {
                    restaurarStock(pedido);
                    pedido.setEstado(nuevoEstado);

                } else {
                    throw new EstadoPedidoInvalidoException(Mensajes.ESTADO_PEDIDO_INVALIDO);
                }
            }

            case PAGADO -> {
                if (nuevoEstado == EstadoPedido.ENVIADO) {
                    pedido.setEstado(nuevoEstado);
                } else {
                    throw new EstadoPedidoInvalidoException(Mensajes.ESTADO_PEDIDO_INVALIDO);
                }
            }

            case ENVIADO -> {
                if (nuevoEstado == EstadoPedido.ENTREGADO) {
                    pedido.setEstado(nuevoEstado);
                } else {
                    throw new EstadoPedidoInvalidoException(Mensajes.ESTADO_PEDIDO_INVALIDO);
                }
            }
            
            default -> throw new EstadoPedidoInvalidoException(Mensajes.ESTADO_PEDIDO_INVALIDO);
        }
        pedidoRepository.save(pedido);

        return convertirAResponse(pedido);
    }

    private void restaurarStock(Pedido pedido) {
        for (PedidoItem item : pedido.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() ->
                            new ProductoNoEncontradoException(Mensajes.PRODUCTO_NO_ENCONTRADO));
            producto.setStock(producto.getStock() + item.getCantidad());
            productoRepository.save(producto);
        }
    }

    public List<PedidoResponse> obtenerTodosLosPedidos(){
        List<PedidoResponse> pedidosResponse = new ArrayList<>();
        List<Pedido> pedidos = pedidoRepository.findAll();
        for(Pedido pedido:pedidos){
            pedidosResponse.add(convertirAResponse(pedido));
        }
        return pedidosResponse;
    }

    public BigDecimal obtenerTotalVentas(){
        List<Pedido> pedidos = pedidoRepository.findAll();
        BigDecimal total = BigDecimal.ZERO;
        for(Pedido pedido:pedidos){
            total = total.add(pedido.getTotal());
        }
        return total;
    }

    public List<PedidoResponse> listarPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public PedidoResponse obtenerPedidoPorId(String id) {
        Pedido pedido = obtenerEntidadPorId(id);
        return convertirAResponse(pedido);
    }

    public List<PedidoResponse> listarPedidosPorUsuario(String usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }
    
}
