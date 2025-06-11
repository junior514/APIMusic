package com.example.APIMusic.controller;
import com.example.APIMusic.dto.TrackDto;
import com.example.APIMusic.service.SpotifyApiService;
import com.example.APIMusic.service.SpotifyAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@RestController
@RequestMapping("/api/spotify")
@CrossOrigin(origins = "*") // Para desarrollo. En producción especifica dominios
public class SpotifyController {
    
    @Autowired
    private SpotifyApiService spotifyApiService;
    
    @Autowired
    private SpotifyAuthService spotifyAuthService;
    
    /**
     * Endpoint para buscar canciones
     * GET /api/spotify/search?q=nombre_cancion&limit=10
     */
    @GetMapping("/search")
    public ResponseEntity<List<TrackDto>> searchTracks(
            @RequestParam String q,
            @RequestParam(defaultValue = "20") int limit) {
        
        try {
            List<TrackDto> tracks = spotifyApiService.searchTracks(q, limit);
            return ResponseEntity.ok(tracks);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Endpoint para obtener información de una canción
     * GET /api/spotify/track/{id}
     */
    @GetMapping("/track/{id}")
    public ResponseEntity<TrackDto> getTrack(@PathVariable String id) {
        try {
            TrackDto track = spotifyApiService.getTrackById(id);
            if (track != null) {
                return ResponseEntity.ok(track);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Endpoint para obtener top tracks por país
     * GET /api/spotify/top-tracks?country=US
     */
    @GetMapping("/top-tracks")
    public ResponseEntity<List<TrackDto>> getTopTracks(
            @RequestParam(defaultValue = "US") String country) {
        
        try {
            List<TrackDto> tracks = spotifyApiService.getTopTracks(country);
            return ResponseEntity.ok(tracks);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Endpoint para obtener URL de autorización
     * GET /api/spotify/auth/url
     */
    @GetMapping("/auth/url")
    public ResponseEntity<Map<String, String>> getAuthUrl() {
        try {
            String state = UUID.randomUUID().toString();
            String authUrl = spotifyAuthService.getAuthorizationUrl(state);
            
            return ResponseEntity.ok(Map.of(
                "authUrl", authUrl,
                "state", state
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}