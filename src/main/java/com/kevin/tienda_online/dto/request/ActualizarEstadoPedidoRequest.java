package com.kevin.tienda_online.dto.request;

import com.kevin.tienda_online.model.EstadoPedido;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarEstadoPedidoRequest {

    private EstadoPedido estado;
}
