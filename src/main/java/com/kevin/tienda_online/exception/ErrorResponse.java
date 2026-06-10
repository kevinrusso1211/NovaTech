package com.kevin.tienda_online.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private String mensaje;
    private int codigo;
    private LocalDateTime fecha;

}
