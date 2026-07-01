package com.kevin.tienda_online.dto.response;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReseñaResponse {

    private String id;

    private String nombreUsuario;

    private Integer calificacion;

    private String comentario;

    private LocalDateTime fecha;

}
