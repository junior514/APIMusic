package com.example.APIMusic.controller;

import com.example.APIMusic.dto.SongDTO;
import com.example.APIMusic.entity.Cancion;
import com.example.APIMusic.repository.CancionRepository;
import com.example.APIMusic.service.SpotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger log = LoggerFactory.getLogger(SpotifyController.class);

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private CancionRepository cancionRepository;

    // Método OPTIONS para manejar preflight requests
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                .build();
    }

    // ========================================
    // ENDPOINTS CON AUTENTICACIÓN
    // ========================================

    /**
     * ✅ Buscar canciones en tiempo real desde Spotify (sin guardar en BD)
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

            log.info("🔍 Búsqueda en tiempo real: {}", query);

            // Buscar directamente en Spotify sin guardar en base de datos
            List<Cancion> canciones = spotifyService.buscarCancionesEnTiempoReal(query.trim());

            // Convertir a SongDTO para compatibilidad con Android
            List<SongDTO> songDTOs = SongDTO.fromCancionList(canciones);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("query", query);
            response.put("total", songDTOs.size());
            response.put("canciones", songDTOs);
            response.put("message", "Búsqueda en tiempo real completada");

            log.info("✅ Búsqueda completada: {} resultados para '{}'", songDTOs.size(), query);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            log.error("❌ Error en búsqueda en tiempo real para '{}': {}", query, e.getMessage(), e);
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
     * ✅ Buscar solo canciones con preview en tiempo real
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

            log.info("🎧 Búsqueda con preview: {}", query);

            // Buscar en tiempo real y filtrar solo las que tienen preview
            List<Cancion> canciones = spotifyService.buscarCancionesEnTiempoRealConPreview(query.trim());

            // Convertir a SongDTO
            List<SongDTO> songDTOs = SongDTO.fromCancionList(canciones);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("query", query);
            response.put("total", songDTOs.size());
            response.put("canciones", songDTOs);
            response.put("message", "Búsqueda con preview en tiempo real completada");

            log.info("✅ Búsqueda con preview completada: {} resultados para '{}'", songDTOs.size(), query);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            log.error("❌ Error en búsqueda con preview para '{}': {}", query, e.getMessage(), e);
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
     * ✅ Obtener recomendaciones populares en tiempo real
     */
    @GetMapping(value = "/recomendaciones", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> obtenerRecomendaciones() {
        try {
            log.info("🎯 Cargando recomendaciones...");

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
                    log.warn("⚠️ Error buscando término '{}': {}", termino, e.getMessage());
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
            response.put("canciones", cancionesFinales);
            response.put("message", "Recomendaciones cargadas");
            response.put("query", "recomendaciones");

            log.info("✅ Recomendaciones cargadas: {} canciones", cancionesFinales.size());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            log.error("❌ Error obteniendo recomendaciones: {}", e.getMessage(), e);
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
     * ✅ Cargar canciones que SÍ tengan preview
     */
    @PostMapping(value = "/cargar-con-preview-garantizado", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cargarCancionesConPreviewGarantizado() {
        try {
            log.info("🎯 Iniciando carga de canciones con preview garantizado...");

            List<Cancion> cancionesConPreview = spotifyService.buscarCancionesPopularesConPreview();

            // Guardar en base de datos usando el repository inyectado
            List<Cancion> guardadas = new ArrayList<>();
            for (Cancion cancion : cancionesConPreview) {
                if (!cancionRepository.existsBySpotifyTrackId(cancion.getSpotifyTrackId())) {
                    Cancion guardada = cancionRepository.save(cancion);
                    guardadas.add(guardada);
                    log.debug("💾 Guardada: {} | Preview: {}", guardada.getNombre(), guardada.hasPreview());
                } else {
                    log.debug("🔄 Ya existe: {}", cancion.getNombre());
                }
            }

            List<SongDTO> songDTOs = SongDTO.fromCancionList(guardadas);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("total", songDTOs.size());
            response.put("canciones", songDTOs);
            response.put("message", "Canciones con preview cargadas exitosamente");

            log.info("✅ Carga completada: {} canciones guardadas con preview", songDTOs.size());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            log.error("❌ Error cargando canciones con preview: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error cargando canciones con preview: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    /**
     * ✅ Método existente mejorado - Cargar todas las canciones
     */
    @PostMapping(value = "/canciones/cargar-todas", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cargarTodasLasCanciones() {
        try {
            log.info("📥 Iniciando carga de todas las canciones...");

            List<String> terminos = List.of(
                    "rock", "pop", "reggaeton", "bachata", "salsa",
                    "shakira", "ricky martin", "coldplay", "karol g", "metallica",
                    "taylor swift", "bad bunny", "the weeknd", "dua lipa", "ed sheeran");

            List<Cancion> todas = new ArrayList<>();
            for (String termino : terminos) {
                try {
                    log.info("🔍 Cargando término: {}", termino);
                    List<Cancion> canciones = spotifyService.buscarYCargarCanciones(termino);
                    todas.addAll(canciones);
                    log.info("✅ Cargadas {} canciones para '{}'", canciones.size(), termino);
                } catch (Exception e) {
                    log.warn("⚠️ Error cargando término '{}': {}", termino, e.getMessage());
                }
            }

            // Convertir a SongDTO para respuesta
            List<SongDTO> songDTOs = SongDTO.fromCancionList(todas);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("total", songDTOs.size());
            response.put("canciones", songDTOs);
            response.put("message", "Todas las canciones cargadas exitosamente");

            log.info("✅ Carga masiva completada: {} canciones totales", songDTOs.size());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            log.error("❌ Error carga masiva: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al cargar todas las canciones: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    /**
     * ✅ Obtener estadísticas de preview
     */
    @GetMapping(value = "/estadisticas-preview", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> obtenerEstadisticasPreview() {
        try {
            log.info("📊 Obteniendo estadísticas de preview...");

            Map<String, Object> stats = spotifyService.obtenerEstadisticasPreview();

            // Agregar estadísticas adicionales del repository
            stats.put("con_preview_valido", cancionRepository.countCancionesConPreviewValido());
            stats.put("top_20_con_preview", SongDTO.fromCancionList(cancionRepository.findTop20CancionesConPreview()));

            log.info("✅ Estadísticas generadas");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(stats);

        } catch (Exception e) {
            log.error("❌ Error obteniendo estadísticas: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error obteniendo estadísticas: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    /**
     * ✅ Debug rápido para verificar preview URLs
     */
    @GetMapping(value = "/debug-preview", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> debugPreview() {
        try {
            List<Cancion> samples = cancionRepository.findTop20CancionesConPreview();

            Map<String, Object> debug = new HashMap<>();
            debug.put("total_samples", samples.size());

            List<Map<String, Object>> samplesInfo = samples.stream()
                    .limit(5)
                    .map(c -> {
                        Map<String, Object> info = new HashMap<>();
                        info.put("nombre", c.getNombre());
                        info.put("artistas", c.getArtistas());
                        info.put("preview_url", c.getPreviewUrl());
                        info.put("has_preview", c.hasPreview());
                        info.put("popularidad", c.getPopularidad());
                        return info;
                    })
                    .collect(Collectors.toList());

            debug.put("samples", samplesInfo);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(debug);

        } catch (Exception e) {
            log.error("❌ Error en debug: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * ✅ Limpiar canciones sin preview
     */
    @DeleteMapping(value = "/limpiar-sin-preview", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> limpiarCancionesSinPreview() {
        try {
            log.info("🧹 Iniciando limpieza de canciones sin preview...");

            int eliminadas = spotifyService.limpiarCancionesSinPreview();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("eliminadas", eliminadas);
            response.put("message", "Limpieza completada: " + eliminadas + " canciones sin preview eliminadas");

            log.info("✅ Limpieza completada: {} canciones eliminadas", eliminadas);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            log.error("❌ Error en limpieza: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error en limpieza: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    // ========================================
    // ENDPOINTS PÚBLICOS PARA TESTING
    // ========================================

    /**
     * ✅ PÚBLICO: Debug rápido SIN autenticación para testing
     */
    @GetMapping(value = "/debug-preview-public", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> debugPreviewPublic() {
        try {
            log.info("🔍 Debug público - verificando preview URLs...");

            // Estadísticas básicas
            long totalCanciones = cancionRepository.count();
            long conPreview = cancionRepository.countCancionesWithPreview();
            long conPreviewValido = cancionRepository.countCancionesConPreviewValido();

            // Muestras
            List<Cancion> samples = cancionRepository.findTop20CancionesConPreview();

            Map<String, Object> debug = new HashMap<>();
            debug.put("timestamp", java.time.LocalDateTime.now().toString());
            debug.put("total_canciones", totalCanciones);
            debug.put("con_preview", conPreview);
            debug.put("con_preview_valido", conPreviewValido);
            debug.put("porcentaje_con_preview", totalCanciones > 0 ? (conPreview * 100.0 / totalCanciones) : 0);
            debug.put("total_samples", samples.size());

            // Primeras 3 canciones como ejemplo
            List<Map<String, Object>> samplesInfo = samples.stream()
                    .limit(3)
                    .map(c -> {
                        Map<String, Object> info = new HashMap<>();
                        info.put("id", c.getId());
                        info.put("nombre", c.getNombre());
                        info.put("artistas", c.getArtistas());
                        info.put("preview_url", c.getPreviewUrl());
                        info.put("has_preview", c.hasPreview());
                        info.put("popularidad", c.getPopularidad());
                        info.put("spotify_track_id", c.getSpotifyTrackId());
                        return info;
                    })
                    .collect(Collectors.toList());

            debug.put("samples", samplesInfo);

            // URLs de ejemplo para verificar
            List<String> previewUrls = samples.stream()
                    .limit(5)
                    .filter(c -> c.getPreviewUrl() != null)
                    .map(Cancion::getPreviewUrl)
                    .collect(Collectors.toList());

            debug.put("preview_urls_examples", previewUrls);

            log.info("✅ Debug público completado: {} canciones, {} con preview",
                    totalCanciones, conPreview);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(debug);

        } catch (Exception e) {
            log.error("❌ Error en debug público: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error en debug: " + e.getMessage());
            error.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    /**
     * ✅ PÚBLICO: Cargar canciones con preview SIN autenticación
     */
    @PostMapping(value = "/cargar-preview-publico", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cargarPreviewPublico() {
        try {
            log.info("🎯 Carga pública de canciones con preview...");

            List<Cancion> cancionesConPreview = spotifyService.buscarCancionesPopularesConPreview();

            // Guardar solo las primeras 20 para testing
            List<Cancion> guardadas = new ArrayList<>();
            int contador = 0;

            for (Cancion cancion : cancionesConPreview) {
                if (contador >= 20)
                    break; // Limitar para testing

                if (!cancionRepository.existsBySpotifyTrackId(cancion.getSpotifyTrackId())) {
                    Cancion guardada = cancionRepository.save(cancion);
                    guardadas.add(guardada);
                    contador++;
                    log.info("💾 Guardada {}: {} | Preview: {}",
                            contador, guardada.getNombre(), guardada.hasPreview());
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("guardadas", guardadas.size());
            response.put("total_procesadas", cancionesConPreview.size());
            response.put("message", "Testing completado: " + guardadas.size() + " canciones guardadas con preview");
            response.put("timestamp", java.time.LocalDateTime.now().toString());

            // Ejemplos de preview URLs guardadas
            List<Map<String, Object>> previewExamples = guardadas.stream()
                    .limit(3)
                    .map(c -> {
                        Map<String, Object> ejemplo = new HashMap<>();
                        ejemplo.put("nombre", c.getNombre());
                        ejemplo.put("preview_url", c.getPreviewUrl() != null ? c.getPreviewUrl() : "NULL");
                        return ejemplo;
                    })
                    .collect(Collectors.toList());

            response.put("preview_examples", previewExamples);

            log.info("✅ Carga pública completada: {} canciones guardadas", guardadas.size());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            log.error("❌ Error en carga pública: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error en carga: " + e.getMessage());
            error.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    /**
     * ✅ DEBUG AVANZADO: Ver respuesta cruda de Spotify
     */
    @GetMapping(value = "/debug-spotify-raw", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> debugSpotifyRaw(@RequestParam(defaultValue = "Bad Bunny") String query) {
        try {
            log.info("🔍 Debug RAW de Spotify para: {}", query);

            // Llamar directamente al método de búsqueda
            List<Cancion> canciones = spotifyService.buscarCancionesEnTiempoReal(query);

            Map<String, Object> debug = new HashMap<>();
            debug.put("query", query);
            debug.put("total_encontradas", canciones.size());
            debug.put("timestamp", java.time.LocalDateTime.now().toString());

            // Analizar las primeras 10 canciones
            List<Map<String, Object>> analisis = new ArrayList<>();

            for (int i = 0; i < Math.min(10, canciones.size()); i++) {
                Cancion cancion = canciones.get(i);
                Map<String, Object> info = new HashMap<>();
                info.put("index", i + 1);
                info.put("nombre", cancion.getNombre());
                info.put("artistas", cancion.getArtistas());
                info.put("preview_url", cancion.getPreviewUrl());
                info.put("preview_is_null", cancion.getPreviewUrl() == null);
                info.put("preview_is_empty",
                        cancion.getPreviewUrl() != null && cancion.getPreviewUrl().trim().isEmpty());
                info.put("preview_length", cancion.getPreviewUrl() != null ? cancion.getPreviewUrl().length() : 0);
                info.put("has_preview_method", cancion.hasPreview());
                info.put("popularidad", cancion.getPopularidad());
                info.put("spotify_id", cancion.getSpotifyTrackId());

                analisis.add(info);
            }

            debug.put("analisis_detallado", analisis);

            // Estadísticas de preview
            long conPreview = canciones.stream().filter(Cancion::hasPreview).count();
            long sinPreview = canciones.size() - conPreview;

            Map<String, Object> estadisticas = new HashMap<>();
            estadisticas.put("con_preview", conPreview);
            estadisticas.put("sin_preview", sinPreview);
            estadisticas.put("porcentaje_con_preview",
                    canciones.size() > 0 ? (conPreview * 100.0 / canciones.size()) : 0);
            debug.put("estadisticas", estadisticas);

            // Ejemplos de URLs de preview (si existen)
            List<String> previewUrls = canciones.stream()
                    .filter(c -> c.getPreviewUrl() != null && !c.getPreviewUrl().trim().isEmpty())
                    .map(Cancion::getPreviewUrl)
                    .limit(5)
                    .collect(Collectors.toList());

            debug.put("preview_urls_encontradas", previewUrls);

            log.info("✅ Debug RAW completado: {} encontradas, {} con preview",
                    canciones.size(), conPreview);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(debug);

        } catch (Exception e) {
            log.error("❌ Error en debug RAW: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error en debug RAW: " + e.getMessage());
            error.put("query", query);
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    /**
     * ✅ DEBUG SIMPLE: Probar un solo artista popular
     */
    @GetMapping(value = "/debug-single-artist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> debugSingleArtist(@RequestParam(defaultValue = "Taylor Swift") String artist) {
        try {
            log.info("🎤 Debug de artista específico: {}", artist);

            // Buscar específicamente este artista
            List<Cancion> canciones = spotifyService.buscarCancionesEnTiempoRealConPreview(artist);

            Map<String, Object> response = new HashMap<>();
            response.put("artist", artist);
            response.put("total_con_preview", canciones.size());
            response.put("timestamp", java.time.LocalDateTime.now().toString());

            // Mostrar las primeras 3 canciones con preview
            List<Map<String, Object>> ejemplos = canciones.stream()
                    .limit(3)
                    .map(c -> {
                        Map<String, Object> ejemplo = new HashMap<>();
                        ejemplo.put("nombre", c.getNombre());
                        ejemplo.put("artistas", c.getArtistas());
                        ejemplo.put("preview_url", c.getPreviewUrl() != null ? c.getPreviewUrl() : "NULL");
                        ejemplo.put("popularidad", c.getPopularidad() != null ? c.getPopularidad() : 0);
                        return ejemplo;
                    })
                    .collect(Collectors.toList());

            response.put("ejemplos_con_preview", ejemplos);

            if (canciones.isEmpty()) {
                response.put("message", "❌ No se encontraron canciones con preview para " + artist);
                response.put("suggestion",
                        "Spotify podría no tener preview URLs disponibles para este artista en este momento");
            } else {
                response.put("message", "✅ Encontradas " + canciones.size() + " canciones con preview para " + artist);
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            log.error("❌ Error en debug single artist: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("artist", artist);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * ✅ NUEVO: Test múltiples artistas populares
     */
    @GetMapping(value = "/test-multiple-artists", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> testMultipleArtists() {
        try {
            log.info("🎤 Testing múltiples artistas para verificar preview URLs...");

            List<String> artistasPopulares = List.of(
                    "Bad Bunny", "Taylor Swift", "The Weeknd", "Dua Lipa",
                    "Ed Sheeran", "Ariana Grande", "Justin Bieber", "Drake");

            Map<String, Object> results = new HashMap<>();
            results.put("timestamp", java.time.LocalDateTime.now().toString());
            results.put("total_artistas_testados", artistasPopulares.size());

            List<Map<String, Object>> artistaResults = new ArrayList<>();
            int totalConPreview = 0;

            for (String artista : artistasPopulares) {
                try {
                    List<Cancion> canciones = spotifyService.buscarCancionesEnTiempoRealConPreview(artista);

                    Map<String, Object> artistaInfo = new HashMap<>();
                    artistaInfo.put("artista", artista);
                    artistaInfo.put("canciones_con_preview", canciones.size());
                    artistaInfo.put("status", canciones.size() > 0 ? "✅ CON PREVIEW" : "❌ SIN PREVIEW");

                    if (!canciones.isEmpty()) {
                        totalConPreview += canciones.size();
                        // Agregar ejemplo de la primera canción
                        Cancion primera = canciones.get(0);
                        Map<String, Object> ejemplo = new HashMap<>();
                        ejemplo.put("nombre", primera.getNombre());
                        ejemplo.put("preview_url", primera.getPreviewUrl() != null ? primera.getPreviewUrl() : "NULL");
                        artistaInfo.put("ejemplo", ejemplo);
                    }

                    artistaResults.add(artistaInfo);

                    // Pequeña pausa para no sobrecargar la API
                    Thread.sleep(200);

                } catch (Exception e) {
                    Map<String, Object> artistaError = new HashMap<>();
                    artistaError.put("artista", artista);
                    artistaError.put("error", e.getMessage());
                    artistaError.put("status", "❌ ERROR");
                    artistaResults.add(artistaError);
                }
            }

            results.put("resultados_por_artista", artistaResults);
            results.put("total_canciones_con_preview", totalConPreview);
            results.put("artistas_exitosos", artistaResults.stream()
                    .mapToInt(r -> (Integer) r.getOrDefault("canciones_con_preview", 0))
                    .filter(count -> count > 0)
                    .count());

            // Resumen
            if (totalConPreview > 0) {
                results.put("conclusion", "✅ SUCCESS: Se encontraron " + totalConPreview + " canciones con preview");
                results.put("recomendacion", "Spotify SÍ está devolviendo preview URLs para algunos artistas");
            } else {
                results.put("conclusion", "❌ PROBLEMA: No se encontraron canciones con preview para ningún artista");
                results.put("recomendacion", "Verificar configuración de Spotify API o restricciones regionales");
            }

            log.info("✅ Test múltiples artistas completado: {} canciones con preview encontradas", totalConPreview);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(results);

        } catch (Exception e) {
            log.error("❌ Error en test múltiples artistas: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("message", "Error en test múltiples artistas");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * ✅ NUEVO: Verificar configuración de Spotify
     */
    @GetMapping(value = "/verify-spotify-config", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verifySpotifyConfig() {
        try {
            log.info("🔧 Verificando configuración de Spotify...");

            Map<String, Object> config = new HashMap<>();
            config.put("timestamp", java.time.LocalDateTime.now().toString());

            // Test básico de conexión - buscar algo simple
            List<Cancion> testResults = spotifyService.buscarCancionesEnTiempoReal("test");

            config.put("spotify_connection", testResults.size() > 0 ? "✅ CONECTADO" : "❌ SIN CONEXIÓN");
            config.put("test_results_count", testResults.size());

            if (testResults.size() > 0) {
                // Analizar primer resultado
                Cancion primera = testResults.get(0);
                Map<String, Object> sampleTrack = new HashMap<>();
                sampleTrack.put("nombre", primera.getNombre());
                sampleTrack.put("artistas", primera.getArtistas());
                sampleTrack.put("preview_url", primera.getPreviewUrl());
                sampleTrack.put("has_preview", primera.hasPreview());
                sampleTrack.put("spotify_id", primera.getSpotifyTrackId());
                config.put("sample_track", sampleTrack);

                // Estadísticas de preview en los resultados de test
                long conPreview = testResults.stream().filter(Cancion::hasPreview).count();
                Map<String, Object> previewStats = new HashMap<>();
                previewStats.put("total_tracks", testResults.size());
                previewStats.put("with_preview", conPreview);
                previewStats.put("without_preview", testResults.size() - conPreview);
                previewStats.put("preview_percentage",
                        testResults.size() > 0 ? (conPreview * 100.0 / testResults.size()) : 0);
                config.put("preview_stats", previewStats);
            }

            // Info adicional
            config.put("market_parameter", "ES");
            config.put("limit_parameter", 50);
            config.put("search_type", "track");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(config);

        } catch (Exception e) {
            log.error("❌ Error verificando configuración: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("spotify_connection", "❌ ERROR");
            error.put("error_message", e.getMessage());
            error.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * ✅ NUEVO: Endpoint de salud general
     */
    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> healthCheck() {
        try {
            Map<String, Object> health = new HashMap<>();
            health.put("status", "✅ ONLINE");
            health.put("timestamp", java.time.LocalDateTime.now().toString());
            health.put("service", "SpotifyController");
            health.put("version", "v1.0");

            // Estadísticas básicas de BD
            Map<String, Object> databaseStats = new HashMap<>();
            databaseStats.put("total_songs", cancionRepository.count());
            databaseStats.put("songs_with_preview", cancionRepository.countCancionesWithPreview());
            databaseStats.put("songs_with_valid_preview", cancionRepository.countCancionesConPreviewValido());
            health.put("database_stats", databaseStats);

            // Endpoints disponibles
            Map<String, String> availableEndpoints = new HashMap<>();
            availableEndpoints.put("public_debug", "/api/spotify/debug-preview-public");
            availableEndpoints.put("public_load", "/api/spotify/cargar-preview-publico");
            availableEndpoints.put("spotify_test", "/api/spotify/debug-spotify-raw");
            availableEndpoints.put("artist_test", "/api/spotify/debug-single-artist");
            availableEndpoints.put("multiple_test", "/api/spotify/test-multiple-artists");
            availableEndpoints.put("config_verify", "/api/spotify/verify-spotify-config");
            health.put("available_endpoints", availableEndpoints);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(health);

        } catch (Exception e) {
            Map<String, Object> errorHealth = new HashMap<>();
            errorHealth.put("status", "❌ ERROR");
            errorHealth.put("error", e.getMessage());
            errorHealth.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.internalServerError().body(errorHealth);
        }
    }

}
