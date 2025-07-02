package com.example.APIMusic.dto;

public class SpotifyUserProfile {
    private String id;
    private String displayName;
    private String email;
    private String country;
    private String followers;
    private String imageUrl;

    // Constructors
    public SpotifyUserProfile() {
    }

    public SpotifyUserProfile(String id, String displayName, String email, String country, String followers,
            String imageUrl) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.country = country;
        this.followers = followers;
        this.imageUrl = imageUrl;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "SpotifyUserProfile{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", followers='" + followers + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
