package com.example.APIMusic.controller.admin;

import com.example.APIMusic.entity.Usuario;
import com.example.APIMusic.service.UsuarioService;
import com.example.APIMusic.service.CancionService;
import com.example.APIMusic.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CancionService cancionService;

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        if (error != null) {
            model.addAttribute("error", "Credenciales incorrectas");
        }
        if (logout != null) {
            model.addAttribute("message", "Has cerrado sesión correctamente");
        }
        return "admin/login";
    }

    @GetMapping("/auth")
    public String adminHome() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String dashboard(Model model) {
        try {
            // Obtener estadísticas generales
            Long totalUsuarios = usuarioService.contarTotalUsuarios();

            Long totalPlaylists = playlistService.contarTotalPlaylists();

            // Obtener usuarios recientes (últimos 5)
            List<Usuario> usuariosRecientes = usuarioService.obtenerUsuariosRecientes(5);

            // Agregar datos al modelo
            model.addAttribute("totalUsuarios", totalUsuarios != null ? totalUsuarios : 0L);
            
            model.addAttribute("totalPlaylists", totalPlaylists != null ? totalPlaylists : 0L);
            model.addAttribute("usuariosRecientes", usuariosRecientes);

            // Datos adicionales
            model.addAttribute("fechaUltimaActualizacion", java.time.LocalDateTime.now());

        } catch (Exception e) {
            // En caso de error, mostrar valores por defecto
            model.addAttribute("totalUsuarios", 0L);
            model.addAttribute("totalCanciones", 0L);
            model.addAttribute("totalPlaylists", 0L);
            model.addAttribute("usuariosRecientes", List.of());
            model.addAttribute("error", "Error al cargar las estadísticas del dashboard");
        }

        return "admin/dashboard";
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String adminIndex() {
        return "redirect:/admin/dashboard";
    }
}
