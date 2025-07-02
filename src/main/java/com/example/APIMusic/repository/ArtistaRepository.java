package com.example.APIMusic.repository;

import com.example.APIMusic.entity.Artista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long> {

    Optional<Artista> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
