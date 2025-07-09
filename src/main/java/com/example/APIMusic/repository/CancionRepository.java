package com.example.APIMusic.repository;

import com.example.APIMusic.entity.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CancionRepository extends JpaRepository<Cancion, Long> {

    // ========================================
    // MÉTODOS BÁSICOS DE BÚSQUEDA
    // ========================================

    /**
     * Buscar canción por su Spotify Track ID
     */
    Optional<Cancion> findBySpotifyTrackId(String spotifyTrackId);

    /**
     * Verificar si existe una canción por su Spotify Track ID
     */
    boolean existsBySpotifyTrackId(String spotifyTrackId);

    /**
     * Buscar canciones por nombre (ignora mayúsculas/minúsculas)
     */
    @Query("SELECT c FROM Cancion c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Cancion> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);

    /**
     * Buscar canciones por artista (ignora mayúsculas/minúsculas)
     */
    @Query("SELECT c FROM Cancion c WHERE LOWER(c.artistas) LIKE LOWER(CONCAT('%', :artista, '%'))")
    List<Cancion> findByArtistasContainingIgnoreCase(@Param("artista") String artista);

    /**
     * Buscar canciones por álbum (ignora mayúsculas/minúsculas)
     */
    @Query("SELECT c FROM Cancion c WHERE LOWER(c.album) LIKE LOWER(CONCAT('%', :album, '%'))")
    List<Cancion> findByAlbumContainingIgnoreCase(@Param("album") String album);

    // ========================================
    // MÉTODOS PARA PREVIEW - CORREGIDOS
    // ========================================

    /**
     * ✅ CORREGIDO: Obtener canciones que tienen preview URL
     */
    @Query("SELECT c FROM Cancion c WHERE c.previewUrl IS NOT NULL AND c.previewUrl != '' AND c.previewUrl != 'null'")
    List<Cancion> findCancionesWithPreview();

    /**
     * ✅ CORREGIDO: Buscar canciones por nombre con preview disponible
     */
    @Query("SELECT c FROM Cancion c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND c.previewUrl IS NOT NULL AND c.previewUrl != '' AND c.previewUrl != 'null'")
    List<Cancion> findByNombreContainingIgnoreCaseWithPreview(@Param("nombre") String nombre);

    /**
     * ✅ CORREGIDO: Buscar canciones por artista con preview disponible
     */
    @Query("SELECT c FROM Cancion c WHERE LOWER(c.artistas) LIKE LOWER(CONCAT('%', :artista, '%')) AND c.previewUrl IS NOT NULL AND c.previewUrl != '' AND c.previewUrl != 'null'")
    List<Cancion> findByArtistasContainingIgnoreCaseWithPreview(@Param("artista") String artista);

    /**
     * ✅ CORREGIDO: Obtener canciones con preview ordenadas por popularidad
     */
    @Query("SELECT c FROM Cancion c WHERE c.previewUrl IS NOT NULL AND c.previewUrl != '' AND c.previewUrl != 'null' ORDER BY c.popularidad DESC")
    List<Cancion> findCancionesWithPreviewOrderByPopularidad();

    /**
     * ✅ CORREGIDO: Contar canciones con preview
     */
    @Query("SELECT COUNT(c) FROM Cancion c WHERE c.previewUrl IS NOT NULL AND c.previewUrl != '' AND c.previewUrl != 'null'")
    long countCancionesWithPreview();

    /**
     * ✅ CORREGIDO: Contar canciones sin preview
     */
    @Query("SELECT COUNT(c) FROM Cancion c WHERE c.previewUrl IS NULL OR c.previewUrl = '' OR c.previewUrl = 'null'")
    long countCancionesSinPreview();

    /**
     * Obtener canciones ordenadas por popularidad
     */
    List<Cancion> findAllByOrderByPopularidadDesc();

    // ========================================
    // MÉTODOS CON NATIVE QUERIES CORREGIDAS
    // ========================================

    /**
     * ✅ CORREGIDO: Obtener primeras N canciones con preview
     */
    @Query(value = "SELECT * FROM canciones WHERE preview_url IS NOT NULL AND preview_url != '' AND preview_url != 'null' ORDER BY popularidad DESC LIMIT :limite", nativeQuery = true)
    List<Cancion> findTopCancionesWithPreview(@Param("limite") int limite);

    /**
     * ✅ CORREGIDO: Obtener canciones aleatorias con preview (MySQL/MariaDB)
     */
    @Query(value = "SELECT * FROM canciones WHERE preview_url IS NOT NULL AND preview_url != '' AND preview_url != 'null' ORDER BY RAND() LIMIT :limite", nativeQuery = true)
    List<Cancion> findRandomCancionesWithPreview(@Param("limite") int limite);

    /**
     * ✅ ALTERNATIVA para PostgreSQL: Obtener canciones aleatorias con preview
     */
    @Query(value = "SELECT * FROM canciones WHERE preview_url IS NOT NULL AND preview_url != '' AND preview_url != 'null' ORDER BY RANDOM() LIMIT :limite", nativeQuery = true)
    List<Cancion> findRandomCancionesWithPreviewPostgreSQL(@Param("limite") int limite);

    // ========================================
    // MÉTODOS DE POPULARIDAD
    // ========================================

    /**
     * ✅ CORREGIDO: Obtener canciones por popularidad mínima
     */
    @Query("SELECT c FROM Cancion c WHERE c.popularidad >= :popularidadMinima ORDER BY c.popularidad DESC")
    List<Cancion> findByPopularidadGreaterThanEqual(@Param("popularidadMinima") BigDecimal popularidadMinima);

    /**
     * ✅ CORREGIDO: Obtener canciones populares con preview
     */
    @Query("SELECT c FROM Cancion c WHERE c.popularidad >= :popularidadMinima AND c.previewUrl IS NOT NULL AND c.previewUrl != '' AND c.previewUrl != 'null' ORDER BY c.popularidad DESC")
    List<Cancion> findPopularCancionesWithPreview(@Param("popularidadMinima") BigDecimal popularidadMinima);

    // ========================================
    // MÉTODOS DE FILTRADO SIMPLE
    // ========================================

    /**
     * ✅ SIMPLE: Obtener canciones sin preview (para limpieza)
     */
    @Query("SELECT c FROM Cancion c WHERE c.previewUrl IS NULL OR c.previewUrl = '' OR c.previewUrl = 'null'")
    List<Cancion> findCancionesSinPreview();

    /**
     * ✅ SIMPLE: Obtener canciones más recientes
     */
    @Query("SELECT c FROM Cancion c ORDER BY c.createdAt DESC")
    List<Cancion> findAllOrderByCreatedAtDesc();

    /**
     * ✅ SIMPLE: Obtener canciones más recientes con preview
     */
    @Query("SELECT c FROM Cancion c WHERE c.previewUrl IS NOT NULL AND c.previewUrl != '' AND c.previewUrl != 'null' ORDER BY c.createdAt DESC")
    List<Cancion> findRecentCancionesWithPreview();

    /**
     * ✅ SIMPLE: Verificar si existe canción por nombre y artista
     */
    @Query("SELECT COUNT(c) > 0 FROM Cancion c WHERE LOWER(c.nombre) = LOWER(:nombre) AND LOWER(c.artistas) LIKE LOWER(CONCAT('%', :artista, '%'))")
    boolean existsByNombreAndArtista(@Param("nombre") String nombre, @Param("artista") String artista);

    // ========================================
    // MÉTODOS ESPECÍFICOS PARA TU CASO
    // ========================================

    /**
     * ✅ ESPECÍFICO: Obtener solo canciones que definitivamente tienen preview válido
     */
    @Query("SELECT c FROM Cancion c WHERE c.previewUrl IS NOT NULL AND c.previewUrl != '' AND c.previewUrl != 'null' AND c.previewUrl LIKE 'https%'")
    List<Cancion> findCancionesConPreviewValido();

    /**
     * ✅ ESPECÍFICO: Contar canciones con preview válido
     */
    @Query("SELECT COUNT(c) FROM Cancion c WHERE c.previewUrl IS NOT NULL AND c.previewUrl != '' AND c.previewUrl != 'null' AND c.previewUrl LIKE 'https%'")
    long countCancionesConPreviewValido();

    /**
     * ✅ ESPECÍFICO: Buscar por nombre solo con preview válido
     */
    @Query("SELECT c FROM Cancion c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND c.previewUrl IS NOT NULL AND c.previewUrl != '' AND c.previewUrl != 'null' AND c.previewUrl LIKE 'https%'")
    List<Cancion> findByNombreConPreviewValido(@Param("nombre") String nombre);

    /**
     * ✅ ESPECÍFICO: Top 20 canciones con preview válido por popularidad
     */
    @Query(value = "SELECT * FROM canciones WHERE preview_url IS NOT NULL AND preview_url != '' AND preview_url != 'null' AND preview_url LIKE 'https%' ORDER BY popularidad DESC LIMIT 20", nativeQuery = true)
    List<Cancion> findTop20CancionesConPreview();

    /**
     * ✅ ESPECÍFICO: Últimas 50 canciones agregadas con preview
     */
    @Query(value = "SELECT * FROM canciones WHERE preview_url IS NOT NULL AND preview_url != '' AND preview_url != 'null' AND preview_url LIKE 'https%' ORDER BY created_at DESC LIMIT 50", nativeQuery = true)
    List<Cancion> findUltimas50ConPreview();
}
