package com.example.APIMusic.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "playlists")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "spotify_playlist_id", unique = true)
    private String spotifyPlaylistId;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "es_publica", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean esPublica = false;

    @Column(name = "es_colaborativa", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean esColaborativa = false;

    @Column(name = "total_canciones", columnDefinition = "INT DEFAULT 0")
    private Integer totalCanciones = 0;

    @Column(name = "fecha_creacion_spotify")
    private LocalDateTime fechaCreacionSpotify;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Playlist() {
    }

    public Playlist(Usuario usuario, String nombre) {
        this.usuario = usuario;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getSpotifyPlaylistId() {
        return spotifyPlaylistId;
    }

    public void setSpotifyPlaylistId(String spotifyPlaylistId) {
        this.spotifyPlaylistId = spotifyPlaylistId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Boolean getEsPublica() {
        return esPublica;
    }

    public void setEsPublica(Boolean esPublica) {
        this.esPublica = esPublica;
    }

    public Boolean getEsColaborativa() {
        return esColaborativa;
    }

    public void setEsColaborativa(Boolean esColaborativa) {
        this.esColaborativa = esColaborativa;
    }

    public Integer getTotalCanciones() {
        return totalCanciones;
    }

    public void setTotalCanciones(Integer totalCanciones) {
        this.totalCanciones = totalCanciones;
    }

    public LocalDateTime getFechaCreacionSpotify() {
        return fechaCreacionSpotify;
    }

    public void setFechaCreacionSpotify(LocalDateTime fechaCreacionSpotify) {
        this.fechaCreacionSpotify = fechaCreacionSpotify;
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

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
