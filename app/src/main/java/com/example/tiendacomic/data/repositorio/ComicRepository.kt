package com.example.tiendacomic.data.repositorio

import com.example.tiendacomic.data.local.usuario.ComicEntity
import com.example.tiendacomic.data.remote.ApiConfig
import com.example.tiendacomic.data.remote.dto.ComicRequest

/**
 * Repositorio de Comics - Conecta con el microservicio Catalogo (puerto 8082)
 * 
 * NOTA: Este repositorio ahora usa la API REST en lugar de Room
 */
class ComicRepository  {

    private val api = ApiConfig.comicApi

    // ------------------ OBTENER TODOS ------------------
    suspend fun obtenerTodos(): List<ComicEntity> {
        return try {
            val comics = api.obtenerTodos()
            comics.map { it.toEntity() }
        } catch (e: Exception) {
            // Si hay error de conexión, devolvemos lista vacía
            emptyList()
        }
    }

    // ------------------ INSERTAR ------------------
    suspend fun insertar(comic: ComicEntity) {
        try {
            val request = ComicRequest(
                titulo = comic.titulo,
                descripcion = comic.descripcion,
                precio = comic.precio,
                imagen = comic.imagen,
                stock = 10,
                categoria = "General",
                activo = true
            )
            api.crear(request)
        } catch (e: Exception) {
            // Log error silencioso
        }
    }

    // ------------------ ACTUALIZAR ------------------
    suspend fun actualizar(comic: ComicEntity) {
        try {
            val request = ComicRequest(
                titulo = comic.titulo,
                descripcion = comic.descripcion,
                precio = comic.precio,
                imagen = comic.imagen,
                stock = 10,
                categoria = "General",
                activo = true
            )
            api.actualizar(comic.id.toLong(), request)
        } catch (e: Exception) {
            // Log error silencioso
        }
    }

    // ------------------ ELIMINAR ------------------
    suspend fun eliminar(comic: ComicEntity) {
        try {
            api.eliminar(comic.id.toLong())
        } catch (e: Exception) {
            // Log error silencioso
        }
    }

    // ------------------ BUSCAR ------------------
    suspend fun buscar(titulo: String): List<ComicEntity> {
        return try {
            val comics = api.buscar(titulo)
            comics.map { it.toEntity() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
