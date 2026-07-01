package com.kevin.tienda_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevin.tienda_online.dto.response.AdminDashboardResponse;
import com.kevin.tienda_online.service.DashboardService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Estadísticas del sistema")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public AdminDashboardResponse obtenerDashboard() {
        return dashboardService.obtenerDashboard();
    }
}
