package com.example.tiendacomic.data.remote.dto

import com.example.tiendacomic.data.local.usuario.ComicEntity

/**
 * DTO para recibir datos de comic desde la API
 */
data class ComicDto(
    val id: Long,
    val titulo: String,
    val descripcion: String?,
    val precio: Int,
    val precioDescuento: Int? = null,
    val imagen: String?,
    val stock: Int? = 0,
    val categoria: String? = null,
    val activo: Boolean? = true
) {
    // Convertir DTO a Entity
    fun toEntity(): ComicEntity {
        return ComicEntity(
            id = id.toInt(),
            titulo = titulo,
            precio = precio,
            imagen = imagen ?: "",
            descripcion = descripcion ?: ""
        )
    }
}

/**
 * Request para crear/actualizar comic
 */
data class ComicRequest(
    val titulo: String,
    val descripcion: String,
    val precio: Int,
    val precioDescuento: Int? = null,
    val imagen: String,
    val stock: Int = 0,
    val categoria: String? = null,
    val activo: Boolean = true
)
