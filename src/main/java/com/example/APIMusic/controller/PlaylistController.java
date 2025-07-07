package com.example.APIMusic.controller;

import com.example.APIMusic.entity.Playlist;
import com.example.APIMusic.entity.Cancion;
import com.example.APIMusic.service.PlaylistService;
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
@RequestMapping("/api/playlists")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.OPTIONS })
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

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
     * Obtener todas las playlists del usuario
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Playlist>> obtenerPlaylistsUsuario(Authentication auth) {
        try {
            String email = auth.getName();
            List<Playlist> playlists = playlistService.obtenerPlaylistsPorUsuario(email);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(playlists);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    /**
     * Crear una nueva playlist
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> crearPlaylist(@RequestBody Map<String, String> request,
            Authentication auth) {
        try {
            String email = auth.getName();
            String nombre = request.get("nombre");
            String descripcion = request.get("descripcion");

            if (nombre == null || nombre.trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "El nombre de la playlist es obligatorio");
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(error);
            }

            Playlist playlist = playlistService.crearPlaylist(email, nombre, descripcion);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Playlist creada exitosamente");
            response.put("playlist", playlist);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al crear playlist: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    /**
     * Agregar canción a una playlist
     */
    @PostMapping(value = "/{playlistId}/canciones/{cancionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> agregarCancionAPlaylist(@PathVariable Long playlistId,
            @PathVariable Long cancionId,
            Authentication auth) {
        try {
            String email = auth.getName();
            boolean agregada = playlistService.agregarCancionAPlaylist(email, playlistId, cancionId);

            Map<String, Object> response = new HashMap<>();
            if (agregada) {
                response.put("success", true);
                response.put("message", "Canción agregada a la playlist exitosamente");
                response.put("playlistId", playlistId);
                response.put("cancionId", cancionId);
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            } else {
                response.put("success", false);
                response.put("message", "No se pudo agregar la canción. Puede que ya esté en la playlist.");
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            }
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al agregar canción: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    /**
     * Quitar canción de una playlist
     */
    @DeleteMapping(value = "/{playlistId}/canciones/{cancionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> quitarCancionDePlaylist(@PathVariable Long playlistId,
            @PathVariable Long cancionId,
            Authentication auth) {
        try {
            String email = auth.getName();
            boolean removida = playlistService.quitarCancionDePlaylist(email, playlistId, cancionId);

            Map<String, Object> response = new HashMap<>();
            if (removida) {
                response.put("success", true);
                response.put("message", "Canción removida de la playlist exitosamente");
                response.put("playlistId", playlistId);
                response.put("cancionId", cancionId);
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            } else {
                response.put("success", false);
                response.put("message", "No se pudo remover la canción de la playlist");
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            }
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al remover canción: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    /**
     * Obtener canciones de una playlist
     */
    @GetMapping(value = "/{playlistId}/canciones", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Cancion>> obtenerCancionesDePlaylist(@PathVariable Long playlistId,
            Authentication auth) {
        try {
            String email = auth.getName();
            List<Cancion> canciones = playlistService.obtenerCancionesDePlaylist(email, playlistId);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(canciones);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    /**
     * Actualizar información de una playlist
     */
    @PutMapping(value = "/{playlistId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> actualizarPlaylist(@PathVariable Long playlistId,
            @RequestBody Map<String, Object> request,
            Authentication auth) {
        try {
            String email = auth.getName();
            String nombre = (String) request.get("nombre");
            String descripcion = (String) request.get("descripcion");
            Boolean esPublica = (Boolean) request.get("esPublica");

            Playlist playlistActualizada = playlistService.actualizarPlaylist(
                    email, playlistId, nombre, descripcion, esPublica);

            if (playlistActualizada != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Playlist actualizada exitosamente");
                response.put("playlist", playlistActualizada);
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            } else {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "No se pudo actualizar la playlist");
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(error);
            }
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al actualizar playlist: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    /**
     * Eliminar una playlist
     */
    @DeleteMapping(value = "/{playlistId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> eliminarPlaylist(@PathVariable Long playlistId, Authentication auth) {
        try {
            String email = auth.getName();
            boolean eliminada = playlistService.eliminarPlaylist(email, playlistId);

            Map<String, Object> response = new HashMap<>();
            if (eliminada) {
                response.put("success", true);
                response.put("message", "Playlist eliminada exitosamente");
                response.put("playlistId", playlistId);
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            } else {
                response.put("success", false);
                response.put("message", "No se pudo eliminar la playlist");
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            }
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al eliminar playlist: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    /**
     * Obtener playlists públicas
     */
    @GetMapping(value = "/publicas", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Playlist>> obtenerPlaylistsPublicas() {
        try {
            List<Playlist> playlistsPublicas = playlistService.obtenerPlaylistsPublicas();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(playlistsPublicas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    /**
     * Obtener estadísticas de una playlist
     */
    @GetMapping(value = "/{playlistId}/estadisticas", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> obtenerEstadisticasPlaylist(@PathVariable Long playlistId,
            Authentication auth) {
        try {
            String email = auth.getName();
            long totalCanciones = playlistService.contarCancionesEnPlaylist(playlistId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("playlistId", playlistId);
            response.put("totalCanciones", totalCanciones);

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
