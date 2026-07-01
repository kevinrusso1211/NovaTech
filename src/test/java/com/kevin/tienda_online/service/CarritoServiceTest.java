package com.kevin.tienda_online.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kevin.tienda_online.dto.request.AgregarProductoCarritoRequest;
import com.kevin.tienda_online.dto.response.CarritoResponse;
import com.kevin.tienda_online.exception.CarritoNoEncontradoException;
import com.kevin.tienda_online.exception.ProductoNoExisteEnCarritoException;
import com.kevin.tienda_online.exception.StockInsuficienteException;
import com.kevin.tienda_online.model.Carrito;
import com.kevin.tienda_online.model.CarritoItem;
import com.kevin.tienda_online.model.Producto;
import com.kevin.tienda_online.repository.CarritoRepository;
import com.kevin.tienda_online.repository.ProductoRepository;


@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private CarritoService carritoService;

    @Test
    void deberiaAgregarProductoAlCarrito() {

        // Arrange
        String usuarioId = "user1";

        AgregarProductoCarritoRequest request = new AgregarProductoCarritoRequest();
        request.setProductoId("prod1");
        request.setCantidad(2);

        Carrito carrito = new Carrito();
        carrito.setUsuarioId(usuarioId);

        Producto producto = new Producto();
        producto.setId("prod1");
        producto.setNombre("Mouse Gamer");
        producto.setPrecio(new BigDecimal("279900"));
        producto.setStock(10);

        when(carritoRepository.findByUsuarioId(usuarioId))
                .thenReturn(carrito);

        when(productoRepository.findById("prod1"))
                .thenReturn(Optional.of(producto));

        when(carritoRepository.save(any(Carrito.class)))
                .thenReturn(carrito);

        // Act
        CarritoResponse response =
                carritoService.agregarProducto(usuarioId, request);

        // Assert
        assertEquals(1, response.getItems().size());

        assertEquals(
                "Mouse Gamer",
                response.getItems().get(0).getNombreProducto()
        );

        assertEquals(
                new BigDecimal("559800"),
                response.getTotal()
        );

        verify(carritoRepository).save(any(Carrito.class));
    }

    @Test
    void deberiaLanzarStockInsuficiente() {
        // Arrange
        String usuarioId = "user1";

        AgregarProductoCarritoRequest request = new AgregarProductoCarritoRequest();
        request.setProductoId("prod1");
        request.setCantidad(10); // El usuario quiere 10

        Carrito carrito = new Carrito();
        carrito.setUsuarioId(usuarioId);

        Producto producto = new Producto();
        producto.setId("prod1");
        producto.setNombre("Mouse Gamer");
        producto.setPrecio(new BigDecimal("279900"));
        producto.setStock(5); // Pero solo hay 5

        when(carritoRepository.findByUsuarioId(usuarioId))
        .thenReturn(carrito);

        when(productoRepository.findById("prod1"))
        .thenReturn(Optional.of(producto));

        // Act + Assert
        assertThrows(
        StockInsuficienteException.class,
        () -> carritoService.agregarProducto(usuarioId, request)
        );

        // Verificamos que sí buscó el carrito y el producto
        verify(carritoRepository).findByUsuarioId(usuarioId);
        verify(productoRepository).findById("prod1");

        // Y que NO guardó el carrito porque ocurrió la excepción
        verify(carritoRepository, never()).save(any(Carrito.class));
    }

        @Test
        void deberiaActualizarCantidadDelProducto() {

            // Arrange
            String usuarioId = "user1";
            String productoId = "prod1";

            CarritoItem item = new CarritoItem();
            item.setProductoId(productoId);
            item.setNombreProducto("Mouse Gamer");
            item.setPrecio(new BigDecimal("279900"));
            item.setCantidad(2);

            Carrito carrito = new Carrito();
            carrito.setUsuarioId(usuarioId);
            carrito.getItems().add(item);

            Producto producto = new Producto();
            producto.setId(productoId);
            producto.setStock(10);

            when(carritoRepository.findByUsuarioId(usuarioId))
                    .thenReturn(carrito);

            when(productoRepository.findById(productoId))
                    .thenReturn(Optional.of(producto));

            when(carritoRepository.save(any(Carrito.class)))
                    .thenReturn(carrito);

            // Act
            CarritoResponse response =
                carritoService.actualizarCantidad(usuarioId, productoId, 5);

            // Assert
            assertEquals(5, response.getItems().get(0).getCantidad());

            verify(carritoRepository).save(any(Carrito.class));
        }  
        
        @Test
        void deberiaLanzarProductoNoExisteEnCarrito(){
                String usuarioId="user1";
                String productoId="product1";
                String productoInexistente = "product2";

                CarritoItem item = new CarritoItem();
                item.setProductoId(productoId);
                item.setNombreProducto("Mouse Gamer");
                item.setPrecio(new BigDecimal("279900"));
                item.setCantidad(2);

                Carrito carrito = new Carrito();
                carrito.setUsuarioId(usuarioId);
                carrito.getItems().add(item);

                when(carritoRepository.findByUsuarioId(usuarioId))
                    .thenReturn(carrito);

                assertThrows(
        ProductoNoExisteEnCarritoException.class,
                        () -> carritoService.actualizarCantidad(
                                usuarioId,
                                productoInexistente,
                                5
                        )       
                );

                verify(carritoRepository, never())
                        .save(any(Carrito.class));
                
        }

        @Test
        void deberiaVaciarCarrito() {

                // Arrange
                String usuarioId = "user1";

                CarritoItem item = new CarritoItem();
                item.setNombreProducto("Mouse Gamer");
                item.setPrecio(new BigDecimal("279900"));
                item.setCantidad(2);

                Carrito carrito = new Carrito();
                carrito.setUsuarioId(usuarioId);
                carrito.getItems().add(item);

                when(carritoRepository.findByUsuarioId(usuarioId))
                        .thenReturn(carrito);

                when(carritoRepository.save(any(Carrito.class)))
                        .thenReturn(carrito);

                // Act
                CarritoResponse response =
                        carritoService.vaciarCarrito(usuarioId);

                // Assert
                assertEquals(0, response.getItems().size());
                assertEquals(BigDecimal.ZERO, response.getTotal());

                verify(carritoRepository)
                        .save(any(Carrito.class));
        }

        @Test
        void deberiaLanzarCarritoNoEncontradoAlVaciar() {

        // Arrange
        String usuarioId = "user1";

        when(carritoRepository.findByUsuarioId(usuarioId))
                .thenReturn(null);

        // Act + Assert
        assertThrows(
                CarritoNoEncontradoException.class,
                () -> carritoService.vaciarCarrito(usuarioId)
        );

        // Verify
        verify(carritoRepository, never())
                .save(any(Carrito.class));
        }

        @Test
        void deberiaEliminarProductoDelCarrito() {

        // Arrange
        String usuarioId = "user1";
        String productoId = "prod1";

        CarritoItem item = new CarritoItem();
        item.setProductoId(productoId);
        item.setNombreProducto("Mouse Gamer");
        item.setPrecio(new BigDecimal("279900"));
        item.setCantidad(2);

        Carrito carrito = new Carrito();
        carrito.setUsuarioId(usuarioId);
        carrito.getItems().add(item);

        when(carritoRepository.findByUsuarioId(usuarioId))
                .thenReturn(carrito);

        when(carritoRepository.save(any(Carrito.class)))
                .thenReturn(carrito);

        // Act
        CarritoResponse response =
                carritoService.eliminarProducto(usuarioId, productoId);

        // Assert
        assertEquals(0, response.getItems().size());
        assertEquals(BigDecimal.ZERO, response.getTotal());

        // Verify
        verify(carritoRepository)
                .save(any(Carrito.class));
        }

        
}
