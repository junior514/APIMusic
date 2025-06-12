package com.example.APIMusic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtistDto {
    private String id;
    private String name;
    private String type;
    private String href;
    private String uri;
    
    @JsonProperty("external_urls")
    private ExternalUrlsDto externalUrls;
    
    // Constructor por defecto
    public ArtistDto() {}
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getHref() { return href; }
    public void setHref(String href) { this.href = href; }
    
    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }
    
    public ExternalUrlsDto getExternalUrls() { return externalUrls; }
    public void setExternalUrls(ExternalUrlsDto externalUrls) { this.externalUrls = externalUrls; }
    
    @Override
    public String toString() {
        return "ArtistDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}