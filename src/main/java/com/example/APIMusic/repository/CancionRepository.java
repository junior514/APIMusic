package com.example.APIMusic.repository;

import com.example.APIMusic.entity.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CancionRepository extends JpaRepository<Cancion, Long> {

    /**
     * Buscar canción por su Spotify Track ID
     */
    Optional<Cancion> findBySpotifyTrackId(String spotifyTrackId);

    /**
     * Verificar si existe una canción por su Spotify Track ID
     */
    boolean existsBySpotifyTrackId(String spotifyTrackId);

    /**
     * Buscar canciones por nombre (ignora mayúsculas/minúsculas)
     */
    @Query("SELECT c FROM Cancion c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Cancion> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);

    /**
     * Buscar canciones por artista (ignora mayúsculas/minúsculas)
     */
    @Query("SELECT c FROM Cancion c WHERE LOWER(c.artistas) LIKE LOWER(CONCAT('%', :artista, '%'))")
    List<Cancion> findByArtistasContainingIgnoreCase(@Param("artista") String artista);

    /**
     * Buscar canciones por álbum (ignora mayúsculas/minúsculas)
     */
    @Query("SELECT c FROM Cancion c WHERE LOWER(c.album) LIKE LOWER(CONCAT('%', :album, '%'))")
    List<Cancion> findByAlbumContainingIgnoreCase(@Param("album") String album);

    /**
     * Obtener canciones que tienen preview URL
     */
    @Query("SELECT c FROM Cancion c WHERE c.previewUrl IS NOT NULL AND c.previewUrl != ''")
    List<Cancion> findCancionesWithPreview();

    /**
     * Obtener canciones ordenadas por popularidad
     */
    List<Cancion> findAllByOrderByPopularidadDesc();

    /**
     * Buscar canciones por nombre con preview disponible
     */
    @Query("SELECT c FROM Cancion c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND c.previewUrl IS NOT NULL AND c.previewUrl != ''")
    List<Cancion> findByNombreContainingIgnoreCaseWithPreview(@Param("nombre") String nombre);
}
