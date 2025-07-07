package com.example.APIMusic.service;

import com.example.APIMusic.entity.Favorito;
import com.example.APIMusic.entity.Cancion;
import com.example.APIMusic.entity.Usuario;
import com.example.APIMusic.repository.FavoritoRepository;
import com.example.APIMusic.repository.CancionRepository;
import com.example.APIMusic.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CancionRepository cancionRepository;

    /**
     * Agregar una canción a favoritos
     */
    @Transactional
    public boolean agregarFavorito(String email, Long cancionId) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
            Optional<Cancion> cancion = cancionRepository.findById(cancionId);

            if (usuario.isEmpty() || cancion.isEmpty()) {
                return false;
            }

            // Verificar si ya está en favoritos
            if (favoritoRepository.existsByUsuarioIdAndCancionId(usuario.get().getId(), cancionId)) {
                return false; // Ya está en favoritos
            }

            Favorito favorito = new Favorito(usuario.get(), cancion.get());
            favoritoRepository.save(favorito);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Quitar una canción de favoritos
     */
    @Transactional
    public boolean quitarFavorito(String email, Long cancionId) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
            if (usuario.isEmpty()) {
                return false;
            }

            favoritoRepository.deleteByUsuarioIdAndCancionId(usuario.get().getId(), cancionId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verificar si una canción es favorita
     */
    public boolean esFavorito(String email, Long cancionId) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isEmpty()) {
            return false;
        }

        return favoritoRepository.existsByUsuarioIdAndCancionId(usuario.get().getId(), cancionId);
    }

    /**
     * Obtener todas las canciones favoritas de un usuario
     */
    public List<Cancion> obtenerFavoritosPorUsuario(String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isEmpty()) {
            return List.of();
        }

        return favoritoRepository.findCancionesFavoritasByUsuarioId(usuario.get().getId());
    }

    /**
     * Contar favoritos de un usuario
     */
    public long contarFavoritos(String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isEmpty()) {
            return 0;
        }

        return favoritoRepository.countByUsuarioId(usuario.get().getId());
    }

    /**
     * Obtener últimos favoritos agregados
     */
    public List<Favorito> obtenerUltimosFavoritos(String email, int limite) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isEmpty()) {
            return List.of();
        }

        return favoritoRepository.findTop10ByUsuarioIdOrderByCreatedAtDesc(usuario.get().getId());
    }
}
