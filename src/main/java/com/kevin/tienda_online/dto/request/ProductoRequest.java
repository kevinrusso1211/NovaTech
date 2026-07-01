package com.kevin.tienda_online.dto.request;

import java.math.BigDecimal;

import com.kevin.tienda_online.model.Categoria;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoRequest {
    @NotBlank
    private String nombre;

    @NotBlank
    private String descripcion;

    @NotNull
    @DecimalMin(value = "1.0")
    private BigDecimal precio;

    @NotNull
    @Min(value = 1)
    private Integer stock;

    @NotBlank
    private String imagenUrl;

    @NotNull
    private Categoria categoria;
}
