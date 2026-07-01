package com.kevin.tienda_online.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kevin.tienda_online.model.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Usuario findByEmail(String email);
    long count();
    
}
