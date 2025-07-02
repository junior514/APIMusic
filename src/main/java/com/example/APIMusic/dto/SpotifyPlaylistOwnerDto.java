package com.example.APIMusic.dto;

public class SpotifyPlaylistOwnerDto {
    private String id;
    private String displayName;

    // Constructors
    public SpotifyPlaylistOwnerDto() {
    }

    public SpotifyPlaylistOwnerDto(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
