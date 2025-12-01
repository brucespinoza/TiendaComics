package com.example.tiendacomic.data.remote

import retrofit2.Response
import retrofit2.http.*

/**
 * Interface para conectar con el microservicio Pedido (puerto 8083)
 */
interface PedidoApi {

    // ==================== MEMBRESIAS ====================

    @POST("membresias/activar")
    suspend fun activarMembresia(@Query("usuarioId") usuarioId: Long): Response<MembresiaDto>

    @GET("membresias/usuario/{usuarioId}")
    suspend fun obtenerMembresia(@Path("usuarioId") usuarioId: Long): Response<MembresiaDto>

    @GET("membresias/verificar/{usuarioId}")
    suspend fun verificarVip(@Path("usuarioId") usuarioId: Long): Response<Boolean>

    @PUT("membresias/renovar/{usuarioId}")
    suspend fun renovarMembresia(@Path("usuarioId") usuarioId: Long): Response<MembresiaDto>
}

/**
 * DTO para membresía
 */
data class MembresiaDto(
    val id: Long,
    val usuarioId: Long,
    val tipo: String,
    val fechaInicio: String?,
    val fechaExpiracion: String?,
    val activa: Boolean,
    val precio: Int
)



