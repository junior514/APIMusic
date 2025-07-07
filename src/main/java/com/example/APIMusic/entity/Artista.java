package com.example.APIMusic.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@Entity
@Table(name = "artistas")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Artista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spotify_artist_id", unique = true)
    private String spotifyArtistId;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "generos", columnDefinition = "TEXT")
    private String generos; // JSON string con los géneros

    @Column(name = "popularidad")
    private Integer popularidad;

    @Column(name = "seguidores")
    private Integer seguidores;

    @Column(name = "spotify_url")
    private String spotifyUrl;

    // REMOVED: The problematic ManyToMany relationship
    // Since Cancion stores artists as a simple string, we don't need this
    // relationship
    // If you need to find songs by artist, create a custom query method in the
    // repository

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Artista() {
    }

    public Artista(String spotifyArtistId, String nombre) {
        this.spotifyArtistId = spotifyArtistId;
        this.nombre = nombre;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
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

    public String getSpotifyArtistId() {
        return spotifyArtistId;
    }

    public void setSpotifyArtistId(String spotifyArtistId) {
        this.spotifyArtistId = spotifyArtistId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getGeneros() {
        return generos;
    }

    public void setGeneros(String generos) {
        this.generos = generos;
    }

    public Integer getPopularidad() {
        return popularidad;
    }

    public void setPopularidad(Integer popularidad) {
        this.popularidad = popularidad;
    }

    public Integer getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(Integer seguidores) {
        this.seguidores = seguidores;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
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

    @Override
    public String toString() {
        return "Artista{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", spotifyArtistId='" + spotifyArtistId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Artista))
            return false;
        Artista artista = (Artista) o;
        return spotifyArtistId != null && spotifyArtistId.equals(artista.spotifyArtistId);
    }

    @Override
    public int hashCode() {
        return spotifyArtistId != null ? spotifyArtistId.hashCode() : 0;
    }
}
