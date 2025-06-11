package com.example.APIMusic.repository;

import com.example.APIMusic.entity.AlbumFavorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumFavoritoRepository extends JpaRepository<AlbumFavorito, Long> {
    List<AlbumFavorito> findByUsuarioId(Long usuarioId);
    Optional<AlbumFavorito> findByIdAndUsuarioId(Long id, Long usuarioId);
    Optional<AlbumFavorito> findByUsuarioIdAndSpotifyAlbumId(Long usuarioId, String spotifyAlbumId);
    void deleteByUsuarioId(Long usuarioId);
}