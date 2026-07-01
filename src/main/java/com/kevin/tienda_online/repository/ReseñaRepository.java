package com.kevin.tienda_online.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kevin.tienda_online.model.Reseña;

@Repository
public interface ReseñaRepository extends MongoRepository<Reseña, String> {

    List<Reseña> findByProductoId(String productoId);

    Optional<Reseña> findByUsuarioIdAndProductoId(
            String usuarioId,
            String productoId);

}
