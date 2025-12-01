package com.example.tiendacomic.data.remote.dto

import com.example.tiendacomic.data.local.usuario.UsuarioEntity

/**
 * DTO para recibir datos de usuario desde la API
 */
data class UsuarioDto(
    val id: Long,
    val nombre: String,
    val rut: String,
    val correo: String,
    val rol: String,
    val fotoPerfil: String? = null,
    val activo: Boolean? = true
) {
    // Convertir DTO a Entity
    fun toEntity(contrasena: String = ""): UsuarioEntity {
        return UsuarioEntity(
            id = id,
            nombre = nombre,
            rut = rut,
            correo = correo,
            contrasena = contrasena,
            rol = rol
        )
    }
}

/**
 * Request para login
 */
data class LoginRequest(
    val correo: String,
    val contrasena: String
)

/**
 * Request para registro
 */
data class RegistroRequest(
    val nombre: String,
    val rut: String,
    val correo: String,
    val contrasena: String
)

/**
 * Request para actualizar perfil
 */
data class ActualizarUsuarioRequest(
    val nombre: String?,
    val correo: String?,
    val fotoPerfil: String? = null
)

/**
 * Request para cambiar contraseña
 */
data class CambiarContrasenaRequest(
    val contrasenaActual: String,
    val nuevaContrasena: String
)
