// AdminSpotifyController.java
package com.example.APIMusic.controller.admin;

import com.example.APIMusic.entity.Cancion;
import com.example.APIMusic.service.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/spotify")
@PreAuthorize("hasRole('ADMIN')")
public class AdminSpotifyController {

    @Autowired
    private SpotifyService spotifyService;

    @GetMapping
    public String mostrarSpotify() {
        return "admin/spotify/buscar";
    }

    @PostMapping("/buscar")
    public String buscarCanciones(@RequestParam String termino, Model model, RedirectAttributes redirectAttributes) {
        try {
            List<Cancion> canciones = spotifyService.buscarYCargarCanciones(termino);
            model.addAttribute("canciones", canciones);
            model.addAttribute("termino", termino);
            model.addAttribute("success", "Se encontraron " + canciones.size() + " canciones");
            return "admin/spotify/resultados";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al buscar en Spotify: " + e.getMessage());
            return "redirect:/admin/spotify";
        }
    }

    @PostMapping("/cargar-populares")
    public String cargarCancionesPopulares(RedirectAttributes redirectAttributes) {
        try {
            List<String> terminos = List.of(
                    "rock", "pop", "reggaeton", "bachata", "salsa",
                    "shakira", "ricky martin", "coldplay", "karol g", "metallica");

            int totalCargadas = 0;
            for (String termino : terminos) {
                List<Cancion> canciones = spotifyService.buscarYCargarCanciones(termino);
                totalCargadas += canciones.size();
            }

            redirectAttributes.addFlashAttribute("success",
                    "Se cargaron " + totalCargadas + " canciones populares desde Spotify");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error al cargar canciones populares: " + e.getMessage());
        }

        return "redirect:/admin/spotify";
    }
}
