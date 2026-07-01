package com.kevin.tienda_online.dto.response;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDashboardResponse {
    private Long totalUsuarios;
    private Long totalProductos;
    private Long totalPedidos;
    private BigDecimal totalVentas;
    private Long productosSinStock;
    private Long pedidosPendientes;
}
