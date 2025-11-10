package com.example.tiendacomic.data.repositorio

import com.example.tiendacomic.data.local.usuario.UsuarioDao
import com.example.tiendacomic.data.local.usuario.UsuarioEntity

class UsuarioRepository(
    private val usuarioDao: UsuarioDao
) {

    // ------------------ LOGIN ------------------
    suspend fun login(correo: String, pass: String): Result<UsuarioEntity> {
        val user = usuarioDao.obtenerPorCorreo(correo)
        return if (user != null && user.contrasena == pass) {
            Result.success(user)
        } else {
            Result.failure(IllegalArgumentException("Credenciales inválidas"))
        }
    }

    // ------------------ REGISTRO ------------------
    suspend fun registro(nombre: String, rut: String, correo: String, pass: String): Result<Long> {
        val exists = usuarioDao.obtenerPorCorreo(correo) != null
        if (exists) {
            return Result.failure(IllegalArgumentException("Correo en uso"))
        }

        val id = usuarioDao.insertar(
            UsuarioEntity(
                nombre = nombre,
                rut = rut,
                correo = correo,
                contrasena = pass,
                rol = "usuario" // por defecto
            )
        )

        return Result.success(id)
    }

    // ------------------ ACTUALIZAR PERFIL ------------------
    suspend fun actualizarPerfil(correo: String, nuevoNombre: String, nuevoCorreo: String): Result<Unit> {
        val user = usuarioDao.obtenerPorCorreo(correo)
        return if (user != null) {
            val actualizado = user.copy(nombre = nuevoNombre, correo = nuevoCorreo)
            usuarioDao.actualizar(actualizado)
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Usuario no encontrado"))
        }
    }

    // ------------------ CAMBIAR CONTRASEÑA ------------------
    suspend fun actualizarContrasena(correo: String, nueva: String): Result<Unit> {
        val user = usuarioDao.obtenerPorCorreo(correo)
        return if (user != null) {
            val actualizado = user.copy(contrasena = nueva)
            usuarioDao.actualizar(actualizado)
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Usuario no encontrado"))
        }
    }
}
