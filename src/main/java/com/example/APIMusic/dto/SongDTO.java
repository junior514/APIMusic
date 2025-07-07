package com.example.APIMusic.dto;

import com.example.APIMusic.entity.Cancion;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para convertir la entidad Cancion al formato que espera la app Android
 */
public class SongDTO {

    private String id;
    private String nombre;
    private List<String> artistas; // ← Convertido a List para Android
    private String album;
    private Integer duracion; // ← Renombrado de duracionMs
    private String imagenUrl;
    private String previewUrl;
    private Double popularidad; // ← Convertido de BigDecimal a Double
    private String spotifyId; // ← Renombrado de spotifyTrackId

    // Constructor vacío
    public SongDTO() {
    }

    // Constructor desde Cancion entity
    public SongDTO(Cancion cancion) {
        this.id = cancion.getId() != null ? cancion.getId().toString() : null;
        this.nombre = cancion.getNombre();
        this.artistas = convertirArtistasStringALista(cancion.getArtistas());
        this.album = cancion.getAlbum();
        this.duracion = cancion.getDuracionMs(); // Mapear duracionMs -> duracion
        this.imagenUrl = cancion.getImagenUrl();
        this.previewUrl = cancion.getPreviewUrl();
        this.popularidad = convertirPopularidad(cancion.getPopularidad());
        this.spotifyId = cancion.getSpotifyTrackId(); // Mapear spotifyTrackId -> spotifyId
    }

    // ✅ Método estático para conversión fácil
    public static SongDTO fromCancion(Cancion cancion) {
        return new SongDTO(cancion);
    }

    // ✅ Método estático para convertir lista
    public static List<SongDTO> fromCancionList(List<Cancion> canciones) {
        return canciones.stream()
                .map(SongDTO::fromCancion)
                .collect(Collectors.toList());
    }

    // 🔧 Métodos de conversión privados
    private List<String> convertirArtistasStringALista(String artistasString) {
        if (artistasString == null || artistasString.trim().isEmpty()) {
            return List.of("Artista desconocido");
        }

        return Arrays.stream(artistasString.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private Double convertirPopularidad(BigDecimal popularidad) {
        return popularidad != null ? popularidad.doubleValue() : null;
    }

    // 🎵 Métodos de utilidad (compatibles con Android)
    public String getArtistasString() {
        if (artistas == null || artistas.isEmpty()) {
            return "Artista desconocido";
        }
        return String.join(", ", artistas);
    }

    public String getDuracionFormatted() {
        if (duracion == null || duracion <= 0) {
            return "0:00";
        }
        int minutes = duracion / 60000;
        int seconds = (duracion % 60000) / 1000;
        return String.format("%d:%02d", minutes, seconds);
    }

    public boolean hasPreview() {
        return previewUrl != null && !previewUrl.trim().isEmpty();
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getArtistas() {
        return artistas;
    }

    public void setArtistas(List<String> artistas) {
        this.artistas = artistas;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public Double getPopularidad() {
        return popularidad;
    }

    public void setPopularidad(Double popularidad) {
        this.popularidad = popularidad;
    }

    @JsonProperty("spotifyId") // Para compatibilidad con Android
    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    @Override
    public String toString() {
        return "SongDTO{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", artistas=" + artistas +
                ", album='" + album + '\'' +
                ", hasPreview=" + hasPreview() +
                '}';
    }
}
