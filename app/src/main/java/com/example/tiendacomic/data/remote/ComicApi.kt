package com.example.tiendacomic.data.remote

import com.example.tiendacomic.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface para conectar con el microservicio Catalogo (puerto 8082)
 */
interface ComicApi {

    // ==================== COMICS ====================

    @GET("comics")
    suspend fun obtenerTodos(): List<ComicDto>

    @GET("comics/{id}")
    suspend fun obtenerPorId(@Path("id") id: Long): Response<ComicDto>

    @GET("comics/buscar")
    suspend fun buscar(@Query("titulo") titulo: String): List<ComicDto>

    @GET("comics/categoria/{categoria}")
    suspend fun obtenerPorCategoria(@Path("categoria") categoria: String): List<ComicDto>

    @POST("comics")
    suspend fun crear(@Body comic: ComicRequest): Response<ComicDto>

    @PUT("comics/{id}")
    suspend fun actualizar(
        @Path("id") id: Long,
        @Body comic: ComicRequest
    ): Response<ComicDto>

    @DELETE("comics/{id}")
    suspend fun eliminar(@Path("id") id: Long): Response<Unit>
}



