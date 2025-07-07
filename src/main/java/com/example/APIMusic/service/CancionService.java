package com.example.APIMusic.service;

import com.example.APIMusic.entity.Cancion;
import com.example.APIMusic.repository.CancionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CancionService {

    @Autowired
    private CancionRepository cancionRepository;

    /**
     * Obtener todas las canciones
     */
    public List<Cancion> obtenerTodas() {
        return cancionRepository.findAll();
    }

    /**
     * Obtener canción por ID
     */
    public Optional<Cancion> obtenerPorId(Long id) {
        return cancionRepository.findById(id);
    }

    /**
     * Guardar canción
     */
    public Cancion guardar(Cancion cancion) {
        return cancionRepository.save(cancion);
    }

    /**
     * Actualizar canción
     */
    public Cancion actualizar(Long id, Cancion cancionActualizada) {
        return cancionRepository.findById(id)
                .map(cancion -> {
                    cancion.setNombre(cancionActualizada.getNombre());
                    cancion.setArtistas(cancionActualizada.getArtistas());
                    cancion.setAlbum(cancionActualizada.getAlbum());
                    cancion.setDuracionMs(cancionActualizada.getDuracionMs());
                    cancion.setImagenUrl(cancionActualizada.getImagenUrl());
                    cancion.setPreviewUrl(cancionActualizada.getPreviewUrl());
                    cancion.setPopularidad(cancionActualizada.getPopularidad());
                    cancion.setAudioFeatures(cancionActualizada.getAudioFeatures());
                    return cancionRepository.save(cancion);
                })
                .orElse(null);
    }

    /**
     * Eliminar canción
     */
    public boolean eliminar(Long id) {
        if (cancionRepository.existsById(id)) {
            cancionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Buscar canciones por nombre
     */
    public List<Cancion> buscarPorNombre(String nombre) {
        return cancionRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Buscar canciones por artista
     */
    public List<Cancion> buscarPorArtistas(String nombreArtista) {
        return cancionRepository.findByArtistasContainingIgnoreCase(nombreArtista);
    }

    /**
     * Buscar canciones por álbum
     */
    public List<Cancion> buscarPorAlbum(String album) {
        return cancionRepository.findByAlbumContainingIgnoreCase(album);
    }

    /**
     * Verificar si existe por Spotify Track ID
     */
    public boolean existePorSpotifyTrackId(String spotifyTrackId) {
        return cancionRepository.existsBySpotifyTrackId(spotifyTrackId);
    }

    /**
     * Obtener canciones con preview disponible
     */
    public List<Cancion> obtenerCancionesConPreview() {
        return cancionRepository.findCancionesWithPreview();
    }

    /**
     * Buscar canciones por nombre que tengan preview
     */
    public List<Cancion> buscarPorNombreConPreview(String nombre) {
        return cancionRepository.findByNombreContainingIgnoreCaseWithPreview(nombre);
    }

    /**
     * Obtener canciones ordenadas por popularidad
     */
    public List<Cancion> obtenerPorPopularidad() {
        return cancionRepository.findAllByOrderByPopularidadDesc();
    }

    /**
     * Buscar canción por Spotify Track ID
     */
    public Optional<Cancion> obtenerPorSpotifyTrackId(String spotifyTrackId) {
        return cancionRepository.findBySpotifyTrackId(spotifyTrackId);
    }
}
