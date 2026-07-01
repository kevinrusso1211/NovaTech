package com.kevin.tienda_online.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.kevin.tienda_online.exception.CuponExpiradoException;
import com.kevin.tienda_online.exception.CuponInactivoException;
import com.kevin.tienda_online.exception.CuponSinUsosException;
import com.kevin.tienda_online.dto.request.CuponRequest;
import com.kevin.tienda_online.dto.response.CuponResponse;
import com.kevin.tienda_online.exception.CuponNoEncontradoException;
import com.kevin.tienda_online.exception.CuponYaExisteException;
import com.kevin.tienda_online.model.Cupon;
import com.kevin.tienda_online.repository.CuponRepository;
import com.kevin.tienda_online.utils.Mensajes;

@Service
public class CuponService {

    @Autowired
    private CuponRepository cuponRepository;

    public CuponResponse crearCupon(CuponRequest request) {
    
        Cupon cuponExistente = cuponRepository.findByCodigo(request.getCodigo())
                .orElse(null);
    
        if (cuponExistente != null) {
            throw new CuponYaExisteException(
                    Mensajes.CUPON_YA_EXISTE);
        }
    
        Cupon cupon = new Cupon();
    
        cupon.setCodigo(request.getCodigo().toUpperCase());
        cupon.setPorcentajeDescuento(request.getPorcentajeDescuento());
        cupon.setFechaInicio(request.getFechaInicio());
        cupon.setFechaFin(request.getFechaFin());
        cupon.setMaximoUsos(request.getMaximoUsos());
        cupon.setUsosActuales(0);
        cupon.setActivo(true);
    
        Cupon cuponGuardado = cuponRepository.save(cupon);
    
        return convertirAResponse(cuponGuardado);
    }

    private CuponResponse convertirAResponse(Cupon cupon) {

        CuponResponse response = new CuponResponse();

        response.setId(cupon.getId());
        response.setCodigo(cupon.getCodigo());
        response.setPorcentajeDescuento(cupon.getPorcentajeDescuento());
        response.setFechaInicio(cupon.getFechaInicio());
        response.setFechaFin(cupon.getFechaFin());
        response.setMaximoUsos(cupon.getMaximoUsos());
        response.setUsosActuales(cupon.getUsosActuales());
        response.setActivo(cupon.getActivo());

        return response;
    }

    public List<CuponResponse> listarCupones() {
    
        return cuponRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public CuponResponse obtenerCuponPorCodigo(String codigo) {

        Cupon cupon = cuponRepository.findByCodigo(codigo.toUpperCase())
                .orElseThrow(() ->
                        new CuponNoEncontradoException(
                                Mensajes.CUPON_NO_ENCONTRADO));

        return convertirAResponse(cupon);
    }

    public CuponResponse actualizarCupon(
        String id,
        CuponRequest request) {

        Cupon cupon = cuponRepository.findById(id)
                .orElseThrow(() ->
                        new CuponNoEncontradoException(
                                Mensajes.CUPON_NO_ENCONTRADO));
                                
        Cupon cuponExistente = cuponRepository.findByCodigo(
        request.getCodigo().toUpperCase())
        .orElse(null);

        if (cuponExistente != null &&
                !cuponExistente.getId().equals(cupon.getId())) {
        throw new CuponYaExisteException(
                Mensajes.CUPON_YA_EXISTE);
        }

        cupon.setCodigo(request.getCodigo().toUpperCase());
        cupon.setPorcentajeDescuento(request.getPorcentajeDescuento());
        cupon.setFechaInicio(request.getFechaInicio());
        cupon.setFechaFin(request.getFechaFin());
        cupon.setMaximoUsos(request.getMaximoUsos());

        return convertirAResponse(
                cuponRepository.save(cupon));
    }

    public void eliminarCupon(String id) {

        Cupon cupon = cuponRepository.findById(id)
                .orElseThrow(() ->
                        new CuponNoEncontradoException(
                                Mensajes.CUPON_NO_ENCONTRADO));

        cuponRepository.delete(cupon);
    }

    public BigDecimal aplicarCupon(
            String codigo,
            BigDecimal totalPedido) {
    
        Cupon cupon = cuponRepository.findByCodigo(codigo.toUpperCase())
                .orElseThrow(() ->
                        new CuponNoEncontradoException(
                                Mensajes.CUPON_NO_ENCONTRADO));
    
        if (!cupon.getActivo()) {
            throw new CuponInactivoException(
                    Mensajes.CUPON_INACTIVO);
        }
    
        LocalDate hoy = LocalDate.now();
    
        if (hoy.isBefore(cupon.getFechaInicio())
                || hoy.isAfter(cupon.getFechaFin())) {
    
            throw new CuponExpiradoException(
                    Mensajes.CUPON_EXPIRADO);
        }
    
        if (cupon.getUsosActuales() >= cupon.getMaximoUsos()) {
    
            throw new CuponSinUsosException(
                    Mensajes.CUPON_SIN_USOS);
        }
    
        BigDecimal descuento = totalPedido
            .multiply(BigDecimal.valueOf(cupon.getPorcentajeDescuento()))
            .divide(BigDecimal.valueOf(100));
    
        BigDecimal totalConDescuento = totalPedido.subtract(descuento);
    
        cupon.setUsosActuales(cupon.getUsosActuales() + 1);
    
        cuponRepository.save(cupon);
    
        return totalConDescuento;
    }

}
