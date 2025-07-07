package com.example.APIMusic.service;

import com.example.APIMusic.entity.Playlist;
import com.example.APIMusic.entity.PlaylistCancion;
import com.example.APIMusic.entity.Cancion;
import com.example.APIMusic.entity.Usuario;
import com.example.APIMusic.repository.PlaylistRepository;
import com.example.APIMusic.repository.PlaylistCancionRepository;
import com.example.APIMusic.repository.CancionRepository;
import com.example.APIMusic.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistCancionRepository playlistCancionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CancionRepository cancionRepository;

    /**
     * Crear una nueva playlist
     */
    @Transactional
    public Playlist crearPlaylist(String email, String nombre, String descripcion) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Playlist playlist = new Playlist();
        playlist.setUsuario(usuario.get());
        playlist.setNombre(nombre);
        playlist.setDescripcion(descripcion);
        playlist.setEsPublica(false); // Por defecto privada
        playlist.setEsColaborativa(false);
        playlist.setTotalCanciones(0);

        return playlistRepository.save(playlist);
    }

    /**
     * Obtener playlists de un usuario
     */
    public List<Playlist> obtenerPlaylistsPorUsuario(String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isEmpty()) {
            return List.of();
        }

        return playlistRepository.findByUsuarioIdAndDeletedAtIsNull(usuario.get().getId());
    }

    /**
     * Agregar canción a playlist
     */
    @Transactional
    public boolean agregarCancionAPlaylist(String email, Long playlistId, Long cancionId) {
        try {
            // Verificar que la playlist pertenece al usuario
            Optional<Playlist> playlist = playlistRepository.findById(playlistId);
            Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
            Optional<Cancion> cancion = cancionRepository.findById(cancionId);

            if (playlist.isEmpty() || usuario.isEmpty() || cancion.isEmpty()) {
                return false;
            }

            // Verificar que la playlist pertenece al usuario
            if (!playlist.get().getUsuario().getId().equals(usuario.get().getId())) {
                return false;
            }

            // Verificar si la canción ya está en la playlist
            if (playlistCancionRepository.existsByPlaylistIdAndCancionId(playlistId, cancionId)) {
                return false; // Ya está en la playlist
            }

            // Obtener el siguiente orden
            Integer maxOrden = playlistCancionRepository.findMaxOrdenByPlaylistId(playlistId);
            int nuevoOrden = (maxOrden == null) ? 1 : maxOrden + 1;

            // Crear la relación
            PlaylistCancion playlistCancion = new PlaylistCancion(
                    playlist.get(),
                    cancion.get(),
                    nuevoOrden);
            playlistCancionRepository.save(playlistCancion);

            // Actualizar contador de canciones
            playlist.get().setTotalCanciones(playlist.get().getTotalCanciones() + 1);
            playlistRepository.save(playlist.get());

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Quitar canción de playlist
     */
    @Transactional
    public boolean quitarCancionDePlaylist(String email, Long playlistId, Long cancionId) {
        try {
            Optional<Playlist> playlist = playlistRepository.findById(playlistId);
            Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

            if (playlist.isEmpty() || usuario.isEmpty()) {
                return false;
            }

            // Verificar que la playlist pertenece al usuario
            if (!playlist.get().getUsuario().getId().equals(usuario.get().getId())) {
                return false;
            }

            // Eliminar la relación
            playlistCancionRepository.deleteByPlaylistIdAndCancionId(playlistId, cancionId);

            // Actualizar contador de canciones
            long totalCanciones = playlistCancionRepository.countByPlaylistId(playlistId);
            playlist.get().setTotalCanciones((int) totalCanciones);
            playlistRepository.save(playlist.get());

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtener canciones de una playlist
     */
    public List<Cancion> obtenerCancionesDePlaylist(String email, Long playlistId) {
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (playlist.isEmpty() || usuario.isEmpty()) {
            return List.of();
        }

        // Verificar que la playlist pertenece al usuario o es pública
        if (!playlist.get().getUsuario().getId().equals(usuario.get().getId()) &&
                !playlist.get().getEsPublica()) {
            return List.of();
        }

        return playlistCancionRepository.findCancionesByPlaylistId(playlistId);
    }

    /**
     * Eliminar una playlist
     */
    @Transactional
    public boolean eliminarPlaylist(String email, Long playlistId) {
        try {
            Optional<Playlist> playlist = playlistRepository.findById(playlistId);
            Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

            if (playlist.isEmpty() || usuario.isEmpty()) {
                return false;
            }

            // Verificar que la playlist pertenece al usuario
            if (!playlist.get().getUsuario().getId().equals(usuario.get().getId())) {
                return false;
            }

            // Soft delete
            playlist.get().setDeletedAt(LocalDateTime.now());
            playlistRepository.save(playlist.get());

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Actualizar información de playlist
     */
    @Transactional
    public Playlist actualizarPlaylist(String email, Long playlistId, String nombre,
            String descripcion, Boolean esPublica) {
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (playlist.isEmpty() || usuario.isEmpty()) {
            return null;
        }

        // Verificar que la playlist pertenece al usuario
        if (!playlist.get().getUsuario().getId().equals(usuario.get().getId())) {
            return null;
        }

        Playlist p = playlist.get();
        if (nombre != null)
            p.setNombre(nombre);
        if (descripcion != null)
            p.setDescripcion(descripcion);
        if (esPublica != null)
            p.setEsPublica(esPublica);

        return playlistRepository.save(p);
    }

    /**
     * Obtener playlists públicas
     */
    public List<Playlist> obtenerPlaylistsPublicas() {
        return playlistRepository.findAllPublicPlaylists();
    }

    /**
     * Contar canciones en una playlist
     */
    public long contarCancionesEnPlaylist(Long playlistId) {
        return playlistCancionRepository.countByPlaylistId(playlistId);
    }

    public Long contarTotalPlaylists() {
        return playlistRepository.count();
    }
}
