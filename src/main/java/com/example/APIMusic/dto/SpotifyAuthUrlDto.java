// ===============================================
// DTOs DE AUTENTICACIÓN
// ===============================================

package com.example.APIMusic.dto;

public class SpotifyAuthUrlDto {
    private String authorizationUrl;
    private String state;

    // Constructors
    public SpotifyAuthUrlDto() {
    }

    public SpotifyAuthUrlDto(String authorizationUrl, String state) {
        this.authorizationUrl = authorizationUrl;
        this.state = state;
    }

    // Getters and Setters
    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
