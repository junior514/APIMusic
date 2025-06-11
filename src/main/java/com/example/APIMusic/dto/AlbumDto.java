package com.example.APIMusic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

class AlbumDto {
    private String id;
    private String name;
    
    @JsonProperty("album_type")
    private String albumType;
    
    @JsonProperty("total_tracks")
    private int totalTracks;
    
    @JsonProperty("external_urls")
    private ExternalUrlsDto externalUrls;
    
    private List<ImageDto> images;
    
    @JsonProperty("release_date")
    private String releaseDate;
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAlbumType() { return albumType; }
    public void setAlbumType(String albumType) { this.albumType = albumType; }
    
    public int getTotalTracks() { return totalTracks; }
    public void setTotalTracks(int totalTracks) { this.totalTracks = totalTracks; }
    
    public ExternalUrlsDto getExternalUrls() { return externalUrls; }
    public void setExternalUrls(ExternalUrlsDto externalUrls) { this.externalUrls = externalUrls; }
    
    public List<ImageDto> getImages() { return images; }
    public void setImages(List<ImageDto> images) { this.images = images; }
    
    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
}