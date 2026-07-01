package com.kevin.tienda_online.dto.response;

import com.kevin.tienda_online.model.Rol;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponse {

    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String direccion;
    private Rol rol;

}
