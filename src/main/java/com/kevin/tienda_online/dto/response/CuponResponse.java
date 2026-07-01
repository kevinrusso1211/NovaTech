package com.kevin.tienda_online.dto.response;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CuponResponse {

    private String id;

    private String codigo;

    private Integer porcentajeDescuento;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private Integer maximoUsos;

    private Integer usosActuales;

    private Boolean activo;

}
