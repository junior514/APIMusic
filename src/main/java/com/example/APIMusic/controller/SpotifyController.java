package com.example.APIMusic.controller;

import com.example.APIMusic.dto.BusquedaDTO;
import com.example.APIMusic.dto.SongDTO;
import com.example.APIMusic.entity.Cancion;
import com.example.APIMusic.service.SpotifyService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/spotify")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.OPTIONS })
public class SpotifyController {

    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

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
     * ✅ CORREGIDO: Buscar canciones en tiempo real desde Spotify (sin guardar en
     * BD)
     */
    @GetMapping(value = "/buscar-tiempo-real", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> buscarEnTiempoReal(@RequestParam String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "El término de búsqueda es obligatorio");
                error.put("total", 0);
                error.put("canciones", new ArrayList<>());
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(error);
            }

            // Buscar directamente en Spotify sin guardar en base de datos
            List<Cancion> canciones = spotifyService.buscarCancionesEnTiempoReal(query.trim());

            // ✅ Convertir a SongDTO para compatibilidad con Android
            List<SongDTO> songDTOs = SongDTO.fromCancionList(canciones);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("query", query);
            response.put("total", songDTOs.size());
            response.put("canciones", songDTOs); // ← Ahora envía SongDTO
            response.put("message", "Búsqueda en tiempo real completada");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error en búsqueda en tiempo real: " + e.getMessage());
            error.put("query", query);
            error.put("total", 0);
            error.put("canciones", new ArrayList<>());
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    /**
     * ✅ CORREGIDO: Buscar solo canciones con preview en tiempo real
     */
    @GetMapping(value = "/buscar-tiempo-real/con-preview", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> buscarEnTiempoRealConPreview(@RequestParam String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "El término de búsqueda es obligatorio");
                error.put("total", 0);
                error.put("canciones", new ArrayList<>());
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(error);
            }

            // Buscar en tiempo real y filtrar solo las que tienen preview
            List<Cancion> canciones = spotifyService.buscarCancionesEnTiempoRealConPreview(query.trim());

            // ✅ Convertir a SongDTO
            List<SongDTO> songDTOs = SongDTO.fromCancionList(canciones);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("query", query);
            response.put("total", songDTOs.size());
            response.put("canciones", songDTOs); // ← Ahora envía SongDTO
            response.put("message", "Búsqueda con preview en tiempo real completada");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error en búsqueda con preview: " + e.getMessage());
            error.put("query", query);
            error.put("total", 0);
            error.put("canciones", new ArrayList<>());
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    /**
     * ✅ CORREGIDO: Obtener recomendaciones populares en tiempo real
     */
    @GetMapping(value = "/recomendaciones", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> obtenerRecomendaciones() {
        try {
            // Términos populares para obtener música trending
            List<String> terminosPopulares = List.of(
                    "top hits 2025", "trending music", "popular songs",
                    "chart hits", "viral music", "new releases");

            List<Cancion> todasLasCanciones = new ArrayList<>();
            for (String termino : terminosPopulares) {
                try {
                    List<Cancion> canciones = spotifyService.buscarCancionesEnTiempoRealConPreview(termino);
                    todasLasCanciones.addAll(canciones);

                    // Limitar para no sobrecargar
                    if (todasLasCanciones.size() >= 50) {
                        break;
                    }
                } catch (Exception e) {
                    // Si falla un término, continuar con el siguiente
                    System.err.println("Error buscando término: " + termino + " - " + e.getMessage());
                }
            }

            // Eliminar duplicados basados en Spotify Track ID
            List<Cancion> cancionesSinDuplicados = new ArrayList<>();
            Set<String> trackIdsVistos = new HashSet<>();

            for (Cancion cancion : todasLasCanciones) {
                if (cancion.getSpotifyTrackId() != null &&
                        !trackIdsVistos.contains(cancion.getSpotifyTrackId())) {
                    trackIdsVistos.add(cancion.getSpotifyTrackId());
                    cancionesSinDuplicados.add(cancion);
                }
            }

            // Limitar a las primeras 30 y convertir a DTO
            List<SongDTO> cancionesFinales = cancionesSinDuplicados.stream()
                    .limit(30)
                    .map(SongDTO::fromCancion)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("total", cancionesFinales.size());
            response.put("canciones", cancionesFinales); // ← Ahora envía SongDTO
            response.put("message", "Recomendaciones cargadas");
            response.put("query", "recomendaciones");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al obtener recomendaciones: " + e.getMessage());
            error.put("total", 0);
            error.put("canciones", new ArrayList<>());
            error.put("query", "recomendaciones");
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    /**
     * ✅ CORREGIDO: Método existente mejorado - Cargar todas las canciones
     */
    @PostMapping(value = "/canciones/cargar-todas", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cargarTodasLasCanciones() {
        try {
            List<String> terminos = List.of(
                    "rock", "pop", "reggaeton", "bachata", "salsa",
                    "shakira", "ricky martin", "coldplay", "karol g", "metallica",
                    "taylor swift", "bad bunny", "the weeknd", "dua lipa", "ed sheeran");

            List<Cancion> todas = new ArrayList<>();
            for (String termino : terminos) {
                try {
                    List<Cancion> canciones = spotifyService.buscarYCargarCanciones(termino);
                    todas.addAll(canciones);
                } catch (Exception e) {
                    // Si falla un término, continuar con el siguiente
                    System.err.println("Error cargando término: " + termino + " - " + e.getMessage());
                }
            }

            // ✅ Convertir a SongDTO para respuesta
            List<SongDTO> songDTOs = SongDTO.fromCancionList(todas);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("total", songDTOs.size());
            response.put("canciones", songDTOs); // ← Ahora envía SongDTO
            response.put("message", "Todas las canciones cargadas exitosamente");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al cargar todas las canciones: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    // Resto de métodos... (mantener igual pero convertir respuestas a SongDTO)
}
