package com.example.APIMusic.dto;

public class SpotifyAlbumDto {
    private String id;
    private String name;
    private String releaseDate;
    private String imageUrl;

    // Constructors
    public SpotifyAlbumDto() {
    }

    public SpotifyAlbumDto(String id, String name, String releaseDate, String imageUrl) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
