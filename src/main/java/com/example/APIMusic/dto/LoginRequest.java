package com.example.APIMusic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public class LoginRequest {

    // Campos sin validaciones para máxima flexibilidad
    private String email;
    private String password;
    private String username;
    private String contrasena;

    // Constructores
    public LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters principales que manejan la lógica de compatibilidad
    public String getEmail() {
        if (email != null && !email.trim().isEmpty()) {
            return email;
        }
        if (username != null && !username.trim().isEmpty()) {
            return username;
        }
        return null;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        if (password != null && !password.trim().isEmpty()) {
            return password;
        }
        if (contrasena != null && !contrasena.trim().isEmpty()) {
            return contrasena;
        }
        return null;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Métodos para compatibilidad con diferentes formatos
    public String getContrasena() {
        return getPassword(); // Usa la misma lógica
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getUsername() {
        return getEmail(); // Usa la misma lógica
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Métodos de validación manual
    public boolean isValid() {
        return getEmail() != null && !getEmail().trim().isEmpty() &&
                getPassword() != null && !getPassword().trim().isEmpty();
    }

    public String getValidationError() {
        if (getEmail() == null || getEmail().trim().isEmpty()) {
            return "El email es requerido";
        }
        if (getPassword() == null || getPassword().trim().isEmpty()) {
            return "La contraseña es requerida";
        }
        return null;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
