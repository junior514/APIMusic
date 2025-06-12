## 🔧 Guía Rápida de Implementación

### **Orden Recomendado para Implementar Endpoints**

#### 1. **Configuración Básica** ⚙️
```bash
# 1. Verificar configuración
GET /api/auth/verify-config

# 2. Obtener token de cliente
POST /api/auth/client-token
```

#### 2. **Búsquedas Básicas** 🔍
```bash
# 3. Buscar canciones
GET /api/spotify/search/tracks?q=song_name&limit=10

# 4. Buscar artistas  
GET /api/spotify/search/artists?q=artist_name&limit=5

# 5. Buscar álbumes
GET /api/spotify/search/albums?q=album_name&limit=5
```

#### 3. **Información Detallada** 📊
```bash
# 6. Obtener información de canción
GET /api/spotify/track/{track_id}

# 7. Obtener información de artista
GET /api/spotify/artist/{artist_id}

# 8. Obtener álbumes de un artista
GET /api/spotify/artist/{artist_id}/albums
```

#### 4. **Exploración y Descubrimiento** 🎵
```bash
# 9. Obtener canciones populares de artista
GET /api/spotify/artist/{artist_id}/top-tracks?market=US

# 10. Obtener álbumes nuevos
GET /api/spotify/browse/new-releases?limit=20

# 11. Obtener playlists destacadas
GET /api/spotify/browse### 9. **👥 Gestión de Usuarios de la API**

#### Listar Usuarios
**Request:**
```
GET http://localhost:8080/api/usuarios
```

**Response:**
```json
[
  {
    "id": 1,
    "nombre": "Juan Pérez",
    "email": "juan@email.com",
    "fechaRegistro": "2025-06-11T15:30:45.123456",
    "perfil": null,
    "favoritasMusicas": [],
    "favoritosAlbumes": [],
    "favoritosPlaylists": []
  }
]
```

#### Guardar Usuario
**Request:**
```
POST http://localhost:8080/api/usuarios
Content-Type: application/json

{
  "nombre": "María García",
  "email": "maria@email.com",
  "contrasena": "password123"
}
```

**Response:**
```json
{
  "id": 2,
  "nombre": "María García",
  "email": "maria@email.com",
  "contrasena": "password123",
  "fechaRegistro": "2025-06-11T16:45:30.987654",
  "perfil": null,
  "favoritasMusicas": [],
  "favoritosAlbumes": [],
  "favoritosPlaylists": []
}
```# API Music - Backend

## Descripción
API REST desarrollada en Spring Boot para la gestión de usuarios de una aplicación de música. Permite administrar usuarios, perfiles y favoritos (músicas, álbumes y playlists) integrada con Spotify Web API para obtener información musical en tiempo real.

## Tecnologías Utilizadas
- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **MySQL** (Base de datos)
- **Maven** (Gestión de dependencias)
- **Spotify Web API** (Integración musical)

## Estructura del Proyecto
```
src/
├── main/
│   ├── java/
│   │   └── com/example/APIMusic/
│   │       ├── controller/
│   │       │   ├── UsuarioController.java
│   │       │   ├── AuthController.java
│   │       │   └── SpotifyController.java
│   │       ├── dto/
│   │       │   ├── SpotifyTokenDto.java
│   │       │   └── TrackDto.java
│   │       ├── entity/
│   │       │   ├── Usuario.java
│   │       │   ├── PerfilUsuario.java
│   │       │   ├── MusicaFavorita.java
│   │       │   ├── AlbumFavorito.java
│   │       │   └── PlaylistFavorita.java
│   │       ├── repository/
│   │       │   ├── UsuarioRepository.java
│   │       │   ├── MusicaFavoritaRepository.java
│   │       │   ├── AlbumFavoritoRepository.java
│   │       │   └── PlaylistFavoritaRepository.java
│   │       ├── service/
│   │       │   ├── UsuarioService.java
│   │       │   ├── SpotifyAuthService.java
│   │       │   └── SpotifyApiService.java
│   │       └── ApimusicApplication.java
│   └── resources/
│       └── application.properties
```

## Configuración de Base de Datos y Spotify

### application.properties
```properties
# Configuración para MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/apimusic_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Puerto del servidor
server.port=8080

# Configuración de Spotify API
spotify.client.id=tu_client_id_de_spotify
spotify.client.secret=tu_client_secret_de_spotify
spotify.redirect.uri=http://localhost:8080/api/auth/spotify/callback
spotify.auth.url=https://accounts.spotify.com/authorize
spotify.token.url=https://accounts.spotify.com/api/token
spotify.api.url=https://api.spotify.com/v1
```

## Configuración de Spotify

### 📋 **Paso a Paso para Obtener Credenciales de Spotify**

#### 1. Crear cuenta de desarrollador
1. Ve a [Spotify for Developers](https://developer.spotify.com/dashboard)
2. Inicia sesión con tu cuenta de Spotify (o crea una nueva)
3. Acepta los términos de servicio para desarrolladores

#### 2. Crear una aplicación
1. Click en **"Create app"**
2. Completa el formulario:
   - **App name:** `API Music App` (o el nombre que prefieras)
   - **App description:** `API REST para gestión musical integrada con Spotify`
   - **Website:** `http://localhost:8080` (opcional)
   - **Redirect URIs:** `http://localhost:8080/api/auth/spotify/callback`
   - **Which API/SDKs are you planning to use?** Selecciona "Web API"

#### 3. Obtener credenciales
1. Una vez creada la app, ve al **Dashboard**
2. Click en tu aplicación
3. Ve a **"Settings"**
4. Copia el **Client ID** (visible públicamente)
5. Click en **"View client secret"** y copia el **Client Secret** (mantén secreto)

#### 4. Configurar Redirect URIs
En la sección **"Redirect URIs"** asegúrate que esté agregada:
```
http://localhost:8080/api/auth/spotify/callback
```

#### 5. Configurar Scopes (Permisos)
Para funcionalidades completas, tu app necesitará estos scopes:
- `user-read-private` - Leer perfil del usuario
- `user-read-email` - Leer email del usuario  
- `user-library-read` - Leer biblioteca del usuario
- `user-library-modify` - Modificar biblioteca del usuario
- `playlist-read-private` - Leer playlists privadas
- `playlist-read-collaborative` - Leer playlists colaborativas
- `user-top-read` - Leer top tracks y artistas
- `user-read-recently-played` - Leer canciones recientes

## Instalación y Ejecución

### Requisitos previos
- Java 17 o superior
- MySQL
- Maven 3.6+
- Cuenta de desarrollador de Spotify

### Pasos de instalación

1. **Clonar el repositorio**
```bash
git clone <url-del-repositorio>
cd APIMusic
```

2. **Configurar la base de datos**
- Crear una base de datos llamada `apimusic_db`
- Actualizar las credenciales en `application.properties`

3. **Configurar Spotify**
- Obtener credenciales de Spotify Developer Dashboard
- Actualizar las credenciales de Spotify en `application.properties`

4. **Instalar dependencias**
```bash
mvn clean install
```

5. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

6. **Verificar que funciona**
- La aplicación estará disponible en: `http://localhost:8080`
- Endpoint de prueba: `GET http://localhost:8080/api/usuarios`
- Verificar configuración de Spotify: `GET http://localhost:8080/api/auth/verify-config`

## Endpoints Principales

### 📁 Gestión de Usuarios

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/usuarios` | **Listar todos los usuarios** |
| `POST` | `/api/usuarios` | **Guardar nuevo usuario** |
| `GET` | `/api/usuarios/{id}` | Obtener usuario por ID |
| `PUT` | `/api/usuarios/{id}` | Actualizar usuario |
| `DELETE` | `/api/usuarios/{id}` | Eliminar usuario |
| `GET` | `/api/usuarios/email/{email}` | Buscar usuario por email |
| `POST` | `/api/usuarios/login` | Login de usuario |

### 📁 Gestión de Perfil
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/usuarios/{id}/perfil` | Obtener perfil del usuario |
| `PUT` | `/api/usuarios/{id}/perfil` | Actualizar perfil del usuario |

### 📁 Músicas Favoritas
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/usuarios/{id}/favoritos/musicas` | Listar músicas favoritas |
| `POST` | `/api/usuarios/{id}/favoritos/musicas` | Agregar música a favoritos |
| `DELETE` | `/api/usuarios/{id}/favoritos/musicas/{musicaId}` | Eliminar música de favoritos |

### 📁 Álbumes Favoritos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/usuarios/{id}/favoritos/albumes` | Listar álbumes favoritos |
| `POST` | `/api/usuarios/{id}/favoritos/albumes` | Agregar álbum a favoritos |
| `DELETE` | `/api/usuarios/{id}/favoritos/albumes/{albumId}` | Eliminar álbum de favoritos |

### 📁 Playlists Favoritas
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/usuarios/{id}/favoritos/playlists` | Listar playlists favoritas |
| `POST` | `/api/usuarios/{id}/favoritos/playlists` | Agregar playlist a favoritos |
| `DELETE` | `/api/usuarios/{id}/favoritos/playlists/{playlistId}` | Eliminar playlist de favoritos |

### 🎵 Integración con Spotify

#### Autenticación Spotify
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/spotify/auth/url` | Obtener URL de autorización de Spotify |
| `GET` | `/api/auth/spotify/callback` | Callback OAuth de Spotify |
| `POST` | `/api/auth/client-token` | Obtener token de cliente (Client Credentials) |
| `POST` | `/api/auth/client-token-alt` | Método alternativo para token de cliente |
| `GET` | `/api/auth/verify-config` | Verificar configuración de Spotify |

#### API de Búsqueda
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/spotify/search` | Buscar canciones, artistas, álbumes y playlists |
| `GET` | `/api/spotify/search/tracks` | Buscar solo canciones |
| `GET` | `/api/spotify/search/artists` | Buscar solo artistas |
| `GET` | `/api/spotify/search/albums` | Buscar solo álbumes |
| `GET` | `/api/spotify/search/playlists` | Buscar solo playlists |

#### API de Canciones (Tracks)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/spotify/track/{id}` | Obtener información de una canción específica |
| `GET` | `/api/spotify/tracks` | Obtener múltiples canciones por IDs |
| `GET` | `/api/spotify/tracks/features/{id}` | Obtener características de audio de una canción |
| `GET` | `/api/spotify/tracks/analysis/{id}` | Obtener análisis de audio de una canción |

#### API de Artistas
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/spotify/artist/{id}` | Obtener información de un artista |
| `GET` | `/api/spotify/artists` | Obtener múltiples artistas por IDs |
| `GET` | `/api/spotify/artist/{id}/albums` | Obtener álbumes de un artista |
| `GET` | `/api/spotify/artist/{id}/top-tracks` | Obtener canciones populares de un artista |
| `GET` | `/api/spotify/artist/{id}/related-artists` | Obtener artistas relacionados |

#### API de Álbumes
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/spotify/album/{id}` | Obtener información de un álbum |
| `GET` | `/api/spotify/albums` | Obtener múltiples álbumes por IDs |
| `GET` | `/api/spotify/album/{id}/tracks` | Obtener canciones de un álbum |
| `GET` | `/api/spotify/browse/new-releases` | Obtener álbumes nuevos |

#### API de Playlists
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/spotify/playlist/{id}` | Obtener información de una playlist |
| `GET` | `/api/spotify/playlist/{id}/tracks` | Obtener canciones de una playlist |
| `GET` | `/api/spotify/browse/featured-playlists` | Obtener playlists destacadas |
| `GET` | `/api/spotify/browse/category/{id}/playlists` | Obtener playlists por categoría |

#### API de Categorías y Browse
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/spotify/browse/categories` | Listar todas las categorías |
| `GET` | `/api/spotify/browse/category/{id}` | Obtener información de una categoría |
| `GET` | `/api/spotify/browse/recommendations` | Obtener recomendaciones musicales |
| `GET` | `/api/spotify/browse/genres` | Obtener géneros disponibles |

#### API de Usuario (requiere OAuth)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/spotify/me` | Obtener perfil del usuario actual |
| `GET` | `/api/spotify/me/playlists` | Obtener playlists del usuario |
| `GET` | `/api/spotify/me/tracks` | Obtener canciones guardadas del usuario |
| `GET` | `/api/spotify/me/albums` | Obtener álbumes guardados del usuario |
| `GET` | `/api/spotify/me/top/tracks` | Obtener canciones más escuchadas |
| `GET` | `/api/spotify/me/top/artists` | Obtener artistas más escuchados |
| `GET` | `/api/spotify/me/recently-played` | Obtener canciones reproducidas recientemente |

## Ejemplos de Uso Completos

### 1. **🔐 Configuración Inicial y Credenciales**

#### Verificar configuración de Spotify
**Request:**
```
GET http://localhost:8080/api/auth/verify-config
```

**Response:**
```json
{
  "clientIdConfigured": true,
  "clientSecretConfigured": true,
  "redirectUriConfigured": true,
  "authUrl": "https://accounts.spotify.com/authorize",
  "tokenUrl": "https://accounts.spotify.com/api/token",
  "apiUrl": "https://api.spotify.com/v1",
  "status": "Configuration OK"
}
```

#### Obtener token de cliente (para búsquedas públicas)
**Request:**
```
POST http://localhost:8080/api/auth/client-token
Content-Type: application/json
```

**Response:**
```json
{
  "access_token": "BQC7XYZ123abc...",
  "token_type": "Bearer",
  "expires_in": 3600
}
```

### 2. **🎵 Búsquedas Musicales**

#### Búsqueda general (canciones, artistas, álbumes, playlists)
**Request:**
```
GET http://localhost:8080/api/spotify/search?q=Bohemian%20Rhapsody&type=track,artist,album&limit=5
```

**Response:**
```json
{
  "tracks": [
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
      }
    }
  ],
  "artists": [
    {
      "id": "1dfeR4HaWDbWqFHLkxsg1d",
      "name": "Queen",
      "popularity": 86,
      "followers": 28500000,
      "genres": ["classic rock", "glam rock", "rock"],
      "imageUrl": "https://i.scdn.co/image/...",
      "externalUrls": {
        "spotify": "https://open.spotify.com/artist/1dfeR4HaWDbWqFHLkxsg1d"
      }
    }
  ]
}
```

#### Buscar solo canciones
**Request:**
```
GET http://localhost:8080/api/spotify/search/tracks?q=hotel%20california&limit=10
```

#### Buscar solo artistas
**Request:**
```
GET http://localhost:8080/api/spotify/search/artists?q=the%20beatles&limit=5
```

### 3. **👤 Información de Artistas**

#### Obtener información detallada de un artista
**Request:**
```
GET http://localhost:8080/api/spotify/artist/1dfeR4HaWDbWqFHLkxsg1d
```

**Response:**
```json
{
  "id": "1dfeR4HaWDbWqFHLkxsg1d",
  "name": "Queen",
  "popularity": 86,
  "followers": 28500000,
  "genres": ["classic rock", "glam rock", "rock"],
  "imageUrl": "https://i.scdn.co/image/ab67616d0000b273...",
  "externalUrls": {
    "spotify": "https://open.spotify.com/artist/1dfeR4HaWDbWqFHLkxsg1d"
  }
}
```

#### Obtener álbumes de un artista
**Request:**
```
GET http://localhost:8080/api/spotify/artist/1dfeR4HaWDbWqFHLkxsg1d/albums?include_groups=album,single&limit=10
```

#### Obtener canciones populares de un artista
**Request:**
```
GET http://localhost:8080/api/spotify/artist/1dfeR4HaWDbWqFHLkxsg1d/top-tracks?market=US
```

**Response:**
```json
{
  "tracks": [
    {
      "id": "4u7EnebtmKWzUH433cf5Qv",
      "name": "Bohemian Rhapsody",
      "popularity": 78,
      "duration": 355394,
      "album": {
        "id": "6X9k3hgEYpK7YW4lLTgFfP",
        "name": "A Night at the Opera"
      }
    }
  ]
}
```

#### Obtener artistas relacionados
**Request:**
```
GET http://localhost:8080/api/spotify/artist/1dfeR4HaWDbWqFHLkxsg1d/related-artists
```

### 4. **💿 Información de Álbumes**

#### Obtener información de un álbum
**Request:**
```
GET http://localhost:8080/api/spotify/album/6X9k3hgEYpK7YW4lLTgFfP
```

**Response:**
```json
{
  "id": "6X9k3hgEYpK7YW4lLTgFfP",
  "name": "A Night at the Opera",
  "artist": "Queen",
  "releaseDate": "1975-11-21",
  "totalTracks": 12,
  "genres": ["classic rock"],
  "popularity": 73,
  "imageUrl": "https://i.scdn.co/image/...",
  "tracks": [
    {
      "id": "4u7EnebtmKWzUH433cf5Qv",
      "name": "Bohemian Rhapsody",
      "trackNumber": 11,
      "duration": 355394
    }
  ]
}
```

#### Obtener canciones de un álbum
**Request:**
```
GET http://localhost:8080/api/spotify/album/6X9k3hgEYpK7YW4lLTgFfP/tracks?limit=50
```

#### Obtener álbumes nuevos
**Request:**
```
GET http://localhost:8080/api/spotify/browse/new-releases?limit=20&offset=0&country=US
```

### 5. **📋 Playlists**

#### Obtener información de una playlist
**Request:**
```
GET http://localhost:8080/api/spotify/playlist/37i9dQZF1DXcBWIGoYBM5M
```

**Response:**
```json
{
  "id": "37i9dQZF1DXcBWIGoYBM5M",
  "name": "Today's Top Hits",
  "description": "The most played songs right now",
  "owner": "Spotify",
  "public": true,
  "followers": 29000000,
  "imageUrl": "https://i.scdn.co/image/...",
  "tracks": {
    "total": 50,
    "items": [...]
  }
}
```

#### Obtener canciones de una playlist
**Request:**
```
GET http://localhost:8080/api/spotify/playlist/37i9dQZF1DXcBWIGoYBM5M/tracks?limit=50&offset=0
```

#### Obtener playlists destacadas
**Request:**
```
GET http://localhost:8080/api/spotify/browse/featured-playlists?limit=20&country=US
```

### 6. **🎼 Canciones (Tracks)**

#### Obtener información detallada de una canción
**Request:**
```
GET http://localhost:8080/api/spotify/track/4u7EnebtmKWzUH433cf5Qv
```

#### Obtener múltiples canciones
**Request:**
```
GET http://localhost:8080/api/spotify/tracks?ids=4u7EnebtmKWzUH433cf5Qv,7qiZfU4dY1lWllzX7mPBI3,1s6ux0lNiTziSrd7iUAADH
```

#### Obtener características de audio de una canción
**Request:**
```
GET http://localhost:8080/api/spotify/tracks/features/4u7EnebtmKWzUH433cf5Qv
```

**Response:**
```json
{
  "id": "4u7EnebtmKWzUH433cf5Qv",
  "danceability": 0.279,
  "energy": 0.434,
  "key": 7,
  "loudness": -17.831,
  "mode": 0,
  "speechiness": 0.105,
  "acousticness": 0.373,
  "instrumentalness": 0.176,
  "liveness": 0.161,
  "valence": 0.279,
  "tempo": 72.038,
  "timeSignature": 4
}
```

### 7. **🔍 Exploración y Categorías**

#### Obtener categorías musicales
**Request:**
```
GET http://localhost:8080/api/spotify/browse/categories?limit=20&country=US
```

#### Obtener playlists de una categoría
**Request:**
```
GET http://localhost:8080/api/spotify/browse/category/toplists/playlists?limit=20&country=US
```

#### Obtener recomendaciones musicales
**Request:**
```
GET http://localhost:8080/api/spotify/browse/recommendations?seed_artists=1dfeR4HaWDbWqFHLkxsg1d&seed_genres=rock&seed_tracks=4u7EnebtmKWzUH433cf5Qv&limit=20
```

### 8. **👤 Datos del Usuario (Requiere OAuth)**

#### Obtener URL de autorización
**Request:**
```
GET http://localhost:8080/api/spotify/auth/url
```

**Response:**
```json
{
  "authUrl": "https://accounts.spotify.com/authorize?client_id=abc123&response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fapi%2Fauth%2Fspotify%2Fcallback&scope=user-read-private%20user-read-email%20user-library-read&state=xyz789",
  "state": "xyz789"
}
```

#### Obtener perfil del usuario (después del OAuth)
**Request:**
```
GET http://localhost:8080/api/spotify/me
Authorization: Bearer USER_ACCESS_TOKEN
```

#### Obtener canciones guardadas del usuario
**Request:**
```
GET http://localhost:8080/api/spotify/me/tracks?limit=50&offset=0
Authorization: Bearer USER_ACCESS_TOKEN
```

#### Obtener top artists del usuario
**Request:**
```
GET http://localhost:8080/api/spotify/me/top/artists?time_range=medium_term&limit=20
Authorization: Bearer USER_ACCESS_TOKEN
```

#### Obtener canciones reproducidas recientemente
**Request:**
```
GET http://localhost:8080/api/spotify/me/recently-played?limit=20
Authorization: Bearer USER_ACCESS_TOKEN
```

## Modelos de Datos

### Usuario
```json
{
  "id": "Long",
  "nombre": "String",
  "email": "String (único)",
  "contrasena": "String",
  "fechaRegistro": "LocalDateTime",
  "perfil": "PerfilUsuario",
  "favoritasMusicas": "List<MusicaFavorita>",
  "favoritosAlbumes": "List<AlbumFavorito>",
  "favoritosPlaylists": "List<PlaylistFavorita>"
}
```

### PerfilUsuario
```json
{
  "id": "Long",
  "fotoUrl": "String",
  "bio": "String",
  "usuario": "Usuario"
}
```

### MusicaFavorita
```json
{
  "id": "Long",
  "spotifyTrackId": "String",
  "fechaAgregado": "LocalDateTime",
  "usuario": "Usuario"
}
```

### AlbumDto (Spotify)
```json
{
  "id": "String",
  "name": "String",
  "artist": "String",
  "releaseDate": "String",
  "totalTracks": "Integer",
  "genres": "List<String>",
  "popularity": "Integer",
  "imageUrl": "String",
  "externalUrls": "Map<String, String>",
  "tracks": "List<TrackDto>"
}
```

### ArtistDto (Spotify)
```json
{
  "id": "String",
  "name": "String",
  "popularity": "Integer",
  "followers": "Integer",
  "genres": "List<String>",
  "imageUrl": "String",
  "externalUrls": "Map<String, String>"
}
```

### PlaylistDto (Spotify)
```json
{
  "id": "String",
  "name": "String",
  "description": "String",
  "owner": "String",
  "public": "Boolean",
  "followers": "Integer",  
  "imageUrl": "String",
  "externalUrls": "Map<String, String>",
  "tracks": "TracksPagingDto"
}
```

### AudioFeaturesDto (Spotify)
```json
{
  "id": "String",
  "danceability": "Float",
  "energy": "Float",
  "key": "Integer",
  "loudness": "Float",
  "mode": "Integer",
  "speechiness": "Float",
  "acousticness": "Float",
  "instrumentalness": "Float",
  "liveness": "Float",
  "valence": "Float",
  "tempo": "Float",
  "timeSignature": "Integer"
}
```

### SearchResultDto (Spotify)
```json
{
  "tracks": "List<TrackDto>",
  "artists": "List<ArtistDto>",
  "albums": "List<AlbumDto>",
  "playlists": "List<PlaylistDto>",
  "total": "Integer"
}
```

### SpotifyTokenDto
```json
{
  "accessToken": "String",
  "tokenType": "String",
  "expiresIn": "Integer",
  "refreshToken": "String",
  "scope": "String"
}
```

## Flujo de Autenticación con Spotify

### Flujo OAuth 2.0 (Authorization Code)
1. **Obtener URL de autorización:** `GET /api/spotify/auth/url`
2. **Redirigir al usuario a Spotify** usando la URL obtenida
3. **Usuario autoriza la aplicación** en Spotify
4. **Spotify redirige de vuelta** a `/api/auth/spotify/callback?code=...`
5. **Intercambiar código por token** automáticamente
6. **Usar el token** para acceder a la API de Spotify

### Flujo Client Credentials (Solo lectura)
1. **Obtener token de cliente:** `POST /api/auth/client-token`
2. **Usar el token** para búsquedas y obtener información pública

## Códigos de Estado HTTP

| Código | Descripción |
|--------|-------------|
| `200 OK` | Operación exitosa |
| `201 Created` | Recurso creado exitosamente |
| `204 No Content` | Eliminación exitosa |
| `400 Bad Request` | Datos inválidos |
| `401 Unauthorized` | Token de Spotify inválido o expirado |
| `403 Forbidden` | Acceso denegado |
| `404 Not Found` | Recurso no encontrado |
| `429 Too Many Requests` | Límite de rate de Spotify excedido |
| `500 Internal Server Error` | Error del servidor |

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