package com.example.APIMusic.service;

import com.example.APIMusic.entity.Cancion;
import com.example.APIMusic.repository.CancionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class SpotifyService {

    private static final Logger log = LoggerFactory.getLogger(SpotifyService.class);

    @Autowired
    private CancionRepository cancionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    private String accessToken;
    private long tokenExpirationTime;

    /**
     * Obtener token de acceso de Spotify
     */
    private String obtenerAccessToken() {
        // Si el token sigue siendo válido, retornarlo
        if (accessToken != null && System.currentTimeMillis() < tokenExpirationTime) {
            log.debug("🔑 Usando token existente válido");
            return accessToken;
        }

        try {
            log.info("🔑 Obteniendo nuevo token de Spotify...");
            String auth = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "Basic " + auth);

            String body = "grant_type=client_credentials";
            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://accounts.spotify.com/api/token",
                    HttpMethod.POST,
                    entity,
                    Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                accessToken = (String) responseBody.get("access_token");
                Integer expiresIn = (Integer) responseBody.get("expires_in");

                // Calcular tiempo de expiración (reducir 5 minutos para seguridad)
                tokenExpirationTime = System.currentTimeMillis() + ((expiresIn - 300) * 1000L);

                log.info("✅ Token de Spotify obtenido exitosamente (expira en {} segundos)", expiresIn);
                return accessToken;
            } else {
                log.error("❌ Error en respuesta de token: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("❌ Error obteniendo token de Spotify: {}", e.getMessage(), e);
        }

        return null;
    }

    /**
     * ✅ NUEVO: Buscar canciones populares que SÍ tengan preview
     */
    public List<Cancion> buscarCancionesPopularesConPreview() {
        log.info("🎯 Iniciando búsqueda de canciones populares con preview garantizado...");

        List<String> artistasPopulares = Arrays.asList(
                "Bad Bunny", "Taylor Swift", "The Weeknd", "Dua Lipa",
                "Ed Sheeran", "Drake", "Ariana Grande", "Post Malone",
                "Billie Eilish", "Harry Styles", "Olivia Rodrigo", "Karol G",
                "Shakira", "Justin Bieber", "Rihanna");

        List<Cancion> todasConPreview = new ArrayList<>();
        int totalBuscados = 0;
        int totalConPreview = 0;

        for (String artista : artistasPopulares) {
            try {
                log.info("🔍 Buscando canciones de: {}", artista);
                List<Cancion> canciones = buscarCancionesEnTiempoRealConPreview(artista);
                totalBuscados += canciones.size();

                // Tomar solo las primeras 3-5 de cada artista
                List<Cancion> limitadas = canciones.stream()
                        .limit(4)
                        .collect(Collectors.toList());

                todasConPreview.addAll(limitadas);
                totalConPreview += limitadas.size();

                log.info("✅ Agregadas {} canciones de {} (con preview)", limitadas.size(), artista);

                // Pequeña pausa para no sobrecargar la API
                Thread.sleep(100);

            } catch (Exception e) {
                log.warn("⚠️ Error buscando {}: {}", artista, e.getMessage());
            }
        }

        log.info("🎯 RESUMEN: {} canciones encontradas, {} con preview ({}%)",
                totalBuscados, totalConPreview,
                totalBuscados > 0 ? (totalConPreview * 100 / totalBuscados) : 0);

        return todasConPreview;
    }

    /**
     * NUEVO: Buscar canciones en tiempo real sin guardar en base de datos
     */
    public List<Cancion> buscarCancionesEnTiempoReal(String query) {
        try {
            String accessToken = obtenerAccessToken();
            if (accessToken == null) {
                log.error("❌ No se pudo obtener el token de acceso de Spotify");
                return new ArrayList<>();
            }

            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // URL de búsqueda de Spotify
            String url = "https://api.spotify.com/v1/search?q=" +
                    URLEncoder.encode(query, StandardCharsets.UTF_8) +
                    "&type=track&limit=50&market=ES";

            log.info("🔍 Buscando en tiempo real en Spotify: {}", query);

            // Hacer petición a Spotify
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return procesarRespuestaSpotify(response.getBody(), false); // false = no guardar
            } else {
                log.warn("⚠️ Respuesta no exitosa de Spotify: {}", response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("❌ Error en búsqueda en tiempo real: {}", e.getMessage(), e);
        }

        return new ArrayList<>();
    }

    /**
     * NUEVO: Buscar canciones en tiempo real solo con preview
     */
    public List<Cancion> buscarCancionesEnTiempoRealConPreview(String query) {
        log.info("🎧 Buscando canciones CON PREVIEW para: {}", query);
        List<Cancion> todasLasCanciones = buscarCancionesEnTiempoReal(query);

        // Filtrar solo las que tienen preview URL
        List<Cancion> conPreview = todasLasCanciones.stream()
                .filter(cancion -> {
                    boolean tienePreview = cancion.getPreviewUrl() != null &&
                            !cancion.getPreviewUrl().trim().isEmpty();
                    if (!tienePreview) {
                        log.debug("🚫 Sin preview: {}", cancion.getNombre());
                    }
                    return tienePreview;
                })
                .collect(Collectors.toList());

        log.info("🎧 Resultado para '{}': {} de {} canciones tienen preview ({}%)",
                query, conPreview.size(), todasLasCanciones.size(),
                todasLasCanciones.size() > 0 ? (conPreview.size() * 100 / todasLasCanciones.size()) : 0);

        return conPreview;
    }

    /**
     * Método mejorado para procesar respuesta de Spotify
     */
    private List<Cancion> procesarRespuestaSpotify(Map<String, Object> spotifyResponse, boolean guardarEnBD) {
        List<Cancion> canciones = new ArrayList<>();

        try {
            Map<String, Object> tracks = (Map<String, Object>) spotifyResponse.get("tracks");
            if (tracks == null) {
                log.warn("⚠️ No se encontró sección 'tracks' en la respuesta");
                return canciones;
            }

            List<Map<String, Object>> items = (List<Map<String, Object>>) tracks.get("items");
            if (items == null || items.isEmpty()) {
                log.warn("⚠️ No se encontraron items en la respuesta");
                return canciones;
            }

            log.info("📋 Procesando {} canciones de Spotify...", items.size());
            int cancionesConPreview = 0;
            int cancionesSinPreview = 0;

            for (Map<String, Object> item : items) {
                try {
                    Cancion cancion = crearCancionDesdeSpotify(item, guardarEnBD);

                    if (cancion != null) {
                        // Estadísticas de preview
                        if (cancion.hasPreview()) {
                            cancionesConPreview++;
                        } else {
                            cancionesSinPreview++;
                        }

                        if (guardarEnBD) {
                            // Verificar si ya existe antes de guardar
                            if (!cancionRepository.existsBySpotifyTrackId(cancion.getSpotifyTrackId())) {
                                cancion = cancionRepository.save(cancion);
                                log.debug("💾 Canción guardada: {} | Preview: {}",
                                        cancion.getNombre(), cancion.hasPreview() ? "SÍ" : "NO");
                            } else {
                                // Si ya existe, obtener de la BD
                                Optional<Cancion> existente = cancionRepository
                                        .findBySpotifyTrackId(cancion.getSpotifyTrackId());
                                if (existente.isPresent()) {
                                    cancion = existente.get();
                                    log.debug("🔄 Canción existente: {}", cancion.getNombre());
                                }
                            }
                        }
                        canciones.add(cancion);
                    }
                } catch (Exception e) {
                    log.warn("⚠️ Error procesando canción individual: {}", e.getMessage());
                }
            }

            log.info("📊 ESTADÍSTICAS: {} con preview, {} sin preview, total: {}",
                    cancionesConPreview, cancionesSinPreview, canciones.size());

        } catch (Exception e) {
            log.error("❌ Error procesando respuesta de Spotify: {}", e.getMessage(), e);
        }

        return canciones;
    }

    /**
     * ✅ Método mejorado para crear canción desde respuesta de Spotify CON DEBUG
     */
    private Cancion crearCancionDesdeSpotify(Map<String, Object> track, boolean procesarArtistas) {
        try {
            Cancion cancion = new Cancion();

            // Información básica
            String nombre = (String) track.get("name");
            String spotifyId = (String) track.get("id");
            String previewUrl = (String) track.get("preview_url");

            cancion.setNombre(nombre);
            cancion.setSpotifyTrackId(spotifyId);
            cancion.setPreviewUrl(previewUrl);

            // ✅ DEBUG: Logs detallados para verificar preview URLs
            if (previewUrl != null && !previewUrl.trim().isEmpty()) {
                log.info("🎧 ✅ CON PREVIEW: {} | URL: {}", nombre, previewUrl);
            } else {
                log.info("🚫 SIN PREVIEW: {} | Preview: {}", nombre,
                        previewUrl == null ? "NULL" : "VACÍO");
            }

            // Popularidad
            Object popularity = track.get("popularity");
            if (popularity instanceof Integer) {
                cancion.setPopularidad(new BigDecimal((Integer) popularity));
                log.debug("⭐ Popularidad: {} - {}", nombre, popularity);
            }

            // Duración
            Object durationMs = track.get("duration_ms");
            if (durationMs instanceof Integer) {
                cancion.setDuracionMs((Integer) durationMs);
            }

            // Artistas
            List<Map<String, Object>> artists = (List<Map<String, Object>>) track.get("artists");
            if (artists != null && !artists.isEmpty()) {
                String artistasString = artists.stream()
                        .map(artistData -> (String) artistData.get("name"))
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining(", "));
                cancion.setArtistas(artistasString);
                log.debug("🎤 Artistas: {}", artistasString);
            }

            // Álbum
            Map<String, Object> album = (Map<String, Object>) track.get("album");
            if (album != null) {
                String albumName = (String) album.get("name");
                cancion.setAlbum(albumName);

                // Imagen del álbum
                List<Map<String, Object>> images = (List<Map<String, Object>>) album.get("images");
                if (images != null && !images.isEmpty()) {
                    String imageUrl = (String) images.get(0).get("url");
                    cancion.setImagenUrl(imageUrl);
                    log.debug("🖼️ Imagen: {} tiene imagen: {}", nombre, imageUrl != null);
                }
            }

            // Verificar campos mínimos
            if (cancion.getNombre() == null || cancion.getSpotifyTrackId() == null) {
                log.warn("❌ Canción incompleta, faltan datos básicos: nombre={}, id={}",
                        cancion.getNombre(), cancion.getSpotifyTrackId());
                return null;
            }

            // ✅ Log final del estado de la canción
            log.debug("✅ Canción procesada: {} | Preview: {} | Popularidad: {}",
                    nombre, cancion.hasPreview() ? "SÍ" : "NO",
                    cancion.getPopularidad());

            return cancion;

        } catch (Exception e) {
            log.error("❌ Error creando canción desde Spotify: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Método existente modificado para usar la nueva lógica con debug
     */
    public List<Cancion> buscarYCargarCanciones(String query) {
        try {
            log.info("🔍 Iniciando búsqueda y carga para: {}", query);
            String accessToken = obtenerAccessToken();
            if (accessToken == null) {
                log.error("❌ No se pudo obtener el token de acceso de Spotify");
                return new ArrayList<>();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = "https://api.spotify.com/v1/search?q=" +
                    URLEncoder.encode(query, StandardCharsets.UTF_8) +
                    "&type=track&limit=50&market=ES";

            log.info("🌐 URL de búsqueda: {}", url);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Cancion> resultado = procesarRespuestaSpotify(response.getBody(), true); // true = guardar en BD
                log.info("✅ Búsqueda completada para '{}': {} canciones procesadas", query, resultado.size());
                return resultado;
            } else {
                log.warn("⚠️ Respuesta no exitosa: {}", response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("❌ Error buscando y cargando canciones para '{}': {}", query, e.getMessage(), e);
        }

        return new ArrayList<>();
    }

    /**
     * ✅ NUEVO: Método para estadísticas de preview en la base de datos
     */
    public Map<String, Object> obtenerEstadisticasPreview() {
        try {
            List<Cancion> todasLasCanciones = cancionRepository.findAll();
            long total = todasLasCanciones.size();
            long conPreview = todasLasCanciones.stream().filter(Cancion::hasPreview).count();

            Map<String, Object> stats = new HashMap<>();
            stats.put("total_canciones", total);
            stats.put("con_preview", conPreview);
            stats.put("sin_preview", total - conPreview);
            stats.put("porcentaje_con_preview", total > 0 ? (conPreview * 100.0 / total) : 0);

            log.info("📊 ESTADÍSTICAS BD: {} total, {} con preview ({}%)",
                    total, conPreview, total > 0 ? (conPreview * 100 / total) : 0);

            return stats;
        } catch (Exception e) {
            log.error("❌ Error obteniendo estadísticas: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * ✅ NUEVO: Limpiar canciones sin preview de la base de datos
     */
    public int limpiarCancionesSinPreview() {
        try {
            List<Cancion> sinPreview = cancionRepository.findAll().stream()
                    .filter(cancion -> !cancion.hasPreview())
                    .collect(Collectors.toList());

            log.info("🧹 Eliminando {} canciones sin preview...", sinPreview.size());

            for (Cancion cancion : sinPreview) {
                cancionRepository.delete(cancion);
                log.debug("🗑️ Eliminada: {}", cancion.getNombre());
            }

            log.info("✅ Limpieza completada: {} canciones eliminadas", sinPreview.size());
            return sinPreview.size();

        } catch (Exception e) {
            log.error("❌ Error en limpieza: {}", e.getMessage());
            return 0;
        }
    }
}
