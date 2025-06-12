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
import java.util.Optional;

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
     * CORREGIDO: Ahora usa Optional para manejar casos null
     */
    public Optional<TrackDto> getTrackById(String trackId) {
        String token = authService.getClientCredentialsToken();
        String url = spotifyConfig.getApiBaseUrl() + "/tracks/" + trackId;
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                TrackDto track = parseTrackFromResponse(response.getBody());
                return Optional.ofNullable(track);
            }
        } catch (Exception e) {
            System.err.println("Error obteniendo canción con ID " + trackId + ": " + e.getMessage());
            // En lugar de lanzar excepción, devolvemos Optional.empty()
            return Optional.empty();
        }
        
        return Optional.empty();
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
            System.err.println("Error obteniendo top tracks: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Método para parsear una sola canción desde la respuesta JSON
     */
    private TrackDto parseTrackFromResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            return objectMapper.treeToValue(root, TrackDto.class);
        } catch (Exception e) {
            System.err.println("Error parseando respuesta de track: " + e.getMessage());
            throw new RuntimeException("Error parseando respuesta de track: " + e.getMessage());
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
            System.err.println("Error parseando respuesta de búsqueda: " + e.getMessage());
            throw new RuntimeException("Error parseando respuesta de búsqueda: " + e.getMessage());
        }
    }
}