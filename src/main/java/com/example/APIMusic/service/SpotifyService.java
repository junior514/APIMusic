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
            return accessToken;
        }

        try {
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

                log.info("Token de Spotify obtenido exitosamente");
                return accessToken;
            }
        } catch (Exception e) {
            log.error("Error obteniendo token de Spotify: {}", e.getMessage(), e);
        }

        return null;
    }

    /**
     * NUEVO: Buscar canciones en tiempo real sin guardar en base de datos
     */
    public List<Cancion> buscarCancionesEnTiempoReal(String query) {
        try {
            String accessToken = obtenerAccessToken();
            if (accessToken == null) {
                log.error("No se pudo obtener el token de acceso de Spotify");
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

            log.info("Buscando en tiempo real en Spotify: {}", query);

            // Hacer petición a Spotify
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return procesarRespuestaSpotify(response.getBody(), false); // false = no guardar
            }

        } catch (Exception e) {
            log.error("Error en búsqueda en tiempo real: {}", e.getMessage(), e);
        }

        return new ArrayList<>();
    }

    /**
     * NUEVO: Buscar canciones en tiempo real solo con preview
     */
    public List<Cancion> buscarCancionesEnTiempoRealConPreview(String query) {
        List<Cancion> todasLasCanciones = buscarCancionesEnTiempoReal(query);

        // Filtrar solo las que tienen preview URL
        return todasLasCanciones.stream()
                .filter(cancion -> cancion.getPreviewUrl() != null &&
                        !cancion.getPreviewUrl().trim().isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Método mejorado para procesar respuesta de Spotify
     *
     * @param spotifyResponse - respuesta de la API de Spotify
     * @param guardarEnBD     - si true, guarda en base de datos; si false, solo
     *                        retorna objetos
     */
    private List<Cancion> procesarRespuestaSpotify(Map<String, Object> spotifyResponse, boolean guardarEnBD) {
        List<Cancion> canciones = new ArrayList<>();

        try {
            Map<String, Object> tracks = (Map<String, Object>) spotifyResponse.get("tracks");
            if (tracks == null)
                return canciones;

            List<Map<String, Object>> items = (List<Map<String, Object>>) tracks.get("items");
            if (items == null)
                return canciones;

            for (Map<String, Object> item : items) {
                try {
                    Cancion cancion = crearCancionDesdeSpotify(item, guardarEnBD);

                    if (cancion != null) {
                        if (guardarEnBD) {
                            // Verificar si ya existe antes de guardar
                            if (!cancionRepository.existsBySpotifyTrackId(cancion.getSpotifyTrackId())) {
                                cancion = cancionRepository.save(cancion);
                                log.info("Canción guardada: {}", cancion.getNombre());
                            } else {
                                // Si ya existe, obtener de la BD usando Optional
                                Optional<Cancion> existente = cancionRepository
                                        .findBySpotifyTrackId(cancion.getSpotifyTrackId());
                                if (existente.isPresent()) {
                                    cancion = existente.get();
                                }
                            }
                        }
                        // Si no se guarda en BD, simplemente agregar a la lista
                        canciones.add(cancion);
                    }
                } catch (Exception e) {
                    log.warn("Error procesando canción individual: {}", e.getMessage());
                }
            }

        } catch (Exception e) {
            log.error("Error procesando respuesta de Spotify: {}", e.getMessage(), e);
        }

        return canciones;
    }

    /**
     * Método mejorado para crear canción desde respuesta de Spotify
     */
    private Cancion crearCancionDesdeSpotify(Map<String, Object> track, boolean procesarArtistas) {
        try {
            Cancion cancion = new Cancion();

            // Información básica
            cancion.setNombre((String) track.get("name"));
            cancion.setSpotifyTrackId((String) track.get("id"));
            cancion.setPreviewUrl((String) track.get("preview_url"));

            // Popularidad - convertir a BigDecimal
            Object popularity = track.get("popularity");
            if (popularity instanceof Integer) {
                cancion.setPopularidad(new BigDecimal((Integer) popularity));
            }

            // Duración
            Object durationMs = track.get("duration_ms");
            if (durationMs instanceof Integer) {
                cancion.setDuracionMs((Integer) durationMs);
            }

            // Artistas - VERSIÓN SIMPLE: usar String separado por comas
            List<Map<String, Object>> artists = (List<Map<String, Object>>) track.get("artists");
            if (artists != null && !artists.isEmpty()) {
                String artistasString = artists.stream()
                        .map(artistData -> (String) artistData.get("name"))
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining(", "));
                cancion.setArtistas(artistasString);
            }

            // Álbum
            Map<String, Object> album = (Map<String, Object>) track.get("album");
            if (album != null) {
                cancion.setAlbum((String) album.get("name"));

                // Imagen del álbum
                List<Map<String, Object>> images = (List<Map<String, Object>>) album.get("images");
                if (images != null && !images.isEmpty()) {
                    // Tomar la primera imagen (suele ser la más grande)
                    cancion.setImagenUrl((String) images.get(0).get("url"));
                }
            }

            // Verificar que tenga los campos mínimos requeridos
            if (cancion.getNombre() == null || cancion.getSpotifyTrackId() == null) {
                log.warn("Canción incompleta, faltan datos básicos");
                return null;
            }

            return cancion;

        } catch (Exception e) {
            log.error("Error creando canción desde Spotify: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Método existente modificado para usar la nueva lógica
     */
    public List<Cancion> buscarYCargarCanciones(String query) {
        try {
            String accessToken = obtenerAccessToken();
            if (accessToken == null) {
                log.error("No se pudo obtener el token de acceso de Spotify");
                return new ArrayList<>();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = "https://api.spotify.com/v1/search?q=" +
                    URLEncoder.encode(query, StandardCharsets.UTF_8) +
                    "&type=track&limit=50&market=ES";

            log.info("Buscando y guardando en Spotify: {}", query);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return procesarRespuestaSpotify(response.getBody(), true); // true = guardar en BD
            }

        } catch (Exception e) {
            log.error("Error buscando y cargando canciones: {}", e.getMessage(), e);
        }

        return new ArrayList<>();
    }
}
