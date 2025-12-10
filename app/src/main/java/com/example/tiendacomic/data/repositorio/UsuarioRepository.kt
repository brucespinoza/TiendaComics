package com.example.tiendacomic.data.repositorio

import com.example.tiendacomic.data.local.usuario.UsuarioEntity
import com.example.tiendacomic.data.remote.ApiConfig
import com.example.tiendacomic.data.remote.dto.*

/**
 * Repositorio de Usuario - Conecta con el microservicio Usuario (puerto 8081)
 * 
 * NOTA: Este repositorio ahora usa la API REST en lugar de Room
 */
class UsuarioRepository {

    private val api = ApiConfig.usuarioApi

    // Variable para guardar la contraseña temporalmente (la API no la devuelve por seguridad)
    private var contrasenaActual: String = ""

    // ------------------ LOGIN ------------------
    suspend fun login(correo: String, pass: String): Result<UsuarioEntity> {
        return try {
            val response = api.login(LoginRequest(correo, pass))
            if (response.isSuccessful && response.body() != null) {
                contrasenaActual = pass // Guardamos la contraseña para uso local
                val usuario = response.body()!!.toEntity(pass)
                Result.success(usuario)
            } else {
                Result.failure(IllegalArgumentException("Credenciales inválidas"))
            }
        } catch (e: Exception) {
            Result.failure(IllegalArgumentException("Error de conexión: ${e.message}"))
        }
    }

    // ------------------ REGISTRO ------------------
    suspend fun registro(nombre: String, rut: String, correo: String, pass: String): Result<Long> {
        return try {
            val request = RegistroRequest(nombre, rut, correo, pass)
            val response = api.registro(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.id)
            } else {
                Result.failure(IllegalArgumentException("Error al registrar: correo o RUT ya en uso"))
            }
        } catch (e: Exception) {
            Result.failure(IllegalArgumentException("Error de conexión: ${e.message}"))
        }
    }

    // ------------------ ACTUALIZAR PERFIL ------------------
    suspend fun actualizarPerfil(correo: String, nuevoNombre: String, nuevoCorreo: String): Result<Unit> {
        return try {
            // Primero obtenemos el usuario por correo para tener su ID
            val userResponse = api.obtenerPorCorreo(correo)
            if (userResponse.isSuccessful && userResponse.body() != null) {
                val userId = userResponse.body()!!.id
                val request = ActualizarUsuarioRequest(nombre = nuevoNombre, correo = nuevoCorreo)
                val response = api.actualizar(userId, request)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(IllegalArgumentException("Error al actualizar perfil"))
                }
            } else {
                Result.failure(IllegalArgumentException("Usuario no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(IllegalArgumentException("Error de conexión: ${e.message}"))
        }
    }

    // ------------------ CAMBIAR CONTRASEÑA ------------------
    suspend fun actualizarContrasena(correo: String, actual: String, nueva: String): Result<Unit> {
        return try {
            // Primero obtenemos el usuario por correo para tener su ID
            val userResponse = api.obtenerPorCorreo(correo)
            if (userResponse.isSuccessful && userResponse.body() != null) {
                val userId = userResponse.body()!!.id
                val request = CambiarContrasenaRequest(
                    contrasenaActual = actual,
                    nuevaContrasena = nueva
                )
                val response = api.cambiarContrasena(userId, request)
                if (response.isSuccessful) {
                    contrasenaActual = nueva // Actualizamos la contraseña local
                    Result.success(Unit)
                } else {
                    Result.failure(IllegalArgumentException("Contraseña actual incorrecta o error al cambiar contraseña"))
                }
            } else {
                Result.failure(IllegalArgumentException("Usuario no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(IllegalArgumentException("Error de conexión: ${e.message}"))
        }
    }
}
