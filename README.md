API Music - Backend
Descripción del Proyecto
API REST desarrollada en Spring Boot para la gestión completa de usuarios de una aplicación musical. El sistema permite administrar usuarios, perfiles personalizados y favoritos musicales (canciones, álbumes y playlists) con integración completa a Spotify Web API para obtener información musical actualizada en tiempo real.
Stack Tecnológico

Java 17+
Spring Boot 3.x
Spring Data JPA
MySQL 8.0 (Base de datos principal)
Maven (Gestión de dependencias)
Spotify Web API (Integración de servicios musicales)

Arquitectura del Proyecto
src/main/java/com/example/APIMusic/
├── config/
│   └── SpotifyConfig.java
├── controller/
│   ├── AuthController.java
│   ├── SpotifyController.java
│   └── UsuarioController.java
├── dto/
│   ├── AlbumDto.java
│   ├── ArtistDto.java
│   ├── ExternalUrlsDto.java
│   ├── ImageDto.java
│   ├── SpotifyTokenDto.java
│   └── TrackDto.java
├── entity/
│   ├── AlbumFavorito.java
│   ├── MusicaFavorita.java
│   ├── PerfilUsuario.java
│   ├── PlaylistFavorita.java
│   └── Usuario.java
├── repository/
│   ├── AlbumFavoritoRepository.java
│   ├── MusicaFavoritaRepository.java
│   ├── PlaylistFavoritaRepository.java
│   └── UsuarioRepository.java
├── service/
│   ├── SpotifyApiService.java
│   ├── SpotifyAuthService.java
│   └── UsuarioService.java
└── ApiMusicApplication.java
Configuración del Sistema
Base de Datos y Servidor
properties# Configuración MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/apimusic_db
spring.datasource.username=root
spring.datasource.password=tu_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuración JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Puerto del servidor
server.port=8080

# Configuración Spotify API
spotify.client.id=tu_spotify_client_id
spotify.client.secret=tu_spotify_client_secret
spotify.redirect.uri=http://localhost:8080/api/auth/spotify/callback
spotify.auth.url=https://accounts.spotify.com/authorize
spotify.token.url=https://accounts.spotify.com/api/token
spotify.api.url=https://api.spotify.com/v1
Configuración de Spotify Developer
Paso 1: Registro en Spotify for Developers

Acceder a https://developer.spotify.com/dashboard
Iniciar sesión con cuenta de Spotify
Aceptar términos de servicio para desarrolladores

Paso 2: Creación de Aplicación

Seleccionar "Create app"
Completar formulario:

App name: API Music Backend
App description: Sistema de gestión musical con integración Spotify
Website: http://localhost:8080
Redirect URIs: http://localhost:8080/api/auth/spotify/callback
Which API/SDKs: Web API



Paso 3: Obtención de Credenciales

Acceder al Dashboard de la aplicación creada
Ir a "Settings"
Copiar Client ID
Mostrar y copiar Client Secret
Configurar Redirect URIs correctamente

Paso 4: Configuración de Scopes
Permisos requeridos para funcionalidad completa:

user-read-private - Acceso a perfil privado
user-read-email - Acceso a email del usuario
user-library-read - Lectura de biblioteca musical
user-library-modify - Modificación de biblioteca
playlist-read-private - Lectura de playlists privadas
playlist-read-collaborative - Playlists colaborativas
user-top-read - Top tracks y artistas
user-read-recently-played - Historial de reproducción

Instalación y Puesta en Marcha
Requisitos del Sistema

Java Development Kit 17+
MySQL Server 8.0+
Maven 3.6+
Cuenta activa de Spotify Developer

Proceso de Instalación

Clonación del repositorio

bashgit clone [URL_DEL_REPOSITORIO]
cd APIMusic

Configuración de base de datos

sqlCREATE DATABASE apimusic_db;
CREATE USER 'apimusic_user'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON apimusic_db.* TO 'apimusic_user'@'localhost';
FLUSH PRIVILEGES;

Actualización de configuración
Editar src/main/resources/application.properties con las credenciales correctas
Instalación de dependencias

bashmvn clean install

Ejecución de la aplicación

bashmvn spring-boot:run

Verificación del sistema


URL base: http://localhost:8080
Test de usuarios: GET http://localhost:8080/api/usuarios
Verificación Spotify: GET http://localhost:8080/api/auth/verify-config

Endpoints de la API
Gestión de Usuarios
MétodoEndpointDescripciónParámetrosGET/api/usuariosListar todos los usuarios registrados-POST/api/usuariosRegistrar nuevo usuarioBody: Usuario JSONGET/api/usuarios/{id}Obtener usuario específico por IDPath: idPUT/api/usuarios/{id}Actualizar datos de usuarioPath: id, Body: Usuario JSONDELETE/api/usuarios/{id}Eliminar usuario del sistemaPath: idGET/api/usuarios/email/{email}Buscar usuario por emailPath: emailPOST/api/usuarios/loginAutenticación de usuarioBody: credenciales
Gestión de Perfiles
MétodoEndpointDescripciónParámetrosGET/api/usuarios/{id}/perfilObtener perfil de usuarioPath: idPUT/api/usuarios/{id}/perfilActualizar perfil de usuarioPath: id, Body: PerfilUsuario JSON
Gestión de Favoritos
Músicas Favoritas
MétodoEndpointDescripciónParámetrosGET/api/usuarios/{id}/favoritos/musicasListar canciones favoritas del usuarioPath: idPOST/api/usuarios/{id}/favoritos/musicasAgregar canción a favoritosPath: id, Body: MusicaFavorita JSONDELETE/api/usuarios/{id}/favoritos/musicas/{musicaId}Eliminar canción de favoritosPath: id, musicaId
Álbumes Favoritos
MétodoEndpointDescripciónParámetrosGET/api/usuarios/{id}/favoritos/albumesListar álbumes favoritos del usuarioPath: idPOST/api/usuarios/{id}/favoritos/albumesAgregar álbum a favoritosPath: id, Body: AlbumFavorito JSONDELETE/api/usuarios/{id}/favoritos/albumes/{albumId}Eliminar álbum de favoritosPath: id, albumId
Playlists Favoritas
MétodoEndpointDescripciónParámetrosGET/api/usuarios/{id}/favoritos/playlistsListar playlists favoritas del usuarioPath: idPOST/api/usuarios/{id}/favoritos/playlistsAgregar playlist a favoritosPath: id, Body: PlaylistFavorita JSONDELETE/api/usuarios/{id}/favoritos/playlists/{playlistId}Eliminar playlist de favoritosPath: id, playlistId
Integración con Spotify API
Autenticación Spotify
MétodoEndpointDescripciónTipo de AutenticaciónGET/api/spotify/auth/urlGenerar URL de autorización OAuth-GET/api/auth/spotify/callbackCallback para procesamiento OAuthAuthorization CodePOST/api/auth/client-tokenObtener token Client CredentialsClient CredentialsPOST/api/auth/client-token-altToken alternativo Client CredentialsClient CredentialsGET/api/auth/verify-configVerificar configuración de Spotify-
API de Búsqueda Musical
MétodoEndpointDescripciónParámetros de QueryGET/api/spotify/searchBúsqueda general multi-tipoq, type, limit, offsetGET/api/spotify/search/tracksBúsqueda específica de cancionesq, limit, offset, marketGET/api/spotify/search/artistsBúsqueda específica de artistasq, limit, offsetGET/api/spotify/search/albumsBúsqueda específica de álbumesq, limit, offset, marketGET/api/spotify/search/playlistsBúsqueda específica de playlistsq, limit, offset, market
API de Canciones (Tracks)
MétodoEndpointDescripciónParámetrosGET/api/spotify/track/{id}Información detallada de canciónPath: id, Query: marketGET/api/spotify/tracksMúltiples canciones por IDsQuery: ids, marketGET/api/spotify/tracks/features/{id}Características de audioPath: idGET/api/spotify/tracks/analysis/{id}Análisis completo de audioPath: id
API de Artistas
MétodoEndpointDescripciónParámetrosGET/api/spotify/artist/{id}Información completa del artistaPath: idGET/api/spotify/artistsMúltiples artistas por IDsQuery: idsGET/api/spotify/artist/{id}/albumsÁlbumes del artistaPath: id, Query: include_groups, market, limit, offsetGET/api/spotify/artist/{id}/top-tracksCanciones más popularesPath: id, Query: marketGET/api/spotify/artist/{id}/related-artistsArtistas relacionadosPath: id
API de Álbumes
MétodoEndpointDescripciónParámetrosGET/api/spotify/album/{id}Información completa del álbumPath: id, Query: marketGET/api/spotify/albumsMúltiples álbumes por IDsQuery: ids, marketGET/api/spotify/album/{id}/tracksCanciones del álbumPath: id, Query: market, limit, offsetGET/api/spotify/browse/new-releasesÁlbumes recién lanzadosQuery: country, limit, offset
API de Playlists
MétodoEndpointDescripciónParámetrosGET/api/spotify/playlist/{id}Información completa de playlistPath: id, Query: market, fieldsGET/api/spotify/playlist/{id}/tracksCanciones de la playlistPath: id, Query: market, fields, limit, offsetGET/api/spotify/browse/featured-playlistsPlaylists destacadasQuery: country, limit, offsetGET/api/spotify/browse/category/{id}/playlistsPlaylists por categoríaPath: id, Query: country, limit, offset
API de Exploración
MétodoEndpointDescripciónParámetrosGET/api/spotify/browse/categoriesTodas las categorías musicalesQuery: country, locale, limit, offsetGET/api/spotify/browse/category/{id}Información de categoría específicaPath: id, Query: country, localeGET/api/spotify/browse/recommendationsRecomendaciones personalizadasQuery: seed_artists, seed_genres, seed_tracks, limit, marketGET/api/spotify/browse/genresGéneros disponibles para seeds-
API de Usuario (Requiere OAuth)
MétodoEndpointDescripciónHeaders RequeridosGET/api/spotify/mePerfil del usuario actualAuthorization: Bearer {token}GET/api/spotify/me/playlistsPlaylists del usuarioAuthorization: Bearer {token}GET/api/spotify/me/tracksCanciones guardadasAuthorization: Bearer {token}GET/api/spotify/me/albumsÁlbumes guardadosAuthorization: Bearer {token}GET/api/spotify/me/top/tracksTop canciones del usuarioAuthorization: Bearer {token}GET/api/spotify/me/top/artistsTop artistas del usuarioAuthorization: Bearer {token}GET/api/spotify/me/recently-playedHistorial de reproducciónAuthorization: Bearer {token}
Modelos de Datos
Entidades de Base de Datos (API Propia)
Usuario
java@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String contrasena;
    
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
    
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private PerfilUsuario perfil;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<MusicaFavorita> favoritasMusicas;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<AlbumFavorito> favoritosAlbumes;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<PlaylistFavorita> favoritosPlaylists;
}
PerfilUsuario
java@Entity
@Table(name = "perfiles_usuario")
public class PerfilUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "foto_url")
    private String fotoUrl;
    
    @Column(columnDefinition = "TEXT")
    private String bio;
    
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
MusicaFavorita
java@Entity
@Table(name = "musicas_favoritas")
public class MusicaFavorita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "spotify_track_id", nullable = false)
    private String spotifyTrackId;
    
    @Column(name = "fecha_agregado")
    private LocalDateTime fechaAgregado;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
AlbumFavorito
java@Entity
@Table(name = "albumes_favoritos")
public class AlbumFavorito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "spotify_album_id", nullable = false)
    private String spotifyAlbumId;
    
    @Column(name = "fecha_agregado")
    private LocalDateTime fechaAgregado;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
PlaylistFavorita
java@Entity
@Table(name = "playlists_favoritas")
public class PlaylistFavorita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "spotify_playlist_id", nullable = false)
    private String spotifyPlaylistId;
    
    @Column(name = "fecha_agregado")
    private LocalDateTime fechaAgregado;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
DTOs para Spotify API
TrackDto
javapublic class TrackDto {
    private String id;
    private String name;
    private String artist;
    private String album;
    private Integer duration;
    private Integer popularity;
    private String previewUrl;
    private String imageUrl;
    private Map<String, String> externalUrls;
    private Boolean isExplicit;
    private Integer trackNumber;
    private String uri;
}
ArtistDto
javapublic class ArtistDto {
    private String id;
    private String name;
    private Integer popularity;
    private Integer followers;
    private List<String> genres;
    private String imageUrl;
    private Map<String, String> externalUrls;
    private String uri;
}
AlbumDto
javapublic class AlbumDto {
    private String id;
    private String name;
    private String artist;
    private String releaseDate;
    private Integer totalTracks;
    private List<String> genres;
    private Integer popularity;
    private String imageUrl;
    private Map<String, String> externalUrls;
    private List<TrackDto> tracks;
    private String albumType;
    private String uri;
}
SpotifyTokenDto
javapublic class SpotifyTokenDto {
    private String accessToken;
    private String tokenType;
    private Integer expiresIn;
    private String refreshToken;
    private String scope;
    private Long expirationTime;
}
ExternalUrlsDto
javapublic class ExternalUrlsDto {
    private String spotify;
}
ImageDto
javapublic class ImageDto {
    private String url;
    private Integer height;
    private Integer width;
}
Repositorios JPA
UsuarioRepository
java@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
    
    @Query("SELECT u FROM Usuario u WHERE u.fechaRegistro >= :fecha")
    List<Usuario> findUsuariosRegistradosDespueDe(@Param("fecha") LocalDateTime fecha);
}
MusicaFavoritaRepository
java@Repository
public interface MusicaFavoritaRepository extends JpaRepository<MusicaFavorita, Long> {
    List<MusicaFavorita> findByUsuarioIdOrderByFechaAgregadoDesc(Long usuarioId);
    boolean existsByUsuarioIdAndSpotifyTrackId(Long usuarioId, String spotifyTrackId);
    void deleteByUsuarioIdAndSpotifyTrackId(Long usuarioId, String spotifyTrackId);
    
    @Query("SELECT COUNT(m) FROM MusicaFavorita m WHERE m.usuario.id = :usuarioId")
    Long contarMusicasFavoritasPorUsuario(@Param("usuarioId") Long usuarioId);
}
AlbumFavoritoRepository
java@Repository
public interface AlbumFavoritoRepository extends JpaRepository<AlbumFavorito, Long> {
    List<AlbumFavorito> findByUsuarioIdOrderByFechaAgregadoDesc(Long usuarioId);
    boolean existsByUsuarioIdAndSpotifyAlbumId(Long usuarioId, String spotifyAlbumId);
    void deleteByUsuarioIdAndSpotifyAlbumId(Long usuarioId, String spotifyAlbumId);
    
    @Query("SELECT COUNT(a) FROM AlbumFavorito a WHERE a.usuario.id = :usuarioId")
    Long contarAlbumesFavoritosPorUsuario(@Param("usuarioId") Long usuarioId);
}
PlaylistFavoritaRepository
java@Repository
public interface PlaylistFavoritaRepository extends JpaRepository<PlaylistFavorita, Long> {
    List<PlaylistFavorita> findByUsuarioIdOrderByFechaAgregadoDesc(Long usuarioId);
    boolean existsByUsuarioIdAndSpotifyPlaylistId(Long usuarioId, String spotifyPlaylistId);
    void deleteByUsuarioIdAndSpotifyPlaylistId(Long usuarioId, String spotifyPlaylistId);
    
    @Query("SELECT COUNT(p) FROM PlaylistFavorita p WHERE p.usuario.id = :usuarioId")
    Long contarPlaylistsFavoritasPorUsuario(@Param("usuarioId") Long usuarioId);
}
Ejemplos de Uso
Configuración Inicial
Verificar Configuración de Spotify
httpGET http://localhost:8080/api/auth/verify-config
Respuesta:
json{
    "clientIdConfigured": true,
    "clientSecretConfigured": true,
    "redirectUriConfigured": true,
    "authUrl": "https://accounts.spotify.com/authorize",
    "tokenUrl": "https://accounts.spotify.com/api/token",
    "apiUrl": "https://api.spotify.com/v1",
    "status": "Configuration OK"
}
Obtener Token de Cliente
httpPOST http://localhost:8080/api/auth/client-token
Content-Type: application/json
Respuesta:
json{
    "access_token": "BQCXYZabc123...",
    "token_type": "Bearer",
    "expires_in": 3600
}
Gestión de Usuarios
Registrar Nuevo Usuario
httpPOST http://localhost:8080/api/usuarios
Content-Type: application/json

{
    "nombre": "Carlos Rodriguez",
    "email": "carlos@email.com",
    "contrasena": "password123"
}
Respuesta:
json{
    "id": 1,
    "nombre": "Carlos Rodriguez",
    "email": "carlos@email.com",
    "fechaRegistro": "2025-06-12T10:30:45.123456",
    "perfil": null,
    "favoritasMusicas": [],
    "favoritosAlbumes": [],
    "favoritosPlaylists": []
}
Listar Todos los Usuarios
httpGET http://localhost:8080/api/usuarios
Búsquedas en Spotify
Búsqueda General de Canciones
httpGET http://localhost:8080/api/spotify/search/tracks?q=bohemian%20rhapsody&limit=5
Respuesta:
json[
    {
        "id": "4u7EnebtmKWzUH433cf5Qv",
        "name": "Bohemian Rhapsody",
        "artist": "Queen",
        "album": "A Night at the Opera",
        "duration": 355394,
        "popularity": 78,
        "previewUrl": "https://p.scdn.co/mp3-preview/...",
        "imageUrl": "https://i.scdn.co/image/...",
        "externalUrls": {
            "spotify": "https://open.spotify.com/track/4u7EnebtmKWzUH433cf5Qv"
        },
        "isExplicit": false,
        "trackNumber": 11,
        "uri": "spotify:track:4u7EnebtmKWzUH433cf5Qv"
    }
]
Información Detallada de Artista
httpGET http://localhost:8080/api/spotify/artist/1dfeR4HaWDbWqFHLkxsg1d
Respuesta:
json{
    "id": "1dfeR4HaWDbWqFHLkxsg1d",
    "name": "Queen",
    "popularity": 86,
    "followers": 28500000,
    "genres": ["classic rock", "glam rock", "rock"],
    "imageUrl": "https://i.scdn.co/image/...",
    "externalUrls": {
        "spotify": "https://open.spotify.com/artist/1dfeR4HaWDbWqFHLkxsg1d"
    },
    "uri": "spotify:artist:1dfeR4HaWDbWqFHLkxsg1d"
}
Gestión de Favoritos
Agregar Canción a Favoritos
httpPOST http://localhost:8080/api/usuarios/1/favoritos/musicas
Content-Type: application/json

{
    "spotifyTrackId": "4u7EnebtmKWzUH433cf5Qv"
}
Respuesta:
json{
    "id": 1,
    "spotifyTrackId": "4u7EnebtmKWzUH433cf5Qv",
    "fechaAgregado": "2025-06-12T11:15:30.456789",
    "usuario": {
        "id": 1,
        "nombre": "Carlos Rodriguez",
        "email": "carlos@email.com"
    }
}
Listar Canciones Favoritas
httpGET http://localhost:8080/api/usuarios/1/favoritos/musicas
Códigos de Estado HTTP
CódigoDescripciónUso200 OKOperación exitosaConsultas y actualizaciones exitosas201 CreatedRecurso creadoCreación de usuarios, favoritos204 No ContentEliminación exitosaEliminación de favoritos400 Bad RequestDatos inválidosValidación fallida, JSON malformado401 UnauthorizedNo autorizadoToken de Spotify inválido/expirado403 ForbiddenAcceso denegadoPermisos insuficientes404 Not FoundRecurso no encontradoUsuario/canción no existe409 ConflictConflicto de recursosEmail duplicado, favorito ya existe429 Too Many RequestsLímite excedidoRate limit de Spotify500 Internal Server ErrorError del servidorError interno no controlado
Flujos de Autenticación
Client Credentials Flow (Búsquedas Públicas)

Solicitar token: POST /api/auth/client-token
Recibir access_token con duración de 3600 segundos
Usar token para búsquedas y consultas públicas
Renovar token cuando expire

Authorization Code Flow (Datos del Usuario)

Obtener URL de autorización: GET /api/spotify/auth/url
Redirigir usuario a Spotify para autorización
Usuario autoriza permisos en Spotify
Spotify redirige a: /api/auth/spotify/callback?code=...
Sistema intercambia código por access_token y refresh_token
Usar access_token para operaciones de usuario
Renovar con refresh_token cuando sea necesario

Guía de Implementación
Orden Recomendado de Implementación
Fase 1: Configuración Básica

Verificar configuración: GET /api/auth/verify-config
Obtener token de cliente: POST /api/auth/client-token
Probar búsqueda básica: GET /api/spotify/search/tracks?q=test

Fase 2: Gestión de Usuarios

Crear usuario: POST /api/usuarios
Listar usuarios: GET /api/usuarios
Obtener usuario específico: GET /api/usuarios/{id}

Fase 3: Búsquedas Musicales

Buscar canciones: GET /api/spotify/search/tracks
Buscar artistas: GET /api/spotify/search/artists
Información de canción: GET /api/spotify/track/{id}
Información de artista: GET /api/spotify/artist/{id}

Fase 4: Favoritos

Agregar canción favorita: POST /api/usuarios/{id}/favoritos/musicas
Listar favoritos: GET /api/usuarios/{id}/favoritos/musicas
Eliminar favorito: DELETE /api/usuarios/{id}/favoritos/musicas/{musicaId}

Fase 5: Funcionalidades Avanzadas

OAuth con Spotify: GET /api/spotify/auth/url
Datos personales del usuario: GET /api/spotify/me
Recomendaciones: GET /api/spotify/browse/recommendations

## 📋 Documentación con Postman

### 🔗 [**COLECCIÓN DE POSTMAN - API MUSIC CON SPOTIFY**](https://documenter.getpostman.com/view/40843950/2sB2x6kBmo#7b715c79-a01d-435f-8adf-2eb1a4fef6fc)

### Cómo usar la colección de Postman:

1. **Importar la colección:**
   - Abre Postman
   - Click en "Import"
   - Pega el enlace de la colección
   - Click en "Import"

2. **Configurar variables de entorno:**
   - Crear un entorno llamado "API Music"
   - Agregar variable: `base_url` = `http://localhost:8080`
   - Agregar variable: `user_id` = `1` (para pruebas)
   - Agregar variable: `spotify_token` = `""` (se actualiza automáticamente)

3. **Ejecutar las pruebas:**
   - Los endpoints están organizados por carpetas:
     - **Usuarios:** Gestión de usuarios y perfiles
     - **Favoritos:** Músicas, álbumes y playlists favoritas
     - **Spotify Auth:** Autenticación con Spotify
     - **Spotify API:** Búsqueda y obtención de música
   - Cada request incluye ejemplos y documentación
   - Se pueden ejecutar individualmente o como colección

## Desarrollo y Contribución

### Para desarrolladores

1. **Clonar y configurar:**
```bash
git clone <repo-url>
cd APIMusic
mvn clean install
```

2. **Configurar variables de entorno (opcional):**
```bash
export SPOTIFY_CLIENT_ID=tu_client_id
export SPOTIFY_CLIENT_SECRET=tu_client_secret
```

3. **Ejecutar en modo desarrollo:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

4. **Ejecutar tests:**
```bash
mvn test
```

## Limitaciones y Consideraciones

### Límites de la API de Spotify
- **Rate Limiting:** Spotify limita el número de requests por segundo
- **Tokens de acceso:** Los tokens expiran (generalmente en 1 hora)
- **Scopes:** Diferentes operaciones requieren diferentes permisos

### Recomendaciones
- Implementar cache para reducir llamadas a Spotify
- Manejar adecuadamente la renovación de tokens
- Implementar retry logic para manejar rate limits
- Considerar usar webhooks de Spotify para actualizaciones en tiempo real

## Troubleshooting

### Problemas comunes

1. **Error 401 al acceder a Spotify:**
   - Verificar que las credenciales estén correctas
   - Comprobar que el token no haya expirado

2. **Error de redirect_uri mismatch:**
   - Verificar que la URI en Spotify Dashboard coincida exactamente
   - Incluir `http://localhost:8080/api/auth/spotify/callback`

   

3. **Error de configuración:**
   - Usar `GET /api/auth/verify-config` para verificar la configuración
   - Revisar los logs para errores específicos
### 🔗 [**COLECCIÓN DE POSTMAN - API SPOTIFY**](https://documenter.getpostman.com/view/40843950/2sB2x6kBmo#7b715c79-a01d-435f-8adf-2eb1a4fef6fc)

**¡La API está lista para ser consumida por tu frontend con integración completa de Spotify!** 🚀🎵