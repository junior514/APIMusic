package com.example.APIMusic.controller;

import com.example.APIMusic.dto.BusquedaDTO;
import com.example.APIMusic.entity.Cancion;
import com.example.APIMusic.service.SpotifyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/spotify")
@CrossOrigin(origins = "*")
public class SpotifyController {

    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @PostMapping("/canciones")
    public ResponseEntity<List<Cancion>> buscarYGuardarCanciones(@RequestBody BusquedaDTO dto) {
        List<Cancion> canciones = spotifyService.buscarYCargarCanciones(dto.getNombre());
        return ResponseEntity.ok(canciones);
    }

    @PostMapping("/canciones/cargar-todas")
    public ResponseEntity<List<Cancion>> cargarTodasLasCanciones() {
        List<String> terminos = List.of(
                "rock", "pop", "reggaeton", "bachata", "salsa",
                "shakira", "ricky martin", "coldplay", "karol g", "metallica");

        List<Cancion> todas = new ArrayList<>();
        for (String termino : terminos) {
            todas.addAll(spotifyService.buscarYCargarCanciones(termino));
        }

        return ResponseEntity.ok(todas);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Cancion>> buscarCanciones(@RequestParam String nombre) {
        List<Cancion> canciones = spotifyService.buscarYCargarCanciones(nombre);
        return ResponseEntity.ok(canciones);
    }
}
