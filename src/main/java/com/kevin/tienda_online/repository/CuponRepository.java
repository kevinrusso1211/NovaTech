package com.kevin.tienda_online.repository;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kevin.tienda_online.model.Cupon;

@Repository
public interface CuponRepository extends MongoRepository<Cupon, String> {

    Optional<Cupon> findByCodigo(String codigo);

}
