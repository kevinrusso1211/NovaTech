package com.kevin.tienda_online.model;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "cupones")
public class Cupon {

    @Id
    private String id;

    private String codigo;
    private Integer porcentajeDescuento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer maximoUsos;
    private Integer usosActuales;
    private Boolean activo;

}
