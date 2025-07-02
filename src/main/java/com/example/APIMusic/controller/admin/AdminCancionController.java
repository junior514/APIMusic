// AdminCancionController.java
package com.example.APIMusic.controller.admin;

import com.example.APIMusic.entity.Cancion;
import com.example.APIMusic.service.CancionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/canciones")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCancionController {

    @Autowired
    private CancionService cancionService;

    @GetMapping
    public String listarCanciones(@RequestParam(defaultValue = "") String buscar, Model model) {
        List<Cancion> canciones;

        if (!buscar.isEmpty()) {
            canciones = cancionService.buscarPorNombre(buscar);
        } else {
            canciones = cancionService.obtenerTodas();
        }

        model.addAttribute("canciones", canciones);
        model.addAttribute("buscar", buscar);
        return "admin/canciones/lista";
    }

    @GetMapping("/ver/{id}")
    public String verCancion(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Cancion> cancion = cancionService.obtenerPorId(id);
        if (cancion.isPresent()) {
            model.addAttribute("cancion", cancion.get());
            return "admin/canciones/detalle";
        } else {
            redirectAttributes.addFlashAttribute("error", "Canción no encontrada");
            return "redirect:/admin/canciones";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCancion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (cancionService.eliminar(id)) {
                redirectAttributes.addFlashAttribute("success", "Canción eliminada correctamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se pudo eliminar la canción");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la canción: " + e.getMessage());
        }

        return "redirect:/admin/canciones";
    }
}
