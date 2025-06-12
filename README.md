# API Music - Backend

## Descripción
API REST desarrollada en Spring Boot para la gestión de usuarios de una aplicación de música. Permite administrar usuarios, perfiles y favoritos (músicas, álbumes y playlists) integrada con Spotify.

## Tecnologías Utilizadas
- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **MySQL** (Base de datos)
- **Maven** (Gestión de dependencias)

## Estructura del Proyecto
```
src/
├── main/
│   ├── java/
│   │   └── com/example/APIMusic/
│   │       ├── controller/
│   │       │   └── UsuarioController.java
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
│   │       │   └── UsuarioService.java
│   │       └── ApimusicApplication.java
│   └── resources/
│       └── application.properties
```

## Configuración de Base de Datos

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
```

## Instalación y Ejecución

### Requisitos previos
- Java 17 o superior
- MySQL/PostgreSQL
- Maven 3.6+

### Pasos de instalación

1. **Clonar el repositorio**
```bash
git clone <url-del-repositorio>
cd APIMusic
```

2. **Configurar la base de datos**
- Crear una base de datos llamada `apimusic_db`
- Actualizar las credenciales en `application.properties`

3. **Instalar dependencias**
```bash
mvn clean install
```

4. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

5. **Verificar que funciona**
- La aplicación estará disponible en: `http://localhost:8080`
- Endpoint de prueba: `GET http://localhost:8080/api/usuarios`

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

## Ejemplos de Uso

### 1. Listar Usuarios (GET)
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

### 2. Guardar Usuario (POST)
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

## Códigos de Estado HTTP

| Código | Descripción |
|--------|-------------|
| `200 OK` | Operación exitosa |
| `201 Created` | Recurso creado exitosamente |
| `204 No Content` | Eliminación exitosa |
| `400 Bad Request` | Datos inválidos |
| `404 Not Found` | Recurso no encontrado |
| `500 Internal Server Error` | Error del servidor |

## 📋 Documentación con Postman

### 🔗 [**COLECCIÓN DE POSTMAN - API MUSIC**](https://documenter.getpostman.com/view/40843950/2sB2x6kBmo#7b715c79-a01d-435f-8adf-2eb1a4fef6fc)

> **Nota:** Sustituye el enlace anterior con tu enlace real de Postman después de crear y publicar tu colección.

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

3. **Ejecutar las pruebas:**
   - Los endpoints están organizados por carpetas
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

2. **Ejecutar en modo desarrollo:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

3. **Ejecutar tests:**
```bash
mvn test
```

## Contacto y Soporte

- **Desarrollador:** [Tu Nombre]
- **Email:** [tu-email@ejemplo.com]
- **Repositorio:** [enlace-al-repositorio]

---

## Notas Adicionales

- La API usa CORS habilitado para desarrollo (`origins = "*"`)
- Para producción, configurar CORS específico y seguridad adicional
- El login actual es básico; implementar JWT para producción
- Las contraseñas se almacenan en texto plano; implementar encriptación

**¡La API está lista para ser consumida por tu frontend!** 🚀