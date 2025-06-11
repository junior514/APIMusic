package com.example.APIMusic.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tracks")
public class Track {
    
    @Id
    private String spotifyId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "duration_ms")
    private Integer durationMs;
    
    @Column(name = "explicit_content")
    private Boolean explicit;
    
    @Column(name = "external_url")
    private String externalUrl;
    
    @Column(name = "popularity")
    private Integer popularity;
    
    @Column(name = "preview_url")
    private String previewUrl;
    
    @Column(name = "created_at")
    private LocalDate createdAt;
    
    // Relación con album (muchos a uno)
    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;
    
    // Relación con artistas (muchos a muchos)
    @ManyToMany
    @JoinTable(
        name = "track_artist",
        joinColumns = @JoinColumn(name = "track_id"),
        inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private List<Artist> artists;
    
    // Constructores
    public Track() {
        this.createdAt = LocalDate.now();
    }
    
    public Track(String spotifyId, String name) {
        this();
        this.spotifyId = spotifyId;
        this.name = name;
    }
    
    // Getters y Setters
    public String getSpotifyId() { return spotifyId; }
    public void setSpotifyId(String spotifyId) { this.spotifyId = spotifyId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getDurationMs() { return durationMs; }
    public void setDurationMs(Integer durationMs) { this.durationMs = durationMs; }
    
    public Boolean getExplicit() { return explicit; }
    public void setExplicit(Boolean explicit) { this.explicit = explicit; }
    
    public String getExternalUrl() { return externalUrl; }
    public void setExternalUrl(String externalUrl) { this.externalUrl = externalUrl; }
    
    public Integer getPopularity() { return popularity; }
    public void setPopularity(Integer popularity) { this.popularity = popularity; }
    
    public String getPreviewUrl() { return previewUrl; }
    public void setPreviewUrl(String previewUrl) { this.previewUrl = previewUrl; }
    
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
    
    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }
    
    public List<Artist> getArtists() { return artists; }
    public void setArtists(List<Artist> artists) { this.artists = artists; }
}