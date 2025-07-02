package com.example.APIMusic.service;


import com.example.APIMusic.entity.Cancion;
import com.example.APIMusic.repository.ArtistaRepository;
import com.example.APIMusic.repository.CancionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;
import java.util.*;

@Service
public class SpotifyService {

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Value("${spotify.api.base.url}")
    private String apiBaseUrl;

    @Value("${spotify.accounts.base.url}")
    private String accountsBaseUrl;

    private final CancionRepository cancionRepository;
    private final ArtistaRepository artistaRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public SpotifyService(CancionRepository cancionRepository, ArtistaRepository artistaRepository) {
        this.cancionRepository = cancionRepository;
        this.artistaRepository = artistaRepository;
        this.objectMapper = new ObjectMapper();
        this.restTemplate = new RestTemplate();
    }

    public List<Cancion> buscarYCargarCanciones(String nombre) {
        try {
            String token = obtenerAccessToken(); // <-- asegúrate de tener este método funcionando bien
            String query = URLEncoder.encode(nombre, StandardCharsets.UTF_8);

            String url = "https://api.spotify.com/v1/search?q=" + query + "&type=track&limit=10";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String json = response.getBody();
                // Aquí debes mapear la respuesta a tus objetos Cancion, dependiendo de tu DTO
                // Usa Jackson o Gson para convertir el JSON
                return new ArrayList<>(); // ← aquí pones tu lógica para transformar a List<Cancion>
            } else {
                System.out.println("Error al consultar Spotify: " + response.getStatusCode());
                return List.of();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private String obtenerAccessToken() {
        String url = accountsBaseUrl + "/api/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedCredentials);

        String body = "grant_type=client_credentials";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        JsonNode json = null;
        try {
            json = objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return json.path("access_token").asText();
    }

    private String obtenerAudioFeatures(String trackId, String accessToken) {
        try {
            String url = apiBaseUrl + "/audio-features/" + trackId;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            return response.getBody(); // Guardamos el JSON completo como string
        } catch (Exception e) {
            return null;
        }
    }
}
