package com.example.APIMusic.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String contrasena;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private PerfilUsuario perfil;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<MusicaFavorita> favoritasMusicas;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<AlbumFavorito> favoritosAlbumes;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<PlaylistFavorita> favoritosPlaylists;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public PerfilUsuario getPerfil() { return perfil; }
    public void setPerfil(PerfilUsuario perfil) { this.perfil = perfil; }

    public List<MusicaFavorita> getFavoritasMusicas() { return favoritasMusicas; }
    public void setFavoritasMusicas(List<MusicaFavorita> favoritasMusicas) { this.favoritasMusicas = favoritasMusicas; }

    public List<AlbumFavorito> getFavoritosAlbumes() { return favoritosAlbumes; }
    public void setFavoritosAlbumes(List<AlbumFavorito> favoritosAlbumes) { this.favoritosAlbumes = favoritosAlbumes; }

    public List<PlaylistFavorita> getFavoritosPlaylists() { return favoritosPlaylists; }
    public void setFavoritosPlaylists(List<PlaylistFavorita> favoritosPlaylists) { this.favoritosPlaylists = favoritosPlaylists; }
}
