package com.example.APIMusic.repository;

import com.example.APIMusic.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar por email
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    // Buscar por Spotify ID
    Optional<Usuario> findBySpotifyUserId(String spotifyUserId);

    // Buscar por nombre (búsqueda parcial, sin importar mayúsculas/minúsculas)
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    // Obtener usuarios recientes
    @Query("SELECT u FROM Usuario u ORDER BY u.createdAt DESC")
    List<Usuario> findTop5ByOrderByCreatedAtDesc();

    // Alternativa si no tienes campo createdAt
    List<Usuario> findTop5ByOrderByIdDesc();
}
