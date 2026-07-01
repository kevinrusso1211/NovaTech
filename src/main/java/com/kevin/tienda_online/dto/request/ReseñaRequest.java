package com.kevin.tienda_online.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import com.kevin.tienda_online.utils.Mensajes;

@Getter
@Setter
public class ReseñaRequest {

    @NotNull(message = Mensajes.CALIFICACION_OBLIGATORIA)
    @Min(value = 1, message = Mensajes.CALIFICACION_MINIMA)
    @Max(value = 5, message = Mensajes.CALIFICACION_MAXIMA)
    private Integer calificacion;

    @NotBlank(message = Mensajes.COMENTARIO_OBLIGATORIO)
    @Size(max = 500, message = Mensajes.COMENTARIO_MAXIMO)
    private String comentario;

}
