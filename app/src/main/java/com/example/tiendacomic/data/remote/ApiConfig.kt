package com.example.tiendacomic.data.remote

import android.os.Build
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Configuración de Retrofit para conectar con los microservicios
 * 
 * Detecta automáticamente si es emulador o dispositivo físico
 */
object ApiConfig {

    // ========================================
    // CAMBIAR ESTA IP POR LA DE TU PC
    // ========================================
    private const val IP_PC = "192.168.1.16"
    // ========================================

    // Detectar si es emulador
    private val isEmulator: Boolean = (
        Build.FINGERPRINT.startsWith("generic") ||
        Build.FINGERPRINT.startsWith("unknown") ||
        Build.MODEL.contains("google_sdk") ||
        Build.MODEL.contains("Emulator") ||
        Build.MODEL.contains("Android SDK built for x86") ||
        Build.MANUFACTURER.contains("Genymotion") ||
        Build.BRAND.startsWith("generic") ||
        Build.DEVICE.startsWith("generic") ||
        Build.PRODUCT.contains("sdk") ||
        Build.HARDWARE.contains("goldfish") ||
        Build.HARDWARE.contains("ranchu")
    )

    // Emulador usa 10.0.2.2, celular físico usa IP_PC
    private val HOST: String = if (isEmulator) "10.0.2.2" else IP_PC

    // URLs de los microservicios INTERNOS
    private val BASE_URL_USUARIO = "http://$HOST:8081/"
    private val BASE_URL_CATALOGO = "http://$HOST:8082/"
    private val BASE_URL_PEDIDO = "http://$HOST:8083/"
    
    // URL de API EXTERNA (tipo de cambio)
    private const val BASE_URL_EXCHANGE_RATE = "https://api.exchangerate-api.com/"

    // Logging para debug
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente HTTP con timeouts
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Retrofit para Usuario (puerto 8081)
    private val retrofitUsuario: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_USUARIO)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Retrofit para Catalogo (puerto 8082)
    private val retrofitCatalogo: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_CATALOGO)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Retrofit para Pedido (puerto 8083)
    private val retrofitPedido: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_PEDIDO)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    // Retrofit para API EXTERNA de tipo de cambio
    private val retrofitExchangeRate: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_EXCHANGE_RATE)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // APIs INTERNAS (microservicios propios)
    val usuarioApi: UsuarioApi by lazy { retrofitUsuario.create(UsuarioApi::class.java) }
    val comicApi: ComicApi by lazy { retrofitCatalogo.create(ComicApi::class.java) }
    val pedidoApi: PedidoApi by lazy { retrofitPedido.create(PedidoApi::class.java) }
    
    // API EXTERNA (servicio de terceros)
    val exchangeRateApi: ExchangeRateApi by lazy { retrofitExchangeRate.create(ExchangeRateApi::class.java) }
}
