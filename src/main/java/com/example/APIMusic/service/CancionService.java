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

    public List<Cancion> obtenerTodas() {
        return cancionRepository.findAll();
    }

    public Optional<Cancion> obtenerPorId(Long id) {
        return cancionRepository.findById(id);
    }

    public Cancion guardar(Cancion cancion) {
        return cancionRepository.save(cancion);
    }

    public Cancion actualizar(Long id, Cancion nuevaData) {
        return cancionRepository.findById(id).map(cancion -> {
            cancion.setSpotifyTrackId(nuevaData.getSpotifyTrackId());
            cancion.setNombre(nuevaData.getNombre());
            cancion.setArtistas(nuevaData.getArtistas());
            cancion.setAlbum(nuevaData.getAlbum());
            cancion.setDuracionMs(nuevaData.getDuracionMs());
            cancion.setImagenUrl(nuevaData.getImagenUrl());
            cancion.setPreviewUrl(nuevaData.getPreviewUrl());
            cancion.setPopularidad(nuevaData.getPopularidad());
            cancion.setAudioFeatures(nuevaData.getAudioFeatures());
            cancion.setUpdatedAt(java.time.LocalDateTime.now());
            return cancionRepository.save(cancion);
        }).orElse(null);
    }

    public boolean eliminar(Long id) {
        if (cancionRepository.existsById(id)) {
            cancionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Cancion> buscarPorNombre(String nombre) {
        return cancionRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Cancion> buscarPorArtistas(String nombreArtista) {
        return cancionRepository.findByArtistas_NombreContainingIgnoreCase(nombreArtista);
    }

    public List<Cancion> buscarPorAlbum(String album) {
        return cancionRepository.findByAlbumContainingIgnoreCase(album);
    }

    public boolean existePorSpotifyTrackId(String spotifyTrackId) {
        return cancionRepository.existsBySpotifyTrackId(spotifyTrackId);
    }

    public List<Cancion> obtenerCancionesConPreview() {
        return cancionRepository.findByPreviewUrlNotNull();
    }
}
