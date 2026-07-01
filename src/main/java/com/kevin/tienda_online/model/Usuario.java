package com.kevin.tienda_online.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "usuarios")
public class Usuario {

    @Id
    private String id;
    
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String direccion;
    private Rol rol;
    
    private String tokenActivo;

}
