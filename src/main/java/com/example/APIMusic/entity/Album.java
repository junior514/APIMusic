package com.example.APIMusic.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "albums")
public class Album {
    
    @Id
    private String spotifyId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "album_type")
    private String albumType;
    
    @Column(name = "total_tracks")
    private Integer totalTracks;
    
    @Column(name = "release_date")
    private LocalDate releaseDate;
    
    @Column(name = "external_url")
    private String externalUrl;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "created_at")
    private LocalDate createdAt;
    
    // Relación con artistas (muchos a muchos)
    @ManyToMany(mappedBy = "albums")
    private List<Artist> artists;
    
    // Relación con tracks (uno a muchos)
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<Track> tracks;
    
    // Constructores
    public Album() {
        this.createdAt = LocalDate.now();
    }
    
    public Album(String spotifyId, String name) {
        this();
        this.spotifyId = spotifyId;
        this.name = name;
    }
    
    // Getters y Setters
    public String getSpotifyId() { return spotifyId; }
    public void setSpotifyId(String spotifyId) { this.spotifyId = spotifyId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAlbumType() { return albumType; }
    public void setAlbumType(String albumType) { this.albumType = albumType; }
    
    public Integer getTotalTracks() { return totalTracks; }
    public void setTotalTracks(Integer totalTracks) { this.totalTracks = totalTracks; }
    
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    
    public String getExternalUrl() { return externalUrl; }
    public void setExternalUrl(String externalUrl) { this.externalUrl = externalUrl; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
    
    public List<Artist> getArtists() { return artists; }
    public void setArtists(List<Artist> artists) { this.artists = artists; }
    
    public List<Track> getTracks() { return tracks; }
    public void setTracks(List<Track> tracks) { this.tracks = tracks; }
}