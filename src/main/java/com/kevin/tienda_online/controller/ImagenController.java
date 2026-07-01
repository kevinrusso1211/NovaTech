package com.kevin.tienda_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import com.kevin.tienda_online.service.FileStorageService;

@RestController
@RequestMapping("/api/imagenes")
public class ImagenController {

    @Autowired
    private FileStorageService fileStorageService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Map<String, String> subirImagen(
            @RequestParam("imagen") MultipartFile imagen)
            throws IOException {
        String ruta = fileStorageService.guardarImagen(imagen);
        return Map.of("url", ruta);
    }

}
