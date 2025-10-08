package com.example.tiendacomic.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,     // Azul fuerte para botones
    secondary = SoftBlue,      // Azul suave de fondo
    tertiary = DarkGrayText    // Texto gris oscuro
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,     // Azul fuerte
    secondary = SoftBlue,      // Fondo suave
    tertiary = DarkGrayText,   // Texto gris oscuro
    background = SoftBlue,     // Fondo azul claro
    surface = SoftBlue,        // Superficies (cards, contenedores)
    onPrimary = Color.White,   // Texto sobre botones azules
    onSecondary = Color.Black, // Texto sobre el fondo suave
    onTertiary = Color.Black,  // Texto sobre gris oscuro
    onBackground = Color.Black,
    onSurface = Color.Black
)


@Composable
fun TiendaComicTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, //true, forzamos a quitar el color deprevinado
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}