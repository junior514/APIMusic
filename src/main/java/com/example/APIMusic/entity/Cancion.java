package com.example.APIMusic.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "canciones")
public class Cancion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spotify_track_id", unique = true, nullable = false)
    private String spotifyTrackId;

    @Column(nullable = false)
    private String nombre;

    @ManyToMany
    @JoinTable(name = "cancion_artista", joinColumns = @JoinColumn(name = "cancion_id"), inverseJoinColumns = @JoinColumn(name = "artista_id"))
    private Set<Artista> artistas = new HashSet<>();

    private String album;

    @Column(name = "duracion_ms")
    private Integer duracionMs;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "preview_url")
    private String previewUrl;

    private BigDecimal popularidad;

    @Column(name = "audio_features", columnDefinition = "JSON")
    private String audioFeatures;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Cancion() {
    }

    public Cancion(String spotifyTrackId, String nombre, Set<Artista> artistas, String album,
            Integer duracionMs, String imagenUrl, String previewUrl,
            BigDecimal popularidad, String audioFeatures) {
        this.spotifyTrackId = spotifyTrackId;
        this.nombre = nombre;
        this.artistas = artistas;
        this.album = album;
        this.duracionMs = duracionMs;
        this.imagenUrl = imagenUrl;
        this.previewUrl = previewUrl;
        this.popularidad = popularidad;
        this.audioFeatures = audioFeatures;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpotifyTrackId() {
        return spotifyTrackId;
    }

    public void setSpotifyTrackId(String spotifyTrackId) {
        this.spotifyTrackId = spotifyTrackId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Artista> getArtistas() {
        return artistas;
    }

    public void setArtistas(Set<Artista> artistas) {
        this.artistas = artistas;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Integer getDuracionMs() {
        return duracionMs;
    }

    public void setDuracionMs(Integer duracionMs) {
        this.duracionMs = duracionMs;
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

    public BigDecimal getPopularidad() {
        return popularidad;
    }

    public void setPopularidad(BigDecimal popularidad) {
        this.popularidad = popularidad;
    }

    public String getAudioFeatures() {
        return audioFeatures;
    }

    public void setAudioFeatures(String audioFeatures) {
        this.audioFeatures = audioFeatures;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
