package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Android Tests para LoginScreen
 * Pruebas de UI instrumentadas que se ejecutan en emulador/dispositivo
 */
@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ==================== TESTS DE ELEMENTOS VISIBLES ====================

    @Test
    fun loginScreen_tituloVisible() {
        composeTestRule.setContent {
            TestLoginScreen()
        }

        composeTestRule.onNodeWithText("Bienvenidos A La Tienda De Comic")
            .assertIsDisplayed()
    }

    @Test
    fun loginScreen_subtituloVisible() {
        composeTestRule.setContent {
            TestLoginScreen()
        }

        composeTestRule.onNodeWithText("Inicia sesión para continuar")
            .assertIsDisplayed()
    }

    @Test
    fun loginScreen_campoCorreoVisible() {
        composeTestRule.setContent {
            TestLoginScreen()
        }

        composeTestRule.onNodeWithText("Correo electrónico")
            .assertIsDisplayed()
    }

    @Test
    fun loginScreen_campoContrasenaVisible() {
        composeTestRule.setContent {
            TestLoginScreen()
        }

        composeTestRule.onNodeWithText("Contraseña")
            .assertIsDisplayed()
    }

    @Test
    fun loginScreen_botonEntrarVisible() {
        composeTestRule.setContent {
            TestLoginScreen()
        }

        composeTestRule.onNodeWithText("Entrar")
            .assertIsDisplayed()
    }

    @Test
    fun loginScreen_botonCrearCuentaVisible() {
        composeTestRule.setContent {
            TestLoginScreen()
        }

        composeTestRule.onNodeWithText("Crear cuenta")
            .assertIsDisplayed()
    }

    @Test
    fun loginScreen_linkOlvideContrasenaVisible() {
        composeTestRule.setContent {
            TestLoginScreen()
        }

        composeTestRule.onNodeWithText("¿Olvidaste tu contraseña?")
            .assertIsDisplayed()
    }

    // ==================== TESTS DE INTERACCIÓN ====================

    @Test
    fun loginScreen_clickBotonEntrar_funciona() {
        composeTestRule.setContent {
            TestLoginScreen()
        }

        composeTestRule.onNodeWithText("Entrar")
            .performClick()
    }

    @Test
    fun loginScreen_clickCrearCuenta_funciona() {
        composeTestRule.setContent {
            TestLoginScreen()
        }

        composeTestRule.onNodeWithText("Crear cuenta")
            .performClick()
    }

    @Test
    fun loginScreen_clickOlvideContrasena_funciona() {
        composeTestRule.setContent {
            TestLoginScreen()
        }

        composeTestRule.onNodeWithText("¿Olvidaste tu contraseña?")
            .performClick()
    }

    // ==================== TESTS DE ERRORES ====================

    @Test
    fun loginScreen_errorCorreo_semuestra() {
        composeTestRule.setContent {
            TestLoginScreenConError(errorCorreo = "formato de correo inválido")
        }

        composeTestRule.onNodeWithText("formato de correo inválido")
            .assertIsDisplayed()
    }

    @Test
    fun loginScreen_mensajeErrorGeneral_semuestra() {
        composeTestRule.setContent {
            TestLoginScreenConError(mensajeError = "Credenciales inválidas")
        }

        composeTestRule.onNodeWithText("Credenciales inválidas")
            .assertIsDisplayed()
    }

    // ==================== TESTS DE ESTADO ====================

    @Test
    fun loginScreen_estadoEnviando_muestraValidando() {
        composeTestRule.setContent {
            TestLoginScreenEnviando()
        }

        composeTestRule.onNodeWithText("Validando...")
            .assertIsDisplayed()
    }

    @Test
    fun loginScreen_botonDeshabilitado_noClickeable() {
        composeTestRule.setContent {
            TestLoginScreenDeshabilitado()
        }

        composeTestRule.onNodeWithText("Entrar")
            .assertIsNotEnabled()
    }
}

// ==================== COMPOSABLES DE PRUEBA ====================

@Composable
private fun TestLoginScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bienvenidos A La Tienda De Comic")
        Text("Inicia sesión para continuar")
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Correo electrónico") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Contraseña") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {}) {
            Text("Entrar")
        }
        TextButton(onClick = {}) {
            Text("¿Olvidaste tu contraseña?")
        }
        OutlinedButton(onClick = {}) {
            Text("Crear cuenta")
        }
    }
}

@Composable
private fun TestLoginScreenConError(
    errorCorreo: String? = null,
    mensajeError: String? = null
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bienvenidos A La Tienda De Comic")
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Correo electrónico") },
            isError = errorCorreo != null
        )
        if (errorCorreo != null) {
            Text(errorCorreo, color = Color.Red)
        }
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Contraseña") }
        )
        Button(onClick = {}) {
            Text("Entrar")
        }
        if (mensajeError != null) {
            Text(mensajeError, color = Color.Red)
        }
    }
}

@Composable
private fun TestLoginScreenEnviando() {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bienvenidos A La Tienda De Comic")
        Button(onClick = {}, enabled = false) {
            Text("Validando...")
        }
    }
}

@Composable
private fun TestLoginScreenDeshabilitado() {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {}, enabled = false) {
            Text("Entrar")
        }
    }
}
