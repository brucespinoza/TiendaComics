package com.example.tiendacomic.data.local.usuario

/**
 * Modelo de datos para Usuario (sin SQLite)
 */
data class UsuarioEntity(
    val id: Long = 0L,
    val nombre: String,
    val rut: String,
    val correo: String,
    val contrasena: String,
    val rol: String = "usuario"
    // Puede ser usuario, premium o admin
)
