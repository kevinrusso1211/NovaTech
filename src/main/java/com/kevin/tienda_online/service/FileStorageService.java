package com.kevin.tienda_online.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final String RUTA_UPLOADS = "src/main/uploads/";

    public String guardarImagen(MultipartFile archivo) throws IOException {
        String nombreArchivo = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
        Path ruta = Paths.get(RUTA_UPLOADS + nombreArchivo);
        Files.createDirectories(ruta.getParent());
        Files.copy(
                archivo.getInputStream(),
                ruta,
                StandardCopyOption.REPLACE_EXISTING
        );

        return "/uploads/" + nombreArchivo;
    }
}
