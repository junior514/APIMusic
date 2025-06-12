# API Music - Backend

## 📋 Descripción
API REST desarrollada en **Spring Boot** para la gestión de usuarios de una aplicación de música. Permite administrar usuarios, perfiles y favoritos (músicas, álbumes y playlists) con integración completa a **Spotify Web API** para obtener información musical en tiempo real.

## 🛠️ Tecnologías Utilizadas
- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **MySQL** (Base de datos)
- **Maven** (Gestión de dependencias)
- **Spotify Web API** (Integración musical)

## 📁 Estructura del Proyecto
```
src/
├── main/
│   ├── java/
│   │   └── com/example/APIMusic/
│   │       ├── config/
│   │       │   └── SpotifyConfig.java
│   │       ├── controller/
│   │       │   ├── AuthController.java
│   │       │   ├── SpotifyController.java
│   │       │   └── UsuarioController.java
│   │       ├── dto/
│   │       │   ├── AlbumDto.java
│   │       │   ├── ArtistDto.java
│   │       │   ├── ExternalUrlDto.java
│   │       │   ├── ImageDto.java
│   │       │   ├── SpotifyTokenDto.java
│   │       │   └── TrackDto.java
│   │       ├── entity/
│   │       │   ├── AlbumFavorito.java
│   │       │   ├── MusicaFavorita.java
│   │       │   ├── PerfilUsuario.java
│   │       │   ├── PlaylistFavorita.java
│   │       │   └── Usuario.java
│   │       ├── repository/
│   │       │   ├── AlbumFavoritoRepository.java
│   │       │   ├── MusicaFavoritaRepository.java
│   │       │   ├── PlaylistFavoritaRepository.java
│   │       │   └── UsuarioRepository.java
│   │       ├── service/
│   │       │   ├── SpotifyApiService.java
│   │       │   ├── SpotifyAuthService.java
│   │       │   └── UsuarioService.java
│   │       └── ApimusicApplication.java
│   └── resources/
│       └── application.properties
```

## ⚙️ Configuración

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

### 🎵 Configuración de Spotify API

#### Obtener credenciales de Spotify:
1. Ve a [Spotify for Developers](https://developer.spotify.com/dashboard)
2. Crea una nueva aplicación
3. Configura la **Redirect URI**: `http://localhost:8080/api/auth/spotify/callback`
4. Copia el **Client ID** y **Client Secret**
5. Actualiza las credenciales en `application.properties`

## 🚀 Instalación y Ejecución

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
```sql
CREATE DATABASE apimusic_db;
```

3. **Configurar credenciales**
- Actualizar `application.properties` con tus credenciales de MySQL y Spotify

4. **Instalar dependencias**
```bash
mvn clean install
```

5. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

6. **Verificar funcionamiento**
- Aplicación disponible en: `http://localhost:8080`
- Verificar configuración: `GET http://localhost:8080/api/auth/verify-config`

## 📋 Endpoints Disponibles

### 🔐 Autenticación con Spotify (`/api/auth`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/auth/spotify/callback` | Callback OAuth de Spotify |
| `POST` | `/api/auth/client-token` | Obtener token de cliente |
| `POST` | `/api/auth/client-token-alt` | Método alternativo para token |
| `GET` | `/api/auth/verify-config` | Verificar configuración de Spotify |

### 🎵 API de Spotify (`/api/spotify`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/spotify/search` | Buscar canciones por query |
| `GET` | `/api/spotify/track/{id}` | Obtener información de una canción |
| `GET` | `/api/spotify/top-tracks` | Obtener canciones populares |
| `GET` | `/api/spotify/auth/url` | Obtener URL de autorización |

### 👥 Gestión de Usuarios (`/api/usuarios`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/usuarios` | **Listar todos los usuarios** |
| `POST` | `/api/usuarios` | **Guardar nuevo usuario** |
| `GET` | `/api/usuarios/{id}` | Obtener usuario por ID |
| `PUT` | `/api/usuarios/{id}` | Actualizar usuario |
| `DELETE` | `/api/usuarios/{id}` | Eliminar usuario |
| `GET` | `/api/usuarios/email/{email}` | Buscar usuario por email |
| `POST` | `/api/usuarios/login` | Login de usuario |

### 👤 Gestión de Perfil

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/usuarios/{id}/perfil` | Obtener perfil del usuario |
| `PUT` | `/api/usuarios/{id}/perfil` | Actualizar perfil del usuario |

### ⭐ Gestión de Favoritos

#### Músicas Favoritas
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/usuarios/{id}/favoritos/musicas` | Listar músicas favoritas |
| `POST` | `/api/usuarios/{id}/favoritos/musicas` | Agregar música a favoritos |
| `DELETE` | `/api/usuarios/{id}/favoritos/musicas/{musicaId}` | Eliminar música de favoritos |

#### Álbumes Favoritos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/usuarios/{id}/favoritos/albumes` | Listar álbumes favoritos |
| `POST` | `/api/usuarios/{id}/favoritos/albumes` | Agregar álbum a favoritos |
| `DELETE` | `/api/usuarios/{id}/favoritos/albumes/{albumId}` | Eliminar álbum de favoritos |

#### Playlists Favoritas
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/usuarios/{id}/favoritos/playlists` | Listar playlists favoritas |
| `POST` | `/api/usuarios/{id}/favoritos/playlists` | Agregar playlist a favoritos |
| `DELETE` | `/api/usuarios/{id}/favoritos/playlists/{playlistId}` | Eliminar playlist de favoritos |

## 📄 Ejemplos de Uso

### 1. Listar Usuarios (GET)
```bash
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

### 2. Guardar Usuario (POST)
```bash
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
  "fechaRegistro": "2025-06-11T16:45:30.987654",
  "perfil": null,
  "favoritasMusicas": [],
  "favoritosAlbumes": [],
  "favoritosPlaylists": []
}
```

### 3. Buscar Canciones en Spotify
```bash
GET http://localhost:8080/api/spotify/search?q=Bohemian%20Rhapsody&limit=10
```

**Response:**
```json
[
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
]
```

### 4. Obtener Token de Spotify
```bash
POST http://localhost:8080/api/auth/client-token
```

**Response:**
```json
{
  "access_token": "BQC7XYZ123abc..."
}
```

## 📊 Modelos de Datos

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

### TrackDto (Spotify)
```json
{
  "id": "String",
  "name": "String",
  "artist": "String",
  "album": "String",
  "duration": "Integer",
  "popularity": "Integer",
  "previewUrl": "String",
  "imageUrl": "String",
  "externalUrls": "Map<String, String>"
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

## 📋 Documentación con Postman

### 🔗 [**COLECCIÓN DE POSTMAN - API SPOTIFY**](https://documenter.getpostman.com/view/40843950/2sB2x6kBmo#7b715c79-a01d-435f-8adf-2eb1a4fef6fc)

### 🔗 [**COLECCIÓN DE POSTMAN - API MUSIC CON SPOTIFY**](https://documenter.getpostman.com/view/40843950/2sB2x6kBmo#7b715c79-a01d-435f-8adf-2eb1a4fef6fc)


La colección incluye:
- ✅ Todos los endpoints de **Usuarios** (Listar y Guardar principales)
- ✅ Endpoints de **Autenticación con Spotify**
- ✅ Endpoints de **Búsqueda Musical**
- ✅ Endpoints de **Gestión de Favoritos**
- ✅ Ejemplos de requests y responses
- ✅ Variables de entorno configuradas

### Configuración en Postman:
1. **Importar la colección** usando el enlace
2. **Crear entorno** con las variables:
   - `base_url`: `http://localhost:8080`
   - `user_id`: `1` (para pruebas)
3. **Ejecutar los endpoints** en el orden sugerido

## 🔧 Orden Recomendado de Implementación

### 1. Configuración Básica
```bash
GET /api/auth/verify-config
POST /api/auth/client-token
```

### 2. Gestión de Usuarios (Principales)
```bash
GET /api/usuarios          # Listar usuarios
POST /api/usuarios         # Guardar usuario
```

### 3. Búsquedas Musicales
```bash
GET /api/spotify/search?q=cancion&limit=10
GET /api/spotify/track/{id}
```

### 4. Gestión de Favoritos
```bash
GET /api/usuarios/{id}/favoritos/musicas
POST /api/usuarios/{id}/favoritos/musicas
```

## 🔍 Códigos de Estado HTTP

| Código | Descripción |
|--------|-------------|
| `200 OK` | Operación exitosa |
| `201 Created` | Recurso creado exitosamente |
| `204 No Content` | Eliminación exitosa |
| `400 Bad Request` | Datos inválidos |
| `401 Unauthorized` | Token de Spotify inválido |
| `404 Not Found` | Recurso no encontrado |
| `500 Internal Server Error` | Error del servidor |

## 🛠️ Desarrollo

### Ejecutar en modo desarrollo:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Ejecutar tests:
```bash
mvn test
```

## 📞 Soporte

Si tienes problemas con la configuración:

1. **Verificar configuración de Spotify:**
   ```bash
   GET http://localhost:8080/api/auth/verify-config
   ```

2. **Revisar logs del servidor** para errores específicos

3. **Verificar que la base de datos esté corriendo**

## 🎯 Funcionalidades Principales

- ✅ **Listar usuarios** - Endpoint principal para obtener todos los usuarios
- ✅ **Guardar usuarios** - Endpoint principal para crear nuevos usuarios
- ✅ **Integración completa con Spotify** - Búsqueda y obtención de música
- ✅ **Gestión de favoritos** - Músicas, álbumes y playlists
- ✅ **Autenticación OAuth 2.0** con Spotify
- ✅ **API RESTful** completa con CRUD operations

---


**🚀 ¡La API está lista para ser consumida por tu frontend!** 🎵