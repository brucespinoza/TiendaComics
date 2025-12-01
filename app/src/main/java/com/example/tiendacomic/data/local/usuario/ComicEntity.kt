package com.example.tiendacomic.data.local.usuario

/**
 * Modelo de datos para Comic (sin SQLite)
 */
data class ComicEntity(
    val id: Int = 0,
    val titulo: String,
    val precio: Int,
    val imagen: String,
    val descripcion: String
)
