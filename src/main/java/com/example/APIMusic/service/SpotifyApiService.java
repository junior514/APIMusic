package com.example.APIMusic.service;
import com.example.APIMusic.config.SpotifyConfig;
import com.example.APIMusic.dto.TrackDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
@Service
public class SpotifyApiService {
    
    @Autowired
    private SpotifyConfig spotifyConfig;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private SpotifyAuthService authService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Buscar canciones por query
     */
    public List<TrackDto> searchTracks(String query, int limit) {
        String token = authService.getClientCredentialsToken();
        String url = spotifyConfig.getApiBaseUrl() + "/search" +
                "?q=" + query.replace(" ", "%20") +
                "&type=track" +
                "&limit=" + limit;
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return parseTracksFromSearchResponse(response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error buscando canciones: " + e.getMessage());
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Obtener información de una canción por ID
     */
    public TrackDto getTrackById(String trackId) {
        String token = authService.getClientCredentialsToken();
        String url = spotifyConfig.getApiBaseUrl() + "/tracks/" + trackId;
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<TrackDto> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, TrackDto.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo canción: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Obtener canciones populares por país
     */
    public List<TrackDto> getTopTracks(String country) {
        String token = authService.getClientCredentialsToken();
        String url = spotifyConfig.getApiBaseUrl() + "/browse/featured-playlists" +
                "?country=" + country + "&limit=1";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Aquí deberías parsear la respuesta para obtener las canciones de la playlist destacada
                // Por ahora, simplemente retorna una lista vacía
                return new ArrayList<>();
            }
            return new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo top tracks: " + e.getMessage());
        }
    }
    
    private List<TrackDto> parseTracksFromSearchResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode tracksNode = root.path("tracks").path("items");
            
            List<TrackDto> tracks = new ArrayList<>();
            
            for (JsonNode trackNode : tracksNode) {
                TrackDto track = objectMapper.treeToValue(trackNode, TrackDto.class);
                tracks.add(track);
            }
            
            return tracks;
        } catch (Exception e) {
            throw new RuntimeException("Error parseando respuesta de búsqueda: " + e.getMessage());
        }
    }
}