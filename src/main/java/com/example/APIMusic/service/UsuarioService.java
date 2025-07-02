package com.example.APIMusic.service;

import com.example.APIMusic.entity.Usuario;
import com.example.APIMusic.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // MÉTODO AGREGADO: Buscar usuarios por nombre
    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Usuario guardarUsuario(Usuario usuario) {
        // Cifrar contraseña si no está cifrada
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            // Verificar si ya está cifrada (las contraseñas cifradas suelen empezar con
            // $2a$)
            if (!usuario.getContrasena().startsWith("$2a$")) {
                usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            }
        }
        return usuarioRepository.save(usuario);
    }

    /**
     * Registra un nuevo usuario cifrando su contraseña
     */
    public Usuario registrar(Usuario usuario) {
        // Cifrar la contraseña antes de guardar
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return usuarioRepository.save(usuario);
    }

    /**
     * Autentica un usuario verificando email y contraseña
     */
    public Usuario autenticar(String email, String contrasenaPlana) {
        Optional<Usuario> usuarioOpt = obtenerPorEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Verificar si la contraseña plana coincide con la cifrada
            if (passwordEncoder.matches(contrasenaPlana, usuario.getContrasena())) {
                return usuario;
            }
        }
        return null;
    }

    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(usuarioActualizado.getNombre());
            if (usuarioActualizado.getApellido() != null) {
                usuario.setApellido(usuarioActualizado.getApellido());
            }
            usuario.setEmail(usuarioActualizado.getEmail());

            // Si se actualiza la contraseña, cifrarla
            if (usuarioActualizado.getContrasena() != null &&
                    !usuarioActualizado.getContrasena().isEmpty()) {
                usuario.setContrasena(passwordEncoder.encode(usuarioActualizado.getContrasena()));
            }

            if (usuarioActualizado.getTelefono() != null) {
                usuario.setTelefono(usuarioActualizado.getTelefono());
            }
            if (usuarioActualizado.getGenero() != null) {
                usuario.setGenero(usuarioActualizado.getGenero());
            }
            if (usuarioActualizado.getFechaNacimiento() != null) {
                usuario.setFechaNacimiento(usuarioActualizado.getFechaNacimiento());
            }

            // CORRECCIÓN: Actualizar roles correctamente
            if (usuarioActualizado.getRoles() != null && !usuarioActualizado.getRoles().isEmpty()) {
                usuario.setRoles(usuarioActualizado.getRoles());
            }

            // Actualizar estado activo
            if (usuarioActualizado.getActivo() != null) {
                usuario.setActivo(usuarioActualizado.getActivo());
            }

            return usuarioRepository.save(usuario);
        }).orElse(null);
    }

    public boolean eliminarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Long contarTotalUsuarios() {
        try {
            return usuarioRepository.count();
        } catch (Exception e) {
            return 0L;
        }
    }

    public List<Usuario> obtenerUsuariosRecientes(int limite) {
        try {
            return usuarioRepository.findTop5ByOrderByCreatedAtDesc();
        } catch (Exception e) {
            // Si falla, intenta por ID o retorna lista vacía
            try {
                return usuarioRepository.findTop5ByOrderByIdDesc();
            } catch (Exception ex) {
                return List.of();
            }
        }
    }
}
