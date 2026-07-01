package com.kevin.tienda_online.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.kevin.tienda_online.model.CarritoItem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarritoResponse {
    private String id;
    private String usuarioId;
    private List<CarritoItem> items;
    private BigDecimal total;
}
