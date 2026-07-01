package com.kevin.tienda_online.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kevin.tienda_online.dto.response.PedidoResponse;
import com.kevin.tienda_online.exception.CarritoVacioException;
import com.kevin.tienda_online.exception.EstadoPedidoInvalidoException;
import com.kevin.tienda_online.exception.StockInsuficienteException;
import com.kevin.tienda_online.model.Carrito;
import com.kevin.tienda_online.model.CarritoItem;
import com.kevin.tienda_online.model.EstadoPedido;
import com.kevin.tienda_online.model.Pedido;
import com.kevin.tienda_online.model.PedidoItem;
import com.kevin.tienda_online.model.Producto;
import com.kevin.tienda_online.model.Usuario;
import com.kevin.tienda_online.repository.CarritoRepository;
import com.kevin.tienda_online.repository.PedidoRepository;
import com.kevin.tienda_online.repository.ProductoRepository;
import com.kevin.tienda_online.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private CuponService cuponService;

    @Test
    void deberiaCrearPedidoCorrectamente() {

        // Arrange
        String usuarioId = "user1";

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        CarritoItem carritoItem = new CarritoItem();
        carritoItem.setProductoId("prod1");
        carritoItem.setNombreProducto("Mouse Gamer");
        carritoItem.setPrecio(new BigDecimal("279900"));
        carritoItem.setCantidad(2);

        Carrito carrito = new Carrito();
        carrito.setUsuarioId(usuarioId);
        carrito.getItems().add(carritoItem);

        Producto producto = new Producto();
        producto.setId("prod1");
        producto.setNombre("Mouse Gamer");
        producto.setPrecio(new BigDecimal("279900"));
        producto.setStock(10);

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(usuario));

        when(carritoRepository.findByUsuarioId(usuarioId))
                .thenReturn(carrito);

        when(productoRepository.findById("prod1"))
                .thenReturn(Optional.of(producto));

        when(productoRepository.save(any(Producto.class)))
                .thenReturn(producto);

        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(carritoRepository.save(any(Carrito.class)))
                .thenReturn(carrito);

        // Act
        PedidoResponse response = pedidoService.crearPedido(usuarioId,null);

        // Assert
        assertEquals(usuarioId, response.getUsuarioId());
        assertEquals(1, response.getItems().size());
        assertEquals(new BigDecimal("559800"), response.getTotal());

        assertEquals(8, producto.getStock());

        assertTrue(carrito.getItems().isEmpty());

        verify(productoRepository).save(any(Producto.class));
        verify(pedidoRepository).save(any(Pedido.class));
        verify(carritoRepository).save(any(Carrito.class));
    }

    @Test
    void deberiaLanzarCarritoVacio() {

        String usuarioId = "user1";

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(usuario));

        when(carritoRepository.findByUsuarioId(usuarioId))
                .thenReturn(new Carrito());

        assertThrows(
                CarritoVacioException.class,
                () -> pedidoService.crearPedido(usuarioId,null)
        );

        verify(pedidoRepository, never()).save(any(Pedido.class));
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void deberiaLanzarStockInsuficiente() {

        // Arrange
        String usuarioId = "user1";

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        CarritoItem item = new CarritoItem();
        item.setProductoId("prod1");
        item.setCantidad(5);

        Carrito carrito = new Carrito();
        carrito.setUsuarioId(usuarioId);
        carrito.getItems().add(item);

        Producto producto = new Producto();
        producto.setId("prod1");
        producto.setNombre("Mouse Gamer");
        producto.setPrecio(new BigDecimal("279900"));
        producto.setStock(2); // Stock insuficiente

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(usuario));

        when(carritoRepository.findByUsuarioId(usuarioId))
                .thenReturn(carrito);

        when(productoRepository.findById("prod1"))
                .thenReturn(Optional.of(producto));

        // Act + Assert
        assertThrows(
                StockInsuficienteException.class,
                () -> pedidoService.crearPedido(usuarioId,null)
        );

        // Verify
        verify(productoRepository, never()).save(any(Producto.class));
        verify(pedidoRepository, never()).save(any(Pedido.class));
        verify(carritoRepository, never()).save(any(Carrito.class));
    }

    @Test
    void deberiaCambiarEstadoPedidoAPagado() {

        // Arrange
        Pedido pedido = new Pedido();
        pedido.setId("pedido1");
        pedido.setEstado(EstadoPedido.PENDIENTE);

        when(pedidoRepository.findById("pedido1"))
                .thenReturn(Optional.of(pedido));

        when(pedidoRepository.save(any(Pedido.class)))
                .thenReturn(pedido);

        // Act
        PedidoResponse response =
                pedidoService.cambiarEstadoPedido(
                        "pedido1",
                        EstadoPedido.PAGADO
                );

        // Assert
        assertEquals(EstadoPedido.PAGADO, response.getEstado());

        verify(pedidoRepository)
                .save(any(Pedido.class));
    }

    @Test
    void deberiaLanzarEstadoPedidoInvalido() {
    
        // Arrange
        Pedido pedido = new Pedido();
        pedido.setId("pedido1");
        pedido.setEstado(EstadoPedido.PENDIENTE);

        when(pedidoRepository.findById("pedido1"))
        .thenReturn(Optional.of(pedido));

        // Act + Assert
        assertThrows(
        EstadoPedidoInvalidoException.class,
        () -> pedidoService.cambiarEstadoPedido(
                "pedido1",
                EstadoPedido.ENTREGADO
        )
        );

        verify(pedidoRepository, never())
        .save(any(Pedido.class));
    }

    @Test
    void deberiaCancelarPedidoYRestaurarStock() {

        // Arrange
        Producto producto = new Producto();
        producto.setId("prod1");
        producto.setStock(5);

        PedidoItem item = new PedidoItem();
        item.setProductoId("prod1");
        item.setCantidad(2);

        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setItems(List.of(item));

        when(pedidoRepository.findById("pedido1"))
                .thenReturn(Optional.of(pedido));

        when(productoRepository.findById("prod1"))
                .thenReturn(Optional.of(producto));

        when(productoRepository.save(any(Producto.class)))
                .thenReturn(producto);

        when(pedidoRepository.save(any(Pedido.class)))
                .thenReturn(pedido);

        // Act
        PedidoResponse response =
                pedidoService.cambiarEstadoPedido(
                        "pedido1",
                        EstadoPedido.CANCELADO
                );

        // Assert
        assertEquals(EstadoPedido.CANCELADO, response.getEstado());
        assertEquals(7, producto.getStock());

        verify(productoRepository).save(any(Producto.class));
        verify(pedidoRepository).save(any(Pedido.class));
    }


}
