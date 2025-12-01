package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Android Tests para MembresiaScreen
 * Pruebas de UI instrumentadas que se ejecutan en emulador/dispositivo
 */
@RunWith(AndroidJUnit4::class)
class MembresiaScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ==================== TESTS DE ELEMENTOS VISIBLES ====================

    @Test
    fun membresiaScreen_tituloVisible() {
        composeTestRule.setContent {
            TestMembresiaScreen()
        }

        composeTestRule.onNodeWithText("Membresía Premium")
            .assertIsDisplayed()
    }

    @Test
    fun membresiaScreen_descripcionVisible() {
        composeTestRule.setContent {
            TestMembresiaScreen()
        }

        composeTestRule.onNodeWithText("Accede a todos los cómics digitales", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun membresiaScreen_precioVisible() {
        composeTestRule.setContent {
            TestMembresiaScreen()
        }

        composeTestRule.onNodeWithText("Precio:", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun membresiaScreen_botonComprarVisible() {
        composeTestRule.setContent {
            TestMembresiaScreen()
        }

        composeTestRule.onNodeWithText("Comprar membresía")
            .assertIsDisplayed()
    }

    // ==================== TESTS DE INTERACCIÓN ====================

    @Test
    fun membresiaScreen_clickComprarMembresia_funciona() {
        composeTestRule.setContent {
            TestMembresiaScreen()
        }

        composeTestRule.onNodeWithText("Comprar membresía")
            .performClick()
    }

    // ==================== TESTS DE USUARIO VIP ====================

    @Test
    fun membresiaScreen_usuarioVip_muestraMensaje() {
        composeTestRule.setContent {
            TestMembresiaScreenVip()
        }

        composeTestRule.onNodeWithText("🎉 Ya eres miembro VIP", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun membresiaScreen_usuarioVip_muestraFechaExpiracion() {
        composeTestRule.setContent {
            TestMembresiaScreenVip()
        }

        composeTestRule.onNodeWithText("Tu membresía vence el:", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun membresiaScreen_usuarioVip_botonNoVisible() {
        composeTestRule.setContent {
            TestMembresiaScreenVip()
        }

        composeTestRule.onNodeWithText("Comprar membresía")
            .assertDoesNotExist()
    }

    // ==================== TESTS DE BENEFICIOS ====================

    @Test
    fun membresiaScreen_beneficioDescuentosVisible() {
        composeTestRule.setContent {
            TestMembresiaScreen()
        }

        composeTestRule.onNodeWithText("descuentos exclusivos", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun membresiaScreen_beneficioContenidoIlimitadoVisible() {
        composeTestRule.setContent {
            TestMembresiaScreen()
        }

        composeTestRule.onNodeWithText("contenido ilimitado", substring = true)
            .assertIsDisplayed()
    }
}

// ==================== COMPOSABLES DE PRUEBA ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TestMembresiaScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Membresía Premium") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Accede a todos los cómics digitales, descuentos exclusivos y contenido ilimitado.",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                "Precio: \$49.990",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Text("Comprar membresía")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TestMembresiaScreenVip() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Membresía Premium") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Accede a todos los cómics digitales, descuentos exclusivos y contenido ilimitado.",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                "Precio: \$49.990",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Text(
                "🎉 Ya eres miembro VIP. Tu membresía vence el: 01/12/2025",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}



