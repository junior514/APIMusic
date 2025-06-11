package com.example.APIMusic.repository;

import com.example.APIMusic.entity.MusicaFavorita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicaFavoritaRepository extends JpaRepository<MusicaFavorita, Long> {
    List<MusicaFavorita> findByUsuarioId(Long usuarioId);
    Optional<MusicaFavorita> findByIdAndUsuarioId(Long id, Long usuarioId);
    Optional<MusicaFavorita> findByUsuarioIdAndSpotifyTrackId(Long usuarioId, String spotifyTrackId);
    void deleteByUsuarioId(Long usuarioId);
    boolean existsByUsuarioIdAndSpotifyTrackId(Long usuarioId, String spotifyTrackId);
}