package com.example.APIMusic.service;

import com.example.APIMusic.dto.SpotifyTokenDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class SpotifyAuthService {

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Value("${spotify.redirect.uri}")
    private String redirectUri;

    @Value("${spotify.accounts.base.url}")
    private String accountsBaseUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SpotifyAuthService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Método principal para obtener token Client Credentials
     * Usando RestTemplate con form-urlencoded
     */
    public String getClientCredentialsToken() {
        try {
            // URL completa para el token de Spotify
            String url = "https://accounts.spotify.com/api/token";
            
            // Crear headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            // Crear Basic Auth header
            String auth = clientId + ":" + clientSecret;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            headers.set("Authorization", "Basic " + encodedAuth);
            
            // Crear body
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "client_credentials");
            
            // Crear request
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            
            // Hacer la petición
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                return jsonNode.get("access_token").asText();
            } else {
                throw new RuntimeException("Error obteniendo token: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("Error en getClientCredentialsToken: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error obteniendo token client credentials: " + e.getMessage());
        }
    }

    /**
     * Método alternativo para obtener token Client Credentials
     * Usando headers manuales
     */
    public String getClientCredentialsTokenAlternative() {
        try {
            // URL completa para el token de Spotify
            String url = "https://accounts.spotify.com/api/token";
            
            // Crear headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded");
            headers.set("Authorization", "Basic " + 
                Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()));
            
            // Crear body como string
            String body = "grant_type=client_credentials";
            
            // Crear request
            HttpEntity<String> request = new HttpEntity<>(body, headers);
            
            // Hacer la petición
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                return jsonNode.get("access_token").asText();
            } else {
                throw new RuntimeException("Error obteniendo token alternativo: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("Error en getClientCredentialsTokenAlternative: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error en método alternativo: " + e.getMessage());
        }
    }

    /**
     * Intercambiar código de autorización por token
     */
    public SpotifyTokenDto exchangeCodeForToken(String code) {
        try {
            // URL completa para el token de Spotify
            String url = "https://accounts.spotify.com/api/token";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "Basic " + 
                Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()));
            
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("code", code);
            body.add("redirect_uri", redirectUri);
            
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            ResponseEntity<SpotifyTokenDto> response = restTemplate.postForEntity(url, request, SpotifyTokenDto.class);
            
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error intercambiando código por token: " + e.getMessage());
        }
    }

    /**
     * Generar URL de autorización
     */
    public String getAuthorizationUrl(String state) {
        String scopes = "user-read-private user-read-email playlist-read-private playlist-read-collaborative";
        
        return accountsBaseUrl + "/authorize" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&scope=" + scopes.replace(" ", "%20") +
                "&redirect_uri=" + redirectUri +
                "&state=" + state;
    }

    /**
     * Verificar configuración
     */
    public Map<String, Object> verifyConfiguration() {
        Map<String, Object> config = new HashMap<>();
        config.put("clientId", clientId != null ? clientId.substring(0, 8) + "..." : "No configurado");
        config.put("clientSecret", clientSecret != null ? "Configurado" : "No configurado");
        config.put("redirectUri", redirectUri);
        config.put("accountsBaseUrl", accountsBaseUrl);
        
        // Probar conexión
        try {
            String token = getClientCredentialsTokenAlternative();
            config.put("tokenTest", "✅ Token obtenido correctamente");
            config.put("tokenLength", token.length());
        } catch (Exception e) {
            config.put("tokenTest", "❌ Error: " + e.getMessage());
        }
        
        return config;
    }
}