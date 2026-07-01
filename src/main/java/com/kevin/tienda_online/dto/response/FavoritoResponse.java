package com.kevin.tienda_online.dto.response;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

import com.kevin.tienda_online.model.Categoria;

@Getter
@Setter
public class FavoritoResponse {

    private String id;

    private String nombre;

    private String descripcion;

    private BigDecimal precio;

    private String imagenUrl;

    private Categoria categoria;

    private LocalDateTime fechaAgregado;

}
