package com.example.tiendacomic.data.remote

import com.example.tiendacomic.data.remote.dto.ExchangeRateDto
import retrofit2.Response
import retrofit2.http.GET

/**
 * API EXTERNA - Servicio de tipo de cambio
 * 
 * Esta es una API de TERCEROS (exchangerate-api.com)
 * Convierte pesos chilenos (CLP) a otras monedas
 */
interface ExchangeRateApi {

    /**
     * Obtiene las tasas de cambio actuales desde CLP
     * Endpoint: https://api.exchangerate-api.com/v4/latest/CLP
     */
    @GET("v4/latest/CLP")
    suspend fun obtenerTasasCambio(): Response<ExchangeRateDto>
}



