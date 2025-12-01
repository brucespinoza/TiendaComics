package com.example.tiendacomic.data.local.usuario

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Modelo de datos para Comic
 */
@Entity(tableName = "comic")
data class ComicEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val precio: Int,
    val imagen: String,
    val descripcion: String
)
