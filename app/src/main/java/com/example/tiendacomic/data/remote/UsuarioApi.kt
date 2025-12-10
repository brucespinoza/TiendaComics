package com.example.tiendacomic.data.remote

import com.example.tiendacomic.data.remote.dto.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface para conectar con el microservicio Usuario (puerto 8081)
 */
interface UsuarioApi {

    // ==================== AUTH ====================

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<UsuarioDto>

    @POST("auth/registro")
    suspend fun registro(@Body request: RegistroRequest): Response<UsuarioDto>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<Unit>

    // ==================== USUARIOS ====================

    @GET("usuarios")
    suspend fun obtenerTodos(): List<UsuarioDto>

    @GET("usuarios/{id}")
    suspend fun obtenerPorId(@Path("id") id: Long): Response<UsuarioDto>

    @GET("usuarios/correo/{correo}")
    suspend fun obtenerPorCorreo(@Path("correo") correo: String): Response<UsuarioDto>

    @PUT("usuarios/{id}")
    suspend fun actualizar(
        @Path("id") id: Long,
        @Body request: ActualizarUsuarioRequest
    ): Response<UsuarioDto>

    @PUT("usuarios/{id}/contrasena")
    suspend fun cambiarContrasena(
        @Path("id") id: Long,
        @Body request: CambiarContrasenaRequest
    ): Response<Unit>

    @PUT("usuarios/{id}/rol")
    suspend fun cambiarRol(
        @Path("id") id: Long,
        @Query("rol") rol: String
    ): Response<UsuarioDto>

    @DELETE("usuarios/{id}")
    suspend fun eliminar(@Path("id") id: Long): Response<Unit>

    // ==================== FOTO ====================

    @Multipart
    @POST("usuarios/{id}/foto")
    suspend fun subirFoto(
        @Path("id") id: Long,
        @Part foto: MultipartBody.Part
    ): Response<Unit>

    @GET("usuarios/{id}/foto")
    suspend fun obtenerFoto(@Path("id") id: Long): Response<ResponseBody>
}



