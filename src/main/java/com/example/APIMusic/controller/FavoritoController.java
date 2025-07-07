package com.example.APIMusic.controller;

import com.example.APIMusic.entity.Cancion;
import com.example.APIMusic.service.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favoritos")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.OPTIONS })
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    // Método OPTIONS para manejar preflight requests
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                .build();
    }

    /**
     * Agregar canción a favoritos
     */
    @PostMapping(value = "/{cancionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> agregarFavorito(@PathVariable Long cancionId, Authentication auth) {
        try {
            String email = auth.getName();
            boolean agregado = favoritoService.agregarFavorito(email, cancionId);

            Map<String, Object> response = new HashMap<>();
            if (agregado) {
                response.put("success", true);
                response.put("message", "Canción agregada a favoritos exitosamente");
                response.put("cancionId", cancionId);
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            } else {
                response.put("success", false);
                response.put("message", "No se pudo agregar a favoritos. Puede que ya esté agregada.");
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            }
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    /**
     * Quitar canción de favoritos
     */
    @DeleteMapping(value = "/{cancionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> quitarFavorito(@PathVariable Long cancionId, Authentication auth) {
        try {
            String email = auth.getName();
            boolean removido = favoritoService.quitarFavorito(email, cancionId);

            Map<String, Object> response = new HashMap<>();
            if (removido) {
                response.put("success", true);
                response.put("message", "Canción removida de favoritos exitosamente");
                response.put("cancionId", cancionId);
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            } else {
                response.put("success", false);
                response.put("message", "No se pudo remover de favoritos");
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            }
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    /**
     * Obtener todas las canciones favoritas del usuario
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Cancion>> obtenerFavoritos(Authentication auth) {
        try {
            String email = auth.getName();
            List<Cancion> favoritos = favoritoService.obtenerFavoritosPorUsuario(email);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(favoritos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    /**
     * Verificar si una canción es favorita
     */
    @GetMapping(value = "/verificar/{cancionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> verificarFavorito(@PathVariable Long cancionId, Authentication auth) {
        try {
            String email = auth.getName();
            boolean esFavorito = favoritoService.esFavorito(email, cancionId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("esFavorito", esFavorito);
            response.put("cancionId", cancionId);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al verificar favorito: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    /**
     * Obtener estadísticas de favoritos
     */
    @GetMapping(value = "/estadisticas", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> obtenerEstadisticas(Authentication auth) {
        try {
            String email = auth.getName();
            long totalFavoritos = favoritoService.contarFavoritos(email);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("totalFavoritos", totalFavoritos);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al obtener estadísticas: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }
}
