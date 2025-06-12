package com.example.APIMusic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalUrlsDto {
    private String spotify;
    
    // Constructor por defecto
    public ExternalUrlsDto() {}
    
    // Getters y Setters
    public String getSpotify() { return spotify; }
    public void setSpotify(String spotify) { this.spotify = spotify; }
    
    @Override
    public String toString() {
        return "ExternalUrlsDto{" +
                "spotify='" + spotify + '\'' +
                '}';
    }
}