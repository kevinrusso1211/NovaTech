package com.kevin.tienda_online.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kevin.tienda_online.dto.response.AdminDashboardResponse;
import com.kevin.tienda_online.repository.PedidoRepository;
import com.kevin.tienda_online.repository.ProductoRepository;
import com.kevin.tienda_online.repository.UsuarioRepository;

@Service
public class AdminService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoService pedidoService;

    public AdminDashboardResponse obtenerDashboard() {
        AdminDashboardResponse response = new AdminDashboardResponse();

        response.setTotalUsuarios(usuarioRepository.count());
        response.setTotalProductos(productoRepository.count());
        response.setTotalPedidos(pedidoRepository.count());
        response.setProductosSinStock(productoRepository.countByStock(0));
        response.setTotalVentas(pedidoService.obtenerTotalVentas());

        return response;
    }
}
