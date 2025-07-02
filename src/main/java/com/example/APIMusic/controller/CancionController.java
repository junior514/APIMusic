package com.example.APIMusic.controller;

import com.example.APIMusic.entity.Cancion;
import com.example.APIMusic.service.CancionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/canciones")
@CrossOrigin(origins = "*")
public class CancionController {

    @Autowired
    private CancionService cancionService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Cancion>> listarCanciones() {
        List<Cancion> canciones = cancionService.obtenerTodas();
        return ResponseEntity.ok(canciones);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Cancion> obtenerCancionPorId(@PathVariable Long id) {
        return cancionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Cancion> crearCancion(@Valid @RequestBody Cancion cancion) {
        if (cancionService.existePorSpotifyTrackId(cancion.getSpotifyTrackId())) {
            return ResponseEntity.badRequest().body(null);
        }
        Cancion guardada = cancionService.guardar(cancion);
        return ResponseEntity.ok(guardada);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Cancion> actualizarCancion(@PathVariable Long id, @Valid @RequestBody Cancion cancion) {
        Cancion actualizada = cancionService.actualizar(id, cancion);
        return actualizada != null ? ResponseEntity.ok(actualizada) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> eliminarCancion(@PathVariable Long id) {
        return cancionService.eliminar(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/buscar/nombre")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Cancion>> buscarPorNombre(@RequestParam String nombre) {
        List<Cancion> resultado = cancionService.buscarPorNombre(nombre);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/buscar/artista")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Cancion>> buscarPorArtista(@RequestParam String artista) {
        List<Cancion> resultado = cancionService.buscarPorArtistas(artista);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/buscar/album")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Cancion>> buscarPorAlbum(@RequestParam String album) {
        List<Cancion> resultado = cancionService.buscarPorAlbum(album);
        return ResponseEntity.ok(resultado);
    }
}
