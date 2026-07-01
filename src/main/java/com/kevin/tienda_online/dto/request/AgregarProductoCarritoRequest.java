package com.kevin.tienda_online.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgregarProductoCarritoRequest {
    @NotBlank
    private String productoId;

    @Min(1)
    private int cantidad;
}
