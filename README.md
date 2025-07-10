# API Music - Guía Básica

## **¿Qué es?**
API en **Spring Boot** para gestionar usuarios y buscar música con **Spotify**.

## **Tecnologías**
Java 17 | Spring Boot | MySQL | Spotify API

## **Configuración**
```properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/apimusic_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña

# Spotify
spotify.client.id=tu_client_id
spotify.client.secret=tu_client_secret
```

## **Instalación**
```bash
git clone https://github.com/tu-usuario/api-music.git
cd api-music
CREATE DATABASE apimusic_db;
mvn clean install
mvn spring-boot:run
```

## **Endpoints**

### **👥 Usuarios**
- `GET /api/usuarios` - Listar usuarios
- `POST /api/usuarios` - Crear usuario

### **🎵 Canciones**
- `GET /api/canciones` - Listar todas las canciones
- `GET /api/canciones/con-preview` - Canciones con preview
- `GET /api/canciones/{id}` - Obtener canción por ID
- `POST /api/canciones` - Crear nueva canción
- `PUT /api/canciones/{id}` - Actualizar canción
- `DELETE /api/canciones/{id}` - Eliminar canción
- `GET /api/canciones/buscar/nombre` - Buscar por nombre
- `GET /api/canciones/buscar/artista` - Buscar por artista
- `GET /api/canciones/buscar/album` - Buscar por álbum

### **⭐ Favoritos**
- `GET /api/favoritos` - Obtener favoritos del usuario
- `POST /api/favoritos/{cancionId}` - Agregar a favoritos
- `DELETE /api/favoritos/{cancionId}` - Quitar de favoritos
- `GET /api/favoritos/verificar/{cancionId}` - Verificar si es favorito
- `GET /api/favoritos/estadisticas` - Estadísticas de favoritos

### **🎧 Spotify**
- `GET /api/spotify/buscar-tiempo-real?query=cancion` - Búsqueda en tiempo real
- `GET /api/spotify/buscar-tiempo-real/con-preview` - Búsqueda con preview
- `GET /api/spotify/recomendaciones` - Obtener recomendaciones
- `POST /api/spotify/cargar-con-preview-garantizado` - Cargar canciones con preview
- `POST /api/spotify/canciones/cargar-todas` - Carga masiva de canciones
- `GET /api/spotify/estadisticas-preview` - Estadísticas de preview
- `DELETE /api/spotify/limpiar-sin-preview` - Limpiar canciones sin preview

#### **Endpoints Públicos (Testing)**
- `GET /api/spotify/debug-preview-public` - Debug público de preview
- `POST /api/spotify/cargar-preview-publico` - Carga pública de testing
- `GET /api/spotify/debug-spotify-raw` - Debug raw de Spotify
- `GET /api/spotify/test-multiple-artists` - Test múltiples artistas
- `GET /api/spotify/verify-spotify-config` - Verificar configuración
- `GET /api/spotify/health` - Estado de la API

### **🔐 Autenticación**
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/client-token` - Obtener token

## **Ejemplos**

### Crear Usuario
```bash
POST http://localhost:8080/api/usuarios
{
  "nombre": "Junior",
  "email": "junior@gmail.com",
  "contrasena": "123456"
}
```

### Buscar Música en Tiempo Real
```bash
GET http://localhost:8080/api/spotify/buscar-tiempo-real?query=Queen
Authorization: Bearer tu_token
```

### Agregar a Favoritos
```bash
POST http://localhost:8080/api/favoritos/123
Authorization: Bearer tu_token
```

### Obtener Recomendaciones
```bash
GET http://localhost:8080/api/spotify/recomendaciones
Authorization: Bearer tu_token
```

## **Documentación Postman**

### [**COLECCIÓN DE POSTMAN - API SPOTIFY**](https://documenter.getpostman.com/view/40843950/2sB2x6kBmo#7b715c79-a01d-435f-8adf-2eb1a4fef6fc)

### [**COLECCIÓN DE POSTMAN - API MUSIC CON SPOTIFY**](https://documenter.getpostman.com/view/40843950/2sB2x6kBmo#7b715c79-a01d-435f-8adf-2eb1a4fef6fc)

---
**🎵 ¡Listo para usar!**









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

## 🎥 Video Demostrativo

**[Enlace al video demo - IMPORTANTE INCLUIR]**
**https://isise.sharepoint.com/:v:/s/EvaluacionEA2_ByUser/EWAx1DRAnYdAjr0xfsB3rGcBv_49p9EYwI5wcXQ2tygaOg?e=EErdX0&nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJTdHJlYW1XZWJBcHAiLCJyZWZlcnJhbFZpZXciOiJTaGFyZURpYWxvZy1MaW5rIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXcifX0%3D**
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

## 👥 Equipo de Desarrollo

| Integrante | Rol Principal | Contribuciones Técnicas |
|------------|---------------|------------------------|
| **PAICO VALVERDE JUNIOR** | Backend Lead & Security | • Implementación del sistema de autenticación con Spring Security<br>• Configuración de roles y protección de rutas<br>• Desarrollo de controllers de administración<br>• Manejo de sesiones y validaciones de seguridad |
| **PAICO VALVERDE BRAYAN** | Frontend Developer & UI/UX | • Desarrollo de templates con Thymeleaf<br>• Implementación del diseño responsive con Bootstrap<br>• Creación de formularios y interfaces de usuario<br>• Optimización de la experiencia de usuario |
| **García Bracho Maickel Adrian** | Database & Backend Services | • Diseño y optimización de la base de datos<br>• Implementación de servicios y repositorios JPA<br>• CRUD completo de entidades (Usuarios y Canciones)<br>• Configuración de conexiones y persistencia |
| **Gotto Santa Cruz Thomy Cristopher** | API Integration & External Services | • Integración con Spotify Web API<br>• Desarrollo del servicio de búsqueda musical<br>• Manejo de APIs externas y RestTemplate<br>• Implementación de funcionalidades de importación de canciones |
| **Frans Espinoza Pilco** | Dashboard & Analytics | • Desarrollo del dashboard de estadísticas<br>• Implementación de reportes y métricas en tiempo real<br>• Creación de vistas de resumen y análisis<br>• Testing y documentación del proyecto |
