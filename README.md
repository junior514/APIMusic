# API Music - Guía Básica

## **¿Qué es?**
API en **Spring Boot** para gestionar usuarios y buscar música con **Spotify**.

##  **Tecnologías**
Java 17 | Spring Boot | MySQL | Spotify API

##  **Configuración**
```properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/apimusic_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña

# Spotify
spotify.client.id=tu_client_id
spotify.client.secret=tu_client_secret
```

##  **Instalación**
```bash
git clone <repo>
CREATE DATABASE apimusic_db;
mvn clean install
mvn spring-boot:run
```

##  **Endpoints**

### ** Usuarios**
- `GET /api/usuarios` - Listar usuarios
- `POST /api/usuarios` - Crear usuario

### ** Spotify**
- `GET /api/spotify/search?q=cancion` - Buscar música
- `POST /api/auth/client-token` - Obtener token

##  **Ejemplos**

### Crear Usuario
```bash
POST http://localhost:8080/api/usuarios
{
  "nombre": "Juan",
  "email": "juan@email.com",
  "contrasena": "123456"
}
```

### Buscar Música
```bash
GET http://localhost:8080/api/spotify/search?q=Queen
```

##  **Documentación Postman**

### [**COLECCIÓN DE POSTMAN - API SPOTIFY**](https://documenter.getpostman.com/view/40843950/2sB2x6kBmo#7b715c79-a01d-435f-8adf-2eb1a4fef6fc)

### [**COLECCIÓN DE POSTMAN - API MUSIC CON SPOTIFY**](https://documenter.getpostman.com/view/40843950/2sB2x6kBmo#7b715c79-a01d-435f-8adf-2eb1a4fef6fc)

---
** ¡Listo para usar!**
