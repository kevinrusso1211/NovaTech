package com.kevin.tienda_online.model;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "resenas")
public class Reseña {

    @Id
    private String id;

    private String usuarioId;
    private String productoId;
    private Integer calificacion;
    private String comentario;
    private LocalDateTime fecha;

}
