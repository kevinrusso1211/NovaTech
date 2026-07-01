package com.kevin.tienda_online.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.kevin.tienda_online.model.EstadoPedido;
import com.kevin.tienda_online.model.PedidoItem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoResponse {
    private String id;
    private String usuarioId;
    private List<PedidoItem> items;
    private BigDecimal total;
    private LocalDateTime fecha;
    private EstadoPedido estado;
}
