package com.example.APIMusic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackDto {
    
    private String id;
    private String name;
    private List<ArtistDto> artists;
    private AlbumDto album;
    
    @JsonProperty("duration_ms")
    private int durationMs;
    
    private boolean explicit;
    
    @JsonProperty("external_urls")
    private ExternalUrlsDto externalUrls;
    
    private int popularity;
    
    @JsonProperty("preview_url")
    private String previewUrl;
    
    @JsonProperty("track_number")
    private Integer trackNumber;
    
    @JsonProperty("disc_number")
    private Integer discNumber;
    
    // Constructores
    public TrackDto() {}
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public List<ArtistDto> getArtists() { return artists; }
    public void setArtists(List<ArtistDto> artists) { this.artists = artists; }
    
    public AlbumDto getAlbum() { return album; }
    public void setAlbum(AlbumDto album) { this.album = album; }
    
    public int getDurationMs() { return durationMs; }
    public void setDurationMs(int durationMs) { this.durationMs = durationMs; }
    
    public boolean isExplicit() { return explicit; }
    public void setExplicit(boolean explicit) { this.explicit = explicit; }
    
    public ExternalUrlsDto getExternalUrls() { return externalUrls; }
    public void setExternalUrls(ExternalUrlsDto externalUrls) { this.externalUrls = externalUrls; }
    
    public int getPopularity() { return popularity; }
    public void setPopularity(int popularity) { this.popularity = popularity; }
    
    public String getPreviewUrl() { return previewUrl; }
    public void setPreviewUrl(String previewUrl) { this.previewUrl = previewUrl; }
    
    public Integer getTrackNumber() { return trackNumber; }
    public void setTrackNumber(Integer trackNumber) { this.trackNumber = trackNumber; }
    
    public Integer getDiscNumber() { return discNumber; }
    public void setDiscNumber(Integer discNumber) { this.discNumber = discNumber; }
    
    @Override
    public String toString() {
        return "TrackDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", artists=" + (artists != null ? artists.size() + " artists" : "no artists") +
                ", album=" + (album != null ? album.getName() : "no album") +
                ", durationMs=" + durationMs +
                ", popularity=" + popularity +
                '}';
    }
}