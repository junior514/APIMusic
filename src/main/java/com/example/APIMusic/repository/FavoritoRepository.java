package com.example.APIMusic.repository;
import com.example.APIMusic.entity.Cancion;
import com.example.APIMusic.entity.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    // Buscar favoritos por usuario
    List<Favorito> findByUsuarioId(Long usuarioId);

    // Buscar un favorito específico por usuario y canción
    Optional<Favorito> findByUsuarioIdAndCancionId(Long usuarioId, Long cancionId);

    // Verificar si una canción es favorita de un usuario
    boolean existsByUsuarioIdAndCancionId(Long usuarioId, Long cancionId);

    // Eliminar favorito por usuario y canción
    void deleteByUsuarioIdAndCancionId(Long usuarioId, Long cancionId);

    // Obtener las canciones favoritas de un usuario (optimizado)
    @Query("SELECT f.cancion FROM Favorito f WHERE f.usuario.id = :usuarioId ORDER BY f.createdAt DESC")
    List<Cancion> findCancionesFavoritasByUsuarioId(@Param("usuarioId") Long usuarioId);

    // Contar favoritos por usuario
    long countByUsuarioId(Long usuarioId);

    // Obtener los últimos favoritos de un usuario
    @Query("SELECT f FROM Favorito f WHERE f.usuario.id = :usuarioId ORDER BY f.createdAt DESC")
    List<Favorito> findTop10ByUsuarioIdOrderByCreatedAtDesc(@Param("usuarioId") Long usuarioId);
}
