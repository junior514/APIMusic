package com.example.APIMusic.repository;

import com.example.APIMusic.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUsuarioIdAndDeletedAtIsNull(Long usuarioId);

    Optional<Playlist> findBySpotifyPlaylistId(String spotifyPlaylistId);

    @Query("SELECT p FROM Playlist p WHERE p.usuario.id = ?1 AND p.esPublica = true AND p.deletedAt IS NULL")
    List<Playlist> findPublicPlaylistsByUserId(Long usuarioId);

    @Query("SELECT p FROM Playlist p WHERE p.esPublica = true AND p.deletedAt IS NULL ORDER BY p.createdAt DESC")
    List<Playlist> findAllPublicPlaylists();
}
