package com.kevin.tienda_online.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kevin.tienda_online.model.Favorito;

@Repository
public interface FavoritoRepository extends MongoRepository<Favorito, String> {

    List<Favorito> findByUsuarioId(String usuarioId);

    Optional<Favorito> findByUsuarioIdAndProductoId(
            String usuarioId,
            String productoId);

    void deleteByUsuarioIdAndProductoId(
            String usuarioId,
            String productoId);

}
