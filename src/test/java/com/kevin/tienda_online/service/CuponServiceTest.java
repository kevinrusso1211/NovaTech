package com.kevin.tienda_online.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kevin.tienda_online.dto.request.CuponRequest;
import com.kevin.tienda_online.dto.response.CuponResponse;
import com.kevin.tienda_online.exception.CuponExpiradoException;
import com.kevin.tienda_online.exception.CuponInactivoException;
import com.kevin.tienda_online.exception.CuponSinUsosException;
import com.kevin.tienda_online.exception.CuponYaExisteException;
import com.kevin.tienda_online.model.Cupon;
import com.kevin.tienda_online.repository.CuponRepository;

@ExtendWith(MockitoExtension.class)
class CuponServiceTest {

    @Mock
    private CuponRepository cuponRepository;

    @InjectMocks
    private CuponService cuponService;

    @Test
    void deberiaCrearCupon() {

        CuponRequest request = new CuponRequest();

        request.setCodigo("BLACK10");
        request.setPorcentajeDescuento(10);
        request.setFechaInicio(LocalDate.now());
        request.setFechaFin(LocalDate.now().plusDays(10));
        request.setMaximoUsos(100);

        when(cuponRepository.findByCodigo("BLACK10"))
                .thenReturn(Optional.empty());

        when(cuponRepository.save(any(Cupon.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CuponResponse response =
                cuponService.crearCupon(request);

        assertEquals("BLACK10", response.getCodigo());

        verify(cuponRepository)
                .save(any(Cupon.class));
    }

    @Test
    void deberiaLanzarCuponYaExiste() {

        // Arrange
        CuponRequest request = new CuponRequest();

        request.setCodigo("BLACK10");
        request.setPorcentajeDescuento(10);
        request.setFechaInicio(LocalDate.now());
        request.setFechaFin(LocalDate.now().plusDays(10));
        request.setMaximoUsos(100);

        Cupon cupon = new Cupon();
        cupon.setCodigo("BLACK10");

        when(cuponRepository.findByCodigo("BLACK10"))
                .thenReturn(Optional.of(cupon));

        // Act + Assert
        assertThrows(
                CuponYaExisteException.class,
                () -> cuponService.crearCupon(request)
        );

        verify(cuponRepository, never())
                .save(any(Cupon.class));
    }

    @Test
    void deberiaAplicarCuponCorrectamente() {

        // Arrange
        Cupon cupon = new Cupon();

        cupon.setCodigo("BLACK10");
        cupon.setActivo(true);
        cupon.setPorcentajeDescuento(10);
        cupon.setFechaInicio(LocalDate.now().minusDays(1));
        cupon.setFechaFin(LocalDate.now().plusDays(10));
        cupon.setMaximoUsos(100);
        cupon.setUsosActuales(0);

        when(cuponRepository.findByCodigo("BLACK10"))
                .thenReturn(Optional.of(cupon));

        when(cuponRepository.save(any(Cupon.class)))
                .thenReturn(cupon);

        // Act
        BigDecimal total = cuponService.aplicarCupon(
                "BLACK10",
                new BigDecimal("100000"));

        // Assert
        assertEquals(
                0,
            total.compareTo(new BigDecimal("90000")));

        assertEquals(1, cupon.getUsosActuales());

        verify(cuponRepository)
                .save(any(Cupon.class));
    }

    @Test
    void deberiaLanzarCuponExpirado() {
    
        Cupon cupon = new Cupon();
    
        cupon.setCodigo("BLACK10");
        cupon.setActivo(true);
        cupon.setPorcentajeDescuento(10);
        cupon.setFechaInicio(LocalDate.now().minusDays(10));
        cupon.setFechaFin(LocalDate.now().minusDays(1));
        cupon.setMaximoUsos(100);
        cupon.setUsosActuales(0);
    
        when(cuponRepository.findByCodigo("BLACK10"))
                .thenReturn(Optional.of(cupon));
    
        assertThrows(
                CuponExpiradoException.class,
                () -> cuponService.aplicarCupon(
                        "BLACK10",
                        new BigDecimal("100000"))
        );
    
        verify(cuponRepository, never())
                .save(any(Cupon.class));
    }

    @Test
    void deberiaLanzarCuponInactivo() {
    
        Cupon cupon = new Cupon();
    
        cupon.setCodigo("BLACK10");
        cupon.setActivo(false);
        cupon.setPorcentajeDescuento(10);
        cupon.setFechaInicio(LocalDate.now().minusDays(1));
        cupon.setFechaFin(LocalDate.now().plusDays(10));
        cupon.setMaximoUsos(100);
        cupon.setUsosActuales(0);
    
        when(cuponRepository.findByCodigo("BLACK10"))
                .thenReturn(Optional.of(cupon));
    
        assertThrows(
                CuponInactivoException.class,
                () -> cuponService.aplicarCupon(
                        "BLACK10",
                        new BigDecimal("100000"))
        );
    
        verify(cuponRepository, never())
                .save(any(Cupon.class));
    }

    @Test
    void deberiaLanzarCuponSinUsos() {
    
        Cupon cupon = new Cupon();
    
        cupon.setCodigo("BLACK10");
        cupon.setActivo(true);
        cupon.setPorcentajeDescuento(10);
        cupon.setFechaInicio(LocalDate.now().minusDays(1));
        cupon.setFechaFin(LocalDate.now().plusDays(10));
        cupon.setMaximoUsos(100);
        cupon.setUsosActuales(100);
    
        when(cuponRepository.findByCodigo("BLACK10"))
                .thenReturn(Optional.of(cupon));
    
        assertThrows(
                CuponSinUsosException.class,
                () -> cuponService.aplicarCupon(
                        "BLACK10",
                        new BigDecimal("100000"))
        );
    
        verify(cuponRepository, never())
                .save(any(Cupon.class));
    }
}
