package com.example.APIMusic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

class ArtistDto {
    private String id;
    private String name;
    private String type;
    
    @JsonProperty("external_urls")
    private ExternalUrlsDto externalUrls;
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public ExternalUrlsDto getExternalUrls() { return externalUrls; }
    public void setExternalUrls(ExternalUrlsDto externalUrls) { this.externalUrls = externalUrls; }
}