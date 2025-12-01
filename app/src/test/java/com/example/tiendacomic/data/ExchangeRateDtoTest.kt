package com.example.tiendacomic.data

import com.example.tiendacomic.data.remote.dto.ExchangeRateDto
import org.junit.Assert.*
import org.junit.Test

/**
 * Tests para ExchangeRateDto - API Externa de tipo de cambio
 * Siguiendo el mismo patrón que ValidadorTest
 */
class ExchangeRateDtoTest {

    // ==================== TESTS DE CONVERSIÓN CLP A USD ====================

    @Test
    fun convertirAUsd_precioNormal_calculoCorrecto() {
        val dto = ExchangeRateDto(
            provider = "test",
            base = "CLP",
            date = "2024-01-01",
            rates = mapOf("USD" to 0.00105, "EUR" to 0.00095)
        )
        
        val resultado = dto.convertirAUsd(20000)
        
        assertEquals(21.0, resultado, 0.1)
    }

    @Test
    fun convertirAUsd_precioCero_retornaCero() {
        val dto = ExchangeRateDto(
            provider = "test",
            base = "CLP",
            date = "2024-01-01",
            rates = mapOf("USD" to 0.00105)
        )
        
        val resultado = dto.convertirAUsd(0)
        
        assertEquals(0.0, resultado, 0.001)
    }

    @Test
    fun convertirAUsd_precioAlto_calculoCorrecto() {
        val dto = ExchangeRateDto(
            provider = "test",
            base = "CLP",
            date = "2024-01-01",
            rates = mapOf("USD" to 0.001)
        )
        
        val resultado = dto.convertirAUsd(100000)
        
        assertEquals(100.0, resultado, 0.1)
    }

    @Test
    fun convertirAUsd_precioBajo_calculoCorrecto() {
        val dto = ExchangeRateDto(
            provider = "test",
            base = "CLP",
            date = "2024-01-01",
            rates = mapOf("USD" to 0.001)
        )
        
        val resultado = dto.convertirAUsd(500)
        
        assertEquals(0.5, resultado, 0.01)
    }

    @Test
    fun convertirAUsd_sinTasaUsd_retornaCero() {
        val dto = ExchangeRateDto(
            provider = "test",
            base = "CLP",
            date = "2024-01-01",
            rates = mapOf("EUR" to 0.00095) // Sin USD
        )
        
        val resultado = dto.convertirAUsd(20000)
        
        assertEquals(0.0, resultado, 0.001)
    }

    // ==================== TESTS DE CONVERSIÓN CLP A EUR ====================

    @Test
    fun convertirAEur_precioNormal_calculoCorrecto() {
        val dto = ExchangeRateDto(
            provider = "test",
            base = "CLP",
            date = "2024-01-01",
            rates = mapOf("USD" to 0.00105, "EUR" to 0.00095)
        )
        
        val resultado = dto.convertirAEur(20000)
        
        assertEquals(19.0, resultado, 0.1)
    }

    @Test
    fun convertirAEur_sinTasaEur_retornaCero() {
        val dto = ExchangeRateDto(
            provider = "test",
            base = "CLP",
            date = "2024-01-01",
            rates = mapOf("USD" to 0.00105) // Sin EUR
        )
        
        val resultado = dto.convertirAEur(20000)
        
        assertEquals(0.0, resultado, 0.001)
    }

    // ==================== TESTS DE getTasaUsd ====================

    @Test
    fun getTasaUsd_conTasa_retornaValor() {
        val dto = ExchangeRateDto(
            provider = "test",
            base = "CLP",
            date = "2024-01-01",
            rates = mapOf("USD" to 0.00105)
        )
        
        val tasa = dto.getTasaUsd()
        
        assertEquals(0.00105, tasa, 0.00001)
    }

    @Test
    fun getTasaUsd_sinTasa_retornaCero() {
        val dto = ExchangeRateDto(
            provider = "test",
            base = "CLP",
            date = "2024-01-01",
            rates = emptyMap()
        )
        
        val tasa = dto.getTasaUsd()
        
        assertEquals(0.0, tasa, 0.00001)
    }

    // ==================== TESTS DE ESTRUCTURA DTO ====================

    @Test
    fun dto_conDatosCompletos_propiedadesCorrectas() {
        val dto = ExchangeRateDto(
            provider = "https://www.exchangerate-api.com",
            base = "CLP",
            date = "2024-12-01",
            rates = mapOf("USD" to 0.00105, "EUR" to 0.00095, "BRL" to 0.0063)
        )
        
        assertEquals("CLP", dto.base)
        assertEquals("2024-12-01", dto.date)
        assertEquals(3, dto.rates.size)
    }

    @Test
    fun dto_provider_esNullable() {
        val dto = ExchangeRateDto(
            provider = null,
            base = "CLP",
            date = "2024-01-01",
            rates = mapOf("USD" to 0.001)
        )
        
        assertNull(dto.provider)
        assertEquals("CLP", dto.base)
    }

    @Test
    fun dto_rates_contieneMultiplesMonedas() {
        val dto = ExchangeRateDto(
            provider = "test",
            base = "CLP",
            date = "2024-01-01",
            rates = mapOf(
                "USD" to 0.00105,
                "EUR" to 0.00095,
                "GBP" to 0.00082,
                "JPY" to 0.16,
                "BRL" to 0.0063
            )
        )
        
        assertEquals(5, dto.rates.size)
        assertTrue(dto.rates.containsKey("USD"))
        assertTrue(dto.rates.containsKey("EUR"))
        assertTrue(dto.rates.containsKey("GBP"))
    }

    // ==================== TESTS DE CASOS LÍMITE ====================

    @Test
    fun convertirAUsd_precioMuyAlto_calculoCorrecto() {
        val dto = ExchangeRateDto(
            provider = "test",
            base = "CLP",
            date = "2024-01-01",
            rates = mapOf("USD" to 0.001)
        )
        
        val resultado = dto.convertirAUsd(1000000)
        
        assertEquals(1000.0, resultado, 0.1)
    }

    @Test
    fun convertirAUsd_tasaMuyBaja_calculoCorrecto() {
        val dto = ExchangeRateDto(
            provider = "test",
            base = "CLP",
            date = "2024-01-01",
            rates = mapOf("USD" to 0.0001)
        )
        
        val resultado = dto.convertirAUsd(10000)
        
        assertEquals(1.0, resultado, 0.01)
    }

    @Test
    fun convertirAUsd_tasaAlta_calculoCorrecto() {
        val dto = ExchangeRateDto(
            provider = "test",
            base = "CLP",
            date = "2024-01-01",
            rates = mapOf("USD" to 0.01)
        )
        
        val resultado = dto.convertirAUsd(1000)
        
        assertEquals(10.0, resultado, 0.1)
    }

    // ==================== TESTS INTEGRACIÓN CON PRECIOS DE COMICS ====================

    @Test
    fun convertir_precioComicBatman_enUsd() {
        val dto = ExchangeRateDto(
            provider = "test",
            base = "CLP",
            date = "2024-01-01",
            rates = mapOf("USD" to 0.00105)
        )
        
        val precioBatman = 20000 // CLP
        val precioUsd = dto.convertirAUsd(precioBatman)
        
        assertTrue(precioUsd > 20.0 && precioUsd < 22.0)
    }

    @Test
    fun convertir_precioMembresia_enUsd() {
        val dto = ExchangeRateDto(
            provider = "test",
            base = "CLP",
            date = "2024-01-01",
            rates = mapOf("USD" to 0.00105)
        )
        
        val precioMembresia = 50000 // CLP
        val precioUsd = dto.convertirAUsd(precioMembresia)
        
        assertTrue(precioUsd > 50.0 && precioUsd < 55.0)
    }

    @Test
    fun convertir_precioConDescuentoVip_enUsd() {
        val dto = ExchangeRateDto(
            provider = "test",
            base = "CLP",
            date = "2024-01-01",
            rates = mapOf("USD" to 0.00105)
        )
        
        val precioOriginal = 20000
        val precioConDescuento = (precioOriginal * 0.5).toInt() // 50% descuento VIP
        val precioUsd = dto.convertirAUsd(precioConDescuento)
        
        assertTrue(precioUsd > 10.0 && precioUsd < 12.0)
    }
}



