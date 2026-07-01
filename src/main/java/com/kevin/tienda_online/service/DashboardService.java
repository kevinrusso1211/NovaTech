package com.kevin.tienda_online.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kevin.tienda_online.dto.response.AdminDashboardResponse;
import com.kevin.tienda_online.model.EstadoPedido;
import com.kevin.tienda_online.model.Pedido;
import com.kevin.tienda_online.repository.PedidoRepository;
import com.kevin.tienda_online.repository.ProductoRepository;
import com.kevin.tienda_online.repository.UsuarioRepository;

@Service
public class DashboardService {
     @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public AdminDashboardResponse obtenerDashboard() {
        AdminDashboardResponse response = new AdminDashboardResponse();
        response.setTotalUsuarios(usuarioRepository.count());
        response.setTotalProductos(productoRepository.count());
        response.setTotalPedidos(pedidoRepository.count());
        response.setProductosSinStock(productoRepository.countByStock(0));
        response.setPedidosPendientes(
                pedidoRepository.countByEstado(EstadoPedido.PENDIENTE)
        );

        BigDecimal ventasTotales = pedidoRepository.findAll()
                .stream()
                .filter(pedido -> pedido.getEstado() != EstadoPedido.CANCELADO)
                .map(Pedido::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        response.setTotalVentas(ventasTotales);

        return response;
    }
}
