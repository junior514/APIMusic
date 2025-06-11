package com.example.APIMusic.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class SpotifyConfig {
    
    @Value("${spotify.client.id}")
    private String clientId;
    
    @Value("${spotify.client.secret}")
    private String clientSecret;
    
    @Value("${spotify.api.base.url}")
    private String apiBaseUrl;
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(apiBaseUrl)
                .build();
    }
    
    // Getters
    public String getClientId() { return clientId; }
    public String getClientSecret() { return clientSecret; }
    public String getApiBaseUrl() { return apiBaseUrl; }
}