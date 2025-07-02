package com.example.APIMusic.controller;

import com.example.APIMusic.entity.Usuario;
import com.example.APIMusic.security.JWTUtil;
import com.example.APIMusic.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getContrasena() == null) {
            return ResponseEntity.badRequest().body("Email y contraseña son obligatorios.");
        }

        Optional<Usuario> existente = usuarioService.obtenerPorEmail(usuario.getEmail());
        if (existente.isPresent()) {
            return ResponseEntity.badRequest().body("Ya existe un usuario con ese correo.");
        }

        Usuario nuevo = usuarioService.registrar(usuario);
        return ResponseEntity.ok(nuevo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getContrasena() == null) {
            return ResponseEntity.badRequest().body("Email y contraseña son requeridos.");
        }

        Usuario user = usuarioService.autenticar(usuario.getEmail(), usuario.getContrasena());
        if (user != null) {
            String token = jwtUtil.generateToken(user.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("email", user.getEmail());
            response.put("nombre", user.getNombre());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas.");
        }
    }
}
