package com.example.APIMusic.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Asumiendo que tienes estas entidades - ajusta según tu código
import com.example.APIMusic.entity.Usuario;
import com.example.APIMusic.entity.Rol;
import com.example.APIMusic.repository.UsuarioRepository;
import java.util.Set;
import java.util.HashSet;

@Component
public class DataInitializer implements CommandLineRunner {


      @Autowired
      private UsuarioRepository usuarioRepository;

      @Autowired
      private PasswordEncoder passwordEncoder;

      @Override
      public void run(String... args) throws Exception {
      // Crear usuario admin si no existe
      if (!usuarioRepository.existsByEmail("admin@admin.com")) {
      Usuario admin = new Usuario();
      admin.setEmail("admin@admin.com");
      admin.setContrasena(passwordEncoder.encode("admin123"));
      // Suponiendo que tienes una clase Rol y un método estático para obtener roles por nombre
      Set<Rol> roles = new HashSet<>();
      roles.add(Rol.valueOf("ADMIN")); // Usa valueOf si es un enum, o reemplaza por el método adecuado
      admin.setRoles(roles);
      admin.setNombre("Administrador");
      admin.setActivo(true);

      usuarioRepository.save(admin);
      System.out.println("Usuario admin creado: admin@admin.com / admin123");
      }
  }
}
