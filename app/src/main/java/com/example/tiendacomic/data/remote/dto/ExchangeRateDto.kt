package com.example.tiendacomic.data.remote.dto

/**
 * DTO para la respuesta de la API externa de tipo de cambio
 * API: https://api.exchangerate-api.com
 * 
 * Ejemplo de respuesta:
 * {
 *   "provider": "https://www.exchangerate-api.com",
 *   "base": "CLP",
 *   "date": "2024-01-15",
 *   "rates": {
 *     "USD": 0.00108,
 *     "EUR": 0.00099,
 *     ...
 *   }
 * }
 */
data class ExchangeRateDto(
    val provider: String?,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
) {
    /**
     * Convierte un monto en CLP a USD
     */
    fun convertirAUsd(montoCLP: Int): Double {
        val tasaUsd = rates["USD"] ?: 0.0
        return montoCLP * tasaUsd
    }
    
    /**
     * Convierte un monto en CLP a EUR
     */
    fun convertirAEur(montoCLP: Int): Double {
        val tasaEur = rates["EUR"] ?: 0.0
        return montoCLP * tasaEur
    }
    
    /**
     * Obtiene la tasa de cambio CLP -> USD
     */
    fun getTasaUsd(): Double {
        return rates["USD"] ?: 0.0
    }
}



