package com.example.APIMusic.repository;

import com.example.APIMusic.entity.PlaylistFavorita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistFavoritaRepository extends JpaRepository<PlaylistFavorita, Long> {
    List<PlaylistFavorita> findByUsuarioId(Long usuarioId);
    Optional<PlaylistFavorita> findByIdAndUsuarioId(Long id, Long usuarioId);
    Optional<PlaylistFavorita> findByUsuarioIdAndSpotifyPlaylistId(Long usuarioId, String spotifyPlaylistId);
    void deleteByUsuarioId(Long usuarioId);
}