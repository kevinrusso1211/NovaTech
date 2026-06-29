package com.kevin.tienda_online.service;

import com.kevin.tienda_online.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.kevin.tienda_online.dto.ProductoRequest;
import com.kevin.tienda_online.dto.ProductoResponse;
import com.kevin.tienda_online.exception.ProductoNoEncontradoException;
import com.kevin.tienda_online.model.Categoria;
import com.kevin.tienda_online.model.Producto;
import java.util.Optional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void deberiaCrearProductoCorrectamente() {

        // Arrange
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Mouse Gamer");
        request.setDescripcion("Mouse RGB");
        request.setPrecio(new BigDecimal("150000"));
        request.setStock(10);
        request.setImagenUrl("mouse.jpg");
        request.setCategoria(Categoria.COMPUTACION);

        Producto productoGuardado = crearProductoPrueba();

        when(productoRepository.save(any(Producto.class)))
                .thenReturn(productoGuardado);

        // Act
        ProductoResponse response = productoService.crearProducto(request);

        // Assert
        assertEquals("1", response.getId());
        assertEquals("Mouse Gamer", response.getNombre());
        assertEquals(new BigDecimal("150000"), response.getPrecio());
        assertEquals(10, response.getStock());
        assertEquals(Categoria.COMPUTACION, response.getCategoria());

        verify(productoRepository, times(1))
                .save(any(Producto.class));
    }

    @Test
    void deberiaObtenerProductoPorId() {

        // Arrange
        Producto producto = crearProductoPrueba();

        // Mock
        when(productoRepository.findById("1"))
                .thenReturn(Optional.of(producto));

        // Act
        ProductoResponse response = productoService.obtenerProductoPorId("1");

        // Assert
        assertEquals("1", response.getId());
        assertEquals("Mouse Gamer", response.getNombre());
        assertEquals(new BigDecimal("150000"), response.getPrecio());

        verify(productoRepository).findById("1");
    }

    @Test
    void deberiaLanzarExcepcionSiProductoNoExiste() {

        when(productoRepository.findById("1"))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductoNoEncontradoException.class,
                () -> productoService.obtenerProductoPorId("1")
        );

        verify(productoRepository).findById("1");
    }

    private Producto crearProductoPrueba() {
        Producto producto = new Producto();
        producto.setId("1");
        producto.setNombre("Mouse Gamer");
        producto.setDescripcion("Mouse RGB");
        producto.setPrecio(new BigDecimal("150000"));
        producto.setStock(10);
        producto.setImagenUrl("mouse.jpg");
        producto.setCategoria(Categoria.COMPUTACION);
        return producto;
    }
}