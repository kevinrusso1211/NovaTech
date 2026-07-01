package com.kevin.tienda_online.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "favoritos")
public class Favorito {

    @Id
    private String id;
    private String usuarioId;
    private String productoId;
    private LocalDateTime fechaAgregado;

}
