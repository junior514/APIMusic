package com.example.APIMusic.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "artists")
public class Artist {
    
    @Id
    private String spotifyId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "artist_type")
    private String type;
    
    @Column(name = "external_url")
    private String externalUrl;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "popularity")
    private Integer popularity;
    
    @Column(name = "followers")
    private Integer followers;
    
    @Column(name = "created_at")
    private LocalDate createdAt;
    
    // Relación con albums (muchos a muchos)
    @ManyToMany
    @JoinTable(
        name = "artist_album",
        joinColumns = @JoinColumn(name = "artist_id"),
        inverseJoinColumns = @JoinColumn(name = "album_id")
    )
    private List<Album> albums;
    
    // Relación con tracks (muchos a muchos)
    @ManyToMany(mappedBy = "artists")
    private List<Track> tracks;
    
    // Constructores
    public Artist() {
        this.createdAt = LocalDate.now();
    }
    
    public Artist(String spotifyId, String name) {
        this();
        this.spotifyId = spotifyId;
        this.name = name;
    }
    
    // Getters y Setters
    public String getSpotifyId() { return spotifyId; }
    public void setSpotifyId(String spotifyId) { this.spotifyId = spotifyId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getExternalUrl() { return externalUrl; }
    public void setExternalUrl(String externalUrl) { this.externalUrl = externalUrl; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public Integer getPopularity() { return popularity; }
    public void setPopularity(Integer popularity) { this.popularity = popularity; }
    
    public Integer getFollowers() { return followers; }
    public void setFollowers(Integer followers) { this.followers = followers; }
    
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
    
    public List<Album> getAlbums() { return albums; }
    public void setAlbums(List<Album> albums) { this.albums = albums; }
    
    public List<Track> getTracks() { return tracks; }
    public void setTracks(List<Track> tracks) { this.tracks = tracks; }
}
