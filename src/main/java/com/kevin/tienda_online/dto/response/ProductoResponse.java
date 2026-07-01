package com.kevin.tienda_online.dto.response;

import java.math.BigDecimal;

import com.kevin.tienda_online.model.Categoria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoResponse {

    private String id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String imagenUrl;
    private Categoria categoria;
    private Double calificacionPromedio;
    private Integer totalReseñas;

}
