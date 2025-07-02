package com.example.APIMusic.controller.admin;

import com.example.APIMusic.entity.Usuario;
import com.example.APIMusic.entity.Rol;
import com.example.APIMusic.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarUsuarios(
            @RequestParam(defaultValue = "") String buscar,
            @RequestParam(defaultValue = "") String filtroRol,
            Model model) {

        List<Usuario> usuarios;

        // Primero obtener todos los usuarios
        usuarios = usuarioService.obtenerTodos();

        // Aplicar filtro de búsqueda por nombre/email si existe
        if (!buscar.isEmpty()) {
            usuarios = usuarios.stream()
                    .filter(u -> (u.getNombre() != null && u.getNombre().toLowerCase().contains(buscar.toLowerCase()))
                            ||
                            (u.getEmail() != null && u.getEmail().toLowerCase().contains(buscar.toLowerCase())))
                    .collect(Collectors.toList());
        }

        // Aplicar filtro por rol si existe
        if (!filtroRol.isEmpty()) {
            usuarios = usuarios.stream()
                    .filter(u -> {
                        if (u.getRoles() != null && !u.getRoles().isEmpty()) {
                            return u.getRoles().stream()
                                    .anyMatch(rol -> rol.name().equalsIgnoreCase(filtroRol));
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        }

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("buscar", buscar);
        model.addAttribute("filtroRol", filtroRol);

        // Agregar estadísticas para debug
        model.addAttribute("totalUsuarios", usuarioService.obtenerTodos().size());
        model.addAttribute("usuariosFiltrados", usuarios.size());

        return "admin/usuarios/lista";
    }

    @GetMapping("/ver/{id}")
    public String verUsuario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuario = usuarioService.obtenerPorId(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "admin/usuarios/detalle";
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/admin/usuarios";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (usuarioService.eliminarUsuario(id)) {
                redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el usuario");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario: " + e.getMessage());
        }

        return "redirect:/admin/usuarios";
    }

    // CREAR USUARIO - GET (mostrar formulario)
    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/usuarios/formulario";
    }

    // CREAR USUARIO - POST (procesar formulario)
    @PostMapping("/crear")
    public String crearUsuario(@ModelAttribute Usuario usuario,
            @RequestParam(required = false) String rolPrincipal,
            RedirectAttributes redirectAttributes) {
        try {
            // CORRECCIÓN: Asignar solo el rol seleccionado
            if (rolPrincipal != null && rolPrincipal.equals("ADMIN")) {
                usuario.setRoles(Set.of(Rol.ADMIN)); // Solo rol ADMIN
            } else {
                usuario.setRoles(Set.of(Rol.USER)); // Solo rol USER
            }

            // Si no se especifica estado activo, por defecto es true
            if (usuario.getActivo() == null) {
                usuario.setActivo(true);
            }

            usuarioService.guardarUsuario(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuario creado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el usuario: " + e.getMessage());
        }

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuario = usuarioService.obtenerPorId(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "admin/usuarios/formulario";
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/admin/usuarios";
        }
    }

    // EDITAR USUARIO - POST
    @PostMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id,
            @ModelAttribute Usuario usuario,
            @RequestParam(required = false) String rolPrincipal,
            RedirectAttributes redirectAttributes) {
        try {
            // CORRECCIÓN: Asignar solo el rol seleccionado
            if (rolPrincipal != null && rolPrincipal.equals("ADMIN")) {
                usuario.setRoles(Set.of(Rol.ADMIN)); // Solo rol ADMIN
            } else {
                usuario.setRoles(Set.of(Rol.USER)); // Solo rol USER
            }

            Usuario actualizado = usuarioService.actualizarUsuario(id, usuario);
            if (actualizado != null) {
                redirectAttributes.addFlashAttribute("success", "Usuario actualizado correctamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se pudo actualizar el usuario");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el usuario: " + e.getMessage());
        }

        return "redirect:/admin/usuarios";
    }

    // MÉTODO ALTERNATIVO - mantener compatibilidad con el formulario actual
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario,
            @RequestParam(required = false) String rolPrincipal,
            RedirectAttributes redirectAttributes) {
        try {
            // CORRECCIÓN: Asignar solo el rol seleccionado
            if (rolPrincipal != null && rolPrincipal.equals("ADMIN")) {
                usuario.setRoles(Set.of(Rol.ADMIN)); // Solo rol ADMIN
            } else {
                usuario.setRoles(Set.of(Rol.USER)); // Solo rol USER
            }

            if (usuario.getId() != null) {
                // Actualizar usuario existente
                Usuario actualizado = usuarioService.actualizarUsuario(usuario.getId(), usuario);
                if (actualizado != null) {
                    redirectAttributes.addFlashAttribute("success", "Usuario actualizado correctamente");
                } else {
                    redirectAttributes.addFlashAttribute("error", "No se pudo actualizar el usuario");
                }
            } else {
                // Crear nuevo usuario
                if (usuario.getActivo() == null) {
                    usuario.setActivo(true);
                }
                usuarioService.guardarUsuario(usuario);
                redirectAttributes.addFlashAttribute("success", "Usuario creado correctamente");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el usuario: " + e.getMessage());
        }

        return "redirect:/admin/usuarios";
    }
}
