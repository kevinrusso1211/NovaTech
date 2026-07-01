package com.kevin.tienda_online.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "productos")
public class Producto {

    @Id
    private String id;

    private String nombre;
    private String descripcion;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal precio;
    private int stock;
    private String imagenUrl;
    private Categoria categoria;
    private Double calificacionPromedio = 0.0;
    private Integer totalReseñas = 0;

}
