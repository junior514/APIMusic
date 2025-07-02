package com.example.APIMusic.dto;

public class SpotifyPlaylistDto {
    private String id;
    private String name;
    private String description;
    private boolean isPublic;
    private boolean collaborative;
    private int totalTracks;
    private String imageUrl;
    private SpotifyPlaylistOwnerDto owner;

    // Constructors
    public SpotifyPlaylistDto() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isCollaborative() {
        return collaborative;
    }

    public void setCollaborative(boolean collaborative) {
        this.collaborative = collaborative;
    }

    public int getTotalTracks() {
        return totalTracks;
    }

    public void setTotalTracks(int totalTracks) {
        this.totalTracks = totalTracks;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public SpotifyPlaylistOwnerDto getOwner() {
        return owner;
    }

    public void setOwner(SpotifyPlaylistOwnerDto owner) {
        this.owner = owner;
    }
}
