// UsuarioService.java
package com.example.APIMusic.service;

import com.example.APIMusic.entity.Usuario;
import com.example.APIMusic.entity.PerfilUsuario;
import com.example.APIMusic.entity.MusicaFavorita;
import com.example.APIMusic.entity.AlbumFavorito;
import com.example.APIMusic.entity.PlaylistFavorita;
import com.example.APIMusic.repository.UsuarioRepository;
import com.example.APIMusic.repository.MusicaFavoritaRepository;
import com.example.APIMusic.repository.AlbumFavoritoRepository;
import com.example.APIMusic.repository.PlaylistFavoritaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MusicaFavoritaRepository musicaFavoritaRepository;

    @Autowired
    private AlbumFavoritoRepository albumFavoritoRepository;

    @Autowired
    private PlaylistFavoritaRepository playlistFavoritaRepository;

    // Métodos básicos CRUD
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return usuarioRepository.existsById(id);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Método para login básico
    public Optional<Usuario> login(String email, String contrasena) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isPresent() && usuario.get().getContrasena().equals(contrasena)) {
            return usuario;
        }
        return Optional.empty();
    }

    // Métodos para manejo de perfil
    public PerfilUsuario updatePerfil(Long usuarioId, PerfilUsuario perfilDetails) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            
            if (usuario.getPerfil() != null) {
                // Actualizar perfil existente
                PerfilUsuario perfil = usuario.getPerfil();
                perfil.setFotoUrl(perfilDetails.getFotoUrl());
                perfil.setBio(perfilDetails.getBio());
                return perfil;
            } else {
                // Crear nuevo perfil
                PerfilUsuario nuevoPerfil = new PerfilUsuario();
                nuevoPerfil.setFotoUrl(perfilDetails.getFotoUrl());
                nuevoPerfil.setBio(perfilDetails.getBio());
                nuevoPerfil.setUsuario(usuario);
                usuario.setPerfil(nuevoPerfil);
                usuarioRepository.save(usuario);
                return nuevoPerfil;
            }
        }
        return null;
    }

    // Métodos para manejo de músicas favoritas
    public List<MusicaFavorita> getMusicasFavoritas(Long usuarioId) {
        return musicaFavoritaRepository.findByUsuarioId(usuarioId);
    }

    public MusicaFavorita addMusicaFavorita(Long usuarioId, MusicaFavorita musicaFavorita) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isPresent()) {
            musicaFavorita.setUsuario(usuario.get());
            return musicaFavoritaRepository.save(musicaFavorita);
        }
        return null;
    }

    public boolean removeMusicaFavorita(Long usuarioId, Long musicaId) {
        Optional<MusicaFavorita> musicaFavorita = musicaFavoritaRepository.findByIdAndUsuarioId(musicaId, usuarioId);
        if (musicaFavorita.isPresent()) {
            musicaFavoritaRepository.delete(musicaFavorita.get());
            return true;
        }
        return false;
    }

    // Métodos para manejo de álbumes favoritos
    public List<AlbumFavorito> getAlbumesFavoritos(Long usuarioId) {
        return albumFavoritoRepository.findByUsuarioId(usuarioId);
    }

    public AlbumFavorito addAlbumFavorito(Long usuarioId, AlbumFavorito albumFavorito) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isPresent()) {
            albumFavorito.setUsuario(usuario.get());
            return albumFavoritoRepository.save(albumFavorito);
        }
        return null;
    }

    public boolean removeAlbumFavorito(Long usuarioId, Long albumId) {
        Optional<AlbumFavorito> albumFavorito = albumFavoritoRepository.findByIdAndUsuarioId(albumId, usuarioId);
        if (albumFavorito.isPresent()) {
            albumFavoritoRepository.delete(albumFavorito.get());
            return true;
        }
        return false;
    }

    // Métodos para manejo de playlists favoritas
    public List<PlaylistFavorita> getPlaylistsFavoritas(Long usuarioId) {
        return playlistFavoritaRepository.findByUsuarioId(usuarioId);
    }

    public PlaylistFavorita addPlaylistFavorita(Long usuarioId, PlaylistFavorita playlistFavorita) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isPresent()) {
            playlistFavorita.setUsuario(usuario.get());
            return playlistFavoritaRepository.save(playlistFavorita);
        }
        return null;
    }

    public boolean removePlaylistFavorita(Long usuarioId, Long playlistId) {
        Optional<PlaylistFavorita> playlistFavorita = playlistFavoritaRepository.findByIdAndUsuarioId(playlistId, usuarioId);
        if (playlistFavorita.isPresent()) {
            playlistFavoritaRepository.delete(playlistFavorita.get());
            return true;
        }
        return false;
    }
}