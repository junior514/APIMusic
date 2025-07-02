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

    public Usuario guardarUsuario(Usuario usuario) {
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
}
