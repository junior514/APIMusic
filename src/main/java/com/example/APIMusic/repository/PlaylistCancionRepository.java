package com.example.APIMusic.repository;

import com.example.APIMusic.entity.PlaylistCancion;
import com.example.APIMusic.entity.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistCancionRepository extends JpaRepository<PlaylistCancion, Long> {

    // Obtener todas las canciones de una playlist en orden
    List<PlaylistCancion> findByPlaylistIdOrderByOrden(Long playlistId);

    // Buscar una relación específica playlist-canción
    Optional<PlaylistCancion> findByPlaylistIdAndCancionId(Long playlistId, Long cancionId);

    // Verificar si una canción ya está en una playlist
    boolean existsByPlaylistIdAndCancionId(Long playlistId, Long cancionId);

    // Eliminar una canción de una playlist
    void deleteByPlaylistIdAndCancionId(Long playlistId, Long cancionId);

    // Obtener solo las canciones de una playlist (optimizado)
    @Query("SELECT pc.cancion FROM PlaylistCancion pc WHERE pc.playlist.id = :playlistId ORDER BY pc.orden")
    List<Cancion> findCancionesByPlaylistId(@Param("playlistId") Long playlistId);

    // Contar canciones en una playlist
    long countByPlaylistId(Long playlistId);

    // Obtener el último orden en una playlist (para agregar al final)
    @Query("SELECT COALESCE(MAX(pc.orden), 0) FROM PlaylistCancion pc WHERE pc.playlist.id = :playlistId")
    Integer findMaxOrdenByPlaylistId(@Param("playlistId") Long playlistId);

    // Eliminar todas las canciones de una playlist
    void deleteByPlaylistId(Long playlistId);
}
