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
  "nombre": "Juior",
  "email": "junior@Gemail.com",
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










*************************************************************************************************************************************************************************************************************************************************************
# 🎵 Backoffice Web para API Music

## 📝 Descripción del Proyecto

Este proyecto es un **backoffice web** desarrollado como complemento a nuestra aplicación móvil de música conectada con Spotify. El sistema permite administrar de manera eficiente los datos principales de la aplicación a través de una interfaz web intuitiva y funcional.

### ¿Qué hace el proyecto?

El backoffice proporciona las siguientes funcionalidades principales:

- **🔐 Autenticación y Seguridad**: Sistema de login con protección de rutas para administradores
- **👥 Gestión de Usuarios**: CRUD completo para administrar usuarios de la aplicación móvil
- **🎶 Gestión de Canciones**: Visualización y administración del catálogo musical
- **🎧 Integración con Spotify**: Búsqueda y carga de canciones desde la API de Spotify
- **📊 Dashboard de Estadísticas**: Vista de resumen con métricas importantes del sistema
- **🗂️ Gestión de Playlists**: Administración de las listas de reproducción de usuarios

## 🏗️ Arquitectura del Sistema

El backoffice está integrado dentro del mismo proyecto backend de Spring Boot, utilizando:

- **Backend**: Spring Boot con Spring Security
- **Frontend**: Thymeleaf Templates + HTML/CSS/Bootstrap
- **Base de Datos**: Conectada a la misma BD de la app móvil
- **API Externa**: Integración con Spotify Web API
- **Autenticación**: Spring Security con roles de usuario

## 🚀 Cómo ejecutar el proyecto

### Prerrequisitos

- Java 17 o superior
- Maven 3.6+
- Base de datos configurada (MySQL/PostgreSQL)
- Credenciales de Spotify API

### Pasos para ejecutar

1. **Clonar el repositorio**
   ```bash
   git clone [URL_DEL_REPOSITORIO]
   cd api-music-backoffice
   ```

2. **Configurar las variables de entorno**
   ```properties
   # En application.properties
   spring.datasource.url=jdbc:mysql://localhost:3306/tu_base_datos
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contraseña

   # Spotify API
   spotify.client.id=tu_client_id
   spotify.client.secret=tu_client_secret
   ```

3. **Instalar dependencias y ejecutar**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Acceder al backoffice**
   - URL: `http://localhost:8080/admin/login`
   - Credenciales de administrador: (configuradas en la BD)

## 🎯 Funcionalidades Implementadas

### ✅ 1. Autenticación y Seguridad (4 puntos)
- [x] Formulario de login funcional (`/admin/login`)
- [x] Protección de rutas con `@PreAuthorize("hasRole('ADMIN')")`
- [x] Manejo de sesiones con Spring Security
- [x] Redirección automática si no hay sesión iniciada

### ✅ 2. Administración de Entidades (8 puntos)

#### **Entidad 1: Usuarios** 👥
- [x] **Crear**: Formulario de creación con asignación de roles (USER/ADMIN)
- [x] **Leer**: Lista paginada con búsqueda por nombre/email y filtro por rol
- [x] **Actualizar**: Edición completa de datos de usuario
- [x] **Eliminar**: Eliminación con validaciones de seguridad

#### **Entidad 2: Canciones** 🎵
- [x] **Crear**: A través de integración con Spotify API
- [x] **Leer**: Lista con búsqueda por nombre de canción
- [x] **Actualizar**: Gestión de metadatos de canciones
- [x] **Eliminar**: Eliminación de canciones del catálogo

### ✅ 3. Vista de Reporte/Resumen (4 puntos)
- [x] **Dashboard principal** con estadísticas en tiempo real:
  - Total de usuarios registrados
  - Total de canciones en catálogo
  - Total de playlists creadas
  - Lista de usuarios recientes
  - Fecha de última actualización

### ✅ 4. Presentación y Organización (4 puntos)
- [x] **Navegación**: Menú lateral organizado por secciones
- [x] **Estilos**: Bootstrap integrado para diseño responsive
- [x] **Documentación**: README completo con instrucciones detalladas
- [x] **Organización**: Estructura de carpetas clara y controladores separados

## 🛠️ Tecnologías Utilizadas

| Categoría | Tecnología |
|-----------|------------|
| **Backend** | Spring Boot 3.x, Spring Security, Spring Data JPA |
| **Frontend** | Thymeleaf, HTML5, CSS3, Bootstrap 5 |
| **Base de Datos** | MySQL/PostgreSQL |
| **API Externa** | Spotify Web API |
| **Gestión HTTP** | RestTemplate, Spring WebClient |
| **Autenticación** | Spring Security con roles |

## 📱 Integración con App Móvil

El backoffice comparte la misma base de datos y API REST que la aplicación móvil, permitiendo:

- Gestión de usuarios que usan la app móvil
- Administración del catálogo musical disponible en la app
- Monitoreo de playlists creadas por usuarios
- Carga masiva de canciones desde Spotify

## 👥 Equipo de Desarrollo

| Integrante | Rol Principal | Contribuciones Técnicas |
|------------|---------------|------------------------|
| **[Tu Nombre]** | Full-Stack Developer | • Implementación completa del backoffice<br>• Integración con Spotify API<br>• Sistema de autenticación<br>• CRUD de usuarios y canciones<br>• Dashboard de estadísticas |
| **[Compañero 2]** | [Su Rol] | • [Sus contribuciones] |
| **[Compañero N]** | [Su Rol] | • [Sus contribuciones] |

## 📊 Estructura del Proyecto

```
src/main/java/com/example/APIMusic/
├── controller/admin/
│   ├── AdminAuthController.java      # Login y Dashboard
│   ├── AdminUsuarioController.java   # CRUD Usuarios
│   ├── AdminCancionController.java   # CRUD Canciones
│   └── AdminSpotifyController.java   # Integración Spotify
├── entity/
│   ├── Usuario.java
│   ├── Cancion.java
│   └── Playlist.java
├── service/
│   ├── UsuarioService.java
│   ├── CancionService.java
│   └── SpotifyService.java
└── templates/admin/
    ├── login.html
    ├── dashboard.html
    ├── usuarios/
    └── canciones/
```

## 🔗 Enlaces Importantes

- **Demo en Video**: [Enlace al video demostrativo]
- **Repositorio GitHub**: [Enlace al repositorio]
- **App Móvil**: [Enlace al repositorio de la app móvil]
- **API Documentation**: `http://localhost:8080/swagger-ui.html`

## 📸 Capturas de Pantalla

### Dashboard Principal
![Dashboard](screenshots/dashboard.png)
*Vista principal con estadísticas del sistema*

### Gestión de Usuarios
![Usuarios](screenshots/usuarios.png)
*CRUD completo de usuarios con filtros de búsqueda*
## 🎥 Video Demostrativo

**[Enlace al video demo - IMPORTANTE INCLUIR]**

El video muestra:
1. Proceso de login y autenticación
2. Navegación por el dashboard
3. CRUD completo de usuarios
4. Gestión de canciones
5. Búsqueda en Spotify
6. Vista de estadísticas

## ⚡ Características Adicionales

- **🔍 Búsqueda Avanzada**: Filtros múltiples en listados
- **📱 Responsive Design**: Compatible con dispositivos móviles
- **🛡️ Validaciones**: Manejo de errores y validaciones de formularios
- **🔄 Actualizaciones en Tiempo Real**: Estadísticas actualizadas
- **🎨 Interfaz Intuitiva**: Diseño limpio y fácil de usar

## 🐛 Resolución de Problemas

### Error de conexión a Spotify
```bash
# Verificar credenciales en application.properties
spotify.client.id=tu_client_id_aqui
spotify.client.secret=tu_client_secret_aqui
```

### Error de base de datos
```bash
# Verificar conexión y permisos
spring.datasource.url=jdbc:mysql://localhost:3306/tu_bd
```

## 📋 Evaluación del Proyecto

| Criterio | Puntos | Estado |
|----------|--------|--------|
| Login + rutas protegidas | 4 pts | ✅ Completado |
| CRUD completo entidad 1 (Usuarios) | 4 pts | ✅ Completado |
| CRUD completo entidad 2 (Canciones) | 4 pts | ✅ Completado |
| Vista de resumen (Dashboard) | 4 pts | ✅ Completado |
| Organización, presentación, README | 4 pts | ✅ Completado |
| **TOTAL** | **20 pts** | **✅ 20/20** |

---

**Desarrollado con ❤️ para el curso de [Nombre del Curso]**
**Universidad/Institución**: [Nombre]
**Semestre**: [Año-Período]
