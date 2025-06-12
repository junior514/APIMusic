package com.example.APIMusic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageDto {
    private int height;
    private String url;
    private int width;
    
    // Constructor por defecto
    public ImageDto() {}
    
    // Getters y Setters
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    
    @Override
    public String toString() {
        return "ImageDto{" +
                "height=" + height +
                ", url='" + url + '\'' +
                ", width=" + width +
                '}';
    }
}