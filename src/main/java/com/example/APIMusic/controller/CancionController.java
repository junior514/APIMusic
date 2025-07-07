package com.example.APIMusic.controller;

import com.example.APIMusic.dto.SongDTO;
import com.example.APIMusic.entity.Cancion;
import com.example.APIMusic.service.CancionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/canciones")
@CrossOrigin(origins = "*")
public class CancionController {

    @Autowired
    private CancionService cancionService;

    // ✅ VERSIÓN CON DEBUG: Logs detallados
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SongDTO>> listarCanciones() {
        System.out.println("🔍 DEBUG: Ejecutando listarCanciones()");

        List<Cancion> canciones = cancionService.obtenerTodas();
        System.out.println("🔍 DEBUG: Canciones obtenidas del service: " + canciones.size());

        // Debug: Mostrar algunas canciones si existen
        if (!canciones.isEmpty()) {
            System.out.println("🔍 DEBUG: Primera canción: " + canciones.get(0).toString());
            System.out.println("🔍 DEBUG: Artistas de primera canción: '" + canciones.get(0).getArtistas() + "'");
            System.out.println("🔍 DEBUG: Preview URL: '" + canciones.get(0).getPreviewUrl() + "'");
        }

        List<SongDTO> songDTOs = SongDTO.fromCancionList(canciones);
        System.out.println("🔍 DEBUG: SongDTOs convertidos: " + songDTOs.size());

        if (!songDTOs.isEmpty()) {
            System.out.println("🔍 DEBUG: Primer SongDTO: " + songDTOs.get(0).toString());
        }

        return ResponseEntity.ok(songDTOs);
    }

    // ✅ VERSIÓN CON DEBUG: Endpoint con preview
    @GetMapping("/con-preview")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SongDTO>> listarCancionesConPreview() {
        System.out.println("🔍 DEBUG: Ejecutando listarCancionesConPreview()");

        List<Cancion> todasLasCanciones = cancionService.obtenerTodas();
        System.out.println("🔍 DEBUG: Total canciones en BD: " + todasLasCanciones.size());

        // Debug: Analizar cuántas tienen preview
        long conPreview = todasLasCanciones.stream()
                .filter(cancion -> cancion.getPreviewUrl() != null && !cancion.getPreviewUrl().trim().isEmpty())
                .count();
        System.out.println("🔍 DEBUG: Canciones con preview URL: " + conPreview);

        // Debug: Mostrar algunos preview URLs
        todasLasCanciones.stream()
                .limit(5)
                .forEach(cancion -> {
                    System.out.println("🔍 DEBUG: Canción: " + cancion.getNombre() +
                            " | Preview: '" + cancion.getPreviewUrl() + "'");
                });

        // Filtrar solo las canciones que tienen preview URL
        List<SongDTO> cancionesConPreview = todasLasCanciones.stream()
                .filter(cancion -> cancion.getPreviewUrl() != null &&
                        !cancion.getPreviewUrl().trim().isEmpty())
                .map(SongDTO::fromCancion)
                .collect(Collectors.toList());

        System.out.println("🔍 DEBUG: Canciones con preview filtradas: " + cancionesConPreview.size());

        return ResponseEntity.ok(cancionesConPreview);
    }

    // ✅ Endpoint para obtener TODAS las canciones (sin filtro de preview)
    @GetMapping("/todas")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SongDTO>> listarTodasLasCanciones() {
        System.out.println("🔍 DEBUG: Ejecutando listarTodasLasCanciones() - SIN FILTROS");

        List<Cancion> canciones = cancionService.obtenerTodas();
        System.out.println("🔍 DEBUG: TODAS las canciones (sin filtros): " + canciones.size());

        List<SongDTO> songDTOs = SongDTO.fromCancionList(canciones);
        System.out.println("🔍 DEBUG: SongDTOs convertidos: " + songDTOs.size());

        return ResponseEntity.ok(songDTOs);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SongDTO> obtenerCancionPorId(@PathVariable Long id) {
        return cancionService.obtenerPorId(id)
                .map(cancion -> ResponseEntity.ok(SongDTO.fromCancion(cancion)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SongDTO> crearCancion(@Valid @RequestBody Cancion cancion) {
        if (cancionService.existePorSpotifyTrackId(cancion.getSpotifyTrackId())) {
            return ResponseEntity.badRequest().body(null);
        }
        Cancion guardada = cancionService.guardar(cancion);
        return ResponseEntity.ok(SongDTO.fromCancion(guardada));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SongDTO> actualizarCancion(@PathVariable Long id, @Valid @RequestBody Cancion cancion) {
        Cancion actualizada = cancionService.actualizar(id, cancion);
        return actualizada != null ? ResponseEntity.ok(SongDTO.fromCancion(actualizada))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> eliminarCancion(@PathVariable Long id) {
        return cancionService.eliminar(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ✅ VERSIÓN CON DEBUG: Buscar por nombre
    @GetMapping("/buscar/nombre")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SongDTO>> buscarPorNombre(@RequestParam String nombre) {
        System.out.println("🔍 DEBUG: Buscando por nombre: '" + nombre + "'");

        List<Cancion> resultado = cancionService.buscarPorNombre(nombre);
        System.out.println("🔍 DEBUG: Resultados encontrados: " + resultado.size());

        List<SongDTO> songDTOs = SongDTO.fromCancionList(resultado);
        return ResponseEntity.ok(songDTOs);
    }

    // ✅ VERSIÓN CON DEBUG: Buscar por nombre solo canciones con preview
    @GetMapping("/buscar/nombre/con-preview")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SongDTO>> buscarPorNombreConPreview(@RequestParam String nombre) {
        System.out.println("🔍 DEBUG: Buscando por nombre CON PREVIEW: '" + nombre + "'");

        List<Cancion> resultado = cancionService.buscarPorNombre(nombre);
        System.out.println("🔍 DEBUG: Resultados de búsqueda: " + resultado.size());

        // Filtrar solo las que tienen preview y convertir a DTO
        List<SongDTO> cancionesConPreview = resultado.stream()
                .filter(cancion -> cancion.getPreviewUrl() != null &&
                        !cancion.getPreviewUrl().trim().isEmpty())
                .map(SongDTO::fromCancion)
                .collect(Collectors.toList());

        System.out.println("🔍 DEBUG: Resultados con preview: " + cancionesConPreview.size());

        return ResponseEntity.ok(cancionesConPreview);
    }

    @GetMapping("/buscar/artista")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SongDTO>> buscarPorArtista(@RequestParam String artista) {
        List<Cancion> resultado = cancionService.buscarPorArtistas(artista);
        List<SongDTO> songDTOs = SongDTO.fromCancionList(resultado);
        return ResponseEntity.ok(songDTOs);
    }

    @GetMapping("/buscar/album")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SongDTO>> buscarPorAlbum(@RequestParam String album) {
        List<Cancion> resultado = cancionService.buscarPorAlbum(album);
        List<SongDTO> songDTOs = SongDTO.fromCancionList(resultado);
        return ResponseEntity.ok(songDTOs);
    }
}
