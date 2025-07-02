package com.example.APIMusic.dto;

import java.util.List;

public class SpotifyPlaylistSearchDto {
    private int total;
    private int limit;
    private int offset;
    private List<SpotifyPlaylistDto> items;

    // Constructors
    public SpotifyPlaylistSearchDto() {
    }

    public SpotifyPlaylistSearchDto(int total, int limit, int offset, List<SpotifyPlaylistDto> items) {
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

    public List<SpotifyPlaylistDto> getItems() {
        return items;
    }

    public void setItems(List<SpotifyPlaylistDto> items) {
        this.items = items;
    }
}
