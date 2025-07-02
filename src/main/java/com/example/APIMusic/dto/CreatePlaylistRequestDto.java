package com.example.APIMusic.dto;

public class CreatePlaylistRequestDto {
    private String name;
    private String description;
    private boolean isPublic;

    // Constructors
    public CreatePlaylistRequestDto() {
    }

    public CreatePlaylistRequestDto(String name, String description, boolean isPublic) {
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}
