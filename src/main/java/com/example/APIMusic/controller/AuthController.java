package com.example.APIMusic.controller;

import com.example.APIMusic.dto.SpotifyTokenDto;
import com.example.APIMusic.service.SpotifyAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private SpotifyAuthService spotifyAuthService;
    

    
    @GetMapping("/spotify/callback")
    public ResponseEntity<SpotifyTokenDto> spotifyCallback(
            @RequestParam String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error) {
        
        if (error != null) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            SpotifyTokenDto token = spotifyAuthService.exchangeCodeForToken(code);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
   
    
    @PostMapping("/client-token")
    public ResponseEntity<Map<String, String>> getClientToken() {
        try {
            String token = spotifyAuthService.getClientCredentialsToken();
            return ResponseEntity.ok(Map.of("access_token", token));
        } catch (Exception e) {
            System.err.println("Error en getClientToken: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
   
    @PostMapping("/client-token-alt")
    public ResponseEntity<Map<String, String>> getClientTokenAlternative() {
        try {
            String token = spotifyAuthService.getClientCredentialsTokenAlternative();
            return ResponseEntity.ok(Map.of("access_token", token));
        } catch (Exception e) {
            System.err.println("Error en getClientTokenAlternative: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
   
    @GetMapping("/verify-config")
    public ResponseEntity<Map<String, Object>> verifyConfiguration() {
        try {
            Map<String, Object> config = spotifyAuthService.verifyConfiguration();
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}