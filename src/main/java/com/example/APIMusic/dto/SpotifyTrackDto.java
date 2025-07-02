package com.example.APIMusic.dto;

import java.util.List;

public class SpotifyTrackDto {
    private String id;
    private String name;
    private int durationMs;
    private int popularity;
    private String previewUrl;
    private List<String> artists;
    private String artistsString;
    private SpotifyAlbumDto album;

    // Constructors
    public SpotifyTrackDto() {
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

    public int getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    public String getArtistsString() {
        return artistsString;
    }

    public void setArtistsString(String artistsString) {
        this.artistsString = artistsString;
    }

    public SpotifyAlbumDto getAlbum() {
        return album;
    }

    public void setAlbum(SpotifyAlbumDto album) {
        this.album = album;
    }
}
