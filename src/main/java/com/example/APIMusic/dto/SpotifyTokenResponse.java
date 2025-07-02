package com.example.APIMusic.dto;

public class SpotifyTokenResponse {
    private String accessToken;
    private String tokenType;
    private String scope;
    private Integer expiresIn;
    private String refreshToken;

    // Constructors
    public SpotifyTokenResponse() {
    }

    public SpotifyTokenResponse(String accessToken, String tokenType, String scope, Integer expiresIn,
            String refreshToken) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.scope = scope;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
    }

    // Getters and Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "SpotifyTokenResponse{" +
                "accessToken='"
                + (accessToken != null ? accessToken.substring(0, Math.min(accessToken.length(), 10)) + "..." : null)
                + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", scope='" + scope + '\'' +
                ", expiresIn=" + expiresIn +
                ", refreshToken='"
                + (refreshToken != null ? refreshToken.substring(0, Math.min(refreshToken.length(), 10)) + "..." : null)
                + '\'' +
                '}';
    }
}
