package com.example.APIMusic.dto;

import java.util.List;

public class SpotifyTrackSearchDto {
    private int total;
    private int limit;
    private int offset;
    private List<SpotifyTrackDto> items;

    // Constructors
    public SpotifyTrackSearchDto() {
    }

    public SpotifyTrackSearchDto(int total, int limit, int offset, List<SpotifyTrackDto> items) {
        this.total = total;
        this.limit = limit;
        this.offset = offset;
        this.items = items;
    }

    // Getters and Setters
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<SpotifyTrackDto> getItems() {
        return items;
    }

    public void setItems(List<SpotifyTrackDto> items) {
        this.items = items;
    }
}
