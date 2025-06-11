// UsuarioController.java
package com.example.APIMusic.controller;

import com.example.APIMusic.entity.Usuario;
import com.example.APIMusic.entity.PerfilUsuario;
import com.example.APIMusic.entity.MusicaFavorita;
import com.example.APIMusic.entity.AlbumFavorito;
import com.example.APIMusic.entity.PlaylistFavorita;
import com.example.APIMusic.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.findAll();
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        try {
            Optional<Usuario> usuario = usuarioService.findById(id);
            if (usuario.isPresent()) {
                return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear nuevo usuario
    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.save(usuario);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetails) {
        try {
            Optional<Usuario> usuarioOptional = usuarioService.findById(id);
            if (usuarioOptional.isPresent()) {
                Usuario usuario = usuarioOptional.get();
                usuario.setNombre(usuarioDetails.getNombre());
                usuario.setEmail(usuarioDetails.getEmail());
                usuario.setContrasena(usuarioDetails.getContrasena());
                
                Usuario usuarioActualizado = usuarioService.save(usuario);
                return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUsuario(@PathVariable Long id) {
        try {
            if (usuarioService.existsById(id)) {
                usuarioService.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Buscar usuario por email
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> getUsuarioByEmail(@PathVariable String email) {
        try {
            Optional<Usuario> usuario = usuarioService.findByEmail(email);
            if (usuario.isPresent()) {
                return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener perfil del usuario
    @GetMapping("/{id}/perfil")
    public ResponseEntity<PerfilUsuario> getPerfilUsuario(@PathVariable Long id) {
        try {
            Optional<Usuario> usuario = usuarioService.findById(id);
            if (usuario.isPresent() && usuario.get().getPerfil() != null) {
                return new ResponseEntity<>(usuario.get().getPerfil(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar perfil del usuario
    @PutMapping("/{id}/perfil")
    public ResponseEntity<PerfilUsuario> updatePerfilUsuario(@PathVariable Long id, @RequestBody PerfilUsuario perfilDetails) {
        try {
            PerfilUsuario perfilActualizado = usuarioService.updatePerfil(id, perfilDetails);
            if (perfilActualizado != null) {
                return new ResponseEntity<>(perfilActualizado, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener músicas favoritas del usuario
    @GetMapping("/{id}/favoritos/musicas")
    public ResponseEntity<List<MusicaFavorita>> getMusicasFavoritas(@PathVariable Long id) {
        try {
            List<MusicaFavorita> musicasFavoritas = usuarioService.getMusicasFavoritas(id);
            return new ResponseEntity<>(musicasFavoritas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Agregar música a favoritos
    @PostMapping("/{id}/favoritos/musicas")
    public ResponseEntity<MusicaFavorita> addMusicaFavorita(@PathVariable Long id, @RequestBody MusicaFavorita musicaFavorita) {
        try {
            MusicaFavorita nuevaMusicaFavorita = usuarioService.addMusicaFavorita(id, musicaFavorita);
            if (nuevaMusicaFavorita != null) {
                return new ResponseEntity<>(nuevaMusicaFavorita, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar música de favoritos
    @DeleteMapping("/{id}/favoritos/musicas/{musicaId}")
    public ResponseEntity<HttpStatus> removeMusicaFavorita(@PathVariable Long id, @PathVariable Long musicaId) {
        try {
            boolean removed = usuarioService.removeMusicaFavorita(id, musicaId);
            if (removed) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener álbumes favoritos del usuario
    @GetMapping("/{id}/favoritos/albumes")
    public ResponseEntity<List<AlbumFavorito>> getAlbumesFavoritos(@PathVariable Long id) {
        try {
            List<AlbumFavorito> albumesFavoritos = usuarioService.getAlbumesFavoritos(id);
            return new ResponseEntity<>(albumesFavoritos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Agregar álbum a favoritos
    @PostMapping("/{id}/favoritos/albumes")
    public ResponseEntity<AlbumFavorito> addAlbumFavorito(@PathVariable Long id, @RequestBody AlbumFavorito albumFavorito) {
        try {
            AlbumFavorito nuevoAlbumFavorito = usuarioService.addAlbumFavorito(id, albumFavorito);
            if (nuevoAlbumFavorito != null) {
                return new ResponseEntity<>(nuevoAlbumFavorito, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar álbum de favoritos
    @DeleteMapping("/{id}/favoritos/albumes/{albumId}")
    public ResponseEntity<HttpStatus> removeAlbumFavorito(@PathVariable Long id, @PathVariable Long albumId) {
        try {
            boolean removed = usuarioService.removeAlbumFavorito(id, albumId);
            if (removed) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener playlists favoritas del usuario
    @GetMapping("/{id}/favoritos/playlists")
    public ResponseEntity<List<PlaylistFavorita>> getPlaylistsFavoritas(@PathVariable Long id) {
        try {
            List<PlaylistFavorita> playlistsFavoritas = usuarioService.getPlaylistsFavoritas(id);
            return new ResponseEntity<>(playlistsFavoritas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Agregar playlist a favoritos
    @PostMapping("/{id}/favoritos/playlists")
    public ResponseEntity<PlaylistFavorita> addPlaylistFavorita(@PathVariable Long id, @RequestBody PlaylistFavorita playlistFavorita) {
        try {
            PlaylistFavorita nuevaPlaylistFavorita = usuarioService.addPlaylistFavorita(id, playlistFavorita);
            if (nuevaPlaylistFavorita != null) {
                return new ResponseEntity<>(nuevaPlaylistFavorita, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar playlist de favoritos
    @DeleteMapping("/{id}/favoritos/playlists/{playlistId}")
    public ResponseEntity<HttpStatus> removePlaylistFavorita(@PathVariable Long id, @PathVariable Long playlistId) {
        try {
            boolean removed = usuarioService.removePlaylistFavorita(id, playlistId);
            if (removed) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Login endpoint (básico)
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestParam String email, @RequestParam String contrasena) {
        try {
            Optional<Usuario> usuario = usuarioService.login(email, contrasena);
            if (usuario.isPresent()) {
                return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}