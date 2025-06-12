package com.example.APIMusic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumDto {
    private String id;
    private String name;
    private String href;
    private String uri;
    
    @JsonProperty("album_type")
    private String albumType;
    
    @JsonProperty("total_tracks")
    private int totalTracks;
    
    @JsonProperty("external_urls")
    private ExternalUrlsDto externalUrls;
    
    private List<ImageDto> images;
    
    @JsonProperty("release_date")
    private String releaseDate;
    
    @JsonProperty("release_date_precision")
    private String releaseDatePrecision;
    
    // Los álbumes también tienen artistas
    private List<ArtistDto> artists;
    
    // Constructor por defecto
    public AlbumDto() {}
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getHref() { return href; }
    public void setHref(String href) { this.href = href; }
    
    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }
    
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
    
    public String getReleaseDatePrecision() { return releaseDatePrecision; }
    public void setReleaseDatePrecision(String releaseDatePrecision) { this.releaseDatePrecision = releaseDatePrecision; }
    
    public List<ArtistDto> getArtists() { return artists; }
    public void setArtists(List<ArtistDto> artists) { this.artists = artists; }
    
    @Override
    public String toString() {
        return "AlbumDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", albumType='" + albumType + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", artists=" + (artists != null ? artists.size() + " artists" : "no artists") +
                '}';
    }
}