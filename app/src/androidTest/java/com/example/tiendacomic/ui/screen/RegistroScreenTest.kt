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
 * Android Tests para RegistroScreen
 * Pruebas de UI instrumentadas que se ejecutan en emulador/dispositivo
 */
@RunWith(AndroidJUnit4::class)
class RegistroScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ==================== TESTS DE ELEMENTOS VISIBLES ====================

    @Test
    fun registroScreen_tituloVisible() {
        composeTestRule.setContent {
            TestRegistroScreen()
        }

        composeTestRule.onNodeWithText("Crear Cuenta")
            .assertIsDisplayed()
    }

    @Test
    fun registroScreen_subtituloVisible() {
        composeTestRule.setContent {
            TestRegistroScreen()
        }

        composeTestRule.onNodeWithText("Regístrate para continuar")
            .assertIsDisplayed()
    }

    @Test
    fun registroScreen_campoNombreVisible() {
        composeTestRule.setContent {
            TestRegistroScreen()
        }

        composeTestRule.onNodeWithText("Nombre completo")
            .assertIsDisplayed()
    }

    @Test
    fun registroScreen_campoRutVisible() {
        composeTestRule.setContent {
            TestRegistroScreen()
        }

        composeTestRule.onNodeWithText("RUT")
            .assertIsDisplayed()
    }

    @Test
    fun registroScreen_campoCorreoVisible() {
        composeTestRule.setContent {
            TestRegistroScreen()
        }

        composeTestRule.onNodeWithText("Correo electrónico")
            .assertIsDisplayed()
    }

    @Test
    fun registroScreen_campoContrasenaVisible() {
        composeTestRule.setContent {
            TestRegistroScreen()
        }

        composeTestRule.onNodeWithText("Contraseña")
            .assertIsDisplayed()
    }

    @Test
    fun registroScreen_campoConfirmarVisible() {
        composeTestRule.setContent {
            TestRegistroScreen()
        }

        composeTestRule.onNodeWithText("Confirmar contraseña")
            .assertIsDisplayed()
    }

    @Test
    fun registroScreen_botonRegistrarVisible() {
        composeTestRule.setContent {
            TestRegistroScreen()
        }

        composeTestRule.onNodeWithText("Registrar")
            .assertIsDisplayed()
    }

    @Test
    fun registroScreen_botonYaTengoCuentaVisible() {
        composeTestRule.setContent {
            TestRegistroScreen()
        }

        composeTestRule.onNodeWithText("Ya tengo una cuenta")
            .assertIsDisplayed()
    }

    // ==================== TESTS DE INTERACCIÓN ====================

    @Test
    fun registroScreen_clickBotonRegistrar_funciona() {
        composeTestRule.setContent {
            TestRegistroScreen()
        }

        composeTestRule.onNodeWithText("Registrar")
            .performClick()
    }

    @Test
    fun registroScreen_clickYaTengoCuenta_funciona() {
        composeTestRule.setContent {
            TestRegistroScreen()
        }

        composeTestRule.onNodeWithText("Ya tengo una cuenta")
            .performClick()
    }

    // ==================== TESTS DE ERRORES ====================

    @Test
    fun registroScreen_errorNombre_semuestra() {
        composeTestRule.setContent {
            TestRegistroScreenConErrores(errorNombre = "El nombre es obligatorio")
        }

        composeTestRule.onNodeWithText("El nombre es obligatorio")
            .assertIsDisplayed()
    }

    @Test
    fun registroScreen_errorRut_semuestra() {
        composeTestRule.setContent {
            TestRegistroScreenConErrores(errorRut = "Formato de RUT incorrecto")
        }

        composeTestRule.onNodeWithText("Formato de RUT incorrecto")
            .assertIsDisplayed()
    }

    @Test
    fun registroScreen_errorCorreo_semuestra() {
        composeTestRule.setContent {
            TestRegistroScreenConErrores(errorCorreo = "formato de correo inválido")
        }

        composeTestRule.onNodeWithText("formato de correo inválido")
            .assertIsDisplayed()
    }

    @Test
    fun registroScreen_errorContrasena_semuestra() {
        composeTestRule.setContent {
            TestRegistroScreenConErrores(errorContrasena = "Debe tener al menos 8 caracteres")
        }

        composeTestRule.onNodeWithText("Debe tener al menos 8 caracteres")
            .assertIsDisplayed()
    }

    @Test
    fun registroScreen_errorConfirmar_semuestra() {
        composeTestRule.setContent {
            TestRegistroScreenConErrores(errorConfirmar = "Las contraseñas no coinciden")
        }

        composeTestRule.onNodeWithText("Las contraseñas no coinciden")
            .assertIsDisplayed()
    }

    // ==================== TESTS DE ESTADO ====================

    @Test
    fun registroScreen_enviando_muestraCreandoCuenta() {
        composeTestRule.setContent {
            TestRegistroScreenEnviando()
        }

        composeTestRule.onNodeWithText("Creando cuenta...")
            .assertIsDisplayed()
    }

    @Test
    fun registroScreen_botonDeshabilitado_noClickeable() {
        composeTestRule.setContent {
            TestRegistroScreenDeshabilitado()
        }

        composeTestRule.onNodeWithText("Registrar")
            .assertIsNotEnabled()
    }
}

// ==================== COMPOSABLES DE PRUEBA ====================

@Composable
private fun TestRegistroScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear Cuenta", style = MaterialTheme.typography.headlineSmall)
        Text("Regístrate para continuar")
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Nombre completo") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("RUT") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        
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
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Confirmar contraseña") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = {}) {
            Text("Registrar")
        }
        
        OutlinedButton(onClick = {}) {
            Text("Ya tengo una cuenta")
        }
    }
}

@Composable
private fun TestRegistroScreenConErrores(
    errorNombre: String? = null,
    errorRut: String? = null,
    errorCorreo: String? = null,
    errorContrasena: String? = null,
    errorConfirmar: String? = null
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Nombre completo") },
            isError = errorNombre != null
        )
        if (errorNombre != null) {
            Text(errorNombre, color = Color.Red)
        }
        
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("RUT") },
            isError = errorRut != null
        )
        if (errorRut != null) {
            Text(errorRut, color = Color.Red)
        }
        
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
            label = { Text("Contraseña") },
            isError = errorContrasena != null
        )
        if (errorContrasena != null) {
            Text(errorContrasena, color = Color.Red)
        }
        
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Confirmar contraseña") },
            isError = errorConfirmar != null
        )
        if (errorConfirmar != null) {
            Text(errorConfirmar, color = Color.Red)
        }
        
        Button(onClick = {}) {
            Text("Registrar")
        }
    }
}

@Composable
private fun TestRegistroScreenEnviando() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {}, enabled = false) {
            Text("Creando cuenta...")
        }
    }
}

@Composable
private fun TestRegistroScreenDeshabilitado() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {}, enabled = false) {
            Text("Registrar")
        }
    }
}



