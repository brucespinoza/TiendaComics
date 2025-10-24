package com.example.tiendacomic.data.repositorio

import com.example.tiendacomic.data.local.usuario.UsuarioDao
import com.example.tiendacomic.data.local.usuario.UsuarioEntity

class UsuarioRepository(
    private val usuarioDao: UsuarioDao
) {

    // //inicio de sesion
    suspend fun login(correo: String, pass: String): Result<UsuarioEntity> {
        val user = usuarioDao.obtenerPorCorreo(correo)
        return if (user != null && user.contrasena == pass) {
            Result.success(user)
        } else {
            Result.failure(IllegalArgumentException("Credenciales inválidas"))
        }
    }

    // //registrar un usuario nuevo
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
}
