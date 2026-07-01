package com.kevin.tienda_online.dto.request;
import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import com.kevin.tienda_online.utils.Mensajes;

@Getter
@Setter
public class CuponRequest {

    @NotBlank(message = Mensajes.CODIGO_OBLIGATORIO)
    private String codigo;

    @NotNull(message = Mensajes.DESCUENTO_OBLIGATORIO)
    @Min(value = 1, message = Mensajes.DESCUENTO_MINIMO)
    @Max(value = 100, message = Mensajes.DESCUENTO_MAXIMO)
    private Integer porcentajeDescuento;

    @NotNull(message = Mensajes.FECHA_INICIO_OBLIGATORIA)
    private LocalDate fechaInicio;

    @NotNull(message = Mensajes.FECHA_FIN_OBLIGATORIA)
    private LocalDate fechaFin;

    @NotNull(message = Mensajes.MAXIMO_USOS_OBLIGATORIO)
    @Min(value = 1, message = Mensajes.MAXIMO_USOS_MINIMO)
    private Integer maximoUsos;

}
