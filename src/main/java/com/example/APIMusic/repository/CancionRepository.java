package com.example.APIMusic.repository;

import com.example.APIMusic.entity.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CancionRepository extends JpaRepository<Cancion, Long> {

    List<Cancion> findByNombreContainingIgnoreCase(String nombre);

    // ✅ Corregido
    List<Cancion> findByArtistas_NombreContainingIgnoreCase(String nombre);

    List<Cancion> findByAlbumContainingIgnoreCase(String album);

    @Query("SELECT c FROM Cancion c WHERE c.previewUrl IS NOT NULL")
    List<Cancion> findByPreviewUrlNotNull();

    Cancion findBySpotifyTrackId(String spotifyTrackId);

    boolean existsBySpotifyTrackId(String spotifyTrackId);
}
