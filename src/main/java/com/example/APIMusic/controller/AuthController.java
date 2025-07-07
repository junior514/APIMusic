package com.example.APIMusic.controller;

import com.example.APIMusic.dto.LoginRequest;
import com.example.APIMusic.dto.LoginResponse;
import com.example.APIMusic.entity.Usuario;
import com.example.APIMusic.security.JWTUtil;
import com.example.APIMusic.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.OPTIONS })
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JWTUtil jwtUtil;

    // Método OPTIONS para manejar preflight requests
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                .build();
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrar(@Valid @RequestBody Usuario usuario) {
        try {
            // Validación mejorada
            if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty() ||
                    usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Email y contraseña son obligatorios");
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(error);
            }

            Optional<Usuario> existente = usuarioService.obtenerPorEmail(usuario.getEmail());
            if (existente.isPresent()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Ya existe un usuario con ese correo");
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(error);
            }

            Usuario nuevo = usuarioService.registrar(usuario);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario registrado exitosamente");
            response.put("user", nuevo);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Validación manual usando el método del DTO
            if (!loginRequest.isValid()) {
                LoginResponse response = new LoginResponse();
                response.setSuccess(false);
                response.setMessage(loginRequest.getValidationError());
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            }

            // Obtener email y contraseña usando los getters que manejan la compatibilidad
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            System.out.println("Login attempt - Email: " + email + ", Password length: " +
                    (password != null ? password.length() : "null"));

            // Autenticar usando email
            Usuario user = usuarioService.autenticar(email, password);

            if (user != null) {
                String token = jwtUtil.generateToken(user.getEmail());

                LoginResponse response = new LoginResponse();
                response.setSuccess(true);
                response.setMessage("Login exitoso");
                response.setToken(token);
                response.setUser(user);

                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            } else {
                LoginResponse response = new LoginResponse();
                response.setSuccess(false);
                response.setMessage("Credenciales incorrectas");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            }
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();

            LoginResponse response = new LoginResponse();
            response.setSuccess(false);
            response.setMessage("Error interno del servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
    }

    // Método simplificado para login alternativo (mantener para compatibilidad)
    @PostMapping(value = "/login-email", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginWithEmail(@RequestBody Map<String, String> loginData) {
        try {
            String email = loginData.get("email");
            String password = loginData.get("password");

            if (email == null || email.trim().isEmpty() ||
                    password == null || password.trim().isEmpty()) {
                LoginResponse response = new LoginResponse();
                response.setSuccess(false);
                response.setMessage("Email y contraseña son requeridos");
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            }

            Usuario user = usuarioService.autenticar(email, password);

            if (user != null) {
                String token = jwtUtil.generateToken(user.getEmail());

                LoginResponse response = new LoginResponse();
                response.setSuccess(true);
                response.setMessage("Login exitoso");
                response.setToken(token);
                response.setUser(user);

                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            } else {
                LoginResponse response = new LoginResponse();
                response.setSuccess(false);
                response.setMessage("Credenciales incorrectas");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            }
        } catch (Exception e) {
            LoginResponse response = new LoginResponse();
            response.setSuccess(false);
            response.setMessage("Error interno del servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
    }

    // Endpoint para verificar el token
    @GetMapping(value = "/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (jwtUtil.validateToken(token)) {
                    String email = jwtUtil.getSubject(token);
                    Optional<Usuario> usuario = usuarioService.obtenerPorEmail(email);

                    if (usuario.isPresent()) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", true);
                        response.put("message", "Token válido");
                        response.put("user", usuario.get());
                        return ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(response);
                    }
                }
            }

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Token inválido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al verificar token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }
}
