package com.example.APIMusic.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "canciones")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Cancion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spotify_track_id", unique = true, nullable = false)
    private String spotifyTrackId;

    @Column(nullable = false)
    private String nombre;

    // VERSIÓN SIMPLE: Usar columna TEXT para artistas
    @Column(name = "artistas", columnDefinition = "TEXT", nullable = false)
    private String artistas = "Artista desconocido"; // Nombres separados por comas

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
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Cancion() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        if (this.artistas == null || this.artistas.trim().isEmpty()) {
            this.artistas = "Artista desconocido";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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

    public String getArtistas() {
        return artistas;
    }

    public void setArtistas(String artistas) {
        this.artistas = (artistas != null && !artistas.trim().isEmpty()) ? artistas : "Artista desconocido";
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

    // Métodos de utilidad
    public String getArtistasString() {
        return artistas != null ? artistas : "Artista desconocido";
    }

    public String getDuracionFormatted() {
        if (duracionMs == null || duracionMs <= 0)
            return "0:00";
        int seconds = duracionMs / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public boolean hasPreview() {
        return previewUrl != null && !previewUrl.trim().isEmpty();
    }

    public String getPrimaryArtist() {
        if (artistas == null || artistas.trim().isEmpty())
            return "Artista desconocido";
        return artistas.split(",")[0].trim();
    }

    public boolean isPopular() {
        return popularidad != null && popularidad.compareTo(new BigDecimal("50")) > 0;
    }

    @Override
    public String toString() {
        return "Cancion{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", artistas='" + artistas + '\'' +
                ", album='" + album + '\'' +
                ", hasPreview=" + hasPreview() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Cancion))
            return false;
        Cancion cancion = (Cancion) o;
        return spotifyTrackId != null && spotifyTrackId.equals(cancion.spotifyTrackId);
    }

    @Override
    public int hashCode() {
        return spotifyTrackId != null ? spotifyTrackId.hashCode() : 0;
    }
}
